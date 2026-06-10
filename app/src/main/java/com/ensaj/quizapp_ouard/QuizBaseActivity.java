package com.ensaj.quizapp_ouard;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * QuizBaseActivity - Classe de base pour toutes les activités Quiz
 *
 * NOUVELLE FONCTIONNALITÉ MAJEURE : au lieu de répéter le même code dans
 * Quiz1, Quiz2 ... Quiz5, on centralise toute la logique commune ici.
 *
 * Fonctionnalités intégrées :
 *  - Timer de 30 secondes par question (CountDownTimer)
 *  - Feedback visuel immédiat : fond VERT (bonne réponse) / ROUGE (mauvaise)
 *  - Affichage du numéro de question et progression (ex: "Question 2 / 5")
 *  - Barre de progression des questions
 *  - La question suivante ne démarre qu'après validation (bouton Next)
 *  - Réponse automatiquement soumise si le timer expire (compte 0)
 */
public abstract class QuizBaseActivity extends AppCompatActivity {

    // ── Constantes ────────────────────────────────────────────────────────────
    protected static final int TOTAL_QUESTIONS  = 5;
    protected static final int TIMER_SECONDS    = 30;
    private   static final int TIMER_INTERVAL   = 1000; // 1 seconde

    // ── Vues communes (initialisées par les sous-classes via setContentView) ──
    protected RadioGroup  rg;
    protected RadioButton rb;
    protected Button      bNext;
    protected TextView    tvTimer;       // Affiche le compte à rebours
    protected TextView    tvProgress;    // Affiche "Question X / 5"
    protected ProgressBar pbQuestions;   // Barre de progression des questions

    // ── État du quiz ──────────────────────────────────────────────────────────
    protected int    score           = 0;
    protected String correctAnswer   = "";   // À définir dans chaque sous-classe
    protected int    questionNumber  = 1;    // Numéro de la question courante
    private   boolean answered       = false; // Empêche de changer la réponse après feedback

    // ── Timer ─────────────────────────────────────────────────────────────────
    private CountDownTimer countDownTimer;

    // ────────────────────────────────────────────────────────────────────────
    // Méthodes abstraites que chaque sous-classe DOIT implémenter
    // ────────────────────────────────────────────────────────────────────────

    /** Retourne le numéro de la question (1 à 5) */
    protected abstract int getQuestionNumber();

    /** Retourne la bonne réponse sous forme de texte */
    protected abstract String getCorrectAnswer();

    /** Lance l'activité suivante en transmettant le score */
    protected abstract void goToNextActivity(int score);

    // ────────────────────────────────────────────────────────────────────────
    // Initialisation commune - appelée par onCreate() des sous-classes
    // ────────────────────────────────────────────────────────────────────────

    /**
     * À appeler dans onCreate() de chaque sous-classe APRÈS setContentView().
     * Récupère les vues, configure le numéro de question, lance le timer.
     */
    protected void initQuiz(Bundle savedInstanceState) {
        // Récupération du score transmis par l'activité précédente
        score          = getIntent().getIntExtra("score", 0);
        questionNumber = getQuestionNumber();
        correctAnswer  = getCorrectAnswer();

        // Récupération des vues
        rg           = findViewById(R.id.rg);
        bNext        = findViewById(R.id.bNext);
        tvTimer      = findViewById(R.id.tvTimer);
        tvProgress   = findViewById(R.id.tvProgress);
        pbQuestions  = findViewById(R.id.pbQuestions);

        // ── Affichage de la progression ───────────────────────────────────────
        if (tvProgress != null) {
            tvProgress.setText("Question " + questionNumber + " / " + TOTAL_QUESTIONS);
        }
        if (pbQuestions != null) {
            pbQuestions.setMax(TOTAL_QUESTIONS);
            pbQuestions.setProgress(questionNumber - 1);
        }

        // ── Listener bouton Next ──────────────────────────────────────────────
        bNext.setOnClickListener(v -> {
            if (!answered) {
                submitAnswer();
            } else {
                // L'utilisateur a déjà vu le feedback → on passe à la suite
                navigateNext();
            }
        });

        // ── Démarrage du timer ────────────────────────────────────────────────
        startTimer();
    }

    // ────────────────────────────────────────────────────────────────────────
    // Timer
    // ────────────────────────────────────────────────────────────────────────

    /**
     * NOUVELLE FONCTIONNALITÉ : Timer de 30 secondes.
     * Affiche le compte à rebours en rouge quand il reste ≤ 10 secondes.
     * Soumet automatiquement la réponse (ou passe si aucune) à 0.
     */
    private void startTimer() {
        countDownTimer = new CountDownTimer(TIMER_SECONDS * 1000L, TIMER_INTERVAL) {

            @Override
            public void onTick(long millisUntilFinished) {
                int secondsLeft = (int) (millisUntilFinished / 1000);
                if (tvTimer != null) {
                    tvTimer.setText("⏱ " + secondsLeft + "s");
                    // Alerte visuelle : rouge quand ≤ 10 secondes
                    tvTimer.setTextColor(secondsLeft <= 10
                            ? Color.RED
                            : Color.DKGRAY);
                }
            }

            @Override
            public void onFinish() {
                if (!answered) {
                    Toast.makeText(QuizBaseActivity.this,
                            "Temps écoulé ! ⏰", Toast.LENGTH_SHORT).show();
                    submitAnswer(); // Soumet sans bonne réponse = pas de point
                }
            }
        };
        countDownTimer.start();
    }

    /** Arrête le timer proprement */
    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    // ────────────────────────────────────────────────────────────────────────
    // Logique de réponse
    // ────────────────────────────────────────────────────────────────────────

    /**
     * Valide la réponse sélectionnée et affiche un feedback coloré.
     * NOUVELLE FONCTIONNALITÉ : feedback immédiat vert/rouge + message.
     */
    private void submitAnswer() {
        if (rg.getCheckedRadioButtonId() == -1) {
            // Aucune réponse sélectionnée
            Toast.makeText(this,
                    "Merci de choisir une réponse S.V.P !", Toast.LENGTH_SHORT).show();
            return;
        }

        stopTimer();
        answered = true;

        rb = findViewById(rg.getCheckedRadioButtonId());
        String selectedAnswer = rb.getText().toString();

        if (selectedAnswer.equals(correctAnswer)) {
            // ✅ Bonne réponse
            score++;
            showFeedback(true);
            Toast.makeText(this, "✅ Bonne réponse !", Toast.LENGTH_SHORT).show();
        } else {
            // ❌ Mauvaise réponse
            showFeedback(false);
            Toast.makeText(this,
                    "❌ Mauvaise réponse. La bonne réponse est : " + correctAnswer,
                    Toast.LENGTH_LONG).show();
        }

        // Changer le label du bouton
        bNext.setText("Suivant →");
        // Désactiver le RadioGroup pour empêcher de changer la réponse
        setRadioGroupEnabled(false);
    }

    /**
     * NOUVELLE FONCTIONNALITÉ : colore le fond de l'écran en vert ou rouge
     * pour un feedback visuel immédiat.
     */
    private void showFeedback(boolean isCorrect) {
        View rootView = findViewById(android.R.id.content);
        rootView.setBackgroundColor(isCorrect
                ? Color.parseColor("#C8E6C9")  // Vert clair
                : Color.parseColor("#FFCDD2")); // Rouge clair
    }

    /** Désactive / réactive tous les RadioButton du groupe */
    private void setRadioGroupEnabled(boolean enabled) {
        for (int i = 0; i < rg.getChildCount(); i++) {
            rg.getChildAt(i).setEnabled(enabled);
        }
    }

    /** Passe à l'activité suivante */
    private void navigateNext() {
        goToNextActivity(score);
        // Animation de transition
        overridePendingTransition(R.anim.entry, R.anim.exit);
        finish();
    }

    // ────────────────────────────────────────────────────────────────────────
    // Cycle de vie
    // ────────────────────────────────────────────────────────────────────────

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer(); // Évite les fuites mémoire
    }

    /** Empêche de quitter avec le bouton Back pendant le quiz */
    @Override
    public void onBackPressed() {
        Toast.makeText(this,
                "Vous ne pouvez pas quitter le quiz en cours !",
                Toast.LENGTH_SHORT).show();
        // Ne pas appeler super.onBackPressed()
    }
}