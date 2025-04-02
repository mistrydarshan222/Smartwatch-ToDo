package com.example.darshanassignment2.Utils;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.example.darshanassignment2.Activities.DueSoonActivity;
import com.example.darshanassignment2.Model.Task;
import com.example.darshanassignment2.R;
import java.util.List;

public class NotificationScheduler {

    // Channel ID for notification
    private static final String CHANNEL_ID = "tasks_channel";

    // Checks if any task is due within the next hour and shows a notification if found.
    public static void checkAndNotify(Context context) {
        List<Task> tasks = TaskManager.loadTasks(context); // Load saved tasks
        long now = System.currentTimeMillis();
        long oneHourLater = now + (60 * 60 * 1000); // Current time + 1 hour
        boolean taskFound = false;

        // Loop through tasks to find any due within the next hour
        for (Task task : tasks) {
            Log.d("NOTIFY_CHECK", "Checking task: " + task.getName());
            if (task.getDueTimeMillis() >= now && task.getDueTimeMillis() <= oneHourLater) {
                taskFound = true;
                break;
            }
        }

        // If a task is found, trigger the notification
        if (taskFound) {
            createNotification(context);
        }
    }

    // Creates and displays a notification with actions (Snooze, Delete).
    private static void createNotification(Context context) {
        // Ensure the notification channel is created (for Android O+)
        createNotificationChannel(context);

        // Intent to open DueSoonActivity when notification is tapped
        Intent viewIntent = new Intent(context, DueSoonActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(viewIntent);
        PendingIntent viewPendingIntent = stackBuilder.getPendingIntent(
                0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Intent for snoozing task
        Intent snoozeIntent = new Intent(context, NotificationReceiver.class);
        snoozeIntent.setAction("SNOOZE_TASK");
        PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(
                context, 1, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Intent for deleting task
        Intent deleteIntent = new Intent(context, NotificationReceiver.class);
        deleteIntent.setAction("DELETE_TASK");
        PendingIntent deletePendingIntent = PendingIntent.getBroadcast(
                context, 2, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Tasks Due Soon")
                .setContentText("You have tasks due in the next hour")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(viewPendingIntent)
                .addAction(R.drawable.ic_launcher_foreground, "Snooze", snoozePendingIntent)
                .addAction(R.drawable.ic_launcher_foreground, "Delete", deletePendingIntent);

        // Show the notification
        NotificationManager manager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }

    // Creates a notification channel
    private static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Task Alerts";
            String description = "Channel for upcoming task notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager =
                    context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Sets a repeating alarm to check for upcoming tasks every minute.
    public static void scheduleRepeatingCheck(Context context) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Intent to trigger NotificationReceiver periodically
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        long interval = 60 * 1000; // 1 minute
        long triggerAt = System.currentTimeMillis() + interval;

        // Set a repeating alarm to broadcast every minute
        manager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAt, interval, pendingIntent);
    }
}
