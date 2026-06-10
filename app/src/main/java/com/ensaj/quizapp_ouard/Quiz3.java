package com.ensaj.quizapp_ouard;

import android.content.Intent;
import android.os.Bundle;

/**
 * Quiz3 - "Avant de partir, je laisse tourner mon moteur
 *          pour qu'il monte en température :"
 * Bonne réponse : Non
 */
public class Quiz3 extends QuizBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz3);
        initQuiz(savedInstanceState);
    }

    @Override
    protected int getQuestionNumber() {
        return 3;
    }

    @Override
    protected String getCorrectAnswer() {
        return "Non";
    }

    @Override
    protected void goToNextActivity(int score) {
        Intent intent = new Intent(Quiz3.this, Quiz4.class);
        intent.putExtra("score", score);
        startActivity(intent);
    }
}