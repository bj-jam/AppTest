package com.app.test.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Aspect
public class OnceClickAspectJ {
    private static final String TAG = OnceClickAspectJ.class.getSimpleName();
    private static Long sLastClick = 0L;

    public OnceClickAspectJ() {
    }

    @Pointcut("execution(@com.app.test.annotation.AOnceClick * *(..))")
    public void executionAOnceClick() {
    }

    @Around("executionAOnceClick()")
    public void onceClick(ProceedingJoinPoint joinPoint) throws Throwable {
        Method realMethod = null;

        try {
            Signature signature = joinPoint.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            Method targetMethod = methodSignature.getMethod();
            realMethod = joinPoint.getTarget().getClass().getDeclaredMethod(signature.getName(), targetMethod.getParameterTypes());
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        if (realMethod == null) {
            this._proceed(joinPoint, OnceClickConfig.getDuring());
        } else {
            AOnceClick aOnceClick = (AOnceClick) realMethod.getAnnotation(AOnceClick.class);
            if (aOnceClick == null) {
                this._proceed(joinPoint, OnceClickConfig.getDuring());
            } else {
                long value = aOnceClick.value();
                if (value <= 0L) {
                    value = OnceClickConfig.getDuring();
                }

                this._proceed(joinPoint, value);
            }
        }
    }

    private void _proceed(ProceedingJoinPoint joinPoint, long value) throws Throwable {
        if (joinPoint != null) {
            long currentTimeMillis = System.currentTimeMillis();
            if (Math.abs(currentTimeMillis - sLastClick) >= value) {
                sLastClick = currentTimeMillis;
                joinPoint.proceed();
            }
        }
    }
}
