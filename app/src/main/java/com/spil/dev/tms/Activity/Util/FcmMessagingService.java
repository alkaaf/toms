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

import com.spil.dev.tms.Activity.BaseActivity;
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
    public static final String NOTIF_ID = "data.notification.id";
    public static final String NOTIF_TYPE = "Data.notification.type.cuk";
    String id, title, body;
    int notifId;
    String type;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Map<String, String> map = remoteMessage.getData();
        id = map.get("id");
        title = map.get("title");
        body = map.get("body");
        type = map.get("type");
        notifId = 0;
        if (map.get("notif_id") != null) {
            notifId = Integer.parseInt(map.get("notif_id"));
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "Channel");
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(body);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        notificationBuilder.setPriority(Notification.PRIORITY_MAX);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder.setSound(alarmSound);
        if (type.equals("blast")) {
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(INTENT_ID_DATA, id);
            intent.putExtra(NOTIF_ID, notifId);
            intent.putExtra(NOTIF_TYPE, type);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            notificationBuilder.setContentIntent(pendingIntent);
            notificationBuilder.setLights(Color.WHITE, 0, 10000);
        } else if (type.equals("pengumuman")) {
            notificationBuilder.setLights(Color.GREEN, 0, 10000);
        } else if (type.equals("logout")) {
            notificationBuilder.setLights(Color.RED, 0, 10000);
            Intent intentLogout = new Intent(BaseActivity.ACTION_GO_LOGOUT2);
            sendBroadcast(intentLogout);
        } else if (type.equals("refresh")) {
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(INTENT_ID_DATA, id);
            intent.putExtra(NOTIF_ID, notifId);
            intent.putExtra(NOTIF_TYPE, type);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            notificationBuilder.setContentIntent(pendingIntent);
            notificationBuilder.setLights(Color.BLUE, 0, 10000);
        } else if (type.equals("cancel")) {
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(INTENT_ID_DATA, id);
            intent.putExtra(NOTIF_ID, notifId);
            intent.putExtra(NOTIF_TYPE, type);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            notificationBuilder.setContentIntent(pendingIntent);
            notificationBuilder.setLights(Color.YELLOW, 0, 10000);
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notifId, notificationBuilder.build());

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