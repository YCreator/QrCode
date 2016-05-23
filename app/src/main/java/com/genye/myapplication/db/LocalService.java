package com.genye.myapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import com.genye.myapplication.model.AccountModel;
import com.genye.myapplication.model.DbInfo;

import java.text.SimpleDateFormat;

public class LocalService extends DbBaseService {


	private static LocalService localService;
	private String[] FieldName = DbInfo.getFieldNames()[0];

	public LocalService(Context ctx) {
		super(ctx);
	}

	public static LocalService getInstance(Context context) {
		if (localService == null) {
			localService = new LocalService(context);
		}
		return localService;
	}

	public synchronized void addAcount(AccountModel accountModel) {
		try {
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = sDateFormat.format(new java.util.Date());
			ContentValues cv = new ContentValues();
			cv.put(FieldName[1],accountModel.getShopName());
			cv.put(FieldName[2],accountModel.getAlipayAccount());
			cv.put(FieldName[3],accountModel.getWxAccount());
			cv.put(FieldName[4],accountModel.getType());
			cv.put(FieldName[5],accountModel.getAlipayCode());
			cv.put(FieldName[6],accountModel.getWxAccount());
			cv.put(FieldName[7],date);
			this.mSQLiteDatabase.insert(DbInfo.getTableNames()[0], null, cv);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public synchronized void updateAcount(AccountModel accountModel) {
		ContentValues cv = new ContentValues();
		cv.put(FieldName[1],accountModel.getShopName());
		cv.put(FieldName[2],accountModel.getAlipayAccount());
		cv.put(FieldName[3],accountModel.getWxAccount());
		cv.put(FieldName[4],accountModel.getUpdateType());
		cv.put(FieldName[5],accountModel.getAlipayCode());
		cv.put(FieldName[6],accountModel.getWxAccount());
		this.mSQLiteDatabase.update(DbInfo.getTableNames()[0], cv
				, "_id = ?", new String[]{String.valueOf(accountModel.getId())});
	}

	public Cursor findByShopName(String shopName) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = sDateFormat.format(new java.util.Date());
		Cursor cursor = null;
		String sql = "select * from " + DbInfo.getTableNames()[0]
				+ " where " + FieldName[1] + " = " + "'" + shopName + "'" + " and " + FieldName[7] + " = '" + date + "'";
		try {
			cursor = this.mSQLiteDatabase.rawQuery(sql,null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cursor;
	}


	public Cursor list() {
		Cursor cursor = null;
		try {
			String sql = "select * from "+DbInfo.getTableNames()[0];
			cursor = this.mSQLiteDatabase.rawQuery(sql, null);
		} catch (SQLiteException ex) {
			ex.printStackTrace();
			cursor = null;
		}
		return cursor;
		
	}


}
