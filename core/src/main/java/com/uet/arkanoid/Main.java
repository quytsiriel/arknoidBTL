package com.uet.arkanoid;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {

    private enum GameState {
        MENU,
        PLAYING,
        PAUSED,
        GAME_OVER
    }
    private GameState gameState = GameState.MENU;

    private SpriteBatch batch;
    private BitmapFont font;
    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    private PauseScreen pauseScreen;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2);

        menuScreen = new MenuScreen();
        pauseScreen = new PauseScreen();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        switch (gameState) {
            case MENU:
                handleMenuState();
                break;
            case PLAYING:
                handlePlayingState();
                break;
            case PAUSED:
                handlePausedState();
                break;
            case GAME_OVER:
                handleGameOverState();
                break;
        }
    }

    private void handleMenuState() {
        MenuAction action = menuScreen.handleInput();
        if (action != null) {
            switch (action.getType()) {
                case START_1_PLAYER:
                case START_2_PLAYER:
                    System.out.println("Starting " +
                        (action.getType() == MenuAction.Type.START_1_PLAYER ? "1" : "2") + " Player game!");
                    startGame(action.getDifficulty());
                    break;
                case CHANGE_DIFFICULTY:
                    System.out.println("Difficulty changed to: " + action.getDifficulty());
                    break;
            }
        }

        batch.begin();
        menuScreen.render(batch, font);
        batch.end();
    }

    private void handlePlayingState() {
        float delta = Gdx.graphics.getDeltaTime();

        gameScreen.update(delta);

        batch.begin();
        gameScreen.render(batch);
        batch.end();

        // paused
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            gameState = GameState.PAUSED;
            pauseScreen.show();
            return;
        }

        if (gameScreen.isGameOver()) {
            System.out.println("GAME OVER");
            gameState = GameState.MENU;
            gameScreen.dispose();
            gameScreen = null;
        }

        if (gameScreen.isLevelComplete()) {
            System.out.println("LEVEL COMPLETE!");
            // Có thể chuyển sang màn tiếp theo hoặc quay về menu
            gameState = GameState.MENU;
            gameScreen.dispose();
            gameScreen = null;
        }
    }

    private void handlePausedState() {
        // Vẫn render game ở background
        batch.begin();
        gameScreen.render(batch);
        batch.end();

        // Xử lý input và render pause menu
        PauseAction action = pauseScreen.handleInput();
        if (action != null) {
            switch (action.getType()) {
                case RESUME:
                    gameState = GameState.PLAYING;
                    pauseScreen.hide();
                    break;
                case VOLUME:
                    System.out.println("Volume settings - TODO");
                    // TODO: Thêm chức năng volume
                    break;
                case QUIT:
                    gameState = GameState.MENU;
                    pauseScreen.hide();
                    gameScreen.dispose();
                    gameScreen = null;
                    break;
            }
        }

        // Render pause menu lên trên cùng
        batch.begin();
        pauseScreen.render(batch, font);
        batch.end();
    }


    private void handleGameOverState() {
        // Hiển thị màn hình game over
        // Tạm thời quay về menu khi click
        if (Gdx.input.justTouched()) {
            gameState = GameState.MENU;
            if (gameScreen != null) {
                gameScreen.dispose();
                gameScreen = null;
            }
        }
    }

    private void startGame(Difficulty difficulty) {
        gameScreen = new GameScreen(difficulty);
        gameState = GameState.PLAYING;
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();

        if (menuScreen != null) {
            menuScreen.dispose();
        }
        if (gameScreen != null) {
            gameScreen.dispose();
        }
        if (pauseScreen != null) {
            pauseScreen.dispose();
        }
    }
}
