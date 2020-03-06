package com.dzm.ffmpeg.utils;

import android.util.Log;

import com.dzm.ffmpeg.BuildConfig;

/**
 * @author lvchong
 */

public class LogUtils {
    private static final boolean DEBUG = BuildConfig.DEBUG;

    private LogUtils() {
    }

    public static void d(String tag, String msg) {
        if (msg != null && DEBUG) {
            Log.d(getTag() + tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (msg != null && DEBUG) {
            Log.e(getTag() + tag, msg);
        }
    }

    public static void d(String msg) {
        if (msg != null && DEBUG) {
            Log.d(getTag(), msg);
        }
    }

    public static void e(String msg) {
        if (msg != null && DEBUG) {
            Log.e(getTag(), msg);
        }
    }

    public static void e(Exception e) {
        if (e != null && DEBUG) {
            Log.e(getTag(), e.getMessage(), e);
        }
    }

    public static String getTag(){
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stacks[4];
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf('.') + 1);

        String method = caller.getMethodName();
        int line = caller.getLineNumber();

        return "&_&  " + callerClazzName + "." + method + "(); L : " + line + " ";
    }
}
