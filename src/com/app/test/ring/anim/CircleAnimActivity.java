package com.app.test.ring.anim;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

import com.app.test.R;

import java.util.Random;

public class CircleAnimActivity extends Activity {
    private CircleBar circleBar;
    private CircleView dvView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_anim);
        circleBar = (CircleBar) findViewById(R.id.circle);
        circleBar.setSweepAngle(360);
        circleBar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                circleBar.startCustomAnimation();
            }
        });
        new Handler().postDelayed(new Runnable() {
            public void run() {
                circleBar.setText("270");
            }
        }, 2000);
        dvView = findViewById(R.id.dv_view);
        dvView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dvView.setValue(new Random().nextInt(360));
            }
        });
    }

}
