package com.example.purrfecttask;

public class TaskItem {
    private String taskName;
    private String deadlineTime;
    private boolean isChecked;

    public TaskItem(String taskName, String deadlineTime) {
        this.taskName = taskName;
        this.deadlineTime = deadlineTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getDeadlineTime() {
        return deadlineTime;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
