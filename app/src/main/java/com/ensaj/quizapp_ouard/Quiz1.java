package com.ensaj.quizapp_ouard;

import android.content.Intent;
import android.os.Bundle;

/**
 * Quiz1 - "À cette intersection, je laisse la priorité à droite :"
 * Bonne réponse : Non
 *
 * Hérite de QuizBaseActivity qui gère :
 *   - Le timer (30s)
 *   - Le feedback vert/rouge
 *   - La progression
 *   - La navigation
 */
public class Quiz1 extends QuizBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz1);

        // Initialisation commune (timer, vues, listeners)
        initQuiz(savedInstanceState);
    }

    @Override
    protected int getQuestionNumber() {
        return 1;
    }

    @Override
    protected String getCorrectAnswer() {
        return "Non";
    }

    @Override
    protected void goToNextActivity(int score) {
        Intent intent = new Intent(Quiz1.this, Quiz2.class);
        intent.putExtra("score", score);
        startActivity(intent);
    }
}