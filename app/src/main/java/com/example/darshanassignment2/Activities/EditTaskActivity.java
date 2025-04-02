package com.example.darshanassignment2.Activities;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.darshanassignment2.Model.Task;
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

        calendar = Calendar.getInstance();

        String taskId = getIntent().getStringExtra("TASK_ID");
        currentTask = TaskManager.getTaskById(this, taskId);

        if (currentTask != null) {
            binding.editTaskName.setText(currentTask.getName());
            calendar.setTimeInMillis(currentTask.getDueTimeMillis());
        }

        binding.btnPickTime.setOnClickListener(this);
        binding.btnSaveTask.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == binding.btnPickTime.getId()) {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute1) {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute1);
                }
            }, hour, minute, true).show();
        } else if (id == binding.btnSaveTask.getId()) {
            String updatedName = binding.editTaskName.getText().toString();
            if (!updatedName.isEmpty()) {
                currentTask.setName(updatedName);
                currentTask.setDueTimeMillis(calendar.getTimeInMillis());
                TaskManager.updateTask(this, currentTask);
                finish();
            } else {
                binding.editTaskName.setError("Name required");
            }
        }
    }
}
