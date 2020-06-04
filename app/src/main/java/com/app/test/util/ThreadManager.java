package com.app.test.util;

import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadManager {

    private static final Object MUTEX = new Object();
    private static ExecutorService sExecutorPool;

    private static void judgeExecutorService() {
        if (null == sExecutorPool || sExecutorPool.isShutdown()) {
            synchronized (ThreadManager.class) {
                if (null == sExecutorPool || sExecutorPool.isShutdown()) {
                    sExecutorPool = new ThreadPoolExecutor(1
                            , 32
                            , 60L
                            , TimeUnit.SECONDS
                            , new LinkedBlockingQueue<Runnable>()
                            , new ThreadManagerFactory("ThreadManager"));
                }
            }
        }
    }

    public static void execute(Runnable runnable) {
        judgeExecutorService();
        sExecutorPool.execute(runnable);
    }

    public static void shutdown() {
        internalShutdown(false);
    }

    public static void shutdownNow() {
        internalShutdown(true);
    }

    private static void internalShutdown(boolean isNow) {
        synchronized (MUTEX) {
            if (Utils.isEmpty(sExecutorPool)) {
                return;
            }
            if (sExecutorPool.isShutdown()) {
                sExecutorPool = null;
                return;
            }
            if (isNow) {
                sExecutorPool.shutdownNow();
            } else {
                sExecutorPool.shutdown();
            }
            sExecutorPool = null;
        }
    }

    private static class ThreadManagerFactory implements ThreadFactory {

        private String threadName;
        private AtomicInteger atomicInteger;
        private boolean needHighPriority;

        public ThreadManagerFactory(String name) {
            this(name, false);
        }

        public ThreadManagerFactory(String threadName, boolean needHighPriority) {
            this.atomicInteger = new AtomicInteger();
            this.threadName = threadName;
            this.needHighPriority = needHighPriority;
        }

        @Override
        public Thread newThread(Runnable runnable) {
            String realThreadName = threadName + "-" + atomicInteger.incrementAndGet();
            Log.e("jam", "create thread: >>>>> " + realThreadName);
            Thread thread = new Thread(runnable, realThreadName);
            if (!needHighPriority) {
                if (thread.isDaemon()) {
                    thread.setDaemon(false);
                }
                if (thread.getPriority() != 5) {
                    thread.setPriority(5);
                }
            }
            return thread;
        }

    }
}
