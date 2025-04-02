package com.example.darshanassignment2.Adapters;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.darshanassignment2.R;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    TextView txtTaskName;
    TextView txtDueTime;

    // Constructor initializes the views from the layout item
    public TaskViewHolder(View itemView) {
        super(itemView);
        // Bind TextViews to their IDs in item_task.xml
        txtTaskName = itemView.findViewById(R.id.txtTaskName);
        txtDueTime = itemView.findViewById(R.id.txtDueTime);
    }
}
