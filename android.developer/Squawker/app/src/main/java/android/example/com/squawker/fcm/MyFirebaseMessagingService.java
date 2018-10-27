package android.example.com.squawker.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.example.com.squawker.MainActivity;
import android.example.com.squawker.R;
import android.example.com.squawker.provider.SquawkContract;
import android.example.com.squawker.provider.SquawkProvider;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String LOG_TAG = MyFirebaseMessagingService.class.getSimpleName();

    private static final String NOTIFICATION_CHANNEL_ID = "squawker.main.notif.channel";
    private static final String NOTIFICATION_CHANNEL_NAME = "Squawker notification";

    private static final int NOTIFICATION_ID = 1000;

    private static final String DATA_KEY_AUTHOR = "author";
    private static final String DATA_KEY_AUTHOR_KEY = "author_key";
    private static final String DATA_KEY_MESSAGE = "message";
    private static final String DATA_KEY_DATE = "date";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String messageId = remoteMessage.getMessageId();
        Log.d(LOG_TAG, "Message ID: " + messageId);

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        // Notification data is available only for a notification message
        if (notification != null) {
            Log.d(LOG_TAG, "This is a notification message");
            Log.d(LOG_TAG, "Notification message title:" + notification.getTitle());
            Log.d(LOG_TAG, "Notification message body:" + notification.getBody());
        }

        // Let's also look at the data payload (optional)
        Map<String, String> data = remoteMessage.getData();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            Log.d(LOG_TAG, "Data entry: " + entry.getKey() + "=" + entry.getValue());
        }

        String author = data.get(DATA_KEY_AUTHOR);
        String message = data.get(DATA_KEY_MESSAGE);
        sendNotification(
                author != null ? author : "Anonymous",
                message != null ? message : "Empty message");

        saveSquawkToDb(data);
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(LOG_TAG, "New token: " + token);
    }

    private void sendNotification(String author, String message) {
        Context context = getApplicationContext();

        Intent intent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager == null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_duck)
                .setContentTitle(author)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // some more OS version dependent code (to increase compatibility)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
            Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        // trigger the actual notification
        manager.notify(NOTIFICATION_ID, builder.build());
    }

    private void saveSquawkToDb(final Map<String, String> data) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ContentValues newMessage = new ContentValues();
                newMessage.put(SquawkContract.COLUMN_AUTHOR, data.get(DATA_KEY_AUTHOR));
                newMessage.put(SquawkContract.COLUMN_AUTHOR_KEY, data.get(DATA_KEY_AUTHOR_KEY));
                newMessage.put(SquawkContract.COLUMN_MESSAGE, data.get(DATA_KEY_MESSAGE));
                newMessage.put(SquawkContract.COLUMN_DATE, data.get(DATA_KEY_DATE));
                getContentResolver().insert(SquawkProvider.SquawkMessages.CONTENT_URI, newMessage);

                Log.d(LOG_TAG, "New squawk saved!");

                return null;
            }
        }.execute();
    }
}
