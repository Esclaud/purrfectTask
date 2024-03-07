package com.example.purrfecttask;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Sleep extends AppCompatActivity implements View.OnClickListener{

    private GridLayout checkboxLayout;
    private TextView resultLabel;
    private Button btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);


        btnMenu = findViewById(R.id.btnMenu);
        checkboxLayout = findViewById(R.id.checkbox_layout);
        resultLabel = findViewById(R.id.result_label);

        btnMenu.setOnClickListener(this);
        setupCheckboxes();
    }

    private void setupCheckboxes() {
        for (int i = 0; i < checkboxLayout.getChildCount(); i++) {
            View childView = checkboxLayout.getChildAt(i);
            if (childView instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) childView;
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        calculateResult();
                    }
                });
            }
        }
    }

    private void calculateResult() {
        int totalCheckedHours = 0;
        for (int i = 0; i < checkboxLayout.getChildCount(); i++) {
            View childView = checkboxLayout.getChildAt(i);
            if (childView instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) childView;
                if (checkBox.isChecked()) {
                    totalCheckedHours++;
                }
            }
        }

        if (totalCheckedHours < 6) {
            resultLabel.setText("You don't have enough sleep. I suggest you get some more.");
            // Perform Google searches on what lack of sleep causes
        } else if (totalCheckedHours >= 8 && totalCheckedHours <= 12) {
            resultLabel.setText("You have enough sleep. Now go and conquer your day!");
        } else {
            resultLabel.setText("You are oversleeping.");
            // Perform Google searches on dangers of oversleeping
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnMenu:
                Toast.makeText(Sleep.this, "Going Back to Menu", Toast.LENGTH_SHORT).show();
                Intent intent5 = new Intent(this, HomePage.class);
                startActivity(intent5);
                break;
        }
    }
}