package com.example.todolist;

public class Task {

    private String taskName;
    private long deadline;

    public Task(String taskName, long deadline) {
        this.taskName = taskName;
        this.deadline = deadline;
    }

    public String getTaskName() {
        return taskName;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }
}
