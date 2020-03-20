package com.app.test.util;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ChatDBUtils {
	private static ChatDBUtils utils;
	private ChatDBHelper helper;

	private ChatDBUtils(Context context) {
		helper = new ChatDBHelper(context);
	}

	public static ChatDBUtils getInstance(Context context) {
		if (utils == null) {
			utils = new ChatDBUtils(context);
		}
		return utils;
	}

	/**
	 * 添加用户，如果用户中存在同account的用户，就更新用户信息，如果不存在则直接新增一个用户
	 */
	public void addUsers(List<ChatUser> users) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.beginTransaction(); // 开始事务
		try {
			for (ChatUser user : users) {
				// account,real_name ,head_image,classid,type,status
				db.execSQL("replace into user values(?,?,?,?,?,?)",
						new Object[] { user.account, user.userId,
								user.headPicUrl, user.classid, user.type,
								user.status });
			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
		db.close();
	}

	/**
	 * 更新用户信息
	 */
	public void updateChatUser(ChatUser user) {
		SQLiteDatabase db = helper.getWritableDatabase();
		// account,real_name ,head_image,classid,type,status
		ContentValues cv = new ContentValues();
		cv.put("head_image", user.headPicUrl);
		cv.put("classid", user.classid);
		cv.put("type", user.type);
		cv.put("status", user.status);
		db.update("user", cv, "account = ?", new String[] { user.account });
		db.close();
	}

	/**
	 * 删除用户
	 */
	public void deleteChatUser(ChatUser user) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("person", "account = ?", new String[] { user.account });
		db.close();
	}

	/**
	 * 根据用户身份，查询用户
	 * 
	 * @param classid
	 *            班级id
	 */
	public List<ChatUser> query(int classid) {
		// account,real_name ,head_image,classid,type,status
		ArrayList<ChatUser> users = new ArrayList<ChatUser>();
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor c = db.rawQuery("select * from user where classid= ?",
				new String[] { String.valueOf(classid) });
		while (c.moveToNext()) {
			ChatUser user = new ChatUser();
			user.account = c.getString(c.getColumnIndex("account"));
			user.userId = c.getInt(c.getColumnIndex("userId"));
			user.headPicUrl = c.getString(c.getColumnIndex("head_image"));
			user.classid = c.getInt(c.getColumnIndex("classid"));
			user.type = c.getInt(c.getColumnIndex("type"));
			user.status = c.getInt(c.getColumnIndex("status"));
			users.add(user);
		}
		c.close();
		return users;
	}

	/**
	 * 根据用户身份，查询用户
	 */
	// public Cursor queryTheCursor(int classid) {
	// SQLiteDatabase db = helper.getWritableDatabase();
	// Cursor c = db.rawQuery("select * from user where classid= ?",
	// new String[] { String.valueOf(classid) });
	// db.close();
	// return c;
	// }

	/**
	 * 根据用户账号判断数据库中是否有该用户
	 * 
	 * @param account
	 *            环信的username
	 * */
	public boolean hasUser(String account) {
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor c = db.rawQuery("select account from user where account=?",
				new String[] { account });
		if (c.moveToNext()) {
			return true;
		} else {
			return false;
		}
	}

	public ChatUser getChatUser(String account) {
		ChatUser chater = null;
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor c = db.rawQuery("select * from user where account=?",
				new String[] { account });
		if (c.moveToNext()) {
			chater = new ChatUser();
			chater.classid = c.getInt(c.getColumnIndex("classid"));
			chater.headPicUrl = c.getString(c.getColumnIndex("head_image"));
			chater.status = c.getInt(c.getColumnIndex("status"));
			chater.type = c.getInt(c.getColumnIndex("type"));
			chater.account = account;
		}
		return chater;
	}
}
