package com.dzm.ffmpeg;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description base activity
 * @date 2020/8/5 15:49
 */
public class BaseActivity extends AppCompatActivity {

    static{
        System.loadLibrary("media-handle");
        System.loadLibrary("mp3lame");
        System.loadLibrary("ffmpeg");
        System.loadLibrary("fmod");
        System.loadLibrary("fmodL");
    }
}
