package com.app.test.recyclerview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

import com.app.test.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/7.
 */

public class DampingActivity extends Activity {
    private RecyclerView recyclerView;
    private CreditSwipeRefreshLayout srl_view;
    private DampingAdapter adapter;
    private List<CourseInfo> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_damping);
        initDate();
        initView();
    }

    private void initDate() {
        list = new ArrayList<>();
        for (int j = 0; j < 10; j++) {
            CourseInfo courseInfo = new CourseInfo();
            if (j % 2 == 0) {
                courseInfo.name = "姓名" + j + "测试新的控件大姐夫拉开到交付快递费显示问题";
            } else {
                courseInfo.name = "姓名" + j;
            }
            courseInfo.price = "价格" + j;
            list.add(courseInfo);
        }
    }

    private void initView() {
        srl_view = findViewById(R.id.srl_view);
        srl_view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        srl_view.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        recyclerView = findViewById(R.id.rv_view);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (srl_view.canScrollVertically(1)) {
                    Log.e("jam", "direction 1: true");
                } else {
                    Log.e("jam", "direction 1: //滑动到底部");
                }

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new DampingAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setList(list);
    }


}
