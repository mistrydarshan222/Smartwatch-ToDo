package com.example.darshanassignment2.Model;

public class Task {
    // Unique identifier for the task
    private String id;

    // Task name/title
    private String name;

    // Due time in milliseconds (Unix timestamp)
    private long dueTimeMillis;

    // Number of times the task has been snoozed
    private int snoozeCount;

    // Constructor to create a new Task
    public Task(String id, String name, long dueTimeMillis) {
        this.id = id;
        this.name = name;
        this.dueTimeMillis = dueTimeMillis;
        this.snoozeCount = 0; // Initialize snooze count to 0
    }

    // Getter for ID
    public String getId() { return id; }

    // Getter for task name
    public String getName() { return name; }

    // Getter for due time in milliseconds
    public long getDueTimeMillis() { return dueTimeMillis; }

    // Getter for snooze count
    public int getSnoozeCount() { return snoozeCount; }

    // Setter to update task name
    public void setName(String name) { this.name = name; }

    // Setter to update due time
    public void setDueTimeMillis(long dueTimeMillis) { this.dueTimeMillis = dueTimeMillis; }

    // Setter to update snooze count
    public void setSnoozeCount(int count) { this.snoozeCount = count; }

    // Increment snooze count by 1
    public void incrementSnooze() { this.snoozeCount++; }
}
