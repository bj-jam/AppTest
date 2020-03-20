package com.app.test.circle.anim.color;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.app.test.R;
import com.app.test.circle.CirclePicWidget;
import com.app.test.circle.MagnificentChart;
import com.app.test.circle.MagnificentChartItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ColorCircleActivity extends Activity implements OnClickListener {
    protected CirclePicWidget cpw;
    private ColorCirclePieView cp;
    List<String> url = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_color_circle);
        cp = (ColorCirclePieView) findViewById(R.id.cp);
        cpw = (CirclePicWidget) findViewById(R.id.CirclePicWidget);
        cp.setOnClickListener(this);
        Random random = new Random();
        int count = random.nextInt(10);
        int[] textColors = new int[count];
        int[] color = new int[count];
        for (int i = 0; i < count; i++) {
            textColors[i] = random.nextInt(1000);
            color[i] = Color.parseColor(getRandColorCode());
        }
        cp.setNumbers(textColors);
        cp.setTextColors(color);
        url.add("https://image.test.com/tes/app/demo/201804/c3ba9e3f8ead4392ae5df8ab29118297.jpg");

        cpw.setPicList(url);
        cpw.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                url.add("https://image.test.com/test/app/demo/201804/c3ba9e3f8ead4392ae5df8ab29118297.jpg");
                cpw.setPicList(url);
            }
        });
        handleChart();
    }

    MagnificentChart magnificentChart;

    private void handleChart() {
        MagnificentChartItem firstItem = new MagnificentChartItem("first", 30, Color.parseColor("#BAF0A2"));
        MagnificentChartItem secondItem = new MagnificentChartItem("second", 12, Color.parseColor("#2F6994"));
        MagnificentChartItem thirdItem = new MagnificentChartItem("third", 3, Color.parseColor("#FF6600"));
        MagnificentChartItem fourthItem = new MagnificentChartItem("fourth", 41, Color.parseColor("#800080"));
        MagnificentChartItem fifthItem = new MagnificentChartItem("fifth", 14, Color.parseColor("#708090"));

        List<MagnificentChartItem> chartItemsList = new ArrayList<MagnificentChartItem>();
        chartItemsList.add(firstItem);
        chartItemsList.add(secondItem);
        chartItemsList.add(thirdItem);
        chartItemsList.add(fourthItem);
        chartItemsList.add(fifthItem);

        magnificentChart = (MagnificentChart) findViewById(R.id.magnificentChart);

        magnificentChart.setChartItemsList(chartItemsList);
        magnificentChart.setMaxValue(100);
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub

        if (view.getId() == R.id.cp) {
            Random random = new Random();
            int count = random.nextInt(10);
            int[] textColors = new int[count];
            int[] color = new int[count];
            for (int i = 0; i < count; i++) {
                textColors[i] = random.nextInt(1000);
                color[i] = Color.parseColor(getRandColorCode());
            }
            cp.setNumbers(textColors);
            cp.setTextColors(color);
        }

        switch (view.getId()) {
            case R.id.animationButton:
                if (magnificentChart.getAnimationState()) {
                    magnificentChart.setAnimationState(false);
                } else {
                    magnificentChart.setAnimationState(true);
                }
                break;

            case R.id.roundButton:
                if (magnificentChart.getRound()) {
                    magnificentChart.setRound(false);
                } else {
                    magnificentChart.setRound(true);
                }
                break;

            case R.id.shadowButton:
                if (magnificentChart.getShadowShowingState()) {
                    magnificentChart.setShadowShowingState(false);
                } else {
                    magnificentChart.setShadowShowingState(true);
                }
                break;

            case R.id.animationSpeedDefault:
                magnificentChart.setAnimationSpeed(MagnificentChart.ANIMATION_SPEED_DEFAULT);
                break;

            case R.id.animationSpeedSlow:
                magnificentChart.setAnimationSpeed(MagnificentChart.ANIMATION_SPEED_SLOW);
                break;

            case R.id.animationSpeedFast:
                magnificentChart.setAnimationSpeed(MagnificentChart.ANIMATION_SPEED_FAST);
                break;

            case R.id.animationSpeedNormal:
                magnificentChart.setAnimationSpeed(MagnificentChart.ANIMATION_SPEED_NORMAL);
                break;
        }
    }

    /**
     * 随机生成颜色
     */
    public static String getRandColorCode() {
        String r, g, b;
        Random random = new Random();
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();

        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;

        return "#FF" + r + g + b;
    }

}
