package com.app.test.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.app.test.util.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author lcx
 * Created at 2020.3.26
 * Describe:
 */
public class ContextUtils {
    public ContextUtils() {
    }

    public static boolean isDestroyed(Context context) {
        Activity activity = findActivity(context);
        if (Utils.isEmpty(activity)) {
            return true;
        } else {
            boolean isDestroyed = false;
            if (Build.VERSION.SDK_INT >= 17) {
                isDestroyed = activity.isDestroyed();
            }

            if (activity instanceof FragmentActivity) {
                FragmentManager supportFragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
                if (!Utils.isEmpty(supportFragmentManager)) {
                    isDestroyed = supportFragmentManager.isDestroyed();
                }
            }

            return isDestroyed;
        }
    }

    public static Activity findActivity(Fragment fragment) {
        return Utils.isEmpty(fragment) ? null : fragment.getActivity();
    }

    public static Activity findActivity(android.app.Fragment fragment) {
        return Utils.isEmpty(fragment) ? null : fragment.getActivity();
    }

    public static Activity findActivity(View view) {
        if (!Utils.isEmpty(view) && !Utils.isEmpty(view.getContext())) {
            Context context = null;
            if (view.getContext().getClass().getName().contains("com.android.internal.policy.DecorContext")) {
                try {
                    Field field = view.getContext().getClass().getDeclaredField("mPhoneWindow");
                    if (field == null) {
                        return null;
                    }

                    field.setAccessible(true);
                    Object obj = field.get(view.getContext());
                    Method m1 = obj.getClass().getMethod("getContext");
                    if (m1 == null) {
                        return null;
                    }

                    context = (Context) ((Context) m1.invoke(obj));
                } catch (Exception var5) {
                }
            } else {
                context = view.getContext();
            }

            return findActivity(context);
        } else {
            return null;
        }
    }

    public static Activity findActivity(Context context) {
        if (Utils.isEmpty(context)) {
            return null;
        } else {

            if (context instanceof Activity) {
                return (Activity) context;
            } else if (context instanceof ContextWrapper) {
                ContextWrapper wrapper = (ContextWrapper) context;
                return findActivity(wrapper.getBaseContext());
            } else {
                return null;
            }
        }
    }
}
