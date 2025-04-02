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

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnAddTask.setOnClickListener(this);
        binding.btnViewTasks.setOnClickListener(this);

        NotificationScheduler.scheduleRepeatingCheck(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
        }

        NotificationScheduler.checkAndNotify(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.btnAddTask.getId()) {
            startActivity(new Intent(this, AddTaskActivity.class));
        } else if (v.getId() == binding.btnViewTasks.getId()) {
            startActivity(new Intent(this, TaskListActivity.class));
        }
    }
}
