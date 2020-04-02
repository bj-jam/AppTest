package com.app.test.flexbox;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.app.test.R;
import com.app.test.path.ViewAdapter;

import java.util.ArrayList;

/**
 * @author lcx
 * Created at 2020.4.1
 * Describe:
 */
public class FlexboxActivity extends Activity {
    private ViewPager vpView;
    private ViewAdapter adapter;
    private ArrayList<View> dataList = new ArrayList<>();
    private FlexboxLayoutView flexboxLayoutView;
    private FlexboxManagerView flexboxManagerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);
        initView();
    }

    private void initView() {
        vpView = (ViewPager) findViewById(R.id.vp_view);
        flexboxLayoutView = new FlexboxLayoutView(this);
        flexboxManagerView = new FlexboxManagerView(this);
        dataList.add(flexboxLayoutView);
        dataList.add(flexboxManagerView);
        adapter = new ViewAdapter(dataList);
        vpView.setAdapter(adapter);
    }
}
