package com.dzm.ffmpeg.optimize;

import android.os.SystemClock;
import android.view.View;

import com.dzm.ffmpeg.utils.LogUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description AOP for launch
 * @date 2020/6/17 15:45
 */
@Aspect
public class LaunchAOP {

    /**
     * 最近一次点击的时间
     */
    private static long mLastClickTime;
    /**
     * 最近一次点击的控件ID
     */
    private static int mLastClickViewId;

    /**
     * 是否是快速点击
     *
     * @param v  点击的控件
     * @param intervalMillis  时间间期（毫秒）
     * @return  true:是，false:不是
     */
    public static boolean isFastDoubleClick(View v, long intervalMillis) {
        int viewId = v.getId();
//        long time = System.currentTimeMillis();
        long time = SystemClock.elapsedRealtime();
        long timeInterval = Math.abs(time - mLastClickTime);
        if (timeInterval < intervalMillis && viewId == mLastClickViewId) {
            return true;
        } else {
            mLastClickTime = time;
            mLastClickViewId = viewId;
            return false;
        }
    }

    // 第一个*所在的位置表示的是返回值，*表示的是任意的返回值，
    // onClick()中的 .. 所在位置是方法参数的位置，.. 表示的是任意类型、任意个数的参数
    // * 表示的是通配
    @Pointcut("execution(* android.view.View.OnClickListener.onClick(..))")
    public void clickMethod() {}

    @Around("clickMethod()")
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        LogUtils.d("AOP ONCLICK SUCCEED");

        // 取出方法的参数
        View view = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof View) {
                view = (View) arg;
                break;
            }
        }
        if (view == null) {
            return;
        }
        // 取出方法的注解
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        if (!method.isAnnotationPresent(AopOnClick.class)) {
            return;
        }
        AopOnClick aopOnclick = method.getAnnotation(AopOnClick.class);
        // 判断是否快速点击
        if (!isFastDoubleClick(view, aopOnclick.value())) {
            // 不是快速点击，执行原方法
            joinPoint.proceed();
        }
    }
}
