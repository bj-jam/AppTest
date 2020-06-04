package com.app.test.network

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import com.app.test.base.App
import com.app.test.base.BaseUtil
import com.app.test.base.BaseUtil.initGson
import com.app.test.util.LogFileUtil
import org.xutils.common.Callback.CancelledException
import org.xutils.common.Callback.CommonCallback
import org.xutils.http.RequestParams
import org.xutils.x
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.*

/**
 * 网络请求工具类
 */
class JsonCallback @JvmOverloads constructor(private val handler: Handler, //请求地址链接
                                             private val url: String, // 请求参数
                                             private val params: HashMap<String, String>?, //用于标示请求链接，区分是哪个请求
                                             private val what: Int, //用于标示请求链接的辅助参数
                                             private var arg1: Int? = 0, // 用于标示请求链接的辅助参数
                                             private var arg2: Int? = 0) : CommonCallback<String> {

    //请求超时时间，15秒
    private var timeout = 15
    private var requestUrl = ""

    //设置请求超时时长，
    fun initTimeout(timeout: String): JsonCallback {
        if (!TextUtils.isEmpty(timeout) && timeout.toInt() > 0) {
            this.timeout = timeout.toInt()
        }
        return this
    }

    /**
     * 请求处理
     */
    fun request() {
        if (isNetworkAvailable(App.context)) { // 网络状态正常，主要是手机能够连接到路由器
            if (params == null || params.isEmpty()) { // 无参请求
                doGet()
            } else { // 有参请求
                doPost()
            }
        } else { // 网络状态异常，主要是手机无网络状态
            val msg = handler.obtainMessage()
            msg.arg1 = arg1 ?: 0
            msg.arg2 = arg2 ?: 0
            msg.what = BaseUtil.noNet
            handler.sendMessage(msg)
        }
    }

    /**
     * 发送get无参请求
     */
    private fun doGet() {
        val rp = RequestParams(url)
        // 插件工具默认为15000毫秒
        rp.connectTimeout = timeout * 1000
        requestUrl = url
        x.http().get(rp, this)
    }

    /**
     * 发送post有参请求
     */
    private fun doPost() {
        val rp = RequestParams(url)
        // 插件工具默认为15000毫秒
        rp.connectTimeout = timeout * 1000
        val set: Set<String> = params!!.keys
        for (str in set) {
            rp.addBodyParameter(str, params[str])
        }
        val param = StringBuilder("$url?")
        val objs: Array<Any> = set.toTypedArray()
        for (i in objs.indices) {
            param.append(objs[i]).append("=").append(params[objs[i]])
            if (i != objs.size - 1) {
                param.append("&")
            }
        }
        if (x.isDebug()) {
            Log.v("net-$timeout", param.toString())
        }
        requestUrl = param.toString()
        x.http().post(rp, this)
    }

    override fun onCancelled(arg0: CancelledException) {}
    override fun onError(arg0: Throwable, arg1: Boolean) {
        val msg = handler.obtainMessage()
        if (arg0 is SocketTimeoutException) { // 连接超时
            msg.what = BaseUtil.timeout
        } else if (arg0 is ConnectException) { // 网络连接异常
            msg.what = BaseUtil.connectException
        } else { // 其他连接异常
            msg.what = BaseUtil.other
        }
        msg.arg1 = this.arg1 ?: 0
        msg.arg2 = arg2 ?: 0
        handler.sendMessage(msg)

        // 记录请求内容
        LogFileUtil.writeLog(requestUrl, LogFileUtil.urlException, true)

        // 记录异常内容
        var log = """
            ${arg0.message}

            """.trimIndent()
        val stackTraces = arg0.stackTrace
        for (stackTrace in stackTraces) {
            log += """
                $stackTrace

                """.trimIndent()
        }
        LogFileUtil.writeLog(log, LogFileUtil.urlException, false)
        arg0.printStackTrace()
    }

    override fun onFinished() {}
    override fun onSuccess(result: String) {
        var result = result
        if (x.isDebug()) {
            Log.v("Gson$timeout", result + "")
        }
        val msg = handler.obtainMessage()
        msg.arg1 = arg1 ?: 0
        msg.arg2 = arg2 ?: 0
        if (TextUtils.isEmpty(result)) {
            msg.what = BaseUtil.jsonException
            handler.sendMessage(msg)
            return
        }
        try {
            if (result.startsWith("foo(")) {
                result = result.subSequence(4, result.length - 1).toString().replace("httpCode", "status")
            }
            if ("success" == result) {
                msg.obj = result
                msg.what = what
            } else {
                val resultR = initGson()!!.fromJson(result, ResultResponse::class.java)
                if (resultR == null) {
                    msg.what = BaseUtil.jsonException // 格式解析异常
                } else if ("200" == resultR.status) {
                    msg.obj = result
                    msg.what = what
                } else { // 返回状态不是200
                    msg.obj = resultR.msg
                    msg.what = BaseUtil.not200
                    val b = Bundle()
                    b.putString("json", result)
                    msg.data = b
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            msg.what = BaseUtil.jsonException // 格式解析异常
        } finally {
            handler.sendMessage(msg)
        }
    }

    private class ResultResponse {
        val status: String? = null
        val msg: String? = null
    }

    private fun isNetworkAvailable(context: Context?): Boolean {
        val manager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                ?: return false
        val network = manager.activeNetworkInfo
        return network != null && network.isAvailable
    }

}