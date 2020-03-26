package com.app.test.radar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.app.test.R;

public class RadarActivity extends Activity {

    private RadarView radarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar);
        radarView = findViewById(R.id.radar);

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radarView.start();
            }
        });

        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radarView.stop();
            }
        });
        View view = findViewById(R.id.layout_ripple);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ViewGroup) v.getParent()).removeView(v);
            }
        });
    }
}
