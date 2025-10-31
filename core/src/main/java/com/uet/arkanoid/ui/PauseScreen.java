package com.uet.arkanoid.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.uet.arkanoid.Main;

public class PauseScreen {
    private final Main game;
    private final SpriteBatch batch;

    private Texture frameTexture;
    private Texture resumeButtonTexture;
    private Texture quitButtonTexture;

    private Rectangle resumeButtonBounds;
    private Rectangle quitButtonBounds;

    private float frameX, frameY, frameWidth, frameHeight;

    public PauseScreen(Main game) {
        this.game = game;
        this.batch = new SpriteBatch();
        loadAssets();
    }

    private void loadAssets() {

        frameTexture = new Texture(Gdx.files.internal("PauseMenu.png"));
        resumeButtonTexture = new Texture(Gdx.files.internal("resume_button.png"));
        quitButtonTexture = new Texture(Gdx.files.internal("quit_button.png"));

        frameWidth = 500;
        frameHeight = 600;

        frameX = (1100 - frameWidth) / 2f;
        frameY = (810 - frameHeight) / 2f;

        float buttonWidth = 300;
        float buttonHeight = 100;
        float buttonX = frameX + (frameWidth - buttonWidth) / 2f + 10;
        float resumeButtonY = frameY + frameHeight / 2f + 20;
        float quitButtonY = frameY + frameHeight / 2f - buttonHeight - 30;

        resumeButtonBounds = new Rectangle(buttonX, resumeButtonY, buttonWidth, buttonHeight);
        quitButtonBounds = new Rectangle(buttonX, quitButtonY, buttonWidth, buttonHeight);
    }

    public void render() {
        handleInput();

        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        boolean isHoverResume = resumeButtonBounds.contains(mouseX, mouseY);
        boolean isHoverQuit = quitButtonBounds.contains(mouseX, mouseY);

        batch.begin();

        batch.draw(frameTexture, frameX, frameY, frameWidth, frameHeight);

        // hieu ung hover
        float scaleResume = isHoverResume ? 1.05f : 1.0f;
        drawButton(resumeButtonTexture, resumeButtonBounds, scaleResume);


        float scaleQuit = isHoverQuit ? 1.05f : 1.0f;
        drawButton(quitButtonTexture, quitButtonBounds, scaleQuit);

        batch.end();
    }

    private void drawButton(Texture texture, Rectangle bounds, float scale) {
        float drawW = bounds.width * scale;
        float drawH = bounds.height * scale;

        float drawX = bounds.x - (drawW - bounds.width) / 2;
        float drawY = bounds.y - (drawH - bounds.height) / 2;
        batch.draw(texture, drawX, drawY, drawW, drawH);
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.resumeGame();
            return;
        }

        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            touchPos.y = Gdx.graphics.getHeight() - touchPos.y;

            if (resumeButtonBounds.contains(touchPos.x, touchPos.y)) {
                game.resumeGame();
            }

            if (quitButtonBounds.contains(touchPos.x, touchPos.y)) {
                game.returnToMenu();
            }
        }
    }

    public void dispose() {
        batch.dispose();
        frameTexture.dispose();
        resumeButtonTexture.dispose();
        quitButtonTexture.dispose();
    }
}
