package com.app.test.annotation.permission;

import android.content.Context;
import android.widget.Toast;

import com.app.test.base.App;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class AspectJTest {

    @Pointcut("execution(@om.app.test.annotation.permission.AspectJAnnotation  * *(..))")
    public void executionAspectJ() {

    }

    @Around("executionAspectJ()")
    public Object aroundAspectJ(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        AspectJAnnotation aspectJAnnotation = methodSignature.getMethod().getAnnotation(AspectJAnnotation.class);
        String permission = aspectJAnnotation.value();
        Context context = App.context;
        Object o = null;
        String result = "";
        if (PermissionManager.getInnerInstance().checkPermission(context, permission)) {
            o = joinPoint.proceed();
            result = "有权限";
        } else {
            result = "没有权限";
        }
        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
        return o;
    }
}  
