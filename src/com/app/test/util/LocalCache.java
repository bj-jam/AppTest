package com.app.test.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * 单例模式
 */
public class LocalCache {
    private Context context;

    public enum FileKey {
        ABOAT_PICTURE,
    }

    /*
     * 缓存的键名
     */
    public enum Key {
        ID, // 用户ID
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
        getSharedPreferences(fileKey.toString()).edit().putString(key.toString(), value).commit();
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
     * 获取批阅缓存的信息
     */
    public Map<String, ?> getAllReadOver() {
        if (context == null) {
            return null;
        }
        return getSharedPreferences(FileKey.ABOAT_PICTURE.toString()).getAll();
    }

    /**
     * 必须清空的配置文件，包括：视频历史信息
     */


}
