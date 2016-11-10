package com.chm006.sunflowerbible.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 数据库操作类
 * 增、删、改、查
 * Created by chenmin on 2016/10/9.
 */

public class UserInfoDao {
    private UserInfoOpenHelper helper;

    public UserInfoDao(Context context) {
        helper = new UserInfoOpenHelper(context);
    }

    /**
     * 添加注册信息到数据库
     *
     * @param phone_number  用户注册时的手机号码
     * @param user_name     登陆用户名
     * @param user_password 登陆密码
     * @return 是否添加成功
     */
    public boolean addUserInfo(String phone_number, String user_name, String user_password) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("phone_number", phone_number);
        values.put("user_name", user_name);
        values.put("user_password", user_password);
        long rowId = db.insert("login_info", null, values);
        //判断当前添加是否成功，如果rowid等于-1表示失败
        if (rowId == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 根据用户名删除用户信息
     *
     * @param user_name 登陆用户名
     * @return 是否删除成功
     */
    public boolean deleteUserInfo(String user_name) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int rowNumber = db.delete("login_info", "user_name = ?", new String[]{user_name});
        //rowNumber表示影响的行数
        if (rowNumber == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 根据用户名修改密码
     *
     * @param user_name     登陆用户名
     * @param user_password 登陆密码
     * @return 是否修改成功
     */
    public boolean changePassword(String user_name, String user_password) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_password", user_password);
        int rowNumber = db.update("login_info", values, "user_name = ?", new String[]{user_name});
        if (rowNumber == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 根据账号和密码查找号码
     * 判断是否存在该账户
     *
     * @param user_name     登陆用户名
     * @param user_password 登陆密码
     * @return 用户名密码是否正确
     */
    public boolean findExists(String user_name, String user_password) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("login_info", new String[]{"phone_number"}, "user_name = ? and user_password = ?", new String[]{user_name, user_password}, null, null, null);
        if (cursor.getCount() == 1) {
            return true;
        } else if (cursor.getCount() > 1) {
            return true;//这个号码注册了两个或两个以上的账号
        }
        return false;
    }

    /**
     * 根据账号和手机号码查找密码
     * 判断是否存在该账户
     *
     * @param user_name     登陆用户名
     * @param phone_number  手机号码
     * @return 用户名密码是否正确
     */
    public boolean findExists2(String user_name, String phone_number) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("login_info", new String[]{"user_password"}, "user_name = ? and phone_number = ?", new String[]{user_name, phone_number}, null, null, null);
        if (cursor.getCount() == 1) {
            return true;
        } else if (cursor.getCount() > 1) {
            return true;//这个号码有两个密码（不正常）
        }
        return false;
    }

    /**
     * 判断是否存在该账户
     *
     * @param user_name     登陆用户名
     * @return 账户是否已经存在
     */
    public boolean findUserName(String user_name) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("login_info", new String[]{"user_name"}, "user_name = ?", new String[]{user_name}, null, null, null);
        if (cursor.getCount() == 1) {
            return true;
        } else if (cursor.getCount() > 1) {
            return true;//有两个或两个以上的这个账号，不正常的数据表
        }
        return false;
    }

    /**
     * @return login_info表里一共有多少条记录
     */
    public int getCount() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from login_info", null);
        cursor.moveToNext();
        int count = cursor.getInt(0);
        return count;
    }
}
