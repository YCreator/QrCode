package com.genye.myapplication.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yzd on 2016/5/18.
 */
public class AccountModel implements Serializable{

    private int id;
    private String shopName;
    private String alipayAccount;
    private String wxAccount;
    private String type;
    private String alipayCode;
    private String wxCode;
    private String updateType;
    private String dateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAlipayAccount() {
        return alipayAccount;
    }

    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }

    public String getWxAccount() {
        return wxAccount;
    }

    public void setWxAccount(String wxAccount) {
        this.wxAccount = wxAccount;
    }

    public String getType() {
        return type == null ? "" : type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAlipayCode() {
        return alipayCode;
    }

    public void setAlipayCode(String alipayCode) {
        this.alipayCode = alipayCode;
    }

    public String getWxCode() {
        return wxCode;
    }

    public void setWxCode(String wxCode) {
        this.wxCode = wxCode;
    }

    public String getUpdateType() {
        return updateType == null || updateType.equals("") ? type : updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public String getDateTime() {
        if (dateTime == null || dateTime.equals("")) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            dateTime = format.format(new Date());
        }
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void clearn() {
        setId(0);
        setShopName("");
        setType("");
        setAlipayAccount("");
        setAlipayCode("");
        setWxAccount("");
        setWxCode("");
        setUpdateType("");
        setDateTime("");
    }

    public void checkType(String t) {
       // Log.i("type", t);
        if (!t.contains(getType())) {
            setUpdateType(String.format("%s,%s", t , getType()));
        } else {
            setUpdateType(t);
        }
       // Log.i("type1", getUpdateType());
    }
}
