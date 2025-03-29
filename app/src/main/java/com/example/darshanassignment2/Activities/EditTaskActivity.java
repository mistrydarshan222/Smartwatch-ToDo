// EditTaskActivity.java
package com.example.darshanassignment2.Activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.darshanassignment2.Model.Task;
import com.example.darshanassignment2.R;
import com.example.darshanassignment2.Utils.TaskManager;

import java.util.Calendar;

public class EditTaskActivity extends AppCompatActivity {
    private EditText editTaskName;
    private Task currentTask;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        editTaskName = findViewById(R.id.editTaskName);
        Button btnPickTime = findViewById(R.id.btnPickTime);
        Button btnSaveChanges = findViewById(R.id.btnSaveTask);

        calendar = Calendar.getInstance();

        String taskId = getIntent().getStringExtra("TASK_ID");
        currentTask = TaskManager.getTaskById(this, taskId);

        if (currentTask != null) {
            editTaskName.setText(currentTask.getName());
            calendar.setTimeInMillis(currentTask.getDueTimeMillis());
        }

        btnPickTime.setOnClickListener(v -> {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            new TimePickerDialog(this, (TimePicker view, int hourOfDay, int minute1) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute1);
            }, hour, minute, true).show();
        });

        btnSaveChanges.setOnClickListener(v -> {
            String updatedName = editTaskName.getText().toString();
            if (!updatedName.isEmpty()) {
                currentTask.setName(updatedName);
                currentTask.setDueTimeMillis(calendar.getTimeInMillis());
                TaskManager.updateTask(this, currentTask);
                finish();
            } else {
                editTaskName.setError("Name required");
            }
        });
    }
}
