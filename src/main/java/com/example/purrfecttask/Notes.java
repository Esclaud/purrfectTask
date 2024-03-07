package com.example.purrfecttask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Notes extends AppCompatActivity {

    private EditText txtTitle;
    private EditText txtNotes;
    private Button btnSave;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        txtTitle = findViewById(R.id.txtTitle);
        txtNotes = findViewById(R.id.txtNotes);
        btnSave = findViewById(R.id.btnSave);

        // Create or open the database
        database = openOrCreateDatabase("Notes", MODE_PRIVATE, null);

        // Create the "Notes" table if it doesn't exist
        database.execSQL("CREATE TABLE IF NOT EXISTS Notes (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, content TEXT)");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = txtTitle.getText().toString();
                String notes = txtNotes.getText().toString();

                // Save the notes to the database
                saveNotes(title, notes);

                Toast.makeText(Notes.this, "Notes saved", Toast.LENGTH_SHORT).show();

                // Finish the activity and go back to the previous activity
                finish();
            }
        });
    }

    private void saveNotes(String title, String notes) {
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", notes);
        database.insert("Notes", null, values);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database
        if (database != null) {
            database.close();
        }
    }
}