package com.genye.myapplication.db;

import android.content.Context;

import com.genye.myapplication.model.TestModel;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by yzd on 2016/5/21.
 */
public class TestDao extends BaseDao<TestModel, Integer> {


    public TestDao(Context context) {
        super(context);
    }

    @Override
    public Dao<TestModel, Integer> getDao() throws SQLException {
        return mDatabaseHelper.getDao(TestModel.class);
    }
}
