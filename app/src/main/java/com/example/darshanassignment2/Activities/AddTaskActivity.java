package com.example.darshanassignment2.Activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.darshanassignment2.Model.Task;
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

        dueDateTime = Calendar.getInstance();

        binding.btnVoiceInput.setOnClickListener(this);
        binding.btnPickTime.setOnClickListener(this);
        binding.btnSaveTask.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == binding.btnVoiceInput.getId()) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            startActivityForResult(intent, VOICE_INPUT_REQUEST_CODE);
        }
        else if (id == binding.btnPickTime.getId()) {
            showDatePicker(); // call our new method
        }
        else if (id == binding.btnSaveTask.getId()) {
            String name = binding.editTaskName.getText().toString();
            if (name.isEmpty()) {
                binding.editTaskName.setError("Enter Task Name");
                return;
            }

            String taskId = UUID.randomUUID().toString();
            long dueMillis = dueDateTime.getTimeInMillis();

            Task task = new Task(taskId, name, dueMillis);
            List<Task> tasks = TaskManager.loadTasks(this);
            tasks.add(task);
            TaskManager.saveTasks(this, tasks);

            finish();
        }
    }

    private void showDatePicker() {
        int year = dueDateTime.get(Calendar.YEAR);
        int month = dueDateTime.get(Calendar.MONTH);
        int day = dueDateTime.get(Calendar.DAY_OF_MONTH);

        new android.app.DatePickerDialog(this, new android.app.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                // Set date in calendar
                dueDateTime.set(Calendar.YEAR, year);
                dueDateTime.set(Calendar.MONTH, month);
                dueDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                // Now show the time picker
                showTimePicker();
            }
        }, year, month, day).show();
    }

    private void showTimePicker() {
        int hour = dueDateTime.get(Calendar.HOUR_OF_DAY);
        int minute = dueDateTime.get(Calendar.MINUTE);

        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute1) {
                dueDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                dueDateTime.set(Calendar.MINUTE, minute1);
            }
        }, hour, minute, true).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VOICE_INPUT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                binding.editTaskName.setText(result.get(0));
            }
        }
    }
}
