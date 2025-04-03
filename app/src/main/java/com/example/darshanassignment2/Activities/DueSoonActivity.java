package com.example.darshanassignment2.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.wear.widget.WearableRecyclerView;

import com.example.darshanassignment2.Adapters.TaskAdapter;
import com.example.darshanassignment2.Model.Task;
import com.example.darshanassignment2.R;
import com.example.darshanassignment2.Utils.TaskManager;
import com.example.darshanassignment2.databinding.ActivityDueSoonBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DueSoonActivity extends AppCompatActivity {

    private WearableRecyclerView recyclerView;
    private TaskAdapter adapter;
    private List<Task> dueSoonTasks;
    private ActivityDueSoonBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDueSoonBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Load tasks due within 1 hour and set up RecyclerView
        dueSoonTasks = getTasksDueWithinHour();
        adapter = new TaskAdapter(dueSoonTasks);
        binding.recyclerViewDueSoon.setAdapter(adapter);
    }

    // Filters and returns a list of tasks due within the next one hour.

    private List<Task> getTasksDueWithinHour() {
        List<Task> allTasks = TaskManager.loadTasks(this);
        List<Task> dueTasks = new ArrayList<>();

        long now = System.currentTimeMillis();
        long oneHourLater = now + (60 * 60 * 1000); // 1 hour in milliseconds

        // Filter tasks whose due time is within the next hour
        for (Task task : allTasks) {
            if (task.getDueTimeMillis() >= now && task.getDueTimeMillis() <= oneHourLater) {
                dueTasks.add(task);
            }
        }

        return dueTasks;
    }
}
