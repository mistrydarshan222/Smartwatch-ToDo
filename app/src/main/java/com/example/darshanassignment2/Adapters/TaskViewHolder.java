package com.example.darshanassignment2.Adapters;

import androidx.recyclerview.widget.RecyclerView;

import com.example.darshanassignment2.databinding.ItemTaskBinding;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    // ViewBinding reference for item_task.xml
    ItemTaskBinding binding;

    //Constructor initializes the ViewHolder with ViewBinding.
    public TaskViewHolder(ItemTaskBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    // Returns the binding reference to access views inside the layout.
    public ItemTaskBinding getBinding() {
        return binding;
    }
}
