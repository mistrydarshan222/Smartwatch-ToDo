package com.example.darshanassignment2.Activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.darshanassignment2.Model.Task;
import com.example.darshanassignment2.Utils.TaskManager;
import com.example.darshanassignment2.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class AddTaskActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTaskName;
    private Button btnVoiceInput, btnPickTime, btnSaveTask;
    private Calendar dueDateTime;
    private final int VOICE_INPUT_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        editTaskName = findViewById(R.id.editTaskName);
        btnVoiceInput = findViewById(R.id.btnVoiceInput);
        btnPickTime = findViewById(R.id.btnPickTime);
        btnSaveTask = findViewById(R.id.btnSaveTask);

        dueDateTime = Calendar.getInstance();

        // Set click listeners to this activity
        btnVoiceInput.setOnClickListener(this);
        btnPickTime.setOnClickListener(this);
        btnSaveTask.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnVoiceInput) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            startActivityForResult(intent, VOICE_INPUT_REQUEST_CODE);

        } else if (id == R.id.btnPickTime) {
            int hour = dueDateTime.get(Calendar.HOUR_OF_DAY);
            int minute = dueDateTime.get(Calendar.MINUTE);

            new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute1) {
                    dueDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    dueDateTime.set(Calendar.MINUTE, minute1);
                }
            }, hour, minute, true).show();

        } else if (id == R.id.btnSaveTask) {
            String name = editTaskName.getText().toString();
            if (name.isEmpty()) {
                editTaskName.setError("Enter Task Name");
                return;
            }

            String taskId = UUID.randomUUID().toString();
            long dueMillis = dueDateTime.getTimeInMillis();

            Task task = new Task(taskId, name, dueMillis);
            List<Task> tasks = TaskManager.loadTasks(this);
            tasks.add(task);
            TaskManager.saveTasks(this, tasks);

            finish(); // Close activity after saving
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VOICE_INPUT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                editTaskName.setText(result.get(0));
            }
        }
    }
}
