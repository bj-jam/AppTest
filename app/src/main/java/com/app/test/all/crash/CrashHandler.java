package com.app.test.all.crash;

import android.content.Context;
import android.util.Log;

/**
 * @author lcx
 * Created at 2020.9.24
 * Describe: 捕获未捕获的异常
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static CrashHandler instance = null;


    public static synchronized CrashHandler getInstance() {
        if (instance == null) {
            instance = new CrashHandler();
        }
        return instance;
    }

    public void init(Context context) {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        String stringBuilder = "Thread:" +
                thread.toString() +
                "\t" +
                ex;
        Log.e("jam", stringBuilder);
    }
}
