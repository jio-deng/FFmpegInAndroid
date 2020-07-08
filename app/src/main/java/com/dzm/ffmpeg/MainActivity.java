package com.dzm.ffmpeg;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.dzm.ffmpeg.cars.KnowCarsActivity;
import com.dzm.ffmpeg.databinding.ActivityMainBinding;
import com.dzm.ffmpeg.optimize.AopOnClick;
import com.dzm.ffmpeg.tools.MorseCodeActivity;
import com.dzm.ffmpeg.utils.NotificationUtil;
import com.dzm.ffmpeg.wanandroid.WanAndroidActivity;
import com.dzm.ffmpeg.yinshipin.ConvertPcm2Mp3Activity;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description main : guide
 * @date 2019/11/28 9:50
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        ActivityMainBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // jump to : lame convert pcm to mp3
        dataBinding.btnConvertPcm2Mp3.setOnClickListener(new View.OnClickListener() {
            @Override
            @AopOnClick
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ConvertPcm2Mp3Activity.class);
                startActivity(intent);
            }
        });

        // jump to : wan android
        dataBinding.btnWanAndroid.setOnClickListener(new View.OnClickListener() {
            @Override
            @AopOnClick(5000)
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WanAndroidActivity.class);
                startActivity(intent);
            }
        });

        // test notification
        dataBinding.btnTestNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            @AopOnClick(5000)
            public void onClick(View v) {
                int id = 1;
                String channelId = "my_channel_01";
                String name = "渠道_01";
                String ticker = "You have received a new message!";
                String contentTitle = "Notification";
                String contentText = "This is a test notification.";
                NotificationUtil.notificate(MainActivity.this, id, channelId, name, ticker, contentTitle, contentText);
            }
        });

        // jump to : morse-code
        dataBinding.btnMorseCode.setOnClickListener(new View.OnClickListener() {
            @Override
            @AopOnClick(5000)
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MorseCodeActivity.class);
                startActivity(intent);
            }
        });

        // jump to : know_cars
        dataBinding.btnKnowCars.setOnClickListener(new View.OnClickListener() {
            @Override
            @AopOnClick(5000)
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, KnowCarsActivity.class);
                startActivity(intent);
            }
        });


        // test : bugly crash
        dataBinding.btnTestBugly.setOnClickListener(new View.OnClickListener() {
            @Override
            @AopOnClick(5000)
            public void onClick(View v) {
                CrashReport.testJavaCrash();
            }
        });
    }

    static{
        System.loadLibrary("native-lib");
    }
}
