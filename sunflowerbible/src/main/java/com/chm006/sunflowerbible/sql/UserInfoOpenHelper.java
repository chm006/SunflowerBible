package com.chm006.sunflowerbible.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 用户信息数据库
 * Created by chenmin on 2016/10/9.
 */

public class UserInfoOpenHelper extends SQLiteOpenHelper {

    public UserInfoOpenHelper(Context context) {
        /**
         * 第二个参数：数据库的名字
         * 第三个参数：游标工厂
         * 第四个参数：版本号
         */
        super(context, "user.db", null, 1);
    }
    /**
     * blacknumber :数据库的表名
     * number ：黑名单电话号码
     * mode ：黑名单的拦截模式
     * _id :主键自动增长
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table login_info (_id integer primary key autoincrement, phone_number varchar(20), user_name varchar(20), user_password varchar(20))");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}