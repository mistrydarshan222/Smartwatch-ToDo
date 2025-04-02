package com.example.darshanassignment2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.darshanassignment2.Activities.EditTaskActivity;
import com.example.darshanassignment2.Model.Task;
import com.example.darshanassignment2.Utils.TaskManager;
import com.example.darshanassignment2.databinding.ItemTaskBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {

    private List<Task> tasks;

    private final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());

    public TaskAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemTaskBinding binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TaskViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        ItemTaskBinding binding = holder.getBinding();

        // Populate views
        binding.txtTaskId.setText("Task ID: " + task.getId());
        binding.txtTaskName.setText(task.getName());

        long dueTime = task.getDueTimeMillis();
        binding.txtDueDate.setText("Due Date: " + dateFormat.format(new Date(dueTime)));
        binding.txtDueTime.setText("Due Time: " + timeFormat.format(new Date(dueTime)));

        View rootView = binding.getRoot();

        // Handle item click
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, EditTaskActivity.class);
                intent.putExtra("TASK_ID", task.getId());
                context.startActivity(intent);
            }
        });

        // Handle item long-click (delete)
        rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Context context = v.getContext();
                TaskManager.deleteTaskById(context, task.getId());
                tasks.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
