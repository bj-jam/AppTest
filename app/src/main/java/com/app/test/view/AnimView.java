package com.app.test.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 饼图
 *
 * @author jam
 */
public class AnimView extends ImageView {
    AnimatorSet animatorSet = new AnimatorSet();
    ObjectAnimator scaleX;
    ObjectAnimator scaleY;

    public AnimView(Context context) {
        super(context);
        init();
    }

    public AnimView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        scaleX = ObjectAnimator.ofFloat(this, "scaleX", 1f, 1.2f, 1f);
        scaleY = ObjectAnimator.ofFloat(this, "scaleY", 1f, 1.2f, 1f);
        scaleY.setRepeatCount(-1);
        scaleX.setRepeatCount(-1);
    }

    public void startAnim() {
        animatorSet.setDuration(1000);
        animatorSet.play(scaleX).with(scaleY);//两个动画同时开始
        animatorSet.start();
    }

    public void stopAnim() {
        animatorSet.end();
    }
}
