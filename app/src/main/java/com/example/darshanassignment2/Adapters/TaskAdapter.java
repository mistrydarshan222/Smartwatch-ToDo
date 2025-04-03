package com.example.darshanassignment2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.darshanassignment2.Activities.EditTaskActivity;
import com.example.darshanassignment2.Model.Task;
import com.example.darshanassignment2.R;
import com.example.darshanassignment2.Utils.TaskManager;
import com.example.darshanassignment2.databinding.ItemTaskBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {

    private List<Task> tasks;

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
        Context context = binding.getRoot().getContext();

        // Load localized date/time format patterns from strings.xml
        SimpleDateFormat timeFormat = new SimpleDateFormat(context.getString(R.string.time_format), Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat(context.getString(R.string.date_format), Locale.getDefault());

        // Set task name and due time details
        binding.txtTaskName.setText(task.getName());

        long dueTime = task.getDueTimeMillis();
        binding.txtDueDate.setText(context.getString(R.string.due_date_label) + dateFormat.format(new Date(dueTime)));
        binding.txtDueTime.setText(context.getString(R.string.due_time_label) + timeFormat.format(new Date(dueTime)));

        // Handle Edit button click
        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditTaskActivity.class);
                intent.putExtra(context.getString(R.string.extra_task_id), task.getId()); // Use string resource key
                context.startActivity(intent);
            }
        });

        // Handle Delete button click
        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskManager.deleteTaskById(context, task.getId());
                tasks.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
