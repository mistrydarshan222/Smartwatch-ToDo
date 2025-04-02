package com.example.darshanassignment2.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;

import com.example.darshanassignment2.Adapters.TaskAdapter;
import com.example.darshanassignment2.Model.Task;
import com.example.darshanassignment2.R;
import com.example.darshanassignment2.Utils.TaskManager;
import com.example.darshanassignment2.databinding.ActivityTaskListBinding;

import java.util.List;

public class TaskListActivity extends AppCompatActivity {

    private WearableRecyclerView recyclerView;
    private TaskAdapter adapter;
    private List<Task> taskList;
    private ActivityTaskListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTaskListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        taskList = TaskManager.loadTasks(this);

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setEdgeItemsCenteringEnabled(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TaskAdapter(taskList);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        taskList.clear();
        taskList.addAll(TaskManager.loadTasks(this));
        adapter.notifyDataSetChanged();
    }

}
