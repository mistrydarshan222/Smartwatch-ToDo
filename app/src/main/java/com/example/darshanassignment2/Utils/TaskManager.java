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

    private static final String PREFS_NAME = "task_prefs";
    private static final String TASKS_KEY = "tasks";

    public static void saveTasks(Context context, List<Task> tasks) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        editor.putString(TASKS_KEY, gson.toJson(tasks));
        editor.apply();
    }

    public static List<Task> loadTasks(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(TASKS_KEY, null);
        if (json != null) {
            Type type = new TypeToken<ArrayList<Task>>() {}.getType();
            return new Gson().fromJson(json, type);
        }
        return new ArrayList<>();
    }

    public static Task getTaskById(Context context, String id) {
        List<Task> tasks = loadTasks(context);
        for (Task task : tasks) {
            if (task.getId().equals(id)) return task;
        }
        return null;
    }

    public static void updateTask(Context context, Task updatedTask) {
        List<Task> tasks = loadTasks(context);
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId().equals(updatedTask.getId())) {
                tasks.set(i, updatedTask);
                break;
            }
        }
        saveTasks(context, tasks);
    }

    public static void deleteTaskById(Context context, String taskId) {
        List<Task> tasks = loadTasks(context);
        tasks.removeIf(task -> task.getId().equals(taskId));
        saveTasks(context, tasks);
    }
}
