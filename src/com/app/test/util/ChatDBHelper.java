package com.app.test.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChatDBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "chat_user.db";
	private static final int DATABASE_VERSION = 1;

	public ChatDBHelper(Context context) {
		// CursorFactory设置为null,使用默认值
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// 数据库第一次被创建时onCreate会被调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists user (account varchar(64) primary key,userId integer,real_name varchar(20),head_image varchar(100),classid integer,type integer,status integer)");
	}

	// 如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}