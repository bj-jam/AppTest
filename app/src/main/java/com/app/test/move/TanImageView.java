package com.app.test.move;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;

import com.app.test.R;

/**
 * @author lcx
 * Created at 2020.1.2
 * Describe: 单选答题正确答案指示
 */
public class TanImageView extends androidx.appcompat.widget.AppCompatImageView {
    ObjectAnimator anim;


    public TanImageView(Context context) {
        super(context);
        initData();
    }

    public TanImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    public TanImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }


    private void initData() {
        setImageResource(R.drawable.ic_game_hand);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == VISIBLE)
            startAim();
    }

    public void startAim() {
        if (anim == null) {
            anim = ObjectAnimator.ofFloat(this, "translationY", 0, -getHeight() / 5, 0);
            anim.setDuration(200);
            anim.setRepeatCount(2);
        }
        anim.start();
    }
}