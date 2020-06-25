package com.dzm.ffmpeg;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Debug;

import androidx.core.os.TraceCompat;

import com.dzm.ffmpeg.hotfix.andfix.AndFixService;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description My Application
 * @date 2019/11/28 9:41
 */
public class MyApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    private static MyApplication sMe;

    private static boolean isAppDebug;

    @Override
    public void onCreate() {
        super.onCreate();

        traceStart();

        // LeakCanary
        initLeakCanary();

        // Bmob SDK
//        Bmob.initialize(this, Constant.BMOB_APP_ID);

        // Bugly : 为了保证运营数据的准确性，建议不要在异步线程初始化Bugly
        initBugly();

        // AndFix Service
        initAndFixService();

        traceEnd();
    }

    private void traceStart() {
        // systrace begin
//        TraceCompat.beginSection("AppOnCreate");
        // traceview start: 生成App.trace
//        Debug.startMethodTracing(getFilesDir() + "/App.trace");
    }

    private void traceEnd() {
        // traceview stop
//        Debug.stopMethodTracing();
        // systrace end
//        TraceCompat.endSection();
    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not initLeakCanary your app in this process.
            return;
        }
        LeakCanary.install(this);
        sMe = this;
        isAppDebug = BuildConfig.DEBUG;
    }

    /**
     * 启动AndFix Service
     */
    private void initAndFixService() {
        Intent intent = new Intent(this, AndFixService.class);
        startService(intent);
    }

    private void initBugly() {
        CrashReport.initCrashReport(getApplicationContext(), "ffb5f5471a", false);
    }


    /**  **/
    public static MyApplication getInstance() {
        return sMe;
    }

    public static boolean isAppDebug() {
        return isAppDebug;
    }
}
