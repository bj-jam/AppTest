package com.app.test.oknet;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author lcx
 * Created at 2020.4.24
 * Describe:
 */
public class OkHttpActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void toGet() {
        String url = "https://www.baidu.com";
        //1,创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();

        //URL带的参数
        HashMap<String, String> params = new HashMap<>();
//GET 请求带的Header
        HashMap<String, String> headers = new HashMap<>();
//HttpUrl.Builder构造带参数url
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        if (params != null) {
            for (String key : params.keySet()) {
                urlBuilder.setQueryParameter(key, params.get(key));
            }
        }
        //2,创建一个Request
        final Request request = new Request.Builder()
//                .url(url)
                .url(urlBuilder.build())
                .headers(headers == null ? new Headers.Builder().build() : Headers.of(headers))
                .get()
                .build();
        //3,新建一个call对象
        Call call = mOkHttpClient.newCall(request);
        //4，请求加入调度，这里是异步Get请求回调
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) {

            }
        });

    }

    public void toPost() {
        //POST参数构造MultipartBody.Builder，表单提交
        HashMap<String, String> params = new HashMap<>();
        MultipartBody.Builder urlBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        for (String key : params.keySet()) {
            if (params.get(key) != null) {
                urlBuilder.addFormDataPart(key, params.get(key));
            }
            //urlBuilder.addFormDataPart(key, params.get(key));
        }
        //1,创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        // 构造Request->call->执行
        Request request = new Request.Builder()
//                .headers(extraHeaders == null ? new Headers.Builder().build() : Headers.of(extraHeaders))//extraHeaders 是用户添加头
//                .url(url)
                .post(urlBuilder.build())//参数放在body体里
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }
}
