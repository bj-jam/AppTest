package com.app.test.recyclerview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.app.test.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/7.
 */

public class Damping1Activity extends Activity {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DampingAdapter adapter;
    private List<CourseInfo> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_damping1);
        initDate();
        initView();
    }

    private void initDate() {
        list = new ArrayList<>();
        for (int j = 0; j < 12; j++) {
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
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 6000);
            }
        });
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                if (recyclerView.canScrollVertically(-1)) {
//                    Log.e("jam", "direction -1: true");
//                    srl_view.setDamping(false);
//                } else {
//                    Log.e("jam", "滑动到顶部");
//                    srl_view.setDamping(true);
//                }
//
//                if (srl_view.canScrollVertically(1)) {
//                    Log.e("jam", "direction 1: true");
//                } else {
//                    Log.e("jam", "direction 1: //滑动到底部");
//                }

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new DampingAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setList(list);
    }


}
