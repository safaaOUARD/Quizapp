package com.ensaj.quizapp_ouard;

import android.content.Intent;
import android.os.Bundle;

/**
 * Quiz5 - "En conduisant, je peux utiliser mon portable
 *          pour écrire un texto ou composer un numéro :"
 * Bonne réponse : Non
 */
public class Quiz5 extends QuizBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz5);
        initQuiz(savedInstanceState);
    }

    @Override
    protected int getQuestionNumber() {
        return 5;
    }

    @Override
    protected String getCorrectAnswer() {
        return "Non";
    }

    @Override
    protected void goToNextActivity(int score) {
        // Dernière question → on va à l'écran Score
        Intent intent = new Intent(Quiz5.this, Score.class);
        intent.putExtra("score", score);
        startActivity(intent);
    }
}