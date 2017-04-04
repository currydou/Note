package com.curry.signapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.curry.signapp.BaseApplication;
import com.curry.signapp.bean.SimpleLocation;
import com.curry.signapp.constant.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by curry on 2016/3/17.
 */
public class LocationDao {

    private LocationOpenHelper helper;
    private static LocationDao instance;

    private LocationDao(Context context) {
        //数据库的名字
        helper = new LocationOpenHelper(context, Constants.DB_NAME, null, 1);
    }

    public static LocationDao getInstance(Context context) {
        if (instance == null) {
            synchronized (BaseApplication.class) {
                if (instance == null) {
                    instance = new LocationDao(context);
                }
            }
        }
        return instance;
    }

    /**
     * 增加
     */
    public boolean add(SimpleLocation location) {
        //需不需要这个判断？
        if (location == null) {
            return false;
        }
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.FIELD_TIME, location.getTime());
        values.put(Constants.FIELD_LONGITUDE, location.getLongitude());
        values.put(Constants.FIELD_LATITUDE, location.getLatitude());
        long rowId = db.insert(Constants.TABLE_LOCATION, null, values);
        if (rowId != -1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除所有
     *
     * @return
     */
    public boolean deleteAll() {
        SQLiteDatabase db = helper.getWritableDatabase();
        int rowNumber = db.delete(Constants.TABLE_LOCATION, null, null);
        if (rowNumber != 0) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 获取所有
     *
     * @return
     */
    public List<SimpleLocation> findAll() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(Constants.TABLE_LOCATION,
                new String[]{Constants.FIELD_TIME, Constants.FIELD_LONGITUDE, Constants.FIELD_LATITUDE},
                null, null, null, null, null);
        List<SimpleLocation> locationList = new ArrayList<>();
        cursor.moveToNext();
        while (cursor.moveToNext()) {
            SimpleLocation location = new SimpleLocation();
            location.setTime(cursor.getString(0));
            location.setLongitude(cursor.getString(1));
            location.setLatitude(cursor.getString(2));
            locationList.add(location);
        }
        cursor.close();
        db.close();
        return locationList;
    }


    /*
    查询总数
     */
   /* public int getTotal() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from blacknumber", null);
        cursor.moveToNext();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }*/


}
