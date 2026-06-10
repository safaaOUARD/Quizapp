package com.ensaj.quizapp_ouard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionBank {

    public static List<Question> getShuffledQuestions(int count) {
        List<Question> all = new ArrayList<>();

        // ── 20 questions de code de la route ──────────────────────
        all.add(new Question(
                "À cette intersection, je laisse la priorité à droite :",
                "q1", "Oui", "Non", "Non"));
        all.add(new Question(
                "Le panneau de danger indique une succession de virages dont le 1er est :",
                "q2", "À droite", "À gauche", "À droite"));
        all.add(new Question(
                "Avant de partir, je laisse tourner mon moteur pour qu'il monte en température :",
                "q3", "Oui", "Non", "Non"));
        all.add(new Question(
                "En tant qu'automobiliste, vous devez être plus vigilant lorsque :",
                "q4", "Le tramway est arrêté", "Le tramway circule", "Le tramway est arrêté"));
        all.add(new Question(
                "En conduisant, je peux utiliser mon portable pour écrire un texto :",
                "q5", "Oui", "Non", "Non"));
        all.add(new Question(
                "Le port de la ceinture de sécurité est obligatoire :",
                "q1", "Seulement sur autoroute", "Sur toutes les routes", "Sur toutes les routes"));
        all.add(new Question(
                "En cas de pluie, je dois augmenter ma distance de sécurité :",
                "q2", "Oui", "Non", "Oui"));
        all.add(new Question(
                "Le feu orange fixe signifie :",
                "q3", "Accélérer", "Freiner et s'arrêter", "Freiner et s'arrêter"));
        all.add(new Question(
                "Sur autoroute, la vitesse maximale par temps sec est :",
                "q4", "110 km/h", "130 km/h", "130 km/h"));
        all.add(new Question(
                "Je peux dépasser un véhicule par la droite sur autoroute :",
                "q5", "Oui", "Non", "Non"));
        all.add(new Question(
                "Le taux d'alcoolémie légal pour un conducteur novice est :",
                "q1", "0.2 g/L", "0.5 g/L", "0.2 g/L"));
        all.add(new Question(
                "Les feux de croisement doivent être allumés de nuit :",
                "q2", "Oui", "Non", "Oui"));
        all.add(new Question(
                "En ville, la vitesse maximale autorisée est :",
                "q3", "50 km/h", "70 km/h", "50 km/h"));
        all.add(new Question(
                "Le panneau triangulaire signale :",
                "q4", "Une obligation", "Un danger", "Un danger"));
        all.add(new Question(
                "Je dois céder le passage aux piétons sur un passage clouté :",
                "q5", "Oui", "Non", "Oui"));
        all.add(new Question(
                "En cas de crevaison sur autoroute, je dois m'arrêter :",
                "q1", "Sur la voie rapide", "Sur la bande d'arrêt d'urgence", "Sur la bande d'arrêt d'urgence"));
        all.add(new Question(
                "Le gilet de sécurité jaune doit être accessible depuis le siège :",
                "q2", "Oui", "Non", "Oui"));
        all.add(new Question(
                "La distance de freinage augmente avec la vitesse :",
                "q3", "Oui", "Non", "Oui"));
        all.add(new Question(
                "Un conducteur sous médicaments peut conduire sans risque :",
                "q4", "Oui", "Non", "Non"));
        all.add(new Question(
                "Le triangle de pré-signalisation doit être placé à :",
                "q5", "10 mètres", "30 mètres", "30 mètres"));

        // Mélange aléatoire
        Collections.shuffle(all);

        // Retourner seulement 'count' questions
        return all.subList(0, Math.min(count, all.size()));
    }
}