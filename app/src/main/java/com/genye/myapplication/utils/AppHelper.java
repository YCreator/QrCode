package com.genye.myapplication.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

public class AppHelper {

    /**
     * 获取包信息
     *
     * @return PackageInfo
     */
    public static PackageInfo getPackageInfo(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    /**
     * 判断指定包名的进程是否运行
     * param context
     * param packageName 指定包名
     * return 是否运行
     */
    public static boolean isRunning(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> infos = am.getRunningAppProcesses();
        for (RunningAppProcessInfo rapi : infos) {
            if (rapi.processName.equals(packageName))
                return true;
        }
        return false;
    }

    /**
     * 用来判断服务是否运行.
     * param className 判断的服务名字
     * return true 在运行 false 不在运行
     */
    public static boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)
                mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /**
     * 根据包名判断应用是否存在
     * param context
     * param packageName
     * return
     */
    public static boolean isAppExist(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        //PackageManager.GET_UNINSTALLED_PACKAGES
        List<PackageInfo> list = manager.getInstalledPackages(0);
        for (PackageInfo info : list) {
            if (info.packageName.endsWith(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取app当前版本名称
     * param context
     * return
     */
    public static String getVersionName(Context context) {
        PackageInfo info = getPackageInfo(context);
        if (info != null) {
            return info.versionName;
        } else {
            return "";
        }
    }

    /**
     * 获取app当前版本号
     * param context
     * return
     */
    public static int getVersionCode(Context context) {
        PackageInfo info = getPackageInfo(context);
        if (info != null) {
            return info.versionCode;
        } else {
            return 0;
        }
    }

}
