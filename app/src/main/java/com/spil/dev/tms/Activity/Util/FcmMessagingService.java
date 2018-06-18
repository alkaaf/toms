package com.spil.dev.tms.Activity.Util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.spil.dev.tms.Activity.DashboardActivity;
import com.spil.dev.tms.Activity.Fragment.BerandaFragment;
import com.spil.dev.tms.Activity.ProsesActivity.ActivityProsesMap;
import com.spil.dev.tms.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by andresual on 1/23/2018.
 */

public class FcmMessagingService extends FirebaseMessagingService {

    public static final String INTENT_ID_DATA = "data.notification";
    String id, title, body;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Map<String, String> map = remoteMessage.getData();
        id = map.get("id");
        title = map.get("title");
        body = map.get("body");

        Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(INTENT_ID_DATA, id);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "Channel");
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(body);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        notificationBuilder.setPriority(Notification.PRIORITY_MAX);
        notificationBuilder.setLights(Color.DKGRAY,0,10000);
        notificationBuilder.setSound(alarmSound);
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());


        reloadUiOnNotif();

        super.onMessageReceived(remoteMessage);


    }

    private void reloadUiOnNotif() {
        // invoke branda fetch again
        BerandaFragment bf = BerandaFragment.getInstance();
        if (bf != null) {
            bf.invokeMe();
        }
        // invoke proses map ui
        if (ActivityProsesMap.getInstance() != null) {
            ActivityProsesMap.getInstance().fetchJob();
        }
    }
}