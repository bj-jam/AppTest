package com.app.test.pickcrop;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.app.test.R;
import com.app.test.util.Utils;


public class PickCropActivity extends Activity {

    private static final int REQUEST_PICK_MEDIA = 101;
    private static final int REQUEST_REQUEST_PERMISSION = 201;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_crop);

        WebViewClient client = new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }
        };
        mWebView.setWebViewClient(client);
//
        WebSettings settings = mWebView.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setAllowFileAccess(true);
//
        findViewById(R.id.pick_media).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
        findViewById(R.id.take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
        findViewById(R.id.capture_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureVideo();
            }
        });
        findViewById(R.id.pick_and_crop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickAndCrop();
            }
        });
        if (!Utils.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_REQUEST_PERMISSION);
        }
        if (!Utils.hasPermission(this, Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_REQUEST_PERMISSION);
        }

    }

    @Override
    protected void onPause() {
        mWebView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mWebView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mWebView.destroy();
        super.onDestroy();
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mWebView = (WebView) findViewById(R.id.webview);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PICK_MEDIA) {
            if (resultCode == RESULT_OK) {
                StringBuilder hb = new StringBuilder();
                hb.append("<!DOCTYPE html>");
                hb.append("<html>");
                hb.append("<head>");
                hb.append("<meta charset=\"UTF-8\">");
                hb.append("</head>");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ClipData clipData = data.getClipData();
                    for (int i = 0, j = clipData.getItemCount(); i < j; i++) {
                        final ClipData.Item item = clipData.getItemAt(i);
                        final Uri uri = item.getUri();
                        final String type = getContentResolver().getType(uri);
                        if (type != null && type.startsWith("video/")) {
                            hb.append("<video src='");
                            hb.append(uri.toString());
                            hb.append("'/>");
                        } else {
                            hb.append("<img src='");
                            hb.append(uri.toString());
                            hb.append("'/>");
                        }
                        hb.append("</br>");
                    }
                } else {
                    final String type = data.resolveType(this);
                    if (type != null && type.startsWith("video/")) {
                        hb.append("<video src='");
                        hb.append(data.getDataString());
                        hb.append("'/>");
                    } else {
                        hb.append("<img src='");
                        hb.append(data.getDataString());
                        hb.append("'/>");
                    }
                }

                hb.append("</html>");
                mWebView.loadDataWithBaseURL("http://example.com/", hb.toString(), "text/html", "UTF-8", null);
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void takePhoto() {
        final Intent intent = MediaPickerActivity.with(this)
                .takePhoto()
                .build();
        startActivityForResult(intent, REQUEST_PICK_MEDIA);
    }

    private void captureVideo() {
        final Intent intent = MediaPickerActivity.with(this)
                .captureVideo()
                .videoQuality(0)
                .build();
        startActivityForResult(intent, REQUEST_PICK_MEDIA);
    }

    private void pickImage() {
        final Intent intent = MediaPickerActivity.with(this)
                .pickMedia()
                .containsVideo(true)
                .videoOnly(false)
                .allowMultiple(true)
                .build();
        startActivityForResult(intent, REQUEST_PICK_MEDIA);
    }

    private void pickAndCrop() {
        final Intent intent = MediaPickerActivity.with(this)
                .pickMedia()
                .containsVideo(false)
                .allowMultiple(false)
                .aspectRatio(1, 1)
                .maximumSize(512, 512)
                .build();
        startActivityForResult(intent, REQUEST_PICK_MEDIA);
    }

}
