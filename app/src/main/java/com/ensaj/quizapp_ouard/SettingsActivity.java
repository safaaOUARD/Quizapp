package com.ensaj.quizapp_ouard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchDarkMode;
    private RadioGroup rgLanguage;
    private Button bSaveSettings;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);

        switchDarkMode = findViewById(R.id.switchDarkMode);
        rgLanguage     = findViewById(R.id.rgLanguage);
        bSaveSettings  = findViewById(R.id.bSaveSettings);

        // Charger paramètres sauvegardés
        boolean isDark = prefs.getBoolean("darkMode", false);
        String lang    = prefs.getString("language", "fr");

        switchDarkMode.setChecked(isDark);

        if (lang.equals("ar"))      rgLanguage.check(R.id.rbArabic);
        else if (lang.equals("en")) rgLanguage.check(R.id.rbEnglish);
        else                        rgLanguage.check(R.id.rbFrench);

        bSaveSettings.setOnClickListener(v -> saveSettings());
    }

    private void saveSettings() {
        boolean isDark = switchDarkMode.isChecked();
        String lang;

        int checked = rgLanguage.getCheckedRadioButtonId();
        if (checked == R.id.rbArabic)       lang = "ar";
        else if (checked == R.id.rbEnglish) lang = "en";
        else                                lang = "fr";

        // Sauvegarder
        prefs.edit()
                .putBoolean("darkMode", isDark)
                .putString("language", lang)
                .apply();

        // Appliquer Dark Mode
        AppCompatDelegate.setDefaultNightMode(
                isDark ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO);

        // Appliquer langue
        applyLanguage(lang);

        Toast.makeText(this, "Paramètres sauvegardés ✅", Toast.LENGTH_SHORT).show();

        // Redémarrer MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void applyLanguage(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config,
                getResources().getDisplayMetrics());
    }
}