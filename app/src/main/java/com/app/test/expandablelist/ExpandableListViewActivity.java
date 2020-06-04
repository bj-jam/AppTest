package com.app.test.expandablelist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.Toast;

import com.app.test.R;
import com.app.test.expandablelist.MyExpandableList.OnFootClickListener;
import com.app.test.expandablelist.MyExpandableList.OnRefreshListener;
import com.app.test.line.LineActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExpandableListViewActivity extends Activity implements Callback {
    private MyExpandableList expandList;
    private ExpandableAdapter adapter;
    private List<CourseDto> list;
    private List<String> stringList;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable);
        EventBus.getDefault().register(this);
        expandList = (MyExpandableList) findViewById(R.id.expandList);
        list = new ArrayList<CourseDto>();
        stringList = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            stringList.add("eclipse" + i);
        }
        for (int i = 0; i < 10; i++) {
            CourseDto dto = new CourseDto();
            dto.name = "File" + i;
            dto.nameList = stringList;
            list.add(dto);
        }
        handler = new Handler(this);
        adapter = new ExpandableAdapter(this, list);
        expandList.setAdapter(adapter);
        adapter.notifyDataSetInvalidated();
        expandList.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(ExpandableListViewActivity.this,
                        list.get(groupPosition).name, Toast.LENGTH_SHORT)
                        .show();
//				WindowUtils.getInstance(ExpandableListViewActivity.this).startAni();
                return false;
            }
        });
        expandList.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ExpandableListViewActivity.this,
                        LineActivity.class);
                startActivity(intent);
                return false;
            }
        });

        expandList.setonRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub
                handler.sendEmptyMessageDelayed(1, 2000);
            }
        });
        expandList.setOnFootClickListener(new OnFootClickListener() {

            @Override
            public void onClick() {
                // TODO Auto-generated method stub
                handler.sendEmptyMessageDelayed(2, 2000);
            }
        });
    }

    @Subscribe()
    public void onEventMainThread(CourseDto dto) {

    }

    public class CourseDto implements Serializable {

        /**
         *
         */
        private static final long serialVersionUID = 1L;
        public String name;
        public List<String> nameList;

    }

    @Override
    public boolean handleMessage(Message msg) {
        // TODO Auto-generated method stub
        if (msg.what == 1) {
            expandList.onRefreshComplete();
            expandList.onLoadComplete();
        } else if (msg.what == 2) {
            expandList.onLoadComplete();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
