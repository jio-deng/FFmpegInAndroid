package com.dzm.ffmpeg;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.dzm.ffmpeg.cars.KnowCarsActivity;
import com.dzm.ffmpeg.databinding.ActivityMainBinding;
import com.dzm.ffmpeg.optimize.AopOnClick;
import com.dzm.ffmpeg.tools.MorseCodeActivity;
import com.dzm.ffmpeg.utils.NotificationUtil;
import com.dzm.ffmpeg.utils.rxpermissions.RxPermissions;
import com.dzm.ffmpeg.wanandroid.KnockNBottomSheetDialogFragment;
import com.dzm.ffmpeg.wanandroid.WanAndroidActivity;
import com.dzm.ffmpeg.yinshipin.ConvertPcm2Mp3Activity;
import com.dzm.ffmpeg.yinshipin.activity.FetchMetaDataActivity;
import com.tencent.bugly.crashreport.CrashReport;

import io.reactivex.disposables.Disposable;

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
        init(dataBinding);

        // get permission
        permissionCheck();
    }

    private void init(ActivityMainBinding dataBinding) {
        /* Video */

        // jump to : lame convert pcm to mp3
        dataBinding.btnConvertPcm2Mp3.setOnClickListener(new View.OnClickListener() {
            @Override
            @AopOnClick
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ConvertPcm2Mp3Activity.class);
                startActivity(intent);
            }
        });

        // jump to : get video Meta data
        dataBinding.btnGetVideoMetaData.setOnClickListener(new View.OnClickListener() {
            @Override
            @AopOnClick
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FetchMetaDataActivity.class);
                startActivity(intent);
            }
        });


        /* WanAndroid */

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

        // jump to : 敲7
        dataBinding.btnKnockN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KnockNBottomSheetDialogFragment.showKnockNDialogFragment(getSupportFragmentManager());
            }
        });


        /* Test */

        // test : bugly crash
        dataBinding.btnTestBugly.setOnClickListener(new View.OnClickListener() {
            @Override
            @AopOnClick(5000)
            public void onClick(View v) {
                CrashReport.testJavaCrash();
            }
        });
    }

    public void permissionCheck() {
        Disposable permissionDisposable = new RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE)
                .subscribe();
    }
}
