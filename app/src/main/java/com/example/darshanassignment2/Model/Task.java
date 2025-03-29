package com.example.darshanassignment2.Model;

public class Task {
    private String id;
    private String name;
    private long dueTimeMillis;
    private int snoozeCount;

    public Task() {
        // Required for Gson
    }

    public Task(String id, String name, long dueTimeMillis) {
        this.id = id;
        this.name = name;
        this.dueTimeMillis = dueTimeMillis;
        this.snoozeCount = 0;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public long getDueTimeMillis() { return dueTimeMillis; }
    public int getSnoozeCount() { return snoozeCount; }

    public void setName(String name) { this.name = name; }
    public void setDueTimeMillis(long dueTimeMillis) { this.dueTimeMillis = dueTimeMillis; }
    public void setSnoozeCount(int count) { this.snoozeCount = count; }
    public void incrementSnooze() { this.snoozeCount++; }
}
