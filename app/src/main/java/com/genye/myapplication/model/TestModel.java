package com.genye.myapplication.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yzd on 2016/5/21.
 */
@DatabaseTable(tableName = "test")
public class TestModel implements Serializable {

    @DatabaseField(generatedId = true, columnName = "_id")
    private int id;
    @DatabaseField(columnName = "_name")
    private String name;
    @DatabaseField(columnName = "datetime", dataType = DataType.DATE_LONG)
    private Date dateTime;

    public TestModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd").format(dateTime);
       /* if (dateTime == null || dateTime.equals("")) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            dateTime = format.format(new Date());
        }
        return dateTime;*/
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "TestModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dateTime='" + getDateTime() + '\'' +
                '}';
    }
}
