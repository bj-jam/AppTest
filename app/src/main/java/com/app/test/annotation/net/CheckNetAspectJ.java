package com.app.test.annotation.net;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.test.base.App;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;


@Aspect
public class CheckNetAspectJ {

    /**
     * 找到处理的切点
     * * *(..)  可以处理所有的方法
     */
    @Pointcut("execution(@com.app.test.annotation.net.CheckNetAnnotation  * *(..))")
    public void executionCheckNet() {
    }

    /**
     * 处理切面
     *
     * @param joinPoint
     * @return
     */
    @Around("executionCheckNet()")
    public Object checkNet(ProceedingJoinPoint joinPoint) throws Throwable {
        Log.e("jam", "checkNet");
        // 做埋点  日志上传  权限检测（我写的，RxPermission , easyPermission） 网络检测
        // 网络检测
        // 1.获取 CheckNet 注解  NDK  图片压缩  C++ 调用Java 方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CheckNetAnnotation checkNet = signature.getMethod().getAnnotation(CheckNetAnnotation.class);
        Log.e("jam", "1" + (checkNet != null));
        if (checkNet != null) {
            // 2.判断有没有网络  怎么样获取 context?
            Object object = joinPoint.getThis();// View Activity Fragment ； getThis() 当前切点方法所在的类
            Context context = App.context;
            Log.e("jam", "2" + (context != null));
            if (context != null) {
                if (!NetworkManager.isNetworkConnected(context)) {
                    // 3.没有网络不要往下执行
                    Log.i("jam", "checkNet: 网络异常");
                    Toast.makeText(context, "请检查您的网络", Toast.LENGTH_LONG).show();
                    return null;
                } else {
                    Log.i("jam", "checkNet: 网络状况良好");
                    Toast.makeText(context, "网络状况良好", Toast.LENGTH_LONG).show();
                    return joinPoint.proceed();
                }
            }
        }
        return joinPoint.proceed();
    }

    /**
     * 通过对象获取上下文
     *
     * @param object
     * @return
     */
    private Context getContext(Object object) {
        if (object instanceof Activity) {
            return (Activity) object;
        } else if (object instanceof Fragment) {
            Fragment fragment = (Fragment) object;
            return fragment.getActivity();
        } else if (object instanceof View) {
            View view = (View) object;
            return view.getContext();
        }
        return null;
    }
}
