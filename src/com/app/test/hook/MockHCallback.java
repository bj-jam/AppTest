package com.app.test.hook;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.app.test.hook.reflect.FieldUtils;

import java.util.List;

/**
 * @author lcx
 * Created at 2020.3.18
 * Describe:
 */
public class MockHCallback implements Handler.Callback {
    /**
     * android 28 以下，ActivityThread$H.LAUNCH_ACTIVITY = 100
     */
    private static final int LAUNCH_ACTIVITY = 100;

    /**
     * android 28 上，ActivityThread$H.EXECUTE_TRANSACTION = 159
     */
    private static final int EXECUTE_TRANSACTION = 159;
    private static final String TAG = "MockHCallback";

    private Handler mBase;

    MockHCallback(Handler base) {
        mBase = base;
    }

    @Override
    public boolean handleMessage(Message msg) {
        handleLaunchActivity(msg);
        mBase.handleMessage(msg);
        return true;
    }

    private void handleLaunchActivity(Message msg) {
        Log.d(TAG, "handleLaunchActivity");
        if (Build.VERSION.SDK_INT >= 28) {
            if (msg.what == EXECUTE_TRANSACTION) {
                handleLaunchActivitySince28(msg);
            }
        } else {
            if (msg.what == LAUNCH_ACTIVITY) {
                handleLaunchActivityBefore28(msg);
            }
        }
    }

    private void handleLaunchActivityBefore28(Message msg) {
        try {
            Object obj = msg.obj;
            if (obj != null) {
                Intent raw = (Intent) FieldUtils.readField(obj, "intent");
                Intent target = raw.getParcelableExtra(AMSPHookHelper.EXTRA_TARGET_INTENT);
                if (target != null) {
                    raw.setComponent(target.getComponent());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleLaunchActivitySince28(Message msg) {
        try {
            Object mActivityCallbacks = FieldUtils.readField(msg.obj, "mActivityCallbacks");
            if (mActivityCallbacks != null) {
                List<?> list = (List<?>) mActivityCallbacks;
                if (list.size() > 0) {
                    Object listItem = list.get(0);
                    Class classLaunchActivityItem = Class.forName("android.app.servertransaction.LaunchActivityItem");
                    if (listItem.getClass() == classLaunchActivityItem) {
                        Intent raw = (Intent) FieldUtils.readField(listItem, "mIntent");
                        Intent target = raw.getParcelableExtra(AMSPHookHelper.EXTRA_TARGET_INTENT);
                        raw.setComponent(target.getComponent());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
