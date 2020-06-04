package com.app.test.hook;

import android.app.ActivityManager;
import android.app.Instrumentation;
import android.content.Context;
import android.os.Build;
import android.os.Handler;

import com.app.test.hook.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * @author lcx
 * Created at 2020.3.18
 * Describe:
 */
public class AMSPHookHelper {
    static final String EXTRA_TARGET_INTENT = "extra_target_intent";


    public static void hookAMSP() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            hookAMSPBefore26();
        } else {
            hookAMSPSince26();
        }
    }


    /**
     * android 26 以下版本 AMSP 的 hook
     */
    private static void hookAMSPBefore26() {
        try {
            Class classActivityManagerNative = Class.forName("android.app.ActivityManagerNative");
            Object gDefault = FieldUtils.readStaticField(classActivityManagerNative, "gDefault");
            Object mInstance = FieldUtils.readField(gDefault, "mInstance");
            Class classIActivityManager = Class.forName("android.app.IActivityManager");
            Object proxy = Proxy.newProxyInstance(
                    Thread.currentThread().getContextClassLoader(),
                    new Class[]{classIActivityManager},
                    new MockAMSP(mInstance)
            );

            FieldUtils.writeField(gDefault, "mInstance", proxy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * android 26 及以上版本 AMSP 的 hook
     */
    private static void hookAMSPSince26() {
        try {
            Object IActivityManagerSingleton = FieldUtils.readStaticField(ActivityManager.class, "IActivityManagerSingleton");


            Object mInstance = FieldUtils.readField(IActivityManagerSingleton, "mInstance");
            Class classIActivityManager = Class.forName("android.app.IActivityManager");
            Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                    new Class[]{classIActivityManager},
                    new MockAMSP(mInstance)
            );

            FieldUtils.writeField(IActivityManagerSingleton, "mInstance", proxy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hookActivityThread() {
        try {
            Class classActivityThread = Class.forName("android.app.ActivityThread");
            Object currentActivityThread = FieldUtils.readStaticField(classActivityThread, "sCurrentActivityThread");
            Handler mH = (Handler) FieldUtils.readField(currentActivityThread, "mH");
            FieldUtils.writeField(mH, "mCallback", new MockHCallback(mH));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public static void hookInstrumentation(Context context) {
//        try {
//            Class<?> activityThread = Class.forName("android.app.ActivityThread");
//            Field mMainThread = context.getClass().getDeclaredField("mMainThread");
//            mMainThread.setAccessible(true);
//            Object o = mMainThread.get(context);
//            Field mInstrumentation = o.getClass().getDeclaredField("mInstrumentation");
//            mInstrumentation.setAccessible(true);
//            Instrumentation instrumentation = (Instrumentation) mInstrumentation.get(o);
//            InstrumentationProxy proxy = new InstrumentationProxy(instrumentation);
//            mInstrumentation.set(o, proxy);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//
//    }
}
