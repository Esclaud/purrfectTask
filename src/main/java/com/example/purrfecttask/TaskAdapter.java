package com.example.purrfecttask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

public class TaskAdapter extends ArrayAdapter<TaskItem> {
    private Context context;
    private ArrayList<TaskItem> taskList;
    private TaskAdapterListener listener; // Interface reference

    public interface TaskAdapterListener {
        void onTaskDeleted(TaskItem taskItem);
        void onTaskChecked(TaskItem taskItem, boolean isChecked);
    }

    public TaskAdapter(Context context, ArrayList<TaskItem> taskList, TaskAdapterListener listener) {
        super(context, 0, taskList);
        this.context = context;
        this.taskList = taskList;
        this.listener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final TaskItem taskItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.task_item_layout, parent, false);
        }

        TextView textViewTask = convertView.findViewById(R.id.textViewTask);
        TextView textViewDeadline = convertView.findViewById(R.id.textViewDeadline);
        Button buttonDelete = convertView.findViewById(R.id.buttonDelete);
        final CheckBox checkBox = convertView.findViewById(R.id.checkBoxTask);

        textViewTask.setText(taskItem.getTaskName());
        textViewDeadline.setText("Deadline: " + taskItem.getDeadlineTime());

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(getItem(position));
                notifyDataSetChanged();
                listener.onTaskDeleted(taskItem); // Pass the taskItem to the listener
            }
        });

        checkBox.setOnCheckedChangeListener(null); // Remove previous listener to prevent unwanted updates
        checkBox.setChecked(taskItem.isChecked());

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                taskItem.setChecked(isChecked);
                listener.onTaskChecked(taskItem, isChecked); // Pass the taskItem and isChecked to the listener
            }
        });

        return convertView;
    }

    public int getProgressPercentage() {
        int checkedCount = 0;
        for (TaskItem taskItem : taskList) {
            if (taskItem.isChecked()) {
                checkedCount++;
            }
        }
        if (taskList.size() > 0) {
            return (int) ((checkedCount / (float) taskList.size()) * 100);
        } else {
            return 0;
        }
    }
}
