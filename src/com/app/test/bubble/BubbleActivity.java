package com.app.test.bubble;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.app.test.R;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author lcx
 * Created at 2020.3.26
 * Describe:
 */
public class BubbleActivity extends Activity {
    private Random mRandom = new Random();
    private Timer mTimer = new Timer();
    private BubbleLayout mBubbleLayout;
    private ImageView iv_anim;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble);
        mBubbleLayout = (BubbleLayout) findViewById(R.id.heart_layout);
        iv_anim = (ImageView) findViewById(R.id.iv_anim);
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mBubbleLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mBubbleLayout.addHeart(randomColor());
                    }
                });
            }
        }, 500, 200);
        startAnim();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }

    private int randomColor() {
        return Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255));
    }

    private void startAnim() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(iv_anim, "rotation", 0, -15, 0, 15, 0, -15, 0, 15, 0, -15, 0, 15, 0, 0);
        anim.setRepeatCount(-1);
        anim.setDuration(800);
        anim.setInterpolator(new LinearInterpolator());
        anim.start();
    }
}
