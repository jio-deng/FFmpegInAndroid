package com.dzm.ffmpeg.utils;

import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Process;

import com.dzm.ffmpeg.MyApplication;

import java.util.List;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description TODO
 * @date 2019/11/28 9:45
 */
public class Utils {

    public static MyApplication getApplication() {
        return MyApplication.getInstance();
    }

    public static boolean isAppDebug() {
        return MyApplication.isAppDebug();
    }

    /**
     * see if main process is running already
     *
     * @param context context
     * @return whether main process is running
     */
    public static boolean isMainProcess(Context context) {
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = context.getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 将内容复制到剪切板
     */
    public static void clipToBoard(String text) {
        ClipboardManager cm = (ClipboardManager) Utils.getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText(null, text));
    }
}
