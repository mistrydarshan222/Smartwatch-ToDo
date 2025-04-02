package com.example.darshanassignment2.Adapters;

import androidx.recyclerview.widget.RecyclerView;

import com.example.darshanassignment2.databinding.ItemTaskBinding;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    ItemTaskBinding binding;

    public TaskViewHolder(ItemTaskBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public ItemTaskBinding getBinding() {
        return binding;
    }
}
