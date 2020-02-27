package com.dzm.ffmpeg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dzm.ffmpeg.utils.NotificationUtil;
import com.dzm.ffmpeg.wanandroid.WanAndroidActivity;
import com.dzm.ffmpeg.yinshipin.ConvertPcm2Mp3Activity;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // jump to : lame convert pcm to mp3
        findViewById(R.id.btn_convert_pcm_2_mp3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ConvertPcm2Mp3Activity.class);
                startActivity(intent);
            }
        });

        // jump to : wan android
        findViewById(R.id.btn_wan_android).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WanAndroidActivity.class);
                startActivity(intent);
            }
        });

        // test notification
        findViewById(R.id.btn_test_notification).setOnClickListener(new View.OnClickListener() {
            @Override
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
    }

    static{
        System.loadLibrary("native-lib");
    }
}
