package com.example.admin.upd;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import java.util.Date;

import static com.example.admin.upd.Constants.dt;
import static com.example.admin.upd.Constants.lastModIntentKey;


public class TimeNotification extends BroadcastReceiver {
    static Date last;
    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            long temp = intent.getLongExtra(lastModIntentKey, 0);
            Date date = new Date(temp);

            if (last == null) {
                last = date;
            } else {
                last = last.after(date) ? last : date;
            }
        } else {
            if (last == null) last = new Date();
        }

        NetworkResult nwRes = new NetworkResult();
        Date t = nwRes.getNetworkResult();

        // save last
        t = t != null ? t : new Date(0);
        if (t.after(last)) {
            MakeNotification(context, t);
            last = t;
        }
    }

    private void MakeNotification(Context context, Date date) {
        Intent intent = new Intent(context, MyActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Build notification
        Notification notify = new Notification.Builder(context)
                .setContentTitle("Расписание обновилось")
                .setContentText(dt.format(date)).setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pIntent)
                .setSound(soundUri)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // hide after selected
        notify.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, notify);
    }

}
