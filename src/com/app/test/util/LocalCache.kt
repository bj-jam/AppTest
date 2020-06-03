package com.app.test.util

import android.content.Context
import android.content.SharedPreferences

/**
 * 单例模式
 */
class LocalCache {
    private var context: Context? = null

    enum class FileKey {
        ABORT_PICTURE
    }

    /*
     * 缓存的键名
     */
    enum class Key {
        imageDir
        // 附件图片所在目录
    }

    private fun getSp(ctx: Context, name: String): SharedPreferences {
        return ctx.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    fun init(context: Context?) {
        this.context = context
    }

    private fun getSharedPreferences(fileKey: String): SharedPreferences {
        return context!!.getSharedPreferences(fileKey, Context.MODE_PRIVATE)
    }

    /*
     * 保存数据到本地，只提供字符串类型数据保存，其他类型请格式化，例如int型
     */
    fun putCache(fileKey: FileKey, key: Key, value: String?) {
        if (context == null) {
            return
        }
        getSharedPreferences(fileKey.toString()).edit().putString(key.toString(), value).apply()
    }

    /*
     * 获取本地字符串数据，其他类型请格式化，例如int型
     */
    fun getCache(fileKey: FileKey, key: Key): String? {
        return if (context == null) {
            null
        } else getSharedPreferences(fileKey.toString()).getString(
                key.toString(), null)
    }

    /**
     * 获取批阅缓存的信息
     */
    val allReadOver: Map<String, *>?
        get() = if (context == null) {
            null
        } else getSharedPreferences(FileKey.ABORT_PICTURE.toString()).all

    companion object {
        private var localCache: LocalCache? = null

        @JvmStatic
        val instance: LocalCache?
            get() {
                if (localCache == null) {
                    localCache = LocalCache()
                }
                return localCache
            }
    }
}