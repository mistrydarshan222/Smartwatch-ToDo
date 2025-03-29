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

    private static final String CHANNEL_ID = "tasks_channel";

    public static void checkAndNotify(Context context) {
        List<Task> tasks = TaskManager.loadTasks(context);
        long now = System.currentTimeMillis();
        long oneHourLater = now + (60 * 60 * 1000);
        boolean taskFound = false;

        for (Task task : tasks) {
            Log.d("NOTIFY_CHECK", "Checking task: " + task.getName());
            if (task.getDueTimeMillis() >= now && task.getDueTimeMillis() <= oneHourLater) {
                taskFound = true;
                break;
            }
        }

        if (taskFound) {
            createNotification(context);
        }
    }

    private static void createNotification(Context context) {
        createNotificationChannel(context);

        // View Tasks intent
        Intent viewIntent = new Intent(context, DueSoonActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(viewIntent);
        PendingIntent viewPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Snooze intent
        Intent snoozeIntent = new Intent(context, NotificationReceiver.class);
        snoozeIntent.setAction("SNOOZE_TASK");
        PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(context, 1, snoozeIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Delete intent
        Intent deleteIntent = new Intent(context, NotificationReceiver.class);
        deleteIntent.setAction("DELETE_TASK");
        PendingIntent deletePendingIntent = PendingIntent.getBroadcast(context, 2, deleteIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

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

        NotificationManager manager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }

    private static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Task Alerts";
            String description = "Channel for upcoming task notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void scheduleRepeatingCheck(Context context) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        long interval = 60 * 1000; // 1 minute (you can change it)
        long triggerAt = System.currentTimeMillis() + interval;

        manager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAt, interval, pendingIntent);
    }
}
