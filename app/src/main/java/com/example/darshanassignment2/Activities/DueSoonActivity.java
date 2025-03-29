package com.example.darshanassignment2.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.wear.widget.WearableRecyclerView;

import com.example.darshanassignment2.Adapters.TaskAdapter;
import com.example.darshanassignment2.Model.Task;
import com.example.darshanassignment2.R;
import com.example.darshanassignment2.Utils.TaskManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DueSoonActivity extends AppCompatActivity {

    private WearableRecyclerView recyclerView;
    private TaskAdapter adapter;
    private List<Task> dueSoonTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_due_soon);

        recyclerView = findViewById(R.id.recyclerViewDueSoon);
        dueSoonTasks = getTasksDueWithinHour();

        adapter = new TaskAdapter(dueSoonTasks);
        recyclerView.setAdapter(adapter);
    }

    private List<Task> getTasksDueWithinHour() {
        List<Task> allTasks = TaskManager.loadTasks(this);
        List<Task> dueTasks = new ArrayList<>();

        long now = System.currentTimeMillis();
        long oneHourLater = now + (60 * 60 * 1000); // 1 hour in milliseconds

        for (Task task : allTasks) {
            if (task.getDueTimeMillis() >= now && task.getDueTimeMillis() <= oneHourLater) {
                dueTasks.add(task);
            }
        }

        return dueTasks;
    }
}
