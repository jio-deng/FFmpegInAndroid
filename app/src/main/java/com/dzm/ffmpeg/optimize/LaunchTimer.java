package com.dzm.ffmpeg.optimize;

import com.dzm.ffmpeg.utils.LogUtils;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description 统计启动时间
 * @date 2020/6/17 14:32
 */
public class LaunchTimer {
    private static final String TAG = "LaunchTimer";

    private static long sTime;

    public static void startRecord() {
        sTime = System.currentTimeMillis();
    }

    public static void endRecord() {
        endRecord(TAG);
    }

    public static void endRecord(String tag) {
        long cost = System.currentTimeMillis() - sTime;
        LogUtils.d(tag, "cost : " + cost);
    }
}
