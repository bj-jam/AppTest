package com.app.test.base

import com.google.gson.Gson

object BaseUtil {
    /** 连接超时  */
    const val timeout = -1

    /** 请求正常，返回值不是200  */
    const val not200 = timeout - 1

    /** 无网络状态  */
    const val noNet = not200 - 1

    /** 数据解析异常  */
    const val jsonException = noNet - 1

    /** 网络连接异常  */
    const val connectException = jsonException - 1

    /** 其他请求异常  */
    const val other = connectException - 1

    /** json数据解析工具  */
    var gson: Gson? = null

    /** 初始化json数据解析工具  */
	@JvmStatic
	fun initGson(): Gson? {
        if (gson == null) {
            gson = Gson()
        }
        return gson
    }
}