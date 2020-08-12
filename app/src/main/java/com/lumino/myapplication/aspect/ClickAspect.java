package com.lumino.myapplication.aspect;

import android.util.Log;
import android.view.View;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class ClickAspect {
    private static final String TAG = "ClickAspect";
    // 第一个*所在的位置表示的是返回值，*表示的是任意的返回值，
    // onClick()中的 .. 所在位置是方法参数的位置，.. 表示的是任意类型、任意个数的参数
    // * 表示的是通配
    @Pointcut("execution(* android.view.View.OnClickListener.onClick(..))")
    public void clickMethod() {}

    @Around("clickMethod()")
    public void onClickMethodAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        View view = null;
        for (Object arg : args) {
            if (arg instanceof View) {
                view = (View) arg;
            }
        }
        //获取View 的 string id
        String resEntryName = null;
        String resName = null;
        if (view != null) {
            resEntryName = view.getContext().getResources().getResourceEntryName(view.getId());
            resName = view.getContext().getResources().getResourceName(view.getId());
        }
        joinPoint.proceed();
        Log.d(TAG, "after onclick: " + "resEntryName: " + resEntryName + "  resName: " + resName);
    }


    @Pointcut("execution(* *..MainActivity+.on**(..))")
    public void lifeCycleCallBack() {}

    /**
     * 传入的是joinPoint，没有proceed()方法
     *
     * @param joinPoint joinPoint
     */
    @After("lifeCycleCallBack()")
    public void method(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = joinPoint.getThis().getClass().getSimpleName();
        Log.e(TAG, "class:" + className + " : " + methodSignature.getName());
    }

}
