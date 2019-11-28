package com.dzm.ffmpeg;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.dzm.ffmpeg.hotfix.andfix.AndFixService;
import com.dzm.ffmpeg.utils.Utils;
import com.squareup.leakcanary.LeakCanary;

import cn.bmob.v3.Bmob;

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

        init();

        // init Bmob SDK
        Bmob.initialize(this, Constant.BMOB_APP_ID);

        // AndFix Service
        startAndFixService();
    }

    private void init() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        sMe = this;
        isAppDebug = BuildConfig.DEBUG;


    }

    /**
     * 启动AndFix Service
     */
    private void startAndFixService() {
        Intent intent = new Intent(this, AndFixService.class);
        startService(intent);
    }

    /**  **/
    public static MyApplication getInstance() {
        return sMe;
    }
}
