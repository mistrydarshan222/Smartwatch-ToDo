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
        String action = intent.getAction(); // Get the action from the received intent

        // Handle SNOOZE_TASK action
        if ("SNOOZE_TASK".equals(action)) {
            List<Task> tasks = TaskManager.loadTasks(context); // Load saved tasks

            if (!tasks.isEmpty()) {
                Task task = tasks.get(0);

                // Allow snoozing up to 5 times only
                if (task.getSnoozeCount() < 5) {
                    task.incrementSnooze(); // Increase snooze count
                    task.setDueTimeMillis(System.currentTimeMillis() + 5 * 60 * 1000);
                    TaskManager.saveTasks(context, tasks); // Save updated task list
                    Log.d("NOTIFY", "Task snoozed!");
                }
            }


        } // DELETE_TASK action
        else if ("DELETE_TASK".equals(action)) {
            List<Task> tasks = TaskManager.loadTasks(context); // Load tasks

            if (!tasks.isEmpty()) {
                tasks.remove(0); // Delete the first task
                TaskManager.saveTasks(context, tasks); // Save the updated list
                Log.d("NOTIFY", "Task deleted!");
            }

        } else {
            NotificationScheduler.checkAndNotify(context); // reschedule tasks
        }
    }
}
