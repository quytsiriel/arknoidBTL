package com.uet.arkanoid;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.ScreenUtils;
import com.uet.arkanoid.ui.*;

public class Main extends ApplicationAdapter {

    private enum GameState {
        MENU,
        PLAYING,
        GAME_OVER,
        PAUSED;
    }

    private GameState gameState;
    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    private PauseScreen pauseScreen;
    private GameOverScreen gameOverScreen;

    @Override
    public void create() {
        menuScreen = new MenuScreen(this);
        gameScreen = new GameScreen(this);
        pauseScreen = new PauseScreen(this);
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
                gameScreen.render();
                break;
            case PAUSED:
                gameScreen.render();
                pauseScreen.render();
                break;
            case GAME_OVER:
                // TODO: thêm GameOverScreen nếu muốn
                break;
        }
    }

    public void startGame() {
        gameScreen.startNewGame();
        gameState = GameState.PLAYING;
    }

    public void resumeGame() {
        gameState = GameState.PLAYING;
        gameScreen.setPaused(false);
    }

    public void showGameOver(int highScore) {
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
        menuScreen.dispose();
        gameScreen.dispose();
        pauseScreen.dispose();
    }
}
