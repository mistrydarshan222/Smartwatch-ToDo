package com.example.darshanassignment2.Activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.darshanassignment2.Model.Task;
import com.example.darshanassignment2.R;
import com.example.darshanassignment2.Utils.TaskManager;
import com.example.darshanassignment2.databinding.ActivityAddTaskBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class AddTaskActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityAddTaskBinding binding;
    private Calendar dueDateTime;
    private final int VOICE_INPUT_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize calendar with current time
        dueDateTime = Calendar.getInstance();

        // Set click listeners for buttons
        binding.btnVoiceInput.setOnClickListener(this);
        binding.btnPickTime.setOnClickListener(this);
        binding.btnSaveTask.setOnClickListener(this);
    }

    // Handles button clicks using their view IDs.
    @Override
    public void onClick(View v) {
        int id = v.getId();

        // Start voice input for task name
        if (id == binding.btnVoiceInput.getId()) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            startActivityForResult(intent, VOICE_INPUT_REQUEST_CODE);
        }

        // Show date picker followed by time picker
        else if (id == binding.btnPickTime.getId()) {
            showDatePicker();
        }

        // Save the task after validating input
        else if (id == binding.btnSaveTask.getId()) {
            String name = binding.editTaskName.getText().toString();
            if (name.isEmpty()) {
                binding.editTaskName.setError(getString(R.string.error_enter_task_name));
                return;
            }

            // Generate a unique task ID and set the due time
            String taskId = UUID.randomUUID().toString();
            long dueMillis = dueDateTime.getTimeInMillis();

            // Create and save the task
            Task task = new Task(taskId, name, dueMillis);
            List<Task> tasks = TaskManager.loadTasks(this);
            tasks.add(task);
            TaskManager.saveTasks(this, tasks);

            // Close the activity after saving
            finish();
        }
    }

    //Shows a DatePicker dialog to pick the due date.

    private void showDatePicker() {
        int year = dueDateTime.get(Calendar.YEAR);
        int month = dueDateTime.get(Calendar.MONTH);
        int day = dueDateTime.get(Calendar.DAY_OF_MONTH);

        new android.app.DatePickerDialog(this, new android.app.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                // Set the selected date
                dueDateTime.set(Calendar.YEAR, year);
                dueDateTime.set(Calendar.MONTH, month);
                dueDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                // Show time picker after date is set
                showTimePicker();
            }
        }, year, month, day).show();
    }

    //Shows a TimePicker dialog to pick the due time.

    private void showTimePicker() {
        int hour = dueDateTime.get(Calendar.HOUR_OF_DAY);
        int minute = dueDateTime.get(Calendar.MINUTE);

        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute1) {
                // Set the selected time
                dueDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                dueDateTime.set(Calendar.MINUTE, minute1);
            }
        }, hour, minute, true).show();
    }

    // Handles the result from the voice input activity.

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If voice input succeeded, set the recognized text to task name field
        if (requestCode == VOICE_INPUT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                binding.editTaskName.setText(result.get(0));
            }
        }
    }
}
