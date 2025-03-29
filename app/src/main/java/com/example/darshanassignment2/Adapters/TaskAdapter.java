package com.example.darshanassignment2.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.darshanassignment2.Activities.EditTaskActivity;
import com.example.darshanassignment2.Model.Task;
import com.example.darshanassignment2.R;
import com.example.darshanassignment2.Utils.TaskManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {

    private List<Task> tasks;
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

    public TaskAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.txtTaskName.setText(task.getName());
        holder.txtDueTime.setText("Due: " + timeFormat.format(new Date(task.getDueTimeMillis())));

        // Edit on item click
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EditTaskActivity.class);
            intent.putExtra("TASK_ID", task.getId());
            v.getContext().startActivity(intent);
        });

        // Long press to delete
        holder.itemView.setOnLongClickListener(v -> {
            TaskManager.deleteTaskById(v.getContext(), task.getId());
            tasks.remove(position);
            notifyItemRemoved(position);
            return true;
        });

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
