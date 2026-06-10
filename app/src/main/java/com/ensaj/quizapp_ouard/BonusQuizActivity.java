package com.ensaj.quizapp_ouard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class BonusQuizActivity extends AppCompatActivity {

    // ── Vues ──────────────────────────────────────────────────────
    private TextView    tvProgress, tvTimer, tvQuestion;
    private ImageView   ivQuestion;
    private RadioGroup  rg;
    private RadioButton rb1, rb2;
    private Button      bNext;
    private ProgressBar pbQuestions;

    // ── Sons ──────────────────────────────────────────────────────
    private SoundManager soundManager;  // ← AJOUTÉ

    // ── Données ───────────────────────────────────────────────────
    private List<Question> questions;
    private int currentIndex = 0;
    private int score        = 0;
    private boolean answered = false;

    // ── Timer ─────────────────────────────────────────────────────
    private CountDownTimer countDownTimer;
    private static final int TIMER_SECONDS   = 30;
    private static final int TOTAL_QUESTIONS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus_quiz);

        // Initialiser les sons ← AJOUTÉ
        soundManager = new SoundManager(this);

        // Récupération des vues
        tvProgress  = findViewById(R.id.tvProgress);
        tvTimer     = findViewById(R.id.tvTimer);
        tvQuestion  = findViewById(R.id.tvQuestion);
        ivQuestion  = findViewById(R.id.ivQuestion);
        rg          = findViewById(R.id.rg);
        rb1         = findViewById(R.id.rb1);
        rb2         = findViewById(R.id.rb2);
        bNext       = findViewById(R.id.bNext);
        pbQuestions = findViewById(R.id.pbQuestions);

        // Charger les questions mélangées
        questions = QuestionBank.getShuffledQuestions(TOTAL_QUESTIONS);

        // Afficher la première question
        loadQuestion();

        bNext.setOnClickListener(v -> {
            if (!answered) {
                submitAnswer();
            } else {
                nextQuestion();
            }
        });
    }

    private void loadQuestion() {
        answered = false;
        rg.clearCheck();
        setRadioGroupEnabled(true);
        bNext.setText("VALIDER");

        View root = findViewById(android.R.id.content);
        root.setBackgroundColor(Color.WHITE);

        Question q = questions.get(currentIndex);

        tvProgress.setText("Question " + (currentIndex + 1) + " / " + TOTAL_QUESTIONS);
        pbQuestions.setMax(TOTAL_QUESTIONS);
        pbQuestions.setProgress(currentIndex);

        tvQuestion.setText(q.getQuestionText());

        int resId = getResources().getIdentifier(
                q.getImageDrawable(), "drawable", getPackageName());
        if (resId != 0) ivQuestion.setImageResource(resId);

        rb1.setText(q.getOptionA());
        rb2.setText(q.getOptionB());

        startTimer();
    }

    private void startTimer() {
        if (countDownTimer != null) countDownTimer.cancel();

        countDownTimer = new CountDownTimer(TIMER_SECONDS * 1000L, 1000) {
            @Override
            public void onTick(long ms) {
                int sec = (int) (ms / 1000);
                tvTimer.setText("⏱ " + sec + "s");
                tvTimer.setTextColor(sec <= 10 ? Color.RED : Color.DKGRAY);
            }
            @Override
            public void onFinish() {
                if (!answered) {
                    Toast.makeText(BonusQuizActivity.this,
                            "Temps écoulé ! ⏰", Toast.LENGTH_SHORT).show();
                    submitAnswer();
                }
            }
        };
        countDownTimer.start();
    }

    private void submitAnswer() {
        if (rg.getCheckedRadioButtonId() == -1 && !answered) {
            Toast.makeText(this,
                    "Merci de choisir une réponse !", Toast.LENGTH_SHORT).show();
            return;
        }

        if (countDownTimer != null) countDownTimer.cancel();
        answered = true;
        setRadioGroupEnabled(false);
        bNext.setText("Suivant →");

        String selected = "";
        if (rg.getCheckedRadioButtonId() != -1) {
            RadioButton rb = findViewById(rg.getCheckedRadioButtonId());
            selected = rb.getText().toString();
        }

        String correct = questions.get(currentIndex).getCorrectAnswer();
        View root = findViewById(android.R.id.content);

        if (selected.equals(correct)) {
            score++;
            root.setBackgroundColor(Color.parseColor("#C8E6C9"));
            Toast.makeText(this, "✅ Bonne réponse !", Toast.LENGTH_SHORT).show();
            soundManager.playCorrect();
        } else {
            root.setBackgroundColor(Color.parseColor("#FFCDD2"));
            Toast.makeText(this,
                    "❌ Bonne réponse : " + correct, Toast.LENGTH_LONG).show();
            soundManager.playWrong();
        }
    }

    private void nextQuestion() {
        currentIndex++;
        if (currentIndex < questions.size()) {
            loadQuestion();
        } else {
            Intent intent = new Intent(this, Score.class);
            intent.putExtra("score", score);
            intent.putExtra("mode", "Mode Bonus");  // ← AJOUTÉ pour historique
            startActivity(intent);
            finish();
        }
    }

    private void setRadioGroupEnabled(boolean enabled) {
        for (int i = 0; i < rg.getChildCount(); i++) {
            rg.getChildAt(i).setEnabled(enabled);
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this,
                "Vous ne pouvez pas quitter le quiz !", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
        if (soundManager != null) soundManager.release();  // ← AJOUTÉ
    }
}