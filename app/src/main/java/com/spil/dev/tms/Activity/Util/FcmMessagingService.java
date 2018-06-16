package com.spil.dev.tms.Activity.Util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
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
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(body);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
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