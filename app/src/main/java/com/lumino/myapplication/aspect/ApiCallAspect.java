package com.lumino.myapplication.aspect;

import android.util.Log;

import com.lumino.myapplication.aspect.annotation.ApiCall;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class ApiCallAspect {
    private static final String TAG = "ApiCallAspect";

    @Pointcut("execution(@com.lumino.myapplication.aspect.annotation.ApiCall * *(..))")
    public void apiCalled() {}

    @Around("apiCalled()")
    public Object methodApiCalled(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        // annotation 传入的值
        String functionName = methodSignature.getMethod().getAnnotation(ApiCall.class).value();

        //统计时间
        long begin = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - begin;
        Log.e(TAG, String.format("功能：%s,%s类的%s方法执行了，用时%d ms", functionName, className, methodName, duration));
        return result;
    }

}
