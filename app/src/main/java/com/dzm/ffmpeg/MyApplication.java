package com.dzm.ffmpeg;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;

import com.dzm.ffmpeg.hotfix.andfix.AndFixService;
import com.dzm.ffmpeg.optimize.LaunchOptimizeManager;
import com.dzm.ffmpeg.utils.LogUtils;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;

import static com.dzm.ffmpeg.optimize.LaunchOptimizeManager.TASK_ANDFIX;
import static com.dzm.ffmpeg.optimize.LaunchOptimizeManager.TASK_BMOB;
import static com.dzm.ffmpeg.optimize.LaunchOptimizeManager.TASK_BUGLY;
import static com.dzm.ffmpeg.optimize.LaunchOptimizeManager.TASK_LEAKCANARY;

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

        List<LaunchOptimizeManager.LaunchTask> tasks = new ArrayList<>();
        // LeakCanary
        initLeakCanary(tasks);
        // Bmob SDK
        initBomb(tasks);
        // Bugly : 为了保证运营数据的准确性，建议不要在异步线程初始化Bugly
        initBugly(tasks);
        // AndFix Service
        initAndFixService(tasks);
        LaunchOptimizeManager.topologicalSort(tasks);

        // TODO CountDown

        traceEnd();
    }

    private void initLeakCanary(List<LaunchOptimizeManager.LaunchTask> tasks) {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not initLeakCanary your app in this process.
            return;
        }

        List<String> pres = new ArrayList<>();
//        pres.add(TASK_ANDFIX);
//        pres.add(TASK_BMOB);
//        pres.add(TASK_BUGLY);
        tasks.add(new LaunchOptimizeManager.LaunchTask(
                () -> {
                    LeakCanary.install(MyApplication.this);
                    LogUtils.d("LeakCanary init!");
                }, pres, TASK_LEAKCANARY));

        sMe = this;
        isAppDebug = BuildConfig.DEBUG;
    }

    private void initBomb(List<LaunchOptimizeManager.LaunchTask> tasks) {
        List<String> pres = new ArrayList<>();
//        pres.add(TASK_ANDFIX);
//        pres.add(TASK_BUGLY);
        tasks.add(new LaunchOptimizeManager.LaunchTask(
                () -> {
                    //        Bmob.initialize(this, Constant.BMOB_APP_ID);
                    LogUtils.d("Bmob init!");
                }, pres, TASK_BMOB));
    }

    /**
     * 启动AndFix Service
     */
    private void initAndFixService(List<LaunchOptimizeManager.LaunchTask> tasks) {
        List<String> pres = new ArrayList<>();
//        pres.add(TASK_BUGLY);
        tasks.add(new LaunchOptimizeManager.LaunchTask(
                () -> {
                    Intent intent = new Intent(MyApplication.this, AndFixService.class);
                    startService(intent);
                    LogUtils.d("AndFix init!");
                }, pres, TASK_ANDFIX));
    }

    private void initBugly(List<LaunchOptimizeManager.LaunchTask> tasks) {
        List<String> pres = new ArrayList<>();
        tasks.add(new LaunchOptimizeManager.LaunchTask(
                () -> {
                    CrashReport.initCrashReport(getApplicationContext(), "ffb5f5471a", false);
                    LogUtils.d("Bugly init!");
                }, pres, TASK_BUGLY));
    }


    /**  **/
    public static MyApplication getInstance() {
        return sMe;
    }

    public static boolean isAppDebug() {
        return isAppDebug;
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
}
