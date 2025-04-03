package com.example.darshanassignment2.Utils;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.darshanassignment2.Activities.DueSoonActivity;
import com.example.darshanassignment2.Model.Task;
import com.example.darshanassignment2.R;

import java.util.List;

public class NotificationScheduler {

    // Channel ID for notification
    private static final String CHANNEL_ID = "tasks_channel";

    // Checks all tasks and triggers a notification if any are due within 1 hour.
    public static void checkAndNotify(Context context) {
        List<Task> tasks = TaskManager.loadTasks(context);
        long now = System.currentTimeMillis();
        long oneHourLater = now + (60 * 60 * 1000);

        Task nextTask = null;

        for (Task task : tasks) {
            if (task.getDueTimeMillis() >= now && task.getDueTimeMillis() <= oneHourLater) {
                nextTask = task;
                break;
            }
        }

        if (nextTask != null) {
            createNotification(context, nextTask);
        }
    }

    // Creates a notification with task details and actions like snooze and delete.
    private static void createNotification(Context context, Task task) {
        createNotificationChannel(context);

        // Intent to open DueSoonActivity
        Intent viewIntent = new Intent(context, DueSoonActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(viewIntent);
        PendingIntent viewPendingIntent = stackBuilder.getPendingIntent(
                0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Snooze action intent
        Intent snoozeIntent = new Intent(context, NotificationReceiver.class);
        snoozeIntent.setAction(context.getString(R.string.action_snooze_task));
        PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(
                context, 1, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Delete action intent
        Intent deleteIntent = new Intent(context, NotificationReceiver.class);
        deleteIntent.setAction(context.getString(R.string.action_delete_task));
        PendingIntent deletePendingIntent = PendingIntent.getBroadcast(
                context, 2, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Build the notification using strings.xml
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(context.getString(R.string.notification_title_prefix) + task.getName())
                .setContentText(context.getString(R.string.notification_content_text))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(viewPendingIntent)
                .addAction(R.drawable.ic_launcher_foreground, context.getString(R.string.notification_action_snooze), snoozePendingIntent)
                .addAction(R.drawable.ic_launcher_foreground, context.getString(R.string.notification_action_delete), deletePendingIntent);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }


    //Creates the notification channel required for Android O and above.
    private static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.notification_channel_name);
            String description = context.getString(R.string.notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Sets a repeating alarm to check for due tasks every minute.
    public static void scheduleRepeatingCheck(Context context) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        long interval = 60 * 1000; // 1 minute
        long triggerAt = System.currentTimeMillis() + interval;

        manager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAt, interval, pendingIntent);
    }
}
