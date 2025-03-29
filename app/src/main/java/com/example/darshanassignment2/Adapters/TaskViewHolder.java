package com.example.darshanassignment2.Adapters;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.darshanassignment2.R;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    TextView txtTaskName, txtTaskId, txtDueTime;

    public TaskViewHolder(View itemView) {
        super(itemView);
        txtTaskName = itemView.findViewById(R.id.txtTaskName);
        txtDueTime = itemView.findViewById(R.id.txtDueTime);
    }
}
