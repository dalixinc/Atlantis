package com.dalixinc.utils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {

    Clip clip;
    URL[] soundURLs = new URL[30];

    public Sound() {

        soundURLs[0] = getClass().getResource("/sound/AtlantisTune1.wav");
        soundURLs[1] = getClass().getResource("/sound/coin.wav");
        soundURLs[2] = getClass().getResource("/sound/powerup.wav");
        soundURLs[3] = getClass().getResource("/sound/unlock.wav");
        soundURLs[4] = getClass().getResource("/sound/fanfare.wav");
        soundURLs[5] = getClass().getResource("/sound/authentication.wav");
        soundURLs[6] = getClass().getResource("/sound/brown-explosion.wav");
        soundURLs[7] = getClass().getResource("/sound/dun-shot-1.wav");
        soundURLs[8] = getClass().getResource("/sound/wilhelm.wav");
        soundURLs[9] = getClass().getResource("/sound/klingon_disruptor_clean.wav");
        soundURLs[10] = getClass().getResource("/sound/pacman_death.wav");

    }

    public void setFile(int i) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundURLs[i]);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }
}
