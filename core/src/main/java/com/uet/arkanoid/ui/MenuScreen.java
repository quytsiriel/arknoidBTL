package com.uet.arkanoid.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.uet.arkanoid.Main;

public class MenuScreen {
    private final Main game;
    private final SpriteBatch batch;
    private final BitmapFont font;

    private Texture menuBackground;
    private Texture button1PlayerTexture;
    private Texture button2PlayerTexture;
    private Texture buttonDifficultyTexture;

    private Rectangle button1PlayerBounds;
    private Rectangle button2PlayerBounds;
    private Rectangle buttonDifficultyBounds;

    private Difficulty currentDifficulty = Difficulty.NORMAL;

    public MenuScreen(Main game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.font.setColor(Color.WHITE);
        this.font.getData().setScale(2);
        loadMenuAssets();
    }

    public void render() {
        handleInput();

        batch.begin();
        batch.draw(menuBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(button1PlayerTexture, button1PlayerBounds.x, button1PlayerBounds.y, button1PlayerBounds.width, button1PlayerBounds.height);
        batch.draw(button2PlayerTexture, button2PlayerBounds.x, button2PlayerBounds.y, button2PlayerBounds.width, button2PlayerBounds.height);
        batch.draw(buttonDifficultyTexture, buttonDifficultyBounds.x, buttonDifficultyBounds.y, buttonDifficultyBounds.width, buttonDifficultyBounds.height);

        font.draw(batch, currentDifficulty.getDisplayName(),
            buttonDifficultyBounds.x + buttonDifficultyBounds.width + 20,
            buttonDifficultyBounds.y + 45);

        batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            touchPos.y = Gdx.graphics.getHeight() - touchPos.y;

            if (button1PlayerBounds.contains(touchPos.x, touchPos.y)) {
                game.startGame(currentDifficulty);
            }
            if (button2PlayerBounds.contains(touchPos.x, touchPos.y)) {
                // Tạm thời dùng chung
                game.startGame(currentDifficulty);
            }
            if (buttonDifficultyBounds.contains(touchPos.x, touchPos.y)) {
                switch (currentDifficulty) {
                    case EASY:   currentDifficulty = Difficulty.NORMAL; break;
                    case NORMAL: currentDifficulty = Difficulty.HARD;   break;
                    case HARD:   currentDifficulty = Difficulty.EASY;   break;
                }
            }
        }
    }

    private void loadMenuAssets() {
        menuBackground = new Texture(Gdx.files.internal("menu_background2.png"));
        button1PlayerTexture = new Texture(Gdx.files.internal("singlePlayer_button.png"));
        button2PlayerTexture = new Texture(Gdx.files.internal("multiPlayer_button.png"));
        buttonDifficultyTexture = new Texture(Gdx.files.internal("Difficult_button.png"));

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        float bw = 400, bh = 90;
        float bx = (w - bw) / 2;

        button1PlayerBounds = new Rectangle(bx, h / 2 + 80, bw, bh);
        button2PlayerBounds = new Rectangle(bx, h / 2, bw, bh);
        buttonDifficultyBounds = new Rectangle(bx, h / 2 - 80, bw, bh);
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
        menuBackground.dispose();
        button1PlayerTexture.dispose();
        button2PlayerTexture.dispose();
        buttonDifficultyTexture.dispose();
    }
}
