package com.app.test.network;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

public class HttpRequest {


    /**
     * 发送get请求
     *
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static String doGet(String url, int timeout)
            throws ClientProtocolException, IOException {
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = HttpClientHelper.getInstance()
                .getHttpClient(timeout).execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();
        return EntityUtils.toString(httpEntity, HTTP.UTF_8);
    }

    /**
     * 发送post请求
     *
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static String doPost(String url, List<NameValuePair> params,
                                int timeout) throws ClientProtocolException, IOException {
        String res;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            HttpResponse httpResponse = HttpClientHelper.getInstance()
                    .getHttpClient(timeout).execute(httpPost);
            res = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
        } catch (OutOfMemoryError e) {
            res = "{\"msg\": \"OutOfMemoryError\",\"status\": \"0\"}";
        } catch (SecurityException e) {
            res = "{\"msg\": \"获取访问网络权限失败\",\"status\": \"0\"}";
        }
        return res;
    }
}
