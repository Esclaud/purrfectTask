package com.example.purrfecttask;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;


import android.os.Bundle;

public class ClassSchedule extends AppCompatActivity {

    private CalendarView calendarView;
    private ScrollView eventDetailsScrollView;
    private LinearLayout eventDetailsLayout;
    private TextView eventDateTextView, reminderTextView;
    private EditText eventNameEditText, eventDescriptionEditText;
    private Button setReminderButton, colorPickerButton, deleteButton, setButton;
    private int selectedYear, selectedMonth, selectedDay;
    private int reminderHour, reminderMinute;
    private EventDatabaseHelper databaseHelper;
    AlertDialog alertDialog;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_schedule);

        databaseHelper = new EventDatabaseHelper(this);

        calendarView = findViewById(R.id.calendarView);
        eventDetailsScrollView = findViewById(R.id.eventDetailsScrollView);
        eventDetailsLayout = findViewById(R.id.eventDetailsLayout);
        eventDateTextView = findViewById(R.id.eventDateTextView);
        eventNameEditText = findViewById(R.id.eventNameEditText);
        eventDescriptionEditText = findViewById(R.id.eventDescriptionEditText);
        reminderTextView = findViewById(R.id.reminderTextView);
        setReminderButton = findViewById(R.id.setReminderButton);
        colorPickerButton = findViewById(R.id.colorPickerButton);
        deleteButton = findViewById(R.id.deleteButton);
        setButton = findViewById(R.id.setButton);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                selectedYear = year;
                selectedMonth = month;
                selectedDay = dayOfMonth;

                showEventDetails();
            }
        });

        colorPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPickerDialog();
            }
        });

        setReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvent();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvent();
            }
        });
    }

    private void showEventDetails() {
        String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
        eventDateTextView.setText(date);

        // Get a readable database
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        // Define the projection (columns to retrieve)
        String[] projection = {
                EventDatabaseHelper.COLUMN_NAME,
                EventDatabaseHelper.COLUMN_DESCRIPTION,
                EventDatabaseHelper.COLUMN_REMINDER
        };

        // Define the selection (WHERE clause)
        String selection = EventDatabaseHelper.COLUMN_DATE + " = ?";

        // Define the selection arguments
        String[] selectionArgs = {selectedYear + "/" + (selectedMonth + 1) + "/" + selectedDay};

        // Perform the query
        Cursor cursor = db.query(
                EventDatabaseHelper.TABLE_EVENTS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String eventName = cursor.getString(cursor.getColumnIndex(EventDatabaseHelper.COLUMN_NAME));
            @SuppressLint("Range") String eventDescription = cursor.getString(cursor.getColumnIndex(EventDatabaseHelper.COLUMN_DESCRIPTION));
            @SuppressLint("Range") String reminderTime = cursor.getString(cursor.getColumnIndex(EventDatabaseHelper.COLUMN_REMINDER));

            eventNameEditText.setText(eventName);
            eventDescriptionEditText.setText(eventDescription);

            if (reminderTime != null) {
                reminderTextView.setText("Remind at: " + reminderTime);
            } else {
                reminderTextView.setText("No reminder set");
            }

            deleteButton.setVisibility(View.VISIBLE);
        } else {
            eventNameEditText.setText("");
            eventDescriptionEditText.setText("");
            reminderTextView.setText("Set Reminder");
            deleteButton.setVisibility(View.GONE);
        }

        cursor.close();

        eventDetailsLayout.setVisibility(View.VISIBLE);
    }


    private void saveEvent() {
        String eventName = eventNameEditText.getText().toString().trim();
        String eventDescription = eventDescriptionEditText.getText().toString().trim();

        // Get the selected date
        String date = selectedYear + "/" + (selectedMonth + 1) + "/" + selectedDay;

        // Get the reminder time
        String reminderTime = ""; // Retrieve the reminder time

        // Get a writable database
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Create a ContentValues object to store the event details
        ContentValues values = new ContentValues();
        values.put(EventDatabaseHelper.COLUMN_DATE, date);
        values.put(EventDatabaseHelper.COLUMN_NAME, eventName);
        values.put(EventDatabaseHelper.COLUMN_DESCRIPTION, eventDescription);
        values.put(EventDatabaseHelper.COLUMN_REMINDER, reminderTime);

        // Insert the event into the database
        long eventId = db.insert(EventDatabaseHelper.TABLE_EVENTS, null, values);

        if (eventId != -1) {
            Toast.makeText(this, "Event saved successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to save event", Toast.LENGTH_SHORT).show();
        }

        clearFields();
    }

    private void deleteEvent() {
        // Get a writable database
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Define the WHERE clause for the delete query
        String whereClause = EventDatabaseHelper.COLUMN_DATE + " = ?";

        // Define the arguments for the delete query
        String[] whereArgs = {selectedYear + "/" + (selectedMonth + 1) + "/" + selectedDay};

        // Perform the delete operation
        int rowsDeleted = db.delete(EventDatabaseHelper.TABLE_EVENTS, whereClause, whereArgs);

        if (rowsDeleted > 0) {
            Toast.makeText(this, "Event deleted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to delete event", Toast.LENGTH_SHORT).show();
        }

        clearFields();
    }


    private void clearFields() {
        eventNameEditText.setText("");
        eventDescriptionEditText.setText("");
        reminderTextView.setText("Set Reminder:");
    }

    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                reminderHour = hourOfDay;
                reminderMinute = minute;

                String reminderTime = String.format("%02d:%02d", reminderHour, reminderMinute);
                reminderTextView.setText("Remind at: " + reminderTime);
            }
        }, hour, minute, DateFormat.is24HourFormat(this));

        timePickerDialog.show();
    }

    private void showColorPickerDialog() {

    }

}