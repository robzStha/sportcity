package com.app.sportcity.pushnotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.app.sportcity.R;
import com.app.sportcity.activities.BaseActivity;
import com.app.sportcity.activities.NewsDetail;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private Random ran;
    private String title, msg;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            Log.d(TAG, "From: " + remoteMessage.getFrom());
//            remoteMessage.getData()
//            remoteMessage.getNotification().getTitle();
//            Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

            for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
//                Log.d(TAG, "fcm_Key, " + key + " fcm_Value " + value);
                if (key.equalsIgnoreCase("title")){
                    title = value;
                } else if (key.equalsIgnoreCase("message")){
                    msg = value;
//                } else if (key.equalsIgnoreCase("JobAlertId")){
//                    jobAlertId = value;
                }
            }
//            remoteMessage.get

            //Calling method to generate notification
            sendNotification();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendNotification() {
        Intent intent = null;
        PendingIntent pendingIntent;
        ran = new Random();
        int not_id = ran.nextInt()+1;

//        if (type.equalsIgnoreCase("JobAlerts")) {
            intent = new Intent(this, BaseActivity.class);
//        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
        } else {
            // Lollipop and greater specific setColor method goes here.
            notificationBuilder
                    .setSmallIcon(R.drawable.ic_notification)
                    .setColor(getResources().getColor(R.color.colorAccent))
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(not_id, notificationBuilder.build());
    }
}