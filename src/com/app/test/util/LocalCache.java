package com.app.test.util;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 单例模式
 */
public class LocalCache {
    private Context context;

    /*
     * 缓存的文件名
     */
    public enum FileKey {
        USERINFO, // 用户信息退出后删除
        ABOAT_PICTURE, // 图片保存,清除
        READOVER_VALUE, // 批阅信息
    }

    /*
     * 缓存的键名
     */
    public enum Key {
        /**
         * 用户信息
         */
        ACCOUNT, // 账号
        EMAIL, // 邮箱地址
        HEAD_PIC, // 头像
        ID, // 用户ID
        INTRODUCTON, // 个人介绍
        PHONE, // 电话
        REAL_NAME, // 真实名字
        SEX, // 性别
        SOURCE, // 来源
        /**
         * 图片
         */
        imageName, // 图片名不带后缀
        imageSuffix, // 图片后缀名
        imageNameSuffix, // 图片名带后缀
        imageDir, // 附件图片所在目录
        imagePath, // 图片完整路径和名称
    }

    private static LocalCache localCache;

    private LocalCache() {

    }

    private SharedPreferences getSp(Context ctx, String name) {
        return ctx.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static LocalCache getInstance() {
        if (localCache == null) {
            localCache = new LocalCache();
        }
        return localCache;
    }

    public void init(Context context) {
        this.context = context;
        config_delete = getSp(context, "config_delete");
        config_progress = getSp(context, "config_progress");
        config_liveReplay = getSp(context, "config_liveReplay");
    }

    private SharedPreferences getSharedPreferences(String fileKey) {
        return context.getSharedPreferences(fileKey, Context.MODE_PRIVATE);
    }

    /*
     * 保存数据到本地，只提供字符串类型数据保存，其他类型请格式化，例如int型
     */
    public void putCache(FileKey fileKey, Key key, String value) {
        if (context == null) {
            return;
        }
        getSharedPreferences(fileKey.toString()).edit()
                .putString(key.toString(), value).commit();
    }

    /*
     * 获取本地字符串数据，其他类型请格式化，例如int型
     */
    public String getCache(FileKey fileKey, Key key) {
        if (context == null) {
            return null;
        }
        return getSharedPreferences(fileKey.toString()).getString(
                key.toString(), null);
    }

    /**
     * 保存数据到本地，只提供字符串类型数据保存，其他类型请格式化，例如int型
     *
     * @param fileKey
     * @param key
     * @param string  招生id、用户Id
     * @param value
     */
    public void putKeyCache(FileKey fileKey, Key key, String string,
                            String value) {
        if (context == null) {
            return;
        }
        getSharedPreferences(fileKey.toString()).edit()
                .putString(key.toString() + string, value).commit();
    }

    /**
     * 获取本地字符串数据，其他类型请格式化，例如int型
     *
     * @param fileKey
     * @param string
     * @param key
     * @return
     */
    public String getKeyCache(FileKey fileKey, Key key, String string) {
        if (context == null) {
            return null;
        }
        return getSharedPreferences(fileKey.toString()).getString(
                key.toString() + string, null);
    }

    /*
     * 保存boolean数据到本地
     */
    public void putbooleanCache(FileKey fileKey, Key key, boolean value) {
        if (context == null) {
            return;
        }
        getSharedPreferences(fileKey.toString()).edit()
                .putBoolean(key.toString(), value).commit();
    }

    /*
     * 获取本地boolean数据
     */
    public boolean getbooleanCache(FileKey fileKey, Key key, boolean value) {
        if (context == null) {
            return false;
        }
        return getSharedPreferences(fileKey.toString()).getBoolean(
                key.toString(), value);
    }

    /*
     * 保存int数据到本地
     */
    public void putIntCache(FileKey fileKey, Key key, String s, int value) {
        if (context == null) {
            return;
        }
        getSharedPreferences(fileKey.toString()).edit()
                .putInt(key.toString() + s, value).commit();
    }

    /*
     * 获取本地int数据
     */
    public int getIntCache(FileKey fileKey, Key key, String s, int value) {
        if (context == null) {
            return 0;
        }
        return getSharedPreferences(fileKey.toString()).getInt(
                key.toString() + s, value);
    }

    /**
     * 记录消息条数
     *
     * @param type 消息模块名
     */
    public void putUnreadMsgCount(FileKey fileKey, String functionName) {
        if (context == null) {
            return;
        }
        int num = getSharedPreferences(fileKey.toString()).getInt(functionName,
                0);
        getSharedPreferences(fileKey.toString()).edit()
                .putInt(functionName, (num + 1)).commit();
    }

    /**
     * 获取消息条数
     *
     * @param type 消息模块名
     */

    public int getUnreadMsgCount(FileKey fileKey, String functionName) {
        if (context == null) {
            return -1;
        }
        return getSharedPreferences(fileKey.toString()).getInt(functionName, 0);
    }

    /**
     * 清除消息条数
     *
     * @param type 消息模块名
     */
    public void clearUnreadMsgCount(FileKey fileKey, String functionName) {
        if (context == null) {
            return;
        }
        getSharedPreferences(fileKey.toString()).edit().remove(functionName)
                .commit();
    }

    /*
     * 保存用户信息到本地
     */
    public void setUserInfo(String userJson) {
        try {
            JSONObject user = new JSONObject(userJson).getJSONObject("rt");
            if (context == null) {
                return;
            }
            putCache(FileKey.USERINFO, Key.ACCOUNT, user.getString("account"));
            putCache(FileKey.USERINFO, Key.EMAIL, user.getString("email"));
            putCache(FileKey.USERINFO, Key.HEAD_PIC, user.getString("headPic"));
            putCache(FileKey.USERINFO, Key.ID, user.getString("id"));
            putCache(FileKey.USERINFO, Key.SOURCE, user.getString("source"));
            putCache(FileKey.USERINFO, Key.INTRODUCTON,
                    user.getString("introduction"));
            putCache(FileKey.USERINFO, Key.PHONE, user.getString("phone"));

            putCache(FileKey.USERINFO, Key.REAL_NAME,
                    user.getString("realName"));
            putCache(FileKey.USERINFO, Key.SEX, user.getString("sex"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 退出时清除的信息
    public void clearInfo(FileKey fileKey) {
        if (context == null) {
            return;
        }
        getSharedPreferences(fileKey.toString()).edit().clear().commit();
    }

    /**
     * 用于特殊类型变量存储
     */
    public void putCache(FileKey fileKey, String key, String value) {
        if (context == null) {
            return;
        }
        getSharedPreferences(fileKey.toString()).edit()
                .putString(key.toString(), value).commit();
    }

    /**
     * 用于特殊类型变量存储
     */
    public String getCache(FileKey fileKey, String key) {
        if (context == null) {
            return null;
        }
        return getSharedPreferences(fileKey.toString()).getString(
                key.toString(), null);
    }

    /**
     * 获取批阅缓存的信息
     */
    public Map<String, ?> getAllReadOver() {
        if (context == null) {
            return null;
        }
        return getSharedPreferences(FileKey.READOVER_VALUE.toString()).getAll();
    }

    /**
     * 必须清空的配置文件，包括：视频历史信息
     */
    private SharedPreferences config_delete;
    private SharedPreferences config_progress;
    /**
     * 保存直播回放的播放进度
     */
    private SharedPreferences config_liveReplay;

    public String getPicMess(String key, String defValue) {
        return config_delete.getString(key, defValue);
    }

    public void setPicMess(String key, String value) {
        config_delete.edit().putString(key, value).commit();
    }

    public String getProMess(String key, String defValue) {
        return config_progress.getString(key, defValue);
    }

    public void setProMess(String k, String v) {
        config_progress.edit().putString(k, v).commit();
    }

    /**
     * 获取课程历史视频-退出登录需要删除
     */
    public String getVideo(String key) {
        return config_delete.getString(key, null);
    }

    /**
     * 保存课程历史视频-退出登录需要删除
     */
    public void setVideo(String key, String value) {
        config_delete.edit().putString(key, value).commit();
    }

    /**
     * 清除投票答案
     */
    public void clearDelete(String key) {
        config_delete.edit().remove(key).commit();
    }

    /**
     * 获取直播回放的播放进度<br>
     * liveReplay_progress_[userId]_[liveCourseId]_[videoId]
     */
    public String getLiveReplay(String key, String defValue) {
        return config_liveReplay.getString(key, defValue);
    }

    /**
     * 保存直播回放的播放进度<br>
     * liveReplay_progress_[userId]_[liveCourseId]_[videoId]
     */
    public void setLiveReplay(String key, String value) {
        config_liveReplay.edit().putString(key, value).commit();
    }
}
