package com.app.test.bubble;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble);
        mBubbleLayout = (BubbleLayout) findViewById(R.id.heart_layout);
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }

    private int randomColor() {
        return Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255));
    }
}
