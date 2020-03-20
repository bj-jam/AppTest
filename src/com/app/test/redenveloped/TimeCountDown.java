package com.app.test.redenveloped;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/***
 *   created by android on 2019/5/6
 */
public class TimeCountDown {
    public interface TimeCallback {
        void onNext(int timeSecond);

        void onComplete();
    }

    private  final int msg_what = 6000;
    private Handler handler;
    //    private static TimeCountDown timeCountDown;
    private TimeCallback timeCallback;

    public TimeCountDown() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg_what != msg.what || timeCallback == null) {
                    return;
                }
                int time = msg.arg1-1;
                sendTime(time, timeCallback);
                if (time == 0) {
                    timeCallback.onComplete();
                    return;
                }
                if(handler!=null){
                    Message message = getMessage(time);
                    handler.sendMessageDelayed(message, 1000);
                }
            }
        };
    }
/*    public static TimeCountDown get() {
        if(timeCountDown==null){
            synchronized (TimeCountDown.class){
                if(timeCountDown==null){
                    timeCountDown=new TimeCountDown();
                }
            }
        }
        return timeCountDown;
    }*/

    public void start(int timeSecond, TimeCallback timeCallback) {
        if(handler!=null){
            handler.removeCallbacksAndMessages(null);
        }

        this.timeCallback = timeCallback;
        sendTime(timeSecond, timeCallback);

        Message message = getMessage(timeSecond);

        handler.sendMessageDelayed(message, 1000);

    }

    private Message getMessage(int time) {
        Message obtain = Message.obtain();
        obtain.what = msg_what;
        obtain.arg1 = time;
        return obtain;
    }

    private void sendTime(int timeSecond, TimeCallback timeCallback) {
        if (timeCallback != null) {
            timeCallback.onNext(timeSecond);
        }
    }

    public void onDestroy() {
        timeCallback=null;
        if(handler!=null){
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }
    public void clear() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
