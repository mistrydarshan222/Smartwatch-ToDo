package com.example.darshanassignment2.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.darshanassignment2.Model.Task;
import com.example.darshanassignment2.R;

import java.util.List;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction(); // Get the action from the received intent

        String snoozeAction = context.getString(R.string.action_snooze_task);
        String deleteAction = context.getString(R.string.action_delete_task);
        String logTag = context.getString(R.string.log_tag_notify);

        // Handle SNOOZE_TASK action
        if (snoozeAction.equals(action)) {
            List<Task> tasks = TaskManager.loadTasks(context); // Load saved tasks

            if (!tasks.isEmpty()) {
                Task task = tasks.get(0);

                // Allow snoozing up to 5 times only
                if (task.getSnoozeCount() < 5) {
                    task.incrementSnooze(); // Increase snooze count
                    task.setDueTimeMillis(System.currentTimeMillis() + 5 * 60 * 1000);
                    TaskManager.saveTasks(context, tasks); // Save updated task list
                    Log.d(logTag, context.getString(R.string.log_task_snoozed));
                }
            }

        }
        // Handle DELETE_TASK action
        else if (deleteAction.equals(action)) {
            List<Task> tasks = TaskManager.loadTasks(context); // Load tasks

            if (!tasks.isEmpty()) {
                tasks.remove(0); // Delete the first task
                TaskManager.saveTasks(context, tasks); // Save the updated list
                Log.d(logTag, context.getString(R.string.log_task_deleted));
            }

        } else {
            // Reschedule task notifications
            NotificationScheduler.checkAndNotify(context);
        }
    }
}
