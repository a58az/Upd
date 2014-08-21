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


public class TimeNotification extends BroadcastReceiver {
    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    Date lastModified;

    @Override
    public void onReceive(Context context, Intent intent) {

        lastModified = new Date(intent.getLongExtra(Constants.lastModKey, 1));

        NetworkResult nwRes = new NetworkResult();
        Date t = nwRes.getNetworkResult();

        // save last
        t = t != null ? t : new Date(0);
        if (t.after(lastModified)) {
            MakeNotification(context, intent, t);
            lastModified = t;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void MakeNotification(Context context, Intent intent, Date date) {
//        Intent intent = new Intent(context, MyActivity.class);
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
