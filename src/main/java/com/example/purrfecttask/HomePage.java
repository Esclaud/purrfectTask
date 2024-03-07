package com.example.purrfecttask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

public class HomePage extends AppCompatActivity implements View.OnClickListener {

    Button btnSched, btnTodo, btnFocus, btnGoal, btnSleep, btnNotes, btnFlashC;
    Toolbar toolbar;
    private static final String SELECTED_THEME_KEY = "selected_theme";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the selected theme from shared preferences
        sharedPreferences = getSharedPreferences("ThemePrefs", MODE_PRIVATE);
        int selectedTheme = getSelectedTheme();

        // Apply the selected theme
        setTheme(selectedTheme);

        setContentView(R.layout.activity_home_page);

        btnSched = findViewById(R.id.btnSched);
        btnTodo = findViewById(R.id.btnTodo);
        btnFocus = findViewById(R.id.btnFocus);
        btnSleep = findViewById(R.id.btnSleep);
        btnNotes = findViewById(R.id.btnNotes);
        btnFlashC = findViewById(R.id.btnFlashCard);

        btnSched.setOnClickListener(this);
        btnTodo.setOnClickListener(this);
        btnFocus.setOnClickListener(this);
        btnSleep.setOnClickListener(this);
        btnNotes.setOnClickListener(this);
        btnFlashC.setOnClickListener(this);

        toolbar = findViewById(R.id.toolbar);

        // Set the default theme initially
        if (selectedTheme == R.drawable.homepage_gradient) {
            toolbar.setBackgroundResource(R.drawable.homepage_gradient);
        }

        sharedPreferences = getSharedPreferences("ThemePrefs", MODE_PRIVATE);
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSched:
                Toast.makeText(this, "Your Schedules", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(this, ClassSchedule.class);
                startActivity(intent1);
                break;
            case R.id.btnTodo:
                Toast.makeText(this, "For To-do list", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(this, TodoList.class);
                startActivity(intent2);
                break;
            case R.id.btnFocus:
                Toast.makeText(this, "Time to Focus!", Toast.LENGTH_SHORT).show();
                Intent intent3 = new Intent(this, FocusMode.class);
                startActivity(intent3);
                break;
            case R.id.btnSleep:
                Toast.makeText(this, "Sleep activities", Toast.LENGTH_SHORT).show();
                Intent intent5 = new Intent(this, Sleep.class);
                startActivity(intent5);
                break;
            case R.id.btnNotes:
                Toast.makeText(this, "Notes and Documents", Toast.LENGTH_SHORT).show();
                Intent intent6 = new Intent(this, Docs.class);
                startActivity(intent6);
                break;
            case R.id.btnFlashCard:
                Toast.makeText(this, "Flash Cards", Toast.LENGTH_SHORT).show();
                Intent intent7 = new Intent(this, FlashCardMaker.class);
                startActivity(intent7);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_themes, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        Drawable selectedThemeDrawable;
        switch (id) {
            case R.id.menu_default:
                selectedThemeDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.homepage_gradient, getTheme());
                break;
            case R.id.menu_purple:
                selectedThemeDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.moon_theme, getTheme());
                break;
            case R.id.menu_decent:
                selectedThemeDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.decent_theme, getTheme());
                break;
            case R.id.menu_candy:
                selectedThemeDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.candy_theme, getTheme());
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        setTheme(selectedThemeDrawable);
        saveSelectedTheme(selectedThemeDrawable);

        return true;
    }

    public void setTheme(Drawable themeDrawable) {
        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayoutHomePage);
        constraintLayout.setBackground(themeDrawable);

        // Update the toolbar background as well
        toolbar.setBackground(themeDrawable);
    }


    private void saveSelectedTheme(Drawable themeDrawable) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (themeDrawable != null) {
            String themeKey = getThemeKey(themeDrawable);
            editor.putString(SELECTED_THEME_KEY, themeKey);
            editor.apply();
        }
    }

    private String getThemeKey(Drawable themeDrawable) {
        if (themeDrawable.getConstantState().equals(getResources().getDrawable(R.drawable.homepage_gradient).getConstantState())) {
            return "theme1";
        } else if (themeDrawable.getConstantState().equals(getResources().getDrawable(R.drawable.moon_theme).getConstantState())) {
            return "theme2";
        } else if (themeDrawable.getConstantState().equals(getResources().getDrawable(R.drawable.decent_theme).getConstantState())) {
            return "theme3";
        } else if (themeDrawable.getConstantState().equals(getResources().getDrawable(R.drawable.candy_theme).getConstantState())) {
            return "theme4";
        }
        return "";
    }

    private int getSelectedTheme() {
        String themeKey = sharedPreferences.getString(SELECTED_THEME_KEY, "");

        if (themeKey != null && !themeKey.isEmpty()) {
            Resources resources = getResources();
            int resourceId = resources.getIdentifier(themeKey, "drawable", getPackageName());
            if (resourceId != 0) {
                return resourceId;
            }
        }

        // Return the default theme if no theme is found
        return R.drawable.homepage_gradient;
    }
}