package com.app.test.network;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.app.test.base.App;
import com.app.test.base.BaseUtil;
import com.app.test.util.LogFileUtil;

import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Set;

/**
 * 网络请求工具类
 */
public class JsonCallback implements CommonCallback<String> {

    private Handler handler;
    //请求地址链接
    private String url;
    // 请求参数
    private HashMap<String, String> params;
    //用于标示请求链接，区分是哪个请求
    private int what;
    //用于标示请求链接的辅助参数
    private int arg1;
    // 用于标示请求链接的辅助参数
    private int arg2;
    //请求超时时间，15秒
    private int timeout = 15;

    private String requestUrl = "";

    public JsonCallback(Handler handler, String url, HashMap<String, String> params, int what) {
        this(handler, url, params, what, 0, 0);
    }

    public JsonCallback(Handler handler, String url, HashMap<String, String> params, int what, int arg1) {
        this(handler, url, params, what, arg1, 0);
    }

    public JsonCallback(Handler handler, String url, HashMap<String, String> params, int what, int arg1, int arg2) {
        this.handler = handler;
        this.url = url;
        this.params = params;
        this.what = what;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    //设置请求超时时长，
    public JsonCallback initTimeout(String timeout) {
        if (!TextUtils.isEmpty(timeout) && Integer.parseInt(timeout) > 0) {
            this.timeout = Integer.parseInt(timeout);
        }
        return this;
    }

    /**
     * 请求处理
     */
    public void request() {
        if (NetWorkUtils.isNetworkAvailable(App.context)) {// 网络状态正常，主要是手机能够连接到路由器
            if (params == null || params.isEmpty()) {// 无参请求
                doGet();
            } else {// 有参请求
                doPost();
            }
        } else {// 网络状态异常，主要是手机无网络状态
            Message msg = handler.obtainMessage();
            msg.arg1 = arg1;
            msg.arg2 = arg2;
            msg.what = BaseUtil.noNet;
            handler.sendMessage(msg);
        }
    }

    /**
     * 发送get无参请求
     */
    public void doGet() {
        RequestParams rp = new RequestParams(url);
        // 插件工具默认为15000毫秒
        rp.setConnectTimeout(timeout * 1000);

        requestUrl = url;
        x.http().get(rp, this);
    }

    /**
     * 发送post有参请求
     */
    public void doPost() {
        RequestParams rp = new RequestParams(url);
        // 插件工具默认为15000毫秒
        rp.setConnectTimeout(timeout * 1000);

        Set<String> set = params.keySet();
        for (String str : set) {
            rp.addBodyParameter(str, params.get(str));
            // rp.addQueryStringParameter(str, params.get(str));
        }

        String param = url + "?";
        Object[] objs = set.toArray();
        for (int i = 0; i < objs.length; i++) {
            param += objs[i] + "=" + params.get(objs[i]);
            if (i != objs.length - 1) {
                param += "&";
            }
        }
        if (x.isDebug()) {
            Log.v("net-" + timeout, param);
        }

        requestUrl = param;
        x.http().post(rp, this);
    }

    @Override
    public void onCancelled(CancelledException arg0) {

    }

    @Override
    public void onError(Throwable arg0, boolean arg1) {
        Message msg = handler.obtainMessage();
        if (arg0 instanceof SocketTimeoutException) { // 连接超时
            msg.what = BaseUtil.timeout;
        } else if (arg0 instanceof ConnectException) {// 网络连接异常
            msg.what = BaseUtil.connectException;
        } else {// 其他连接异常
            msg.what = BaseUtil.other;
        }
        msg.arg1 = this.arg1;
        msg.arg2 = this.arg2;
        handler.sendMessage(msg);

        // 记录请求内容
        LogFileUtil.init().writeLog(requestUrl, LogFileUtil.urlException, true);

        // 记录异常内容
        String log = arg0.getMessage() + "\n";
        StackTraceElement[] stackTraces = arg0.getStackTrace();
        for (StackTraceElement stackTrace : stackTraces) {
            log += stackTrace.toString() + "\n";
        }
        LogFileUtil.init().writeLog(log, LogFileUtil.urlException, false);

        arg0.printStackTrace();
    }

    @Override
    public void onFinished() {
    }

    @Override
    public void onSuccess(String result) {
        if (x.isDebug()) {
            Log.v("Gson" + timeout, result + "");
        }

        Message msg = handler.obtainMessage();
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        if (TextUtils.isEmpty(result)) {// 返回值为空
            msg.what = BaseUtil.jsonException;// 格式解析异常
            handler.sendMessage(msg);
            return;
        }

        try {
            if (result.startsWith("foo(")) {
                result = result.subSequence(4, result.length() - 1).toString().replace("httpCode", "status");
            }
            // 直播数据上报结果处理
            if ("success".equals(result)) {
                msg.obj = result;
                msg.what = what;
            } else {// 正常结果处理
                ResultResponse resultR = BaseUtil.initGson().fromJson(result, ResultResponse.class);

                if (resultR == null) {
                    msg.what = BaseUtil.jsonException;// 格式解析异常
                } else if ("200".equals(resultR.status)) {
                    msg.obj = result;
                    msg.what = what;
                } else {// 返回状态不是200
                    msg.obj = resultR.msg;
                    msg.what = BaseUtil.not200;
                    Bundle b = new Bundle();
                    b.putString("json", result);
                    msg.setData(b);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg.what = BaseUtil.jsonException;// 格式解析异常
        } finally {
            handler.sendMessage(msg);
        }
    }

    private class ResultResponse {
        private String status;
        private String msg;
    }
}
