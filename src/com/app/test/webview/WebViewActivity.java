package com.app.test.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.app.test.R;

/**
 * Created by jam on 17/4/28.
 */

public class WebViewActivity extends Activity {
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initView();
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    private void initView() {
        mWebView = (WebView) findViewById(R.id.webView);


        removeAllCookie();
        // 放大缩小按钮
        mWebView.getSettings().setBuiltInZoomControls(false);
        // 使Webview具有标准视口(如普通桌面浏览器)
        mWebView.getSettings().setUseWideViewPort(true);
        // 能够执行Javascript脚本
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            mWebView.getSettings().setLoadsImagesAutomatically(false);
        }
        mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        // 添加JS交互接口类
        mWebView.addJavascriptInterface(new AppWebView(), "appWebView");
        // 当前webview中跳转

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!TextUtils.isEmpty(url) && url.contains("tel")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {

            }

        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            // 只有确定按钮的提示框
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     final JsResult result) {
                return super.onJsAlert(view, url, message, result);

            }

            // 有确定、取消按钮的提示框
            @Override
            public boolean onJsConfirm(WebView view, String url,
                                       String message, final JsResult result) {
                return onJsConfirm(view, url, message, result);
            }

        });
        mWebView.loadUrl("http://192.168.70.112:8020/StudyHtml/js/android.html");
    }

    /**
     * 清除cookie
     */
    private void removeAllCookie() {
        CookieSyncManager cookieSyncManager = CookieSyncManager
                .createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();
        cookieManager.removeAllCookie();
        cookieSyncManager.sync();
    }

    public class AppWebView {
        @JavascriptInterface
        public void getString(final String info) {
            Toast.makeText(WebViewActivity.this, info + "--", Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public String postString() {
            return "android和web交互";
        }

        @JavascriptInterface
        public void attenStatusReload(String teacherId, boolean isAttened) {
            Log.e("jam", teacherId + "======" + isAttened);
        }
    }
}
