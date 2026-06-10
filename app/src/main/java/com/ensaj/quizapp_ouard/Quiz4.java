package com.ensaj.quizapp_ouard;

import android.content.Intent;
import android.os.Bundle;

/**
 * Quiz4 - "En tant qu'automobiliste, vous devez être plus vigilant lorsque :"
 * Bonne réponse : Le tramway est arrêté
 */
public class Quiz4 extends QuizBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz4);
        initQuiz(savedInstanceState);
    }

    @Override
    protected int getQuestionNumber() {
        return 4;
    }

    @Override
    protected String getCorrectAnswer() {
        return "Le tramway est arrêté";
    }

    @Override
    protected void goToNextActivity(int score) {
        Intent intent = new Intent(Quiz4.this, Quiz5.class);
        intent.putExtra("score", score);
        startActivity(intent);
    }
}