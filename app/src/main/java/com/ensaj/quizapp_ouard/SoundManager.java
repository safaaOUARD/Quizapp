package com.ensaj.quizapp_ouard;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundManager {

    private Context context;

    public SoundManager(Context context) {
        this.context = context;
    }

    public void playCorrect() {
        try {
            MediaPlayer mp = MediaPlayer.create(context, R.raw.sound_correct);
            if (mp != null) {
                mp.start();
                // Libérer automatiquement après lecture
                mp.setOnCompletionListener(MediaPlayer::release);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playWrong() {
        try {
            MediaPlayer mp = MediaPlayer.create(context, R.raw.sound_wrong);
            if (mp != null) {
                mp.start();
                mp.setOnCompletionListener(MediaPlayer::release);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void release() {
        // Plus rien à libérer avec MediaPlayer
    }
}