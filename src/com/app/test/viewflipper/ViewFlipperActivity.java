package com.app.test.viewflipper;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.app.test.R;

import java.util.List;

/**
 * @author lcx
 * Created at 2020.2.26
 * Describe:
 */
public class ViewFlipperActivity extends Activity {
    private ViewFlipper viewFlipper;
    private List<String> list;
    private Context context;
    private View view;
    private TextView tv_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewflipper);

        context = ViewFlipperActivity.this;

        viewFlipper = findViewById(R.id.viewFlipper);

        viewFlipper.clearFocus();

        for (int i = 0; i < 10; i++) {

            view = LayoutInflater.from(context).inflate(R.layout.item_viewflipper, null);
            tv_show = view.findViewById(R.id.tv_show);
            tv_show.setText("立元" + i);
            viewFlipper.addView(view);
        }

        viewFlipper.setInAnimation(context, R.anim.come_in);
        viewFlipper.setOutAnimation(context, R.anim.come_out);
        viewFlipper.setFlipInterval(2000);

        // 1、设置幻灯片的形式滚动
        // viewFlipper.startFlipping();

        // 2、设置自动翻页滚动
        viewFlipper.setAutoStart(true);
        viewFlipper.isAutoStart();

    }
}
