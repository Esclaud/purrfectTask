package com.example.purrfecttask;

import android.provider.BaseColumns;

public final class TaskContract {
    private TaskContract() {}

    public static class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "task";
        public static final String COLUMN_TASK_NAME = "task_name";
        public static final String COLUMN_DEADLINE_TIME = "deadline_time";
        public static final String COLUMN_CHECKED = "checked";
    }
}