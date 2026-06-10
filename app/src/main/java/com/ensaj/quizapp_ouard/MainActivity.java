package com.ensaj.quizapp_ouard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    // ── Vues ──────────────────────────────────────────────────────────────────
    private EditText   etLogin, etPassword;
    private Button     bLogin;
    private TextView   tvRegister;
    private ImageView  ivAvatar;
    private Switch     switchDarkMode;

    // ── Text-To-Speech ────────────────────────────────────────────────────────
    private TextToSpeech tts;
    private boolean      ttsReady = false;

    // ── Préférences ───────────────────────────────────────────────────────────
    private SharedPreferences prefs;

    private static final String MESSAGE_BIENVENUE =
            "Bienvenue dans Quiz Auto ! Connectez-vous pour tester vos connaissances.";

    // ─────────────────────────────────────────────────────────────────────────
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ── Appliquer Dark Mode AVANT setContentView ──────────────────────────
        prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("darkMode", false);
        AppCompatDelegate.setDefaultNightMode(
                isDark ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_main);

        // ── Récupération des vues ─────────────────────────────────────────────
        etLogin        = findViewById(R.id.etMail);
        etPassword     = findViewById(R.id.etPassword);
        bLogin         = findViewById(R.id.bLogin);
        tvRegister     = findViewById(R.id.tvRegister);
        ivAvatar       = findViewById(R.id.ivAvatar);
        switchDarkMode = findViewById(R.id.switchDarkMode);

        // ── Dark Mode switch ──────────────────────────────────────────────────
        switchDarkMode.setChecked(isDark);
        switchDarkMode.setOnCheckedChangeListener((btn, isChecked) -> {
            // Sauvegarder le choix
            prefs.edit().putBoolean("darkMode", isChecked).apply();
            // Appliquer le mode
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES
                            : AppCompatDelegate.MODE_NIGHT_NO);
            // Recréer l'activité pour appliquer visuellement
            recreate();
        });

        // ── Initialisation TTS ────────────────────────────────────────────────
        tts = new TextToSpeech(this, this);

        // ── Listeners ─────────────────────────────────────────────────────────
        bLogin.setOnClickListener(v -> handleLogin());

        tvRegister.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, Register.class)));

        ivAvatar.setOnClickListener(v -> speakWelcome());
    }

    // ── TextToSpeech ──────────────────────────────────────────────────────────
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.FRENCH);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts.setLanguage(Locale.ENGLISH);
            }
            tts.setSpeechRate(0.9f);
            tts.setPitch(1.0f);
            ttsReady = true;
            speakWelcome();
        } else {
            Toast.makeText(this,
                    "Message vocal non disponible sur cet appareil.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void speakWelcome() {
        if (ttsReady && tts != null) {
            tts.speak(MESSAGE_BIENVENUE, TextToSpeech.QUEUE_FLUSH, null, "welcome");
        }
    }

    // ── Connexion ─────────────────────────────────────────────────────────────
    private void handleLogin() {
        String email    = etLogin.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etLogin.setError("Veuillez saisir votre email");
            etLogin.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            etPassword.setError("Veuillez saisir votre mot de passe");
            etPassword.requestFocus();
            return;
        }

        // Vérifier compte sauvegardé OU compte par défaut toto/123
        SharedPreferences userPrefs = getSharedPreferences("UserData", MODE_PRIVATE);
        String savedEmail    = userPrefs.getString("email", "toto");
        String savedPassword = userPrefs.getString("password", "123");

        if (email.equals(savedEmail) && password.equals(savedPassword)) {
            stopTts();
            Toast.makeText(this, "Connexion réussie !", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, Quiz1.class);
            intent.putExtra("username", email);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this,
                    "Email ou mot de passe incorrect !", Toast.LENGTH_SHORT).show();
            etPassword.setText("");
            etPassword.requestFocus();
        }
    }

    // ── Cycle de vie ──────────────────────────────────────────────────────────
    private void stopTts() {
        if (tts != null) tts.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTts();
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
        super.onDestroy();
    }
}