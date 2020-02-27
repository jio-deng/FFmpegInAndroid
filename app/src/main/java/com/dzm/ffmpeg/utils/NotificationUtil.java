package com.dzm.ffmpeg.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.dzm.ffmpeg.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description Util for Notifications
 * @date 2020/2/18 11:41
 */
public class NotificationUtil {
    private static final String TAG = "NotificationUtil";

    public static void notificate(Context context, int id, String channelId, String name, String ticker, String contentTitle, String contentText) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_LOW);
            Log.i(TAG, mChannel.toString());
            notificationManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(context)
                    .setChannelId(channelId)
                    .setContentTitle(contentTitle)
                    .setContentText(contentText)
                    .setTicker(ticker)
                    .setSmallIcon(R.drawable.icon_video).build();
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId)
                    .setContentTitle(contentTitle)
                    .setContentText(contentText)
                    .setTicker(ticker)
                    .setSmallIcon(R.drawable.icon_video)
                    .setOngoing(true);
            notification = notificationBuilder.build();
        }

        notificationManager.notify(id, notification);
    }
}
