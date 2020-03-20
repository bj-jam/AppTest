package com.app.test.showimage;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.test.R;
import com.app.test.utils.DisplayUtil;

/**
 * @author lcx
 * Created at 2020.1.4
 * Describe:
 */
public class GameToTipView extends RelativeLayout {

    private View targetView;
    private ImageView iv_move;
    private int margin = DisplayUtil.dip2px(getContext(), 5);

    public GameToTipView(Context context) {
        super(context);
        initView();
    }

    public GameToTipView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public GameToTipView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    protected void initView() {
        View.inflate(getContext(), R.layout.view_game_to_tips, this);
        iv_move = findViewById(R.id.iv_move);
    }


    protected void initData() {

    }


    protected void setViewListener() {

    }

    public void showView(View targetView) {
        this.targetView = targetView;
        int[] targetLocation = new int[2];
        targetView.getLocationOnScreen(targetLocation);
        setX(targetLocation[0] + targetView.getWidth() / 2);
        setY(targetLocation[1] - getStatusBarHeight(getContext()) + targetView.getHeight() / 2);
        playAnim();
    }

    private AnimatorSet animatorSet;

    private void playAnim() {
        animatorSet = new AnimatorSet();
        ObjectAnimator animX = ObjectAnimator.ofFloat(iv_move, "translationX", 0, margin, 0);
        ObjectAnimator animY = ObjectAnimator.ofFloat(iv_move, "translationY", 0, margin, 0);
        animX.setRepeatCount(-1);
        animX.setInterpolator(new LinearInterpolator());
        animY.setInterpolator(new LinearInterpolator());
        animY.setRepeatCount(-1);
        animatorSet.setDuration(500);
        animatorSet.playTogether(animX, animY);
        animatorSet.start();
    }

    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        if (isFlymeOs4x()) {
            return 2 * statusBarHeight;
        }

        return statusBarHeight;
    }

    public static boolean isFlymeOs4x() {
        String sysVersion = Build.VERSION.RELEASE;
        if ("4.4.4".equals(sysVersion)) {
            String sysIncrement = Build.VERSION.INCREMENTAL;
            String displayId = Build.DISPLAY;
            if (!TextUtils.isEmpty(sysIncrement)) {
                return sysIncrement.contains("Flyme_OS_4");
            } else {
                return displayId.contains("Flyme OS 4");
            }
        }
        return false;
    }


}
