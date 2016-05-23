package com.genye.myapplication.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DbBaseService {
	protected Context ctx = null;
	protected SQLiteDatabase mSQLiteDatabase = null;

	public DbBaseService(Context ctx) {
		this.ctx = ctx;
	}
	
	public void open() {
		/**try {
			dh = new DataBaseHelper(ctx);
			if (dh == null) {
				return;
			}*/
		//mSQLiteDatabase = dh.getWritableDatabase();
		/*} catch (SQLiteException ex) {
			ex.printStackTrace();
		}*/
		//mSQLiteDatabase = DataBaseHelper.getInstance(ctx);
		mSQLiteDatabase = DatabaseManager.getInstance().openDatabase();
	}
	
	public void close() {
		/**if (mSQLiteDatabase != null && mSQLiteDatabase.isOpen()) {
			mSQLiteDatabase.close();
		}*/
		DatabaseManager.getInstance().closeDatabase();
		//dh.close();
	}
}
