package com.app.test.hook;

import android.content.ComponentName;
import android.content.Intent;

import com.app.test.main.MainActivity;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author lcx
 * Created at 2020.3.18
 * Describe:
 */
public class MockAMSP implements InvocationHandler {
    private static final String START_ACTIVITY = "startActivity";

    private Object mBase;

    MockAMSP(Object base) {
        mBase = base;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (START_ACTIVITY.equals(method.getName())) {
            // 找到旧的 intent
            Intent raw;
            int index = 0;
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Intent) {
                    index = i;
                    break;
                }
            }
            raw = (Intent) args[index];

            // 创建新的 intent
            Intent newIntent = new Intent();
            String stubPackage = "com.app.test";
            ComponentName componentName = new ComponentName(stubPackage, MainActivity.class.getName());
            newIntent.setComponent(componentName);
            newIntent.putExtra(AMSPHookHelper.EXTRA_TARGET_INTENT, raw);

            // 替换旧的 intent 为新的 intent
            args[index] = newIntent;

            // 调用 "startActivity" 方法
            return method.invoke(mBase, args);
        }
        return method.invoke(mBase, args);
    }
}
