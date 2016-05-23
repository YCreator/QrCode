package com.genye.myapplication.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

/**
 * 二维码工具类
 * Created by Administrator on 2015/12/30.
 */
public class QRCodeUtils {

    public static final int IMAGE_HALFWIDTH = 30;

    /**
     * 生成二维码图像
     *
     * @param content 文本内容
     * @param width   二维码图像宽度
     * @param height  二维码图像高度
     * @return
     * @throws WriterException
     * @throws OutOfMemoryError
     */
    public static Bitmap qrEncode(String content, int width, int height)
            throws WriterException, OutOfMemoryError {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = new QRCodeWriter().encode(content,
                BarcodeFormat.QR_CODE, width, height, hints);// 生成矩阵
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (bitMatrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                } else {
                    pixels[y * width + x] = 0xffffffff;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 生成带图标的二维码图像
     *
     * @param content 文本内容
     * @param width   二维码图像宽度
     * @param height  二维码图像高度
     * @return
     * @throws WriterException
     * @throws OutOfMemoryError
     */
    public static Bitmap qrEncode(String content, Bitmap mBitmap, int width, int height)
            throws WriterException, OutOfMemoryError {
        Matrix m = new Matrix();
        float sx = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getWidth();
        float sy = (float) 2 * IMAGE_HALFWIDTH /mBitmap.getHeight();
        m.setScale(sx, sy);
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0,mBitmap.getWidth(), mBitmap.getHeight(), m, false);
        MultiFormatWriter writer = new MultiFormatWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hints.put(EncodeHintType.MAX_SIZE, 350);
        hints.put(EncodeHintType.MIN_SIZE, 100);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = writer.encode(content,
                BarcodeFormat.QR_CODE, width, height, hints);// 生成矩阵
        int halfW = width / 2;
        int halfH = height / 2;

        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (bitMatrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                } else {
                    pixels[y * width + x] = 0xffffffff;
                }
                if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH && y > halfH - IMAGE_HALFWIDTH && y < halfH + IMAGE_HALFWIDTH) {
                    pixels[y * width + x] =  mBitmap.getPixel(x - halfW + IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
        //return Bitmap.createBitmap(bitmap, 50, 50, bitmap.getWidth() - 50, bitmap.getHeight() - 50);
    }

}
