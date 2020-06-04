package com.app.test.utils;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 *
 */
public class VoicePlayUtil implements OnPreparedListener, OnCompletionListener,
        OnBufferingUpdateListener {

    public final int state_release = 0;// 释放状态(播放器销毁)
    public final int state_prepare = state_release + 1;// 准备状态(加载播放资源)
    public final int state_play = state_prepare + 1;// 播放状态
    public final int state_pause = state_play + 1;// 暂停状态
    public final int state_stop = state_pause + 1;// 停止 状态

    private MediaPlayer mp;
    // private SeekBarView seekBarView;
    private SeekBar seekBar;
    private TextView current;// 当前播放时间
    private TextView total;// 总时间
    private int state = state_release;// 状态判断值
    private PlayListener listener;

    public VoicePlayUtil(SeekBar seekBar, TextView current, TextView total) {
        this.seekBar = seekBar;
        this.current = current;
        this.total = total;
    }

    public int getState() {
        return state;
    }

    public void setListener(PlayListener listener) {
        this.listener = listener;
    }

    /**
     * 准备播放
     */
    public void playPrepared(String path) {
        state = state_prepare;
        if (mp == null) {
            mp = new MediaPlayer();
            mp.setOnBufferingUpdateListener(this);
            mp.setOnPreparedListener(this);
            mp.setOnCompletionListener(this);
        }
        try {
            mp.reset();
            mp.setDataSource(path);
            mp.prepareAsync();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 准备播放
     */
    public void playPrepare() {
        try {
            if (mp != null) {
                state = state_prepare;
                mp.prepare();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始播放
     */
    public void playStart() {
        if (mp != null) {
            state = state_play;
            mp.start();
            listener.playListener(true);
        }
    }

    /**
     * 暂停播放
     */
    public void playPause() {
        if (mp != null) {
            state = state_pause;
            mp.pause();
        }
    }

    /**
     * 停止播放
     */
    public void playStop() {
        if (mp != null) {
            state = state_stop;
            // seekBarView.updateProgress(0);// 拖动条归零
            seekBar.setProgress(0);
            current.setText("00:00");// 当前播放时间归零
            mp.stop();
            listener.playListener(false);
        }
    }

    /**
     * 播放完释放资源
     */
    public void playRelease() {
        if (mp != null) {
            state = state_release;
            mp.stop();
            mp.release();
            mp = null;
        }
    }

    /**
     * 更新进度条线程
     */
    private class PregrossThread implements Runnable {
        @Override
        public void run() {
            while (state != state_release) {
                // 只在播放状态下更新
                if (state == state_play) {
                    try {
                        handler.sendEmptyMessage(0);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 更新进度值
     */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            int time = mp.getCurrentPosition() / 1000;
            // seekBarView.updateProgress(time);
            seekBar.setProgress(time);
            current.setText(measureTime(time));
        }

        ;
    };

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        // 获取时长
        int duration = mp.getDuration() / 1000;
        // seekBarView.setTotalProgress(duration);
        seekBar.setMax(duration);
        total.setText(measureTime(duration));
        playStart();
        // 开启进度条线程
        new Thread(new PregrossThread()).start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        playStop();
    }

    /**
     * 换算时间
     */
    private String measureTime(int second) {
        int s = second % 60;
        int m = (second - s) / 60;
        return (m == 0 ? "00" : (m < 10 ? "0" + m : m)) + " : "
                + (s == 0 ? "00" : (s < 10 ? "0" + s : s));
    }

    public interface PlayListener {
        public void playListener(boolean isPlay);
    }
}
