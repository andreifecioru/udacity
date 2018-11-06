package com.example.android.shushme;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = GeofenceBroadcastReceiver.class.getSimpleName();

    private static final int NOTIFICATION_ID = 1000;
    private static final String NOTIFICATION_CHANNEL_ID = "shushme.main.channel.id";
    private static final String NOTIFICATION_CHANNEL_NAME = "main";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(LOG_TAG, "onReceive() called.");
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);

        switch (event.getGeofenceTransition()) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                triggerNotification(context, "Entering location", R.drawable.ic_volume_off_white_24dp);
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                triggerNotification(context, "Exiting location", R.drawable.ic_volume_up_white_24dp);
                break;
        }
    }

    private void triggerNotification(Context context, String title, int iconResId) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager == null) return;

        Intent intent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);

            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(iconResId)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), iconResId))
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        Log.i(LOG_TAG, "Triggering notification");
        manager.notify(NOTIFICATION_ID, builder.build());
    }
}
