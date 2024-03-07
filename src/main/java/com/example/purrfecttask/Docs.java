package com.example.purrfecttask;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.pspdfkit.configuration.activity.PdfActivityConfiguration;
import com.pspdfkit.ui.PdfActivity;

import java.util.ArrayList;
import java.util.List;

public class Docs extends AppCompatActivity  implements View.OnClickListener {

    ImageButton btnDoc1, btnDoc2, btnDoc3, btnDoc4, btnDoc5, btnDoc6;
    Button btnNotes, btnMenu;
    private boolean notesSaved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docs);

        btnDoc1 = findViewById(R.id.btnDoc1);
        btnDoc2 = findViewById(R.id.btnDoc2);
        btnDoc3 = findViewById(R.id.btnDoc3);
        btnDoc4 = findViewById(R.id.btnDoc4);
        btnDoc5 = findViewById(R.id.btnDoc5);
        btnDoc6 = findViewById(R.id.btnDoc6);
        btnNotes = findViewById(R.id.btnNotes);
        btnMenu = findViewById(R.id.btnMenu);

        btnDoc1.setOnClickListener(this);
        btnDoc2.setOnClickListener(this);
        btnDoc3.setOnClickListener(this);
        btnDoc4.setOnClickListener(this);
        btnDoc5.setOnClickListener(this);
        btnDoc6.setOnClickListener(this);
        btnMenu.setOnClickListener(this);
        btnNotes.setOnClickListener(this);

        // Check if there are saved notes
        notesSaved = hasSavedNotes();

        // Add the notes button if there are saved notes
        if (notesSaved) {
            addNotesButton();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDoc1:
                Toast.makeText(Docs.this, "Document 1 is clicked", Toast.LENGTH_SHORT).show();
                final Uri uri = Uri.parse("file:///android_asset/Test.pdf");
                final PdfActivityConfiguration config = new PdfActivityConfiguration.Builder(Docs.this).build();
                PdfActivity.showDocument(this, uri, config);
                break;
            case R.id.btnDoc2:
                Toast.makeText(Docs.this, "Document 2 is Clicked", Toast.LENGTH_SHORT).show();
                final Uri uri1 = Uri.parse("file:///android_asset/TFN.pdf");
                final PdfActivityConfiguration config1 = new PdfActivityConfiguration.Builder(Docs.this).build();
                PdfActivity.showDocument(this, uri1, config1);
                break;
            case R.id.btnDoc3:
                Toast.makeText(Docs.this, "Document 3 is Clicked", Toast.LENGTH_SHORT).show();
                final Uri uri2 = Uri.parse("file:///android_asset/chap01.pdf");
                final PdfActivityConfiguration config2 = new PdfActivityConfiguration.Builder(Docs.this).build();
                PdfActivity.showDocument(this, uri2, config2);
                break;
            case R.id.btnDoc4:
                Toast.makeText(Docs.this, "Document 4 is Clicked", Toast.LENGTH_SHORT).show();
                final Uri uri3 = Uri.parse("file:///android_asset/act8.pdf");
                final PdfActivityConfiguration config3 = new PdfActivityConfiguration.Builder(Docs.this).build();
                PdfActivity.showDocument(this, uri3, config3);
                break;
            case R.id.btnDoc5:
                Toast.makeText(Docs.this, "Document 5 is Clicked", Toast.LENGTH_SHORT).show();
                final Uri uri4 = Uri.parse("file:///android_asset/hat.pdf");
                final PdfActivityConfiguration config4 = new PdfActivityConfiguration.Builder(Docs.this).build();
                PdfActivity.showDocument(this, uri4, config4);
                break;
            case R.id.btnDoc6:
                Toast.makeText(Docs.this, "Congratulations you know how to click", Toast.LENGTH_SHORT).show();
                final Uri uri5 = Uri.parse("file:///android_asset/sampol.pdf");
                final PdfActivityConfiguration config5 = new PdfActivityConfiguration.Builder(Docs.this).build();
                PdfActivity.showDocument(this, uri5, config5);
                break;
            case R.id.btnMenu:
                Toast.makeText(Docs.this, "Going Back to Menu", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, HomePage.class);
                startActivity(intent);
                break;
            case R.id.btnNotes:
                Toast.makeText(Docs.this, "Creating Notes", Toast.LENGTH_SHORT).show();
                Intent notesIntent = new Intent(Docs.this, Notes.class);
                startActivityForResult(notesIntent, 1);
                break;
        }
    }

    private boolean hasSavedNotes() {
        SQLiteDatabase database = openOrCreateDatabase("Notes", MODE_PRIVATE, null);

        // Create the "Notes" table if it doesn't exist
        database.execSQL("CREATE TABLE IF NOT EXISTS Notes (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, content TEXT)");

        Cursor cursor = database.rawQuery("SELECT * FROM Notes", null);
        boolean hasNotes = cursor.getCount() > 0;
        cursor.close();
        database.close();
        return hasNotes;
    }

    private void addNotesButton() {
        // Retrieve the saved notes from the database
        List<String> savedNotes = getSavedNotes();

        LinearLayout layout = findViewById(R.id.mainLayout);

        // Create a button for each saved note
        for (final String note : savedNotes) {
            ImageButton btnNote = new ImageButton(this);
            btnNote.setImageResource(R.mipmap.notes);
            btnNote.setBackgroundColor(Color.WHITE);
            btnNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Show the note in a dialog
                    showNoteDialog(note);
                }
            });

            GridLayout buttonLayout = findViewById(R.id.buttonLayout);
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
            layoutParams.setMargins(0, 10, 0, 10);
            layoutParams.setGravity(Gravity.CENTER);
            layoutParams.width = 122;
            layoutParams.height = 100;
            buttonLayout.addView(btnNote, layoutParams);
        }
    }

    private void showNoteDialog(String note) {
        // Split the note into title and content
        String[] noteParts = note.split("\n", 2);
        String title = noteParts[0];
        String content = noteParts[1];

        AlertDialog.Builder builder = new AlertDialog.Builder(Docs.this);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private List<String> getSavedNotes() {
        // Query the database to retrieve the saved notes
        List<String> notesList = new ArrayList<>();

        SQLiteDatabase database = openOrCreateDatabase("Notes", MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("SELECT * FROM Notes", null);

        int titleIndex = cursor.getColumnIndex("title");
        int contentIndex = cursor.getColumnIndex("content");

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(titleIndex);
                String content = cursor.getString(contentIndex);
                String note = title + "\n" + content;
                notesList.add(note);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return notesList;
    }
}