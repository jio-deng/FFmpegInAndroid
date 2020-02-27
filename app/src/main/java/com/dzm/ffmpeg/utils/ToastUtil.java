package com.dzm.ffmpeg.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description toast
 * @date 2020/2/18 11:19
 */
public class ToastUtil {

    public static void makeText(Context context, String text) {
        makeText(context, text, Toast.LENGTH_SHORT);
    }

    public static void makeText(Context context, String text, int time) {
        Toast.makeText(context, text, time).show();
    }
}
