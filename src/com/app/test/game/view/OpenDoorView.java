package com.app.test.game.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.test.R;
import com.app.test.util.DensityUtil;
import com.app.test.util.Utils;

/**
 * @author lcx
 * Created at 2020.1.2
 * Describe:
 */
public class OpenDoorView extends LinearLayout {
    private ImageView mLeft;
    private ImageView mRight;
    private OpenDoorListen openDoorListen;
    private int width = DensityUtil.getScreenWidth(getContext());
    private int animTime = 500;
    private ValueAnimator closeAnim;
    private ValueAnimator openAnim;

    public OpenDoorView(Context context) {
        super(context);
        initView();
    }

    public OpenDoorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public OpenDoorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void initView() {
        View.inflate(getContext(), R.layout.view_open_door_anim, this);
        mLeft = findViewById(R.id.iv_left);
        mRight = findViewById(R.id.iv_right);
        setVisibility(INVISIBLE);
        setClickable(false);
    }


    public void setOpenDoorListen(OpenDoorListen openDoorListen) {
        this.openDoorListen = openDoorListen;
    }

    /**
     * 开始关门动画
     */
    public void startCloseAnim() {
        stopAnim();
        setVisibility(VISIBLE);
        setClickable(true);
        if (closeAnim == null) {
            closeAnim = new ValueAnimator();
            closeAnim.setIntValues(-width, 0);
            closeAnim.setDuration(animTime);
            closeAnim.setInterpolator(new LinearInterpolator());
            closeAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int pointX = (int) animation.getAnimatedValue();
                    if (!Utils.isEmpty(mLeft))
                        mLeft.setX(pointX);
                    if (!Utils.isEmpty(mRight))
                        mRight.setX(-pointX);
                    if (pointX == 0) {
                        startOpenAnim();
                    }
                }
            });
        }
        closeAnim.start();
        if (!Utils.isEmpty(openDoorListen))
            openDoorListen.closeAnimStart();
    }

    /**
     * 开始开门动画
     */
    private void startOpenAnim() {
        if (Utils.isEmpty(openAnim)) {
            openAnim = new ValueAnimator();
            openAnim.setIntValues(0, width);
            openAnim.setDuration(animTime);
            openAnim.setInterpolator(new LinearInterpolator());
            openAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int pointX = (int) animation.getAnimatedValue();
                    if (!Utils.isEmpty(mLeft))
                        mLeft.setX(-pointX);
                    if (!Utils.isEmpty(mRight))
                        mRight.setX(pointX);
                    if (pointX == width) {
                        setVisibility(GONE);
                        if (openDoorListen != null) {
                            openDoorListen.openAnimEnd();
                        }
                    }
                }
            });
        }
        openAnim.start();
        if (!Utils.isEmpty(openDoorListen)) {
            openDoorListen.closeAnimEnd();
            openDoorListen.openAnimStart();
        }
    }


    private void stopAnim() {
        if (!Utils.isEmpty(openAnim) && openAnim.isRunning()) {
            openAnim.cancel();
        }
        if (!Utils.isEmpty(closeAnim) && closeAnim.isRunning()) {
            closeAnim.cancel();
        }
    }


    public abstract static class OpenDoorListen {
        public void openAnimStart() {
        }

        public void openAnimEnd() {
        }

        public void closeAnimStart() {
        }

        public void closeAnimEnd() {
        }
    }
}
