package com.uet.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class MenuScreen {
    private Texture menuBackground;
    private Texture button1PlayerTexture;
    private Texture button2PlayerTexture;
    private Texture buttonDifficultyTexture;
    private Rectangle button1PlayerBounds;
    private Rectangle button2PlayerBounds;
    private Rectangle buttonDifficultyBounds;
    private Difficulty currentDifficulty;

    private float screenWidth;
    private float screenHeight;

    public MenuScreen() {
        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();
        this.currentDifficulty = Difficulty.NORMAL;

        loadMenuAssets();
        setupButtons();
    }

    private void loadMenuAssets() {
        menuBackground = new Texture(Gdx.files.internal("menu_background2.png"));
        button1PlayerTexture = new Texture(Gdx.files.internal("singlePlayer_button.png"));
        button2PlayerTexture = new Texture(Gdx.files.internal("multiPlayer_button.png"));
        buttonDifficultyTexture = new Texture(Gdx.files.internal("Difficult_button.png"));
    }

    private void setupButtons() {
        float buttonWidth = 400;
        float buttonHeight = 90;
        float buttonX = (screenWidth - buttonWidth) / 2;

        button1PlayerBounds = new Rectangle(buttonX, screenHeight / 2 + 80, buttonWidth, buttonHeight);
        button2PlayerBounds = new Rectangle(buttonX, screenHeight / 2, buttonWidth, buttonHeight);
        buttonDifficultyBounds = new Rectangle(buttonX, screenHeight / 2 - 80, buttonWidth, buttonHeight);
    }

    public MenuAction handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            touchPos.y = screenHeight - touchPos.y;

            if (button1PlayerBounds.contains(touchPos.x, touchPos.y)) {
                return new MenuAction(MenuAction.Type.START_1_PLAYER, currentDifficulty);
            }
            if (button2PlayerBounds.contains(touchPos.x, touchPos.y)) {
                return new MenuAction(MenuAction.Type.START_2_PLAYER, currentDifficulty);
            }
            if (buttonDifficultyBounds.contains(touchPos.x, touchPos.y)) {
                switch (currentDifficulty) {
                    case EASY:   currentDifficulty = Difficulty.NORMAL; break;
                    case NORMAL: currentDifficulty = Difficulty.HARD;   break;
                    case HARD:   currentDifficulty = Difficulty.EASY;   break;
                }
                return new MenuAction(MenuAction.Type.CHANGE_DIFFICULTY, currentDifficulty);
            }
        }
        return null;
    }

    public void render(SpriteBatch batch, BitmapFont font) {
        batch.draw(menuBackground, 0, 0, screenWidth, screenHeight);
        batch.draw(button1PlayerTexture, button1PlayerBounds.x, button1PlayerBounds.y,
            button1PlayerBounds.width, button1PlayerBounds.height);
        batch.draw(button2PlayerTexture, button2PlayerBounds.x, button2PlayerBounds.y,
            button2PlayerBounds.width, button2PlayerBounds.height);
        batch.draw(buttonDifficultyTexture, buttonDifficultyBounds.x, buttonDifficultyBounds.y,
            buttonDifficultyBounds.width, buttonDifficultyBounds.height);

        String difficultyText = currentDifficulty.getDisplayName();
        font.draw(batch, difficultyText, buttonDifficultyBounds.x + buttonDifficultyBounds.width + 20,
            buttonDifficultyBounds.y + 45);
    }

    public void dispose() {
        if (menuBackground != null) menuBackground.dispose();
        if (button1PlayerTexture != null) button1PlayerTexture.dispose();
        if (button2PlayerTexture != null) button2PlayerTexture.dispose();
        if (buttonDifficultyTexture != null) buttonDifficultyTexture.dispose();
    }

    // Getter cho difficulty hiện tại
    public Difficulty getCurrentDifficulty() {
        return currentDifficulty;
    }

    // Setter cho difficulty
    public void setCurrentDifficulty(Difficulty difficulty) {
        this.currentDifficulty = difficulty;
    }
}
