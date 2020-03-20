package com.app.test.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.test.R;

/**
 * Created by jam on 16/11/7.
 * 测试activity与service间的通信：
 * 1、获取服务的对象，通过服务的实例调用服务的方法进行通信<br>
 * 2、通过回调函数<br>
 * 3、通过广播<br>
 * 由于Service 的onStart()方法只有在startService()启动Service的情况下才调用，故使用onStart()的时候要注意这点。
 * 与 Service 通信并且让它持续运行
 * 如果我们想保持和 Service 的通信，又不想让 Service 随着 Activity 退出而退出呢？
 * 你可以先 startService() 然后再 bindService() 。
 * 当你不需要绑定的时候就执行 unbindService() 方法，执行这个方法只会触发 Service 的 onUnbind() 而不会把这个 Service 销毁。
 * 这样就可以既保持和 Service 的通信，也不会随着 Activity 销毁而销毁了。
 */

public class ServiceActivity extends Activity {
    /**
     * 服务实例
     */
    private DownLoadService downLoadService;
    /**
     * 启动下载按钮
     */
    private Button getProgress;
    /**
     * 实例的方式通信显示
     */
    private TextView showProgress;
    /**
     * 回调函数方式显示
     */
    private TextView showProgress1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_service);
        getProgress = (Button) findViewById(R.id.getProgress);
        showProgress = (TextView) findViewById(R.id.showProgress);
        showProgress1 = (TextView) findViewById(R.id.showProgress1);

        getProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (downLoadService != null) {
                    //启动下载线程，并且开启获取数据线程
                    downLoadService.startDownLoad();
                    new Thread(runnable).start();
                }
            }
        });
        //启动服务
        this.bindService(new Intent(this, DownLoadService.class), connection, Context.BIND_AUTO_CREATE);
    }

    /**
     *
     */

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downLoadService = ((DownLoadService.MsgBind) service).getService();
            //同坐回调函数函数得到数据
            if (downLoadService == null)
                return;
            downLoadService.setProgressListen(new DownLoadService.OnProgressListen() {
                @Override
                public void showProgress(final int progress) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress1.setText((progress - 1) + "");
                        }
                    });

                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    /**
     * 获取数据线程
     */

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (true) {

                if (downLoadService != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.setText(downLoadService.getProgress() + "");
                        }
                    });
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    @Override
    protected void onDestroy() {
        //解绑服务（停止服务）
        unbindService(connection);
        super.onDestroy();
    }
}
