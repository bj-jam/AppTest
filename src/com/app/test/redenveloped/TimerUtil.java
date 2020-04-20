package com.app.test.redenveloped;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lcx
 * Created at 2020.1.6
 * Describe:
 */
public class TimerUtil {
    public interface TimerCallback {
        boolean onCheck(int checkCount);

        void onEnd();
    }

    private static final int MSG_WHAT = 20200420;
    private Handler handler;
    private long intervalTime = 1000;
    private TimerCallback timerCallback;
    private AtomicInteger atomicInteger = new AtomicInteger();

    public TimerUtil() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (MSG_WHAT != msg.what || timerCallback == null) {
                    return;
                }
                boolean isSuccess = timerCallback.onCheck(atomicInteger.incrementAndGet());
                if (isSuccess) {
                    timerCallback.onEnd();
                } else {
                    if (handler != null) {
                        handler.sendMessageDelayed(getMessage(), intervalTime);
                    }
                }
            }
        };
    }

    public static TimerUtil get() {
        return new TimerUtil();
    }

    public void start(TimerCallback timerCallback) {
        startPolling(0, 500, timerCallback);
    }


    public void startTime(long intervalTimeMillis, TimerCallback timerCallback) {
        startPolling(0, intervalTimeMillis, timerCallback);
    }

    public void startDelayTime(long delayTimeMillis, long intervalTimeMillis, TimerCallback timerCallback) {
        startPolling(delayTimeMillis, intervalTimeMillis, timerCallback);
    }

    private void startPolling(long delayTimeMillis, long intervalTimeMillis, TimerCallback timerCallback) {
        reset();
        this.intervalTime = intervalTimeMillis;
        this.timerCallback = timerCallback;
        handler.sendMessageDelayed(getMessage(), delayTimeMillis);
    }

    private Message getMessage() {
        Message obtain = Message.obtain();
        obtain.what = MSG_WHAT;
        return obtain;
    }


    public void reset() {
        if (atomicInteger == null) {
            atomicInteger = new AtomicInteger();
        } else {
            atomicInteger.set(0);
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    public void onDestroy() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    public static void onDestroy(TimerUtil timerUtil) {
        if (timerUtil != null) {
            timerUtil.onDestroy();
        }
    }
}
