package com.app.test.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * Created by jam on 16/11/7.
 */

public class DownLoadService extends Service {
    /**
     * 进度条的最大值
     */
    public static final int MAX_PROGRESS = 100;
    /**
     * 进度条的进度值
     */
    private int progress = 0;

    private OnProgressListen progressListen;

    @Override
    public IBinder onBind(Intent intent) {
        return new MsgBind();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    public int getProgress() {
        return progress;
    }

    public class MsgBind extends Binder {
        /**
         * 获取当前Service的实例
         *
         * @return
         */
        public DownLoadService getService() {
            return DownLoadService.this;
        }
    }

    /**
     * 模拟下载任务，每秒钟更新一次
     */
    public void startDownLoad() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (progress < MAX_PROGRESS) {
                    progress += 5;
                    if (progressListen != null) {
                        progressListen.showProgress(progress);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }


    public void setProgressListen(OnProgressListen progressListen) {
        this.progressListen = progressListen;
    }

    @Override
    public void onDestroy() {
        System.out.print("onDestroy");
        super.onDestroy();
    }

    public interface OnProgressListen {
        public void showProgress(int progress);
    }
}
