package com.ensaj.quizapp_ouard;

import android.content.Intent;
import android.os.Bundle;

/**
 * Quiz2 - "Le panneau de danger indique une succession de virages
 *          dont le 1er est :"
 * Bonne réponse : À droite
 */
public class Quiz2 extends QuizBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz2);
        initQuiz(savedInstanceState);
    }

    @Override
    protected int getQuestionNumber() {
        return 2;
    }

    @Override
    protected String getCorrectAnswer() {
        return "À droite";
    }

    @Override
    protected void goToNextActivity(int score) {
        Intent intent = new Intent(Quiz2.this, Quiz3.class);
        intent.putExtra("score", score);
        startActivity(intent);
    }
}