package com.example.darshanassignment2.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;

import com.example.darshanassignment2.Adapters.TaskAdapter;
import com.example.darshanassignment2.Model.Task;
import com.example.darshanassignment2.R;
import com.example.darshanassignment2.Utils.TaskManager;

import java.util.List;

public class TaskListActivity extends AppCompatActivity {

    private WearableRecyclerView recyclerView;
    private TaskAdapter adapter;
    private List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list); // Inflate layout

        // Initialize RecyclerView and load stored tasks
        recyclerView = findViewById(R.id.recyclerView);
        taskList = TaskManager.loadTasks(this);

        // Setup RecyclerView for optimal performance on Wear OS
        recyclerView.setHasFixedSize(true);
        recyclerView.setEdgeItemsCenteringEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Bind adapter to task list
        adapter = new TaskAdapter(taskList);
        recyclerView.setAdapter(adapter);
    }

    //Reloads the task list every time the activity is resumed.
    @Override
    protected void onResume() {
        super.onResume();
        taskList.clear(); // Clear current list
        taskList.addAll(TaskManager.loadTasks(this)); // Reload tasks
        adapter.notifyDataSetChanged(); // Refresh UI
    }
}
