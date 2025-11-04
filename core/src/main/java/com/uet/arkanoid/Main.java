package com.uet.arkanoid;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.ScreenUtils;
import com.uet.arkanoid.ui.*;

public class Main extends Game {

    private enum GameState {
        MENU,
        PLAYING,
        PLAYING_TWO_PLAYER,
        GAME_OVER,
        PAUSED;
    }

    private GameState gameState;
    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    private GameScreenTwoPlayer gameScreenTwoPlayer;
    private PauseScreen pauseScreen;
    private GameOverScreen gameOverScreen;
    private int currentMode = 1;

    @Override
    public void create() {
        menuScreen = new MenuScreen(this);
        gameScreen = new GameScreen(this);
        pauseScreen = new PauseScreen(this);
        gameScreenTwoPlayer = new GameScreenTwoPlayer(this);
        gameState = GameState.MENU;
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        switch (gameState) {
            case MENU:
                menuScreen.render();
                break;
            case PLAYING:
                // render single hoặc two-player tùy mode hiện tại
                if (currentMode == 1)
                    gameScreen.render();
                else
                    gameScreenTwoPlayer.render();
                break;
            case PAUSED:
                if (currentMode == 1)
                    gameScreen.render();
                else
                    gameScreenTwoPlayer.render();
                pauseScreen.render();
                break;
            case GAME_OVER:
                break;
        }
    }


    public void startGame() {
        currentMode = 1;
        gameScreen.startNewGame();
        gameState = GameState.PLAYING;
    }

    public void startTwoPlayerGame() {
        currentMode = 2;
        gameScreenTwoPlayer.startNewGame();
        gameState = GameState.PLAYING;
    }

    public void resumeGame() {
        gameState = GameState.PLAYING;
        if (currentMode == 1) gameScreen.setPaused(false);
        else gameScreenTwoPlayer.setPaused(false);
    }

    public void showGameOver(int highScore) {
        if (gameOverScreen != null)
            gameOverScreen.showScore(highScore);
        gameState = GameState.GAME_OVER;
    }

    public void pauseGame() {
        gameState = GameState.PAUSED;
    }

    public void returnToMenu() {
        gameState = GameState.MENU;
    }

    @Override
    public void dispose() {
        if (menuScreen != null) menuScreen.dispose();
        if (gameScreen != null) gameScreen.dispose();
        if (gameScreenTwoPlayer != null) gameScreenTwoPlayer.dispose();
        if (pauseScreen != null) pauseScreen.dispose();
    }
}
