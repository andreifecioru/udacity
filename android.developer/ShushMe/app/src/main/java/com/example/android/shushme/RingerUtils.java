package com.example.android.shushme;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.os.Build.VERSION.SDK_INT;
import static android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS;

class RingerUtils {
    static boolean isRingerPermissionGranted(Context context) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        if (notificationManager == null) return false;

        return !(SDK_INT >= 24 && !notificationManager.isNotificationPolicyAccessGranted());
    }

    static void askForRingerPermissions(Context context) {
        if (SDK_INT >= 23) {
            Intent intent = new Intent(ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            context.startActivity(intent);
        }
    }

    private RingerUtils() {
        throw new IllegalStateException("Utility class");
    }
}
