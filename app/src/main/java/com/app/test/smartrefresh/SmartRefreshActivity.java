package com.app.test.smartrefresh;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.test.R;
import com.app.test.recyclerview.CourseInfo;
import com.app.test.recyclerview.DampingAdapter;
import com.app.test.util.Utils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/3/7.
 */

public class SmartRefreshActivity extends Activity {
    private final int MSG_UPDATE_TIME_TXT = 10000000;
    private RecyclerView recyclerView;
    private AnimHeader animHeader;
    private SmartRefreshLayout smartRefreshLayout;
    private DampingAdapter adapter;
    private List<CourseInfo> list;
    private ScheduledExecutorService executorService;
    private Handler handlerUpdateTxt;
    private TextView tvTime;
    private TextView tvTime1;
    private long time = 10000000000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_refresh);
        initDate();
        initView();
        register();
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
        handlerUpdateTxt = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MSG_UPDATE_TIME_TXT) {
                    time--;
                    if (tvTime != null)
                        tvTime.setText("-" + time);
                }
                super.handleMessage(msg);
            }
        };
        isChangeTime = true;
        executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
//                if (!isChangeTime)
//                    return;
//                try {
//                    handlerUpdateTxt.removeMessages(MSG_UPDATE_TIME_TXT);
//                    handlerUpdateTxt.sendEmptyMessage(MSG_UPDATE_TIME_TXT);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    private void initView() {
        tvTime = findViewById(R.id.tvTime);
        tvTime1 = findViewById(R.id.tvTime1);
        animHeader = findViewById(R.id.walkHeader);
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setDragRate(0.8f);
        smartRefreshLayout.setEnableHeaderTranslationContent(false);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                Toast.makeText(SmartRefreshActivity.this, "正在刷新", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        smartRefreshLayout.finishRefresh();
                    }
                }, 1000);
            }
        });
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new DampingAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setList(list);
    }

    private void releaseExecutorService() {
        try {
            if (executorService != null) {
                if (!executorService.isShutdown()) {
                    executorService.shutdownNow();
                }
                executorService = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean isChangeTime;
    private long screen_of_time;

    /**
     * screen状态广播接收者
     */
    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) { // 开屏
                time -= (System.currentTimeMillis() - screen_of_time);
                isChangeTime = true;
                tvTime1.setText("ACTION_SCREEN_ON");
            } else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) { // 锁屏
                isChangeTime = false;
                screen_of_time = SystemClock.elapsedRealtime();
                tvTime1.setText("ACTION_SCREEN_OFF");

            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
    }

    private PowerManager.WakeLock wakeLock;

    private void acquireWakeLock() {
        if (wakeLock == null) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//            if (!Utils.isEmpty(pm))
//                wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getCanonicalName());
//            if (!Utils.isEmpty(wakeLock))
//                wakeLock.acquire();
        }
    }


    private void releaseWakeLock() {
//        if (!Utils.isEmpty(wakeLock)) {
//            if (wakeLock.isHeld())
//                wakeLock.release();
//            wakeLock = null;
//        }

    }

    /**
     * 注册锁屏 开屏广播
     */
    private void register() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(broadcastReceiver, filter);
    }

    /**
     * 注销广播
     */
    private void unregister() {
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        releaseExecutorService();
        unregister();
        if (!Utils.isEmpty(animHeader))
            animHeader.onDestroy();
        super.onDestroy();
    }
}
