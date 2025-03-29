package com.example.darshanassignment2.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.darshanassignment2.Model.Task;

import java.util.List;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if ("SNOOZE_TASK".equals(action)) {
            List<Task> tasks = TaskManager.loadTasks(context);
            if (!tasks.isEmpty()) {
                Task task = tasks.get(0); // ⏱ Snooze first due task for now
                if (task.getSnoozeCount() < 5) {
                    task.incrementSnooze();
                    task.setDueTimeMillis(System.currentTimeMillis() + 5 * 60 * 1000); // 5 minutes
                    TaskManager.saveTasks(context, tasks);
                    Log.d("NOTIFY", "Task snoozed!");
                }
            }
        } else if ("DELETE_TASK".equals(action)) {
            List<Task> tasks = TaskManager.loadTasks(context);
            if (!tasks.isEmpty()) {
                tasks.remove(0); // Remove first due task
                TaskManager.saveTasks(context, tasks);
                Log.d("NOTIFY", "Task deleted!");
            }
        } else {
            NotificationScheduler.checkAndNotify(context); // default
        }
    }
}
