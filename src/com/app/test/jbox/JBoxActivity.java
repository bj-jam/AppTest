package com.app.test.jbox;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.app.test.R;

/**
 * Author : wudu
 * Time : 2017/7/23.
 * Tips :
 */

public class JBoxActivity extends Activity implements SensorEventListener {
    private JBoxCollisionView mobike_jbox_view;
    SensorManager sensorManager;
    Sensor sensor;

    private int[] imgs = {
            R.drawable.share_fb,
            R.drawable.share_kongjian,
            R.drawable.share_pyq,
            R.drawable.share_qq,
            R.drawable.share_qq,
            R.drawable.share_tw,
            R.drawable.ic_game_gold_00,
            R.drawable.ic_game_gold_01,
            R.drawable.ic_game_gold_02,
            R.drawable.ic_game_gold_03,
            R.drawable.ic_game_gold_04,
            R.drawable.ic_game_gold_05,
            R.drawable.ic_game_gold_06,
            R.drawable.ic_game_gold_07,
            R.drawable.ic_game_gold_08,
            R.drawable.ic_game_gold_09,
            R.drawable.share_wechat,
            R.drawable.share_weibo,
            R.drawable.ic_game_gold_10,
            R.drawable.share_weibo,
            R.drawable.ic_game_gold_10,
            R.drawable.share_weibo,
            R.drawable.ic_game_gold_10,
            R.drawable.share_weibo,
            R.drawable.ic_game_gold_10,
            R.drawable.share_weibo,
            R.drawable.ic_game_gold_10,
            R.drawable.share_weibo,
            R.drawable.share_weibo,
            R.drawable.ic_game_gold_10,
            R.drawable.ic_game_gold_10,
            R.drawable.share_weibo,
            R.drawable.ic_game_gold_10,
            R.drawable.share_weibo,
            R.drawable.ic_game_gold_10,
            R.drawable.share_weibo,
            R.drawable.ic_game_gold_10,
            R.drawable.ic_game_gold_11,
            R.drawable.ic_game_gold_12,
            R.drawable.ic_game_gold_13,
            R.drawable.ic_game_gold_14,
            R.drawable.ic_game_gold_15,
            R.drawable.ic_game_gold_16,
            R.drawable.ic_game_gold_17,
            R.drawable.share_wechat,
            R.drawable.share_weibo
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobike_layout);
    }

    private void initView() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.TOP;
        for (int i = 0; i < imgs.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(imgs[i]);
            imageView.setTag(R.id.wd_view_circle_tag, true);
            mobike_jbox_view.addView(imageView, layoutParams);
        }
    }


    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mobike_jbox_view = (JBoxCollisionView) findViewById(R.id.mobike_jbox_view);
        initView();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1] * 2.0f;
            mobike_jbox_view.onSensorChanged(-x, y);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
