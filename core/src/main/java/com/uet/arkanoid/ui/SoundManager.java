package com.uet.arkanoid.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class SoundManager {
    public Music BackgroundMusic;
    public Music GameMusic;
    public SoundManager() {
        BackgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background.mp3"));
        GameMusic = Gdx.audio.newMusic(Gdx.files.internal("game.mp3"));
        BackgroundMusic.setLooping(true);
        BackgroundMusic.setVolume(0.2f);

        GameMusic.setLooping(true);
        GameMusic.setVolume(0.1f);
        BackgroundMusic.setLooping(true);
    }

    public void stopBackgroundMusic() {
        BackgroundMusic.stop();
    }
    public void stopGameMusic() {
        GameMusic.stop();
    }
    public void playBackgroundMusic() {
        BackgroundMusic.play();
    }
    public void playGameMusic() {
        GameMusic.play();
    }

}
