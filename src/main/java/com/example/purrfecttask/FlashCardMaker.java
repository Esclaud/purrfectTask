package com.example.purrfecttask;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class FlashCardMaker extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private GestureDetector gestureDetector;
    private FrameLayout cardContainer;
    private List<FlashCard> flashCards;
    private int currentIndex;

    private Button addFlashcardButton;
    private TextView questionTextView;
    private TextView answerTextView;
    private LinearLayout flashcardListLayout;

    private boolean isPlaying = false;
    private static final int SWIPE_THRESHOLD = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        cardContainer = findViewById(R.id.card_container);
        gestureDetector = new GestureDetector(this, this);

        flashCards = new ArrayList<>();
        currentIndex = 0;

        addFlashcardButton = findViewById(R.id.btn_add_flashcard);
        questionTextView = findViewById(R.id.edit_question);
        answerTextView = findViewById(R.id.edit_answer);
        flashcardListLayout = findViewById(R.id.layout_flashcard_list);

        addFlashcardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = questionTextView.getText().toString().trim();
                String answer = answerTextView.getText().toString().trim();

                if (!question.isEmpty() && !answer.isEmpty()) {
                    FlashCard flashCard = new FlashCard(question, answer);
                    flashCards.add(flashCard);

                    questionTextView.setText("");
                    answerTextView.setText("");

                    Toast.makeText(FlashCardMaker.this, "Flashcard added", Toast.LENGTH_SHORT).show();

                    // Update flashcard list
                    updateFlashcardList();
                } else {
                    Toast.makeText(FlashCardMaker.this, "Please enter a question and an answer", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnStartFlashcardPlayback = findViewById(R.id.btn_start_flashcard_playback);
        btnStartFlashcardPlayback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlaying) {
                    startFlashcardPlayback();
                } else {
                    stopFlashcardPlayback();
                }
            }
        });

        showFlashCard(currentIndex);
        updateFlashcardList();
    }



    private void startFlashcardPlayback() {
        isPlaying = true;
        currentIndex = 0; // Reset the current index to the beginning
        showFlashCard(currentIndex);

        // Clear the flashcard list layout
        flashcardListLayout.removeAllViews();
    }

    private void stopFlashcardPlayback() {
        isPlaying = false;
    }

    private void showFlashCard(int index) {
        if (flashCards.size() > 0) {
            FlashCard flashCard = flashCards.get(index);

            // Inflate the card layout
            View cardView = getLayoutInflater().inflate(R.layout.card_layout, null);
            TextView textDescription = cardView.findViewById(R.id.text_description);
            TextView textAnswer = cardView.findViewById(R.id.text_answer);

            textDescription.setText(flashCard.getDescription());
            textAnswer.setText(flashCard.getAnswer());

            // Set visibility of answer based on card flip
            boolean isFlipped = flashCard.isFlipped();
            textDescription.setVisibility(isFlipped ? View.GONE : View.VISIBLE);
            textAnswer.setVisibility(isFlipped ? View.VISIBLE : View.GONE);

            // Set an OnClickListener to flip the card
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flipCard(flashCard);
                }
            });

            // Set the card layout as the content of the card container
            cardContainer.removeAllViews();
            cardContainer.addView(cardView);
        }
    }



    private void flipCard(FlashCard flashCard) {
        flashCard.setFlipped(!flashCard.isFlipped());
        showFlashCard(currentIndex);
    }

    private void updateFlashcardList() {
        flashcardListLayout.removeAllViews();

        for (FlashCard flashCard : flashCards) {
            View flashcardView = getLayoutInflater().inflate(R.layout.flashcard_list_item, null);
            TextView questionTextView = flashcardView.findViewById(R.id.text_question);
            TextView answerTextView = flashcardView.findViewById(R.id.text_answer);

            questionTextView.setText(flashCard.getDescription());
            answerTextView.setText(flashCard.getAnswer());

            flashcardListLayout.addView(flashcardView);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isPlaying) {
            gestureDetector.onTouchEvent(event);
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float deltaX = e2.getX() - e1.getX();
        if (Math.abs(deltaX) > SWIPE_THRESHOLD) {
            if (deltaX > 0) {
                showPreviousFlashCard();
            } else {
                showNextFlashCard();
            }
            return true;
        }
        return false;
    }


    private void showNextFlashCard() {
        if (currentIndex < flashCards.size() - 1) {
            currentIndex++;
            showFlashCard(currentIndex);
        } else {
            stopFlashcardPlayback();
        }
    }

    private void showPreviousFlashCard() {
        if (currentIndex > 0) {
            currentIndex--;
            showFlashCard(currentIndex);
        }
    }

    // Other methods from GestureDetector.OnGestureListener interface
    // These methods are not required for the basic functionality of the flash card generator.
    // You can choose to implement them if you want to add more gestures or animations.
    @Override
    public void onLongPress(MotionEvent e) {}

    @Override
    public boolean onDown(MotionEvent e) { return false; }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) { return false; }

    @Override
    public void onShowPress(MotionEvent e) {}

    @Override
    public boolean onSingleTapUp(MotionEvent e) { return false; }
}