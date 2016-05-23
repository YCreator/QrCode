package com.genye.myapplication;

import android.app.Application;

import com.genye.myapplication.db.DataBaseHelper;
import com.genye.myapplication.db.DatabaseManager;

/**
 * Created by yzd on 2016/5/14.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseManager.initializeInstance(new DataBaseHelper(this));
        //OrmDataBaseHelper.getHelper(this);
        //ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 5, 200, TimeUnit.SECONDS,)
    }
}
