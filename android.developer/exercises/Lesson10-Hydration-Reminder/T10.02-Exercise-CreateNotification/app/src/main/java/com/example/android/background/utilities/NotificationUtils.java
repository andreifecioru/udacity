package com.example.android.background.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.android.background.MainActivity;
import com.example.android.background.R;

/**
 * Utility class for creating hydration notifications
 */
public class NotificationUtils {
    public static final int WATER_REMINDER_PENDING_INTENT_ID = 100;
    public static final int WATER_REMINDER_NOTIFICATION_ID = 1000;
    public static final String WATER_REMINDER_NOTIFICATION_CHANNEL_ID = "notif-channel-water-reminder";

    public static void remindUserBecauseCharging(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager == null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    WATER_REMINDER_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);

            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context,
                WATER_REMINDER_NOTIFICATION_CHANNEL_ID)
            .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
            .setSmallIcon(R.drawable.ic_drink_notification)
            .setLargeIcon(NotificationUtils.largeIcon(context))
            .setContentTitle(context.getString(R.string.charging_reminder_notification_title))
            .setContentText(context.getString(R.string.charging_reminder_notification_body))
            .setStyle(new NotificationCompat.BigTextStyle().bigText(
                    context.getString(R.string.charging_reminder_notification_body)))
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setContentIntent(contentIntent(context))
            .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
            Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {

            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        manager.notify(WATER_REMINDER_NOTIFICATION_ID, builder.build());

    }

    private static PendingIntent contentIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);

        return PendingIntent.getActivity(
                context,
                WATER_REMINDER_PENDING_INTENT_ID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        return BitmapFactory.decodeResource(res, R.drawable.ic_local_drink_black_24px);
    }
}
