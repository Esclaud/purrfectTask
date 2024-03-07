package com.example.purrfecttask;

public class FlashCard {
    private String description;
    private String answer;
    private boolean isFlipped;

    public FlashCard(String description, String answer) {
        this.description = description;
        this.answer = answer;
        this.isFlipped = false; // Initialize isFlipped to false
    }

    public String getDescription() {
        return description;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public void setFlipped(boolean flipped) {
        isFlipped = flipped;
    }
}
