package com.ensaj.quizapp_ouard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Score - Écran de résultats final
 *
 * NOUVELLES FONCTIONNALITÉS :
 *  - Message personnalisé selon la performance :
 *      100%         → "🏆 Excellent ! Score parfait !"
 *      60% – 80%    → "👍 Bien joué ! Continuez comme ça !"
 *      < 60%        → "📚 À améliorer. Révisez et réessayez !"
 *  - Couleur du score : vert (≥ 60%) ou rouge (< 60%)
 *  - Barre de progression animée avec la couleur adaptée
 *  - Bouton "Try Again" repart de Quiz1 (score remis à 0)
 *  - Bouton "Logout" revient à l'écran de connexion
 */
public class Score extends AppCompatActivity {

    // ── Vues ──────────────────────────────────────────────────────────────────
    private Button      bLogout, bTry;
    private ProgressBar progressBar;
    private TextView    tvScore, tvMessage;
    private Button bBonus; // ← ajouter
    // ── Données ───────────────────────────────────────────────────────────────
    private int score;
    private int percentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        // ── Récupération des vues ─────────────────────────────────────────────
        tvScore     = findViewById(R.id.tvScore);
        tvMessage   = findViewById(R.id.tvMessage);  // TextView du message personnalisé
        progressBar = findViewById(R.id.progressBar);
        bLogout     = findViewById(R.id.bLogout);
        bTry        = findViewById(R.id.bTry);
        bBonus = findViewById(R.id.bBonus);
        bBonus.setOnClickListener(v -> {
            Intent intent = new Intent(Score.this, BonusQuizActivity.class);
            startActivity(intent);
            finish();
        });
        // ── Récupération du score depuis l'Intent ─────────────────────────────
        score      = getIntent().getIntExtra("score", 0);
        percentage = (100 * score) / QuizBaseActivity.TOTAL_QUESTIONS;
        // Sauvegarder le score dans l'historique
        String mode = getIntent().getStringExtra("mode") != null
                ? getIntent().getStringExtra("mode") : "Quiz Principal";
        HistoriqueActivity.saveScore(this, score, percentage, mode);

// Initialiser les sons
        SoundManager soundManager = new SoundManager(this);
        // ── Affichage du score et de la barre ─────────────────────────────────
        displayScore();

        // ── Listeners ────────────────────────────────────────────────────────
        bLogout.setOnClickListener(v -> handleLogout());
        bTry.setOnClickListener(v    -> handleTryAgain());
        Button bHistorique = findViewById(R.id.bHistorique);
        bHistorique.setOnClickListener(v ->
                startActivity(new Intent(Score.this, HistoriqueActivity.class)));
    }

    /**
     * Affiche le score avec couleur adaptée et message personnalisé.
     */
    private void displayScore() {
        // Pourcentage + affichage
        tvScore.setText(percentage + " %");
        progressBar.setMax(100);
        progressBar.setProgress(percentage);

        // NOUVELLE FONCTIONNALITÉ : couleur + message selon la performance
        if (percentage == 100) {
            tvScore.setTextColor(Color.parseColor("#2E7D32"));   // Vert foncé
            if (tvMessage != null)
                tvMessage.setText("🏆 Excellent ! Score parfait !");
        } else if (percentage >= 60) {
            tvScore.setTextColor(Color.parseColor("#1565C0"));   // Bleu
            if (tvMessage != null)
                tvMessage.setText("👍 Bien joué ! Continuez comme ça !");
        } else {
            tvScore.setTextColor(Color.parseColor("#C62828"));   // Rouge
            if (tvMessage != null)
                tvMessage.setText("📚 À améliorer. Révisez et réessayez !");
        }
    }

    /**
     * Déconnexion : retour à la page de login, vide la pile d'activités.
     */
    private void handleLogout() {
        Toast.makeText(this, "Merci de votre participation !", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Score.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Recommencer le quiz depuis la première question (score remis à 0).
     */
    private void handleTryAgain() {
        Intent intent = new Intent(Score.this, Quiz1.class);
        intent.putExtra("score", 0); // Score réinitialisé
        startActivity(intent);
        finish();
    }
}

