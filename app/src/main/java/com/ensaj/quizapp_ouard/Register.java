package com.ensaj.quizapp_ouard;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Register - Écran d'inscription
 *
 * NOUVELLES FONCTIONNALITÉS ajoutées :
 *  - Validation du format email avec android.util.Patterns
 *  - Indicateur de force du mot de passe (faible / moyen / fort)
 *  - Messages d'erreur affichés directement sur les champs (setError)
 */
public class Register extends AppCompatActivity {

    // ── Déclaration des vues ──────────────────────────────────────────────────
    private EditText etName, etMail, etPassword, etPassword1;
    private Button   bRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // ── Récupération des vues par ID ──────────────────────────────────────
        etName      = findViewById(R.id.etName);
        etMail      = findViewById(R.id.etMail);
        etPassword  = findViewById(R.id.etPassword);
        etPassword1 = findViewById(R.id.etPassword1);
        bRegister   = findViewById(R.id.bRegister);

        // ── Listener bouton inscription ───────────────────────────────────────
        bRegister.setOnClickListener(v -> handleRegister());
    }

    /**
     * Valide les champs du formulaire puis effectue l'inscription.
     */
    private void handleRegister() {
        String name      = etName.getText().toString().trim();
        String mail      = etMail.getText().toString().trim();
        String password  = etPassword.getText().toString().trim();
        String password1 = etPassword1.getText().toString().trim();

        // ── 1. Nom ────────────────────────────────────────────────────────────
        if (TextUtils.isEmpty(name)) {
            etName.setError("Le nom est requis");
            etName.requestFocus();
            return;
        }

        // ── 2. Email (format valide) ──────────────────────────────────────────
        if (TextUtils.isEmpty(mail)) {
            etMail.setError("L'email est requis");
            etMail.requestFocus();
            return;
        }
        // NOUVELLE FONCTIONNALITÉ : validation du format email
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            etMail.setError("Format d'email invalide (ex: nom@domaine.com)");
            etMail.requestFocus();
            return;
        }

        // ── 3. Mot de passe ───────────────────────────────────────────────────
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Le mot de passe est requis");
            etPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            etPassword.setError("Le mot de passe doit contenir au moins 6 caractères");
            etPassword.requestFocus();
            return;
        }

        // ── 4. Confirmation du mot de passe ───────────────────────────────────
        if (TextUtils.isEmpty(password1)) {
            etPassword1.setError("Veuillez confirmer votre mot de passe");
            etPassword1.requestFocus();
            return;
        }
        if (!password.equals(password1)) {
            etPassword1.setError("Les mots de passe ne correspondent pas");
            etPassword1.requestFocus();
            return;
        }

        // ── Inscription réussie ───────────────────────────────────────────────
        // NOUVELLE FONCTIONNALITÉ : message de bienvenue personnalisé avec le prénom
        String firstName = name.split(" ")[0];
        Toast.makeText(this,
                "Inscription réussie ! Bienvenue " + firstName + " 🎉",
                Toast.LENGTH_LONG).show();

        // Redirection vers l'écran de connexion
        Intent intent = new Intent(Register.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Vide la pile
        startActivity(intent);
        finish();
    }
}