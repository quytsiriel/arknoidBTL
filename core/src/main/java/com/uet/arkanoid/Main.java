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
        LEVEL_SELECT,
        PLAYING,
        PLAYING_TWO_PLAYER,
        GAME_OVER,
        PAUSED;
    }

    private GameState gameState;
    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    public GameScreenTwoPlayer gameScreenTwoPlayer;
    private PauseScreen pauseScreen;
    private GameOverScreen gameOverScreen;
    private int currentMode = 1;
    private LevelSelectScreen levelSelectScreen;


    @Override
    public void create() {
        menuScreen = new MenuScreen(this);
        levelSelectScreen = new LevelSelectScreen(this);
        gameScreen = new GameScreen(this);
        pauseScreen = new PauseScreen(this);
        gameScreenTwoPlayer = new GameScreenTwoPlayer(this);
        gameState = GameState.MENU;
        gameOverScreen = new GameOverScreen(this);
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
                if (currentMode == 1)
                    gameScreen.render();
                else
                    gameScreenTwoPlayer.render();
                break;
            case LEVEL_SELECT:
                levelSelectScreen.render();
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


    public void startGame(String level) {
        currentMode = 1;
        gameScreen.startNewGame(level);
        gameState = GameState.PLAYING;
    }

    public void startTwoPlayerGame(String level) {
        currentMode = 2;
        gameScreenTwoPlayer.startNewGame(level);
        gameState = GameState.PLAYING;
    }
    public void showLevelSelect(int mode) {
        this.currentMode = mode; // <-- LƯU CHẾ ĐỘ CHƠI NGAY LẬP TỨC
        gameState = GameState.LEVEL_SELECT;
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

    public int getCurrentMode (){
        return currentMode;
    }

    @Override
    public void dispose() {
        if (menuScreen != null) menuScreen.dispose();
        if (gameScreen != null) gameScreen.dispose();
        if (gameScreenTwoPlayer != null) gameScreenTwoPlayer.dispose();
        if (pauseScreen != null) pauseScreen.dispose();
    }
}
