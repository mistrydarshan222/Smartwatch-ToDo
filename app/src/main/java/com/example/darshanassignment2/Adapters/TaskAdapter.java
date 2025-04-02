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

    // List to hold task data
    private List<Task> tasks;

    // Formatter for displaying time in hh:mm AM/PM format
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

    // Constructor to initialize the adapter with a list of tasks
    public TaskAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    // Called when RecyclerView needs a new ViewHolder (inflate item layout here)
    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    // Called to bind data to the ViewHolder at the given position
    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = tasks.get(position);

        // Set task name and due time
        holder.txtTaskName.setText(task.getName());
        holder.txtDueTime.setText("Due: " + timeFormat.format(new Date(task.getDueTimeMillis())));

        // On item click, open EditTaskActivity with task ID
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EditTaskActivity.class);
            intent.putExtra("TASK_ID", task.getId());
            v.getContext().startActivity(intent);
        });

        // On long press, delete the task and remove it from the list
        holder.itemView.setOnLongClickListener(v -> {
            TaskManager.deleteTaskById(v.getContext(), task.getId()); // Delete from storage
            tasks.remove(position); // Remove from local list
            notifyItemRemoved(position); // Notify adapter to refresh UI
            return true;
        });
    }

    // Returns the total number of items in the list
    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
