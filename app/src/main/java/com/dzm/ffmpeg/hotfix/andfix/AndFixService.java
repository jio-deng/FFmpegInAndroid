package com.dzm.ffmpeg.hotfix.andfix;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description Service of AndFix, aim to look up for patches, download patched and load patches
 *              Remember to register this in manifest.xml
 * @date 2019/11/20 11:40
 */
public class AndFixService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO

        return START_NOT_STICKY;
    }
}
