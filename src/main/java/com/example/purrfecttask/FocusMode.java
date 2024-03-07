package com.example.purrfecttask;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.ObjectAnimator;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


import android.os.Bundle;

import java.util.Locale;

public class FocusMode extends AppCompatActivity {

    private TextView textViewTimer;
    private EditText editTextTime;
    private Button buttonStart, buttonPause, buttonReset;
    private ProgressBar progressBar;
    private CountDownTimer countDownTimer;
    private boolean timerRunning;
    private long timeLeftInMillis;
    private ObjectAnimator progressBarAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus_mode);

        textViewTimer = findViewById(R.id.textViewTimer);
        editTextTime = findViewById(R.id.editTextTime);
        buttonStart = findViewById(R.id.buttonStart);
        buttonPause = findViewById(R.id.buttonPause);
        buttonReset = findViewById(R.id.buttonReset);
        progressBar = findViewById(R.id.progressBar);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!timerRunning) {
                    String inputTime = editTextTime.getText().toString();
                    if (!inputTime.isEmpty()) {
                        long timeInMinutes = Long.parseLong(inputTime);
                        long timeInMillis = timeInMinutes * 60 * 1000; // Convert minutes to milliseconds
                        startTimer(timeInMillis);
                    }
                }
            }
        });

        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
    }

    private void startTimer(long timeInMillis) {
        countDownTimer = new CountDownTimer(timeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimer();
                updateProgressBarAnimation();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                updateProgressBarAnimation();
            }
        }.start();

        timerRunning = true;
        editTextTime.setEnabled(false);
        buttonStart.setEnabled(false);
        buttonPause.setEnabled(true);
        buttonReset.setEnabled(true);
        updateProgressBarAnimation();
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        timerRunning = false;
        buttonPause.setEnabled(false);
        buttonStart.setEnabled(true);
        updateProgressBarAnimation();
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        timerRunning = false;
        timeLeftInMillis = 0;
        updateTimer();
        editTextTime.setEnabled(true);
        buttonStart.setEnabled(true);
        buttonPause.setEnabled(false);
        buttonReset.setEnabled(false);
        updateProgressBarAnimation();
    }

    private void updateTimer() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        textViewTimer.setText(timeLeftFormatted);
    }

    private void updateProgressBarAnimation() {
        if (timerRunning) {
            if (progressBarAnimator != null && progressBarAnimator.isRunning()) {
                progressBarAnimator.cancel();
            }

            long totalTimeInMillis = Long.parseLong(editTextTime.getText().toString()) * 60 * 1000; // Convert input minutes to milliseconds
            long timeElapsedInMillis = totalTimeInMillis - timeLeftInMillis;
            int progress = (int) ((timeElapsedInMillis * 360) / totalTimeInMillis);
            progressBar.setProgress(progress);

            progressBarAnimator = ObjectAnimator.ofInt(progressBar, "progress", progress, 360);
            progressBarAnimator.setDuration(timeLeftInMillis);
            progressBarAnimator.setInterpolator(new LinearInterpolator());
            progressBarAnimator.start();
        } else {
            if (progressBarAnimator != null) {
                progressBarAnimator.cancel();
            }

            progressBar.setProgress(360);
        }
    }
}