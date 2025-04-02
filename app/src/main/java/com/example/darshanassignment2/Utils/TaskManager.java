package com.example.darshanassignment2.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.darshanassignment2.Model.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    // SharedPreferences file name and key for storing tasks
    private static final String PREFS_NAME = "task_prefs";
    private static final String TASKS_KEY = "tasks";

    // Saves the list of tasks to SharedPreferences as a JSON string.
    public static void saveTasks(Context context, List<Task> tasks) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        editor.putString(TASKS_KEY, gson.toJson(tasks)); // Convert list into JSON
        editor.apply(); // applky changes
    }

    // Loads the list of tasks from SharedPreferences.
    public static List<Task> loadTasks(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(TASKS_KEY, null); // Read stored JSON

        if (json != null) {
            Type type = new TypeToken<ArrayList<Task>>() {}.getType();
            return new Gson().fromJson(json, type); // Convert JSON back to list
        }

        return new ArrayList<>(); // Return empty list if no tasks found
    }

    //Finds and returns a task by its unique ID.
    public static Task getTaskById(Context context, String id) {
        List<Task> tasks = loadTasks(context);
        for (Task task : tasks) {
            if (task.getId().equals(id)) return task;
        }
        return null; // Return null if task not found
    }

    // Updates an existing task in the list and saves the new list.

    public static void updateTask(Context context, Task updatedTask) {
        List<Task> tasks = loadTasks(context);

        // Find and replace the task with the same ID
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId().equals(updatedTask.getId())) {
                tasks.set(i, updatedTask);
                break;
            }
        }

        saveTasks(context, tasks); // Save updated list
    }

    // Deletes a task by its ID and saves the updated list.

    public static void deleteTaskById(Context context, String taskId) {
        List<Task> tasks = loadTasks(context);

        // Remove task matching the given ID
        tasks.removeIf(task -> task.getId().equals(taskId));

        saveTasks(context, tasks); // Save updated list
    }
}
