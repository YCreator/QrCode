package com.genye.myapplication.utils;

import android.util.Log;

/**
 * 日志打印工具
 * Created by Administrator on 2015/12/2.
 */
public class TLog {
    public static boolean DEBUG = true;
    public static final String LOG_TAG = "SIMICO";

    public static void analytics(String paramString) {
        if (DEBUG)
            Log.d(LOG_TAG, paramString);
    }

    public static void error(String paramString) {
        if (DEBUG)
            Log.e(LOG_TAG, paramString);
    }

    public static void log(String paramString) {
        if (DEBUG)
            Log.i(LOG_TAG, paramString);
    }

    public static void log(String paramString1, String paramString2) {
        if (DEBUG)
            Log.i(paramString1, paramString2);
    }

    public static void logv(String paramString) {
        if (DEBUG)
            Log.v(LOG_TAG, paramString);
    }

    public static void warn(String paramString) {
        if (DEBUG)
            Log.w(LOG_TAG, paramString);
    }
}
