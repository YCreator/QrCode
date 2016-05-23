package com.genye.myapplication.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.genye.myapplication.Constants;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件操作工具
 */
public class FileUtils {

    /**
     * 初始化保存路径
     * return
     */
    public static String initPath(String path) {
        File f = new File(path);
        if (!f.exists()) {
            f.mkdir();
        }
        return path;
    }

    /**
     * 保存Bitmap到sdcard
     * param b
     */
    public static void saveBitmap(Bitmap b, String path, String filename) {

        String filepath = initPath(path);
        /*long dataTake = System.currentTimeMillis();*/
        String jpegName = filepath + "/" + filename;
        try {
            FileOutputStream fout = new FileOutputStream(jpegName);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 创建并获取app子存储文件
     */
    public static File getChileFile(String path) {
        if (path == null) return getAppMainFile();
        File childFile = new File(getAppMainFile(), path);
        if (!childFile.exists()) {
            childFile.mkdir();
        }
        return childFile;
    }

    /**
     * 创建并获取app主存储文件
     */
    public static File getAppMainFile() {
        File appDir;
        if (StorageUtils.isSDcardExist()) {
            appDir = new File(Environment.getExternalStorageDirectory(), Constants.MAIN_PATH);
        } else {
            appDir = new File(Environment.getDownloadCacheDirectory(), Constants.MAIN_PATH);
        }
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        return appDir;
    }

    /**
     * 复制文件
     *
     * @param oldPath
     * @param newPath
     * @return
     */
    public static boolean copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时

                // 新建文件输入流并对它进行缓冲
                FileInputStream input = new FileInputStream(oldPath);
                BufferedInputStream inBuff = new BufferedInputStream(input);

                // 新建文件输出流并对它进行缓冲
                FileOutputStream output = new FileOutputStream(newPath);
                BufferedOutputStream outBuff = new BufferedOutputStream(output);

			/*	InputStream inStream = new FileInputStream(oldPath); //读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);*/
                byte[] buffer = new byte[1024 * 5];
                int length;
                while ((length = inBuff.read(buffer)) != -1) {
                    bytesum += length; //字节数 文件大小
                    System.out.println(bytesum);
                    outBuff.write(buffer, 0, length);
                }
                outBuff.flush();
                inBuff.close();
                outBuff.close();
                output.close();
                input.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
            return false;
        }
        return true;

    }

    /**
     * 获取文件夹下的所有文件的数量
     * param files
     * return
     */
    public static int getListFileSize(File files) {
        File[] file = files.listFiles();
        return file.length;
    }

    /**
     * 删除文件夹下的所有文件
     * param files
     * return
     */
    public static boolean deleteFiles(File files) {
        File[] file = files.listFiles();
        for (File f : file) {
            boolean success = f.delete();
            if (!success) {
                return false;
            }
        }
        return true;
    }

    /**
     * 删除文件
     * param file
     * return boolean
     */
    public static boolean deleteFile(File file) {
        return file.delete();
    }

    /**
     * 根据文件获取uri
     * param file
     * return Uri
     */
    public static Uri getUriFromFile(File file) {
        return Uri.fromFile(file);
    }

    /**
     * 根据uri获取文件路径
     * param context
     * param uri
     * return string
     */
    public static String getPath(Context context, Uri uri) {
        boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (StorageUtils.isExternalStorageDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (StorageUtils.isDownloadsDocument(uri)) {
                String id = DocumentsContract.getDocumentId(uri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads")
                        , Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (StorageUtils.isMediaDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = "_id=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (StorageUtils.isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
}
