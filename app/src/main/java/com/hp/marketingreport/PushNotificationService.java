package com.hp.marketingreport;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Calendar;
import java.util.Date;

public class PushNotificationService extends FirebaseMessagingService {


    @SuppressLint("NewApi")
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        String title = remoteMessage.getData().get("title");
        String text = remoteMessage.getData().get("body");
        String CHANNEL_ID = "MESSAGE";
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.putExtra("title1",title);
        intent.putExtra("text1",text);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationManager notificationManager = null;
        NotificationCompat.Builder mBuilder;
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
            if (mChannel == null) {
                mChannel = new NotificationChannel(CHANNEL_ID, "Marketing Report", importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mChannel.setSound(alarmSound,audioAttributes);
                notificationManager.createNotificationChannel(mChannel);
            }

            mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
            mBuilder.setContentTitle(title)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText(text)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setSound(alarmSound)
                    .setDefaults(Notification.DEFAULT_ALL);
        } else {
            mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setContentTitle(title)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText(text)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setSound(alarmSound)
                    .setDefaults(Notification.DEFAULT_VIBRATE);
        }
        notificationManager.notify(0, mBuilder.build());
        Date date = Calendar.getInstance().getTime();
        SharedPreferences sharedPreferences = getSharedPreferences("msgRecieved", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(date.toString(), date +"$"+title+"$"+text);
        editor.apply();
        super.onMessageReceived(remoteMessage);
    }
}