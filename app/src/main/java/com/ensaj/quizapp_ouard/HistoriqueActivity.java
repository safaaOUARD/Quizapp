package com.ensaj.quizapp_ouard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import org.json.JSONArray;
import org.json.JSONObject;

public class HistoriqueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);

        LinearLayout container = findViewById(R.id.containerHistorique);
        Button bQuitter        = findViewById(R.id.bQuitterHistorique);
        Switch switchDark      = findViewById(R.id.switchDarkModeHistorique);

        // ── Dark Mode switch ──────────────────────────────────────
        SharedPreferences settings = getSharedPreferences("AppSettings", MODE_PRIVATE);
        boolean isDark = settings.getBoolean("darkMode", false);
        switchDark.setChecked(isDark);

        switchDark.setOnCheckedChangeListener((btn, isChecked) -> {
            settings.edit().putBoolean("darkMode", isChecked).apply();
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES
                            : AppCompatDelegate.MODE_NIGHT_NO);
        });

        // ── Bouton Quitter ────────────────────────────────────────
        bQuitter.setOnClickListener(v -> finish());

        // ── Affichage historique ──────────────────────────────────
        SharedPreferences prefs = getSharedPreferences("ScoreHistory", MODE_PRIVATE);
        String json = prefs.getString("scores", "[]");

        try {
            JSONArray array = new JSONArray(json);

            if (array.length() == 0) {
                TextView tv = new TextView(this);
                tv.setText("Aucun score enregistré pour l'instant.");
                tv.setTextSize(16);
                tv.setPadding(0, 20, 0, 0);
                container.addView(tv);
                return;
            }

            // Afficher du plus récent au plus ancien
            for (int i = array.length() - 1; i >= 0; i--) {
                JSONObject obj = array.getJSONObject(i);
                int score      = obj.getInt("score");
                int percent    = obj.getInt("percent");
                String date    = obj.getString("date");
                String mode    = obj.getString("mode");

                // Carte pour chaque score
                LinearLayout card = new LinearLayout(this);
                card.setOrientation(LinearLayout.VERTICAL);
                card.setPadding(20, 16, 20, 16);
                card.setBackgroundColor(percent >= 60
                        ? 0xFFE8F5E9  // Vert clair
                        : 0xFFFFEBEE); // Rouge clair
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 0, 12);
                card.setLayoutParams(params);

                // Mode + score
                TextView tvMode = new TextView(this);
                tvMode.setText(mode + "  —  " + score + "/5");
                tvMode.setTextSize(16);
                tvMode.setTypeface(null, android.graphics.Typeface.BOLD);
                tvMode.setTextColor(percent >= 60 ? 0xFF2E7D32 : 0xFFC62828);
                card.addView(tvMode);

                // Pourcentage
                TextView tvPercent = new TextView(this);
                tvPercent.setText("Score : " + percent + "%");
                tvPercent.setTextSize(22);
                tvPercent.setTextColor(percent >= 60 ? 0xFF388E3C : 0xFFD32F2F);
                card.addView(tvPercent);

                // Date
                TextView tvDate = new TextView(this);
                tvDate.setText("📅 " + date);
                tvDate.setTextSize(13);
                tvDate.setTextColor(0xFF757575);
                card.addView(tvDate);

                container.addView(card);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ── Méthode statique pour sauvegarder depuis Score.java ───────
    public static void saveScore(android.content.Context ctx,
                                 int score, int percent, String mode) {
        SharedPreferences prefs = ctx.getSharedPreferences(
                "ScoreHistory", android.content.Context.MODE_PRIVATE);
        String json = prefs.getString("scores", "[]");

        try {
            JSONArray array = new JSONArray(json);
            JSONObject obj  = new JSONObject();
            obj.put("score",   score);
            obj.put("percent", percent);
            obj.put("mode",    mode);
            obj.put("date",    new java.text.SimpleDateFormat(
                    "dd/MM/yyyy HH:mm",
                    java.util.Locale.getDefault())
                    .format(new java.util.Date()));
            array.put(obj);

            // Garder seulement les 10 derniers
            if (array.length() > 10) {
                JSONArray trimmed = new JSONArray();
                for (int i = array.length() - 10; i < array.length(); i++)
                    trimmed.put(array.get(i));
                array = trimmed;
            }

            prefs.edit().putString("scores", array.toString()).apply();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}