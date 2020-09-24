package com.app.test.annotation.permission;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;

public class PermissionManager {
    private static volatile PermissionManager permissionManager;

    public PermissionManager() {
    }

    //DCL单例模式  
    public static PermissionManager getInstance() {
        if (permissionManager == null) {
            synchronized (PermissionManager.class) {
                if (permissionManager == null) {
                    permissionManager = new PermissionManager();
                }
            }
        }
        return permissionManager;
    }

    private static class InnerInsatance {
        public static final PermissionManager instance = new PermissionManager();
    }

    //内部类单例模式  
    public static PermissionManager getInnerInstance() {
        synchronized (PermissionManager.class) {
            return InnerInsatance.instance;
        }
    }

    public boolean checkPermission(Context context, String permission) {
        Log.i("tag", "检查的权限：" + permission);
        int targetSdkVersion = context.getApplicationInfo().targetSdkVersion;
        if (targetSdkVersion >= 23) {
            return ContextCompat.checkSelfPermission(context, permission) == 0;
        } else {
            return PermissionChecker.checkSelfPermission(context, permission) == 0;
        }
    }
}
