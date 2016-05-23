package com.genye.myapplication.model;

/**
 * Created by yzd on 2016/5/18.
 */
public class DbInfo {
    public static final String DATABASE_NAME = "db_data.db";
    public static final int DATABASE_VERSION = 2;

    private static String[][] FieldNames;
    private static String[][] FieldTypes;
    private static String[] TableNames = {"shop_account"};

    static {
        FieldNames = new String[][]{
                {"_id", "shop_name", "alipay_account", "wx_account", "type", "alipay_code", "wx_code", "datetime"}
        };
        FieldTypes = new String[][] {
                {"INTEGER PRIMARY KEY AUTOINCREMENT","VARCHAR(100)"
                        ,"VARCHAR(100)","VARCHAR(100)"
                        ,"VARCHAR(10)","VARCHAR(100)","VARCHAR(100)", "DATETIME"}
        };
    }

    public static String[][] getFieldNames() {
        return FieldNames;
    }

    public static String[][] getFieldTypes() {
        return FieldTypes;
    }

    public static String[] getTableNames() {
        return TableNames;
    }
}
