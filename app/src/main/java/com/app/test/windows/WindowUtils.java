package com.app.test.windows;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.test.R;
import com.app.test.base.App;
import com.app.test.rotate.MyView;
import com.app.test.util.DensityUtil;

/**
 * 弹窗辅助类
 *
 * @ClassName WindowUtils
 * Created by Administrator on 2017/6/29.
 */
public class WindowUtils implements Handler.Callback, View.OnClickListener {

    private View mView = null;
    private WindowManager mWindowManager = null;
    private Context mContext = null;
    private LayoutParams params;
    public Boolean isShown = false;
    private MyView myView;
    private TextView videoName;
    private ImageView closeView;
    private Activity activity;
//    private Handler handler;

    // 图片的转动动画
    private ObjectAnimator mRotateAni;


    private static WindowUtils instance;

    private WindowUtils(Context context) {
        init(context);
    }

    /**
     * @param context
     * @return
     */
    public static WindowUtils getInstance(Context context) {
        if (instance == null) {
            synchronized (WindowUtils.class) {
                if (instance == null) {
                    instance = new WindowUtils(context);
                }
            }
        }
        return instance;
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        // 获取应用的Context
        mContext = context.getApplicationContext();
        // 获取WindowManager
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        //获取view
        mView = View.inflate(context, R.layout.view_windows, null);
        myView = (MyView) mView.findViewById(R.id.myView);
        mView.findViewById(R.id.collectVideo).setOnClickListener(this);
        mView.findViewById(R.id.shareVideo).setOnClickListener(this);
        videoName = (TextView) mView.findViewById(R.id.videoName);
        closeView = (ImageView) mView.findViewById(R.id.closeView);
        closeView.setOnClickListener(this);
        // 获取params
        params = new LayoutParams();
        // 类型
        params.type = LayoutParams.TYPE_SYSTEM_ERROR;
        // 设置flag

        int flags = LayoutParams.FLAG_NOT_TOUCH_MODAL ;
        // 如果设置了WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，弹出的View收不到Back键的事件
        params.flags = flags;
        // 不设置这个弹出框的透明遮罩显示为黑色
        params.format = PixelFormat.RGBA_8888;
        params.width = App.sWidth;
        params.gravity = Gravity.CENTER;
        params.y = 0;
        params.x = 0;
        params.height = App.sHeight;

        mRotateAni = ObjectAnimator.ofFloat("", "rotation", 0f, 360f);
        mRotateAni.setDuration(7200);
        mRotateAni.setInterpolator(new LinearInterpolator());
        mRotateAni.setRepeatMode(ValueAnimator.RESTART);
        mRotateAni.setRepeatCount(ValueAnimator.INFINITE);
    }

    /**
     * 显示弹出框
     * 在屏幕的最底部
     */
    public void showPopupWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(activity)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + mContext.getPackageName()));
            activity.startActivityForResult(intent, 10);
            return;
        }
        if (isShown) {
            return;
        }
        isShown = true;
        mWindowManager.addView(mView, params);

    }

    public void setDetailActivity(Activity activity) {
        this.activity = activity;
    }

    /**
     * 更新悬浮窗的位置
     *
     * @param y
     */
    private void updateView(int y) {
        if (y >= 0 && isShown && null != mView && mWindowManager != null && params != null) {
//            this.y = y;
//            mView.setVisibility(View.GONE);
            params.y = DensityUtil.dp2px(y);
            mWindowManager.updateViewLayout(mView, params);
//            handler.sendEmptyMessageDelayed(1, 1500);
        }
    }


    /**
     * 隐藏弹出框
     */
    public void hidePopupWindow() {
        if (isShown && null != mView) {
            mWindowManager.removeView(mView);
            isShown = false;
        }
    }

    /**
     * 悬浮框是否显示
     *
     * @return
     */
    public boolean isShow() {
        return isShown;
    }

    /**
     * 设置播放信息
     *
     * @param drawable
     * @param playTime
     * @param allTime
     */
    public void setInfo(BitmapDrawable drawable, int playTime, int allTime) {
        showPopupWindow();
        if (playTime % 2 == 0) {
            updateView(50);
        } else {
            updateView(0);
        }
        myView.setInfo(drawable, playTime, allTime);
    }

    /**
     * 播放停止了，动画也要停止
     */
    public void stopAni() {
        if (myView == null) {
            return;
        }
        myView.stopAni();
    }

    /**
     * 播放开始了，动画也开始
     */
    public void startAni() {
        if (myView == null) {
            return;
        }
        myView.startAni();
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (null != mView) {
            mView.setVisibility(View.VISIBLE);
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.closeView) {
            hidePopupWindow();
        } else if (v.getId() == R.id.shareVideo) {
            Toast.makeText(mContext, "分享", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.collectVideo) {
            Toast.makeText(mContext, "收藏", Toast.LENGTH_SHORT).show();
        }
    }
}
