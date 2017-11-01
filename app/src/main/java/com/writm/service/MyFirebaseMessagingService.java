package com.writm.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.writm.R;

/**
 * Created by Atreyee Ray on 6/4/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {



        Log.v("FRONT VALUE",remoteMessage.toString());
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            //sendNotification("title",remoteMessage.getData().toString());

        }


            sendNotification(remoteMessage);
    }

    private void sendNotification(RemoteMessage remoteMessage) {

        boolean flag=false;
        String click_action=remoteMessage.getData().get("click_action");
        if(remoteMessage.getData().get("type")!=null && remoteMessage.getData().get("type").equals("author_id"))
        {
            flag=true;

        }
        Log.v(TAG,click_action);
        Intent intent=new Intent(click_action);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra("body",remoteMessage.getData().get("body"));
        if(flag)
            intent.putExtra("auth_id",remoteMessage.getData().get("id"));
        else if(remoteMessage.getData().get("id")!=null)
        {
            intent.putExtra("post_id",remoteMessage.getData().get("id"));
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0 /* request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long[] pattern = {500,500,500,500,500};

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setSmallIcon(getNotificationIcon())
                .setContentText(remoteMessage.getData().get("body"))
                .setAutoCancel(true)
                .setVibrate(pattern)
                .setLights(Color.BLUE,1,1)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        //If the build version is higher than kitkat we need to create Silhouette icon.
        return useWhiteIcon ? R.drawable.ic_stat_notifications : R.drawable.notificationicon;
    }
}
