package com.example.android.classicalmusicquiz;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.app.NotificationCompat.MediaStyle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import static android.support.v4.media.session.PlaybackStateCompat.STATE_PLAYING;


class NotificationUtils {
    private static final int NOTIFICATION_ID = 100;
    private static final String NOTIFICATION_CHANNEL_ID = "music.quiz.main";
    private static final String NOTIFICATION_CHANNEL_NAME = "main";

    static void triggerMediaStyleNotification(Context context, MediaSessionCompat mediaSession, PlaybackStateCompat playbackState) {
        int iconRes;
        String actionTitle;

        switch (playbackState.getState()) {
            case STATE_PLAYING:
                iconRes = R.drawable.exo_controls_pause;
                actionTitle = context.getString(R.string.pause);
                break;
           default:
                iconRes = R.drawable.exo_controls_play;
                actionTitle = context.getString(R.string.play);
                break;
        }

        // by invoking MediaButtonReceiver#buildMediaButtonPendingIntent()
        // we make sure the appropriate callback in the media session will be called
        NotificationCompat.Action playPauseAction = new NotificationCompat.Action(iconRes, actionTitle,
                MediaButtonReceiver.buildMediaButtonPendingIntent(context, PlaybackStateCompat.ACTION_PLAY_PAUSE));

        NotificationCompat.Action restartAction = new NotificationCompat.Action(
                R.drawable.exo_controls_previous,
                context.getString(R.string.restart),
                MediaButtonReceiver.buildMediaButtonPendingIntent(context, PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));

        Intent intent = new Intent(context, QuizActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager == null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);

            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(context.getString(R.string.guess))
                .setContentText(context.getString(R.string.notification_text))
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(playPauseAction)
                .addAction(restartAction)
                .setStyle(new MediaStyle()
                    .setMediaSession(mediaSession.getSessionToken())
<<<<<<< HEAD
=======
                    // show both actions in the compact view
>>>>>>> [ANDROID.DEV] - MusicQuiz | Adding MediaStyle notifications
                    .setShowActionsInCompactView(0, 1)
                )
                .setAutoCancel(true);

        // some more OS version dependent code (to increase compatibility)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

<<<<<<< HEAD
        manager.notify(builder.build());
=======
        manager.notify(NOTIFICATION_ID, builder.build());
>>>>>>> [ANDROID.DEV] - MusicQuiz | Adding MediaStyle notifications
    }

    private NotificationUtils() {
        throw new IllegalStateException("Utility class");
    }
}
