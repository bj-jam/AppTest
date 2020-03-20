package com.app.test.utils;

import android.os.Handler;

import com.app.test.network.JsonCallback;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池辅助类
 */
public class ThreadUtils {

    // 核心线程数
    private static int CORE_POOL_SIZE = 20;
    // 最大线程数
    private static int MAX_POOL_SIZE = 150;
    private static int KEEP_ALIVE_TIME = 10000;

    private static BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(10);

    // 线程工厂
    private static ThreadFactory threadFactory = new ThreadFactory() {
        private final AtomicInteger integer = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "ThreadUtils thread:" + integer.getAndIncrement());
        }
    };

    // 线程池
    private static ThreadPoolExecutor threadPool;

    static {
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
                KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue, threadFactory);
    }

    /**
     * 从线程池中抽取线程，执行指定的Runnable对象
     *
     * @param runnable
     */
    public static void execute(Runnable runnable) {
        threadPool.execute(runnable);
    }

    /**
     *
     */
    public static void execute(Handler handler, String url,
                               HashMap<String, String> hashMap, int what) {
        execute(handler, url, hashMap, what, "");
    }

    public static void execute(Handler handler, String url,
                               HashMap<String, String> hashMap, int what, String timeout) {
        new JsonCallback(handler, url, hashMap, what).initTimeout(timeout)
                .request();
    }

    public static void execute(Handler handler, String url,
                               HashMap<String, String> hashMap, int what, int arg1) {
        execute(handler, url, hashMap, what, arg1, "");
    }

    public static void execute(Handler handler, String url,
                               HashMap<String, String> hashMap, int what, int arg1, String timeout) {
        new JsonCallback(handler, url, hashMap, what, arg1)
                .initTimeout(timeout).request();
    }

    public static void execute(Handler handler, String url,
                               HashMap<String, String> hashMap, int what, int arg1, int arg2) {
        execute(handler, url, hashMap, what, arg1, arg2, "");
    }

    public static void execute(Handler handler, String url,
                               HashMap<String, String> hashMap, int what, int arg1, int arg2,
                               String timeout) {
        new JsonCallback(handler, url, hashMap, what, arg1, arg2).initTimeout(
                timeout).request();
    }
}
