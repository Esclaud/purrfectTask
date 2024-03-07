package com.example.purrfecttask;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class TodoList extends AppCompatActivity implements TaskAdapter.TaskAdapterListener, View.OnClickListener {
    private EditText editTextTask;
    private Button buttonAddTask, btnMenu, buttonSetDeadline;
    private ListView listViewTasks;
    private ArrayList<TaskItem> taskList;
    private TaskAdapter adapter;
    private TextView textViewDeadline, textViewProgress;

    private Calendar deadlineCalendar;
    private SimpleDateFormat dateFormat;

    private TaskDbHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        editTextTask = findViewById(R.id.editTextTask);
        buttonAddTask = findViewById(R.id.buttonAddTask);
        btnMenu = findViewById(R.id.btnMenu);
        listViewTasks = findViewById(R.id.listViewTasks);
        buttonSetDeadline = findViewById(R.id.buttonSetDeadline);
        textViewDeadline = findViewById(R.id.textViewDeadlineTime);

        taskList = new ArrayList<>();
        adapter = new TaskAdapter(this, taskList, this);
        listViewTasks.setAdapter(adapter);

        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = editTextTask.getText().toString();
                String deadline = textViewDeadline.getText().toString();
                if (!task.isEmpty()) {
                    TaskItem taskItem = new TaskItem(task, deadline);
                    taskList.add(taskItem);
                    adapter.notifyDataSetChanged();
                    editTextTask.setText("");
                    textViewDeadline.setText("");
                    updateProgressLabel();
                }
            }
        });

        btnMenu.setOnClickListener(this);
        buttonSetDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        deadlineCalendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        textViewProgress = findViewById(R.id.textViewProgress);
        updateProgressLabel();

        dbHelper = new TaskDbHelper(this);
        db = dbHelper.getWritableDatabase();

        // Retrieve tasks from the database and populate the taskList
        taskList.addAll(getTasksFromDatabase());
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }

    void updateProgressLabel() {
        int progress = adapter.getProgressPercentage();
        String progressLabel = "Percent until Goal: " + progress + "%";
        textViewProgress.setText(progressLabel);
    }

    private void showTimePickerDialog() {
        int hour = deadlineCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = deadlineCalendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        deadlineCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        deadlineCalendar.set(Calendar.MINUTE, minute);

                        String formattedTime = dateFormat.format(deadlineCalendar.getTime());
                        textViewDeadline.setText(formattedTime);
                    }
                }, hour, minute, true);

        timePickerDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnMenu:
                // Save tasks to the database before navigating back to the menu
                saveTasksToDatabase();
                Toast.makeText(TodoList.this, "Going Back to Menu", Toast.LENGTH_SHORT).show();
                Intent intent5 = new Intent(this, HomePage.class);
                startActivity(intent5);
                break;
        }
    }

    private void saveTaskToDatabase(TaskItem taskItem) {
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_TASK_NAME, taskItem.getTaskName());
        values.put(TaskContract.TaskEntry.COLUMN_DEADLINE_TIME, taskItem.getDeadlineTime());
        values.put(TaskContract.TaskEntry.COLUMN_CHECKED, taskItem.isChecked() ? 1 : 0);
        db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values);
    }

    private ArrayList<TaskItem> getTasksFromDatabase() {
        ArrayList<TaskItem> taskList = new ArrayList<>();

        Cursor cursor = db.query(
                TaskContract.TaskEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            String taskName = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_NAME));
            String deadlineTime = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_DEADLINE_TIME));
            boolean isChecked = cursor.getInt(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_CHECKED)) == 1;

            TaskItem taskItem = new TaskItem(taskName, deadlineTime);
            taskItem.setChecked(isChecked);
            taskList.add(taskItem);
        }

        cursor.close();
        return taskList;
    }

    private void saveTasksToDatabase() {
        // Clear the existing tasks in the database
        db.delete(TaskContract.TaskEntry.TABLE_NAME, null, null);

        // Save each task in the taskList to the database
        for (TaskItem taskItem : taskList) {
            saveTaskToDatabase(taskItem);
        }
    }


    @Override
    public void onTaskDeleted(TaskItem taskItem) {
        taskList.remove(taskItem);
        adapter.notifyDataSetChanged();
        updateProgressLabel();
    }

    @Override
    public void onTaskChecked(TaskItem taskItem, boolean isChecked) {
        taskItem.setChecked(isChecked);
        adapter.notifyDataSetChanged();
        updateProgressLabel();
    }

}
