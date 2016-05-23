package com.genye.myapplication.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast的工具类
 */
public class ToastUtil {

    private static Toast toast;

    //Toast显示（短时间）
    public static void showShort(Context context ,String text){
        if (toast == null) {
            toast =  Toast.makeText(context.getApplicationContext(),text,Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    //Toast显示（短时间）
    public static void showLong(Context context ,String text){
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(),text,Toast.LENGTH_LONG);
            toast.show();
        } else {
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        }
    }

    //Toast显示没网络
    public static void showNonet(Context context){
        Toast.makeText(context,"当前网络不可用,请检查网络",Toast.LENGTH_SHORT).show();
    }
}
