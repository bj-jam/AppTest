package com.app.test.floatwindow;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.app.test.R;
import com.app.test.circle.CircleActivity;

/**
 *
 */
public class FloatWindowManager {

    private static final String TAG = FloatWindowManager.class.getName();

    private static FloatWindowManager instance;
    private Application mApplication;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams params;
    private boolean isShowing;
    private boolean hasShowPermission;//是否显示过权限弹框

    private FloatWindowManager() {

    }

    public static FloatWindowManager getInstance() {
        if (instance == null) {
            synchronized (FloatWindowManager.class) {
                if (instance == null) {
                    instance = new FloatWindowManager();
                }
            }
        }
        return instance;
    }

    public FloatWindowManager init(Application mApplication) {
        this.mApplication = mApplication;
        this.mWindowManager = (WindowManager) mApplication.getSystemService(Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= 26) {//8.0新特性
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        params.packageName = mApplication.getPackageName();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.format = PixelFormat.RGBA_8888;
        params.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        view = View.inflate(mApplication, R.layout.dialog_top_all_app_top, null);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.e("jam", "onKey: " + keyCode);
                return false;
            }
        });
        view.findViewById(R.id.close_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
        return instance;
    }

    public void show() {
        if (isShowing)
            return;
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(mApplication.getApplicationContext())) {
                if (hasShowPermission || null == mApplication) {
                    return;
                }
                Intent intent = new Intent(mApplication, CircleActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mApplication.startActivity(intent);
                hasShowPermission = true;
                return;
            } else {
                //Android6.0以上
                showView();
            }
        } else {
            //Android6.0以下，不用动态声明权限
            showView();
        }
    }

    View view;

    public void showView() {

        if (view.getParent() == null && params != null) {//没有添加
            view.setVisibility(View.VISIBLE);
            mWindowManager.addView(view, params);
            isShowing = true;
        }
    }

    public void hide() {
        if (view != null) {
            if (view.getParent() != null) {//有添加
                mWindowManager.removeViewImmediate(view);
                isShowing = false;
            }
        }
    }

}
