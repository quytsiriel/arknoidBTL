package com.uet.arkanoid.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.uet.arkanoid.Main;


public class GameOverScreen {
    private final Main game;
    private final SpriteBatch batch;

    private Texture frameTexture;
    private Texture backToMenuTexture;
    private Texture gameOverTexture;

    private Rectangle backButtonBounds;
    private float frameX, frameY, frameWidth, frameHeight;

    private BitmapFont titleFont;
    private BitmapFont scoreFont;
    private BitmapFont buttonFont;

    private int highScore;

    public GameOverScreen(Main game) {
        this.game = game;
        this.batch = new SpriteBatch();
        loadAssets();
        initFonts();
    }

    private void loadAssets() {
        frameTexture = new Texture(Gdx.files.internal("PauseMenu.png"));
        gameOverTexture = new Texture(Gdx.files.internal("gameover.png"));
        backToMenuTexture = new Texture(Gdx.files.internal("back_to_menu_button.png"));

        frameWidth = 500;
        frameHeight = 600;
        frameX = (1100 - frameWidth) / 2f;
        frameY = (810 - frameHeight) / 2f;

        float buttonWidth = 300;
        float buttonHeight = 100;
        float buttonX = frameX + (frameWidth - buttonWidth) / 2f + 10;
        float buttonY = frameY + 100;
        backButtonBounds = new Rectangle(buttonX, buttonY, buttonWidth, buttonHeight);
    }

    private void initFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Pixeboy-z8XGD.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();

        // Điểm số
        param.size = 36;
        param.color = Color.WHITE;
        scoreFont = generator.generateFont(param);

        // Nút "Back to Menu"
        param.size = 28;
        param.color = Color.YELLOW;
        buttonFont = generator.generateFont(param);

        generator.dispose();
    }

    public void showScore(int highScore) {
        this.highScore = highScore;
    }

    public void render() {
        handleInput();

        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        boolean hoverBack = backButtonBounds.contains(mouseX, mouseY);

        batch.begin();

        // Khung trung tâm
        batch.draw(frameTexture, frameX, frameY, frameWidth, frameHeight);

        scoreFont.draw(batch, "HIGH SCORE: " + highScore, frameX + 135, frameY + 300);

        // Game Over
        float gameOverWidth = 350;
        float gameOverHeight = 250;
        float gameOverX = 380;
        float gameOverY = 370;

        batch.draw(gameOverTexture, gameOverX, gameOverY, gameOverWidth, gameOverHeight);

        // Nút
        float scale = hoverBack ? 1.05f : 1f;
        float drawW = backButtonBounds.width * scale;
        float drawH = backButtonBounds.height * scale;
        float drawX = backButtonBounds.x - (drawW - backButtonBounds.width) / 2;
        float drawY = backButtonBounds.y - (drawH - backButtonBounds.height) / 2;
        batch.draw(backToMenuTexture, drawX, drawY, drawW, drawH);


        batch.end();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            touchPos.y = Gdx.graphics.getHeight() - touchPos.y;

            if (backButtonBounds.contains(touchPos.x, touchPos.y)) {
                game.returnToMenu();
            }
        }
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public void dispose() {
        batch.dispose();
        frameTexture.dispose();
        backToMenuTexture.dispose();
        scoreFont.dispose();
        buttonFont.dispose();
        gameOverTexture.dispose();
    }
}
