package com.example.darshanassignment2.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.darshanassignment2.Utils.NotificationScheduler;
import com.example.darshanassignment2.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding binding; // ViewBinding instance for accessing layout elements

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set click listeners for buttons
        binding.btnAddTask.setOnClickListener(this);
        binding.btnViewTasks.setOnClickListener(this);

        // Schedule background notifications to check for upcoming tasks
        NotificationScheduler.scheduleRepeatingCheck(this);

        // Request POST_NOTIFICATIONS permission on Android 13+ (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
        }

        // Immediate notification check for any due tasks
        NotificationScheduler.checkAndNotify(this);
    }

    //Handles button clicks for Add Task and View Tasks.
    @Override
    public void onClick(View v) {
        if (v.getId() == binding.btnAddTask.getId()) {
            // Navigate to AddTaskActivity
            startActivity(new Intent(this, AddTaskActivity.class));
        } else if (v.getId() == binding.btnViewTasks.getId()) {
            // Navigate to TaskListActivity
            startActivity(new Intent(this, TaskListActivity.class));
        }
    }
}
