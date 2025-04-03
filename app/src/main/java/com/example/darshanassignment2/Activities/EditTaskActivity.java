package com.example.darshanassignment2.Activities;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.darshanassignment2.Model.Task;
import com.example.darshanassignment2.R;
import com.example.darshanassignment2.Utils.TaskManager;
import com.example.darshanassignment2.databinding.ActivityEditTaskBinding;

import java.util.Calendar;

public class EditTaskActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityEditTaskBinding binding;
    private Task currentTask;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize calendar with current time
        calendar = Calendar.getInstance();

        // Retrieve task ID passed via Intent
        String taskId = getIntent().getStringExtra(getString(R.string.extra_task_id));

        // Load the task using the task ID
        currentTask = TaskManager.getTaskById(this, taskId);

        // Populate input fields with current task data
        if (currentTask != null) {
            binding.editTaskName.setText(currentTask.getName());
            calendar.setTimeInMillis(currentTask.getDueTimeMillis());
        }

        // Set up listeners for buttons
        binding.btnPickTime.setOnClickListener(this);
        binding.btnSaveTask.setOnClickListener(this);
    }

    //Handles clicks for pick time and save buttons.
    @Override
    public void onClick(View v) {
        int id = v.getId();

        // Show a time picker dialog
        if (id == binding.btnPickTime.getId()) {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute1) {
                    // Update calendar with selected time
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute1);
                }
            }, hour, minute, true).show();
        }

        // Save the updated task
        else if (id == binding.btnSaveTask.getId()) {
            String updatedName = binding.editTaskName.getText().toString();

            if (!updatedName.isEmpty()) {
                // Update task object
                currentTask.setName(updatedName);
                currentTask.setDueTimeMillis(calendar.getTimeInMillis());

                // Save the task
                TaskManager.updateTask(this, currentTask);

                // Close activity
                finish();
            } else {
                binding.editTaskName.setError(getString(R.string.error_name_required));
            }
        }
    }
}
