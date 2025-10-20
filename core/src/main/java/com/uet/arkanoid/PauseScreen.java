package com.uet.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class PauseScreen {
    private Rectangle backgroundBounds;
    private Rectangle resumeButtonBounds;
    private Rectangle volumeButtonBounds;
    private Rectangle quitButtonBounds;

    private float screenWidth;
    private float screenHeight;
    private boolean visible = false;

    public PauseScreen() {
        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();
        setupUI();
    }

    private void setupUI() {
        // Kích thước background bằng 1/4 màn hình
        float bgWidth = screenWidth / 3;
        float bgHeight = screenHeight / 3;
        float bgX = (screenWidth - bgWidth) / 2;
        float bgY = (screenHeight - bgHeight) / 2;
        backgroundBounds = new Rectangle(bgX, bgY, bgWidth, bgHeight);

        // Kích thước các nút
        float buttonWidth = bgWidth * 0.8f;
        float buttonHeight = bgHeight * 0.2f;
        float buttonX = bgX + (bgWidth - buttonWidth) / 2;

        // Vị trí các nút
        float buttonSpacing = buttonHeight * 0.3f;
        float startY = bgY + bgHeight - buttonHeight - buttonSpacing;

        resumeButtonBounds = new Rectangle(buttonX, startY, buttonWidth, buttonHeight);
        volumeButtonBounds = new Rectangle(buttonX, startY - buttonHeight - buttonSpacing, buttonWidth, buttonHeight);
        quitButtonBounds = new Rectangle(buttonX, startY - 2 * (buttonHeight + buttonSpacing), buttonWidth, buttonHeight);
    }

    public PauseAction handleInput() {
        if (!visible) return null;

        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            touchPos.y = screenHeight - touchPos.y;

            if (resumeButtonBounds.contains(touchPos.x, touchPos.y)) {
                return new PauseAction(PauseAction.Type.RESUME);
            }
            if (volumeButtonBounds.contains(touchPos.x, touchPos.y)) {
                return new PauseAction(PauseAction.Type.VOLUME);
            }
            if (quitButtonBounds.contains(touchPos.x, touchPos.y)) {
                return new PauseAction(PauseAction.Type.QUIT);
            }
        }

        // Kiểm tra phím ESC để resume
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            return new PauseAction(PauseAction.Type.RESUME);
        }

        return null;
    }

    public void render(SpriteBatch batch, BitmapFont font) {
        if (!visible) return;

        // Vẽ background semi-transparent
        batch.setColor(0, 0, 0, 0.7f);
        batch.draw(createWhitePixel(), backgroundBounds.x, backgroundBounds.y,
            backgroundBounds.width, backgroundBounds.height);
        batch.setColor(1, 1, 1, 1);

        // Vẽ border cho background
        batch.setColor(1, 1, 1, 1);
        drawBorder(batch, backgroundBounds, 2);

        // Vẽ các nút (tạm thời dùng hình chữ nhật + text)
        drawButton(batch, font, resumeButtonBounds, "RESUME", Color.GREEN);
        drawButton(batch, font, volumeButtonBounds, "VOLUME", Color.YELLOW);
        drawButton(batch, font, quitButtonBounds, "QUIT", Color.RED);
    }

    private void drawButton(SpriteBatch batch, BitmapFont font, Rectangle bounds, String text, Color color) {
        // Vẽ nền nút
        batch.setColor(color.r * 0.3f, color.g * 0.3f, color.b * 0.3f, 1);
        batch.draw(createWhitePixel(), bounds.x, bounds.y, bounds.width, bounds.height);

        // Vẽ border nút
        batch.setColor(color);
        drawBorder(batch, bounds, 1);

        // Vẽ text
        font.setColor(color);
        font.getData().setScale(1.5f);
        float textWidth = font.getXHeight() * text.length() * 2;
        float textHeight = font.getLineHeight();
        float textX = bounds.x + (bounds.width - textWidth) / 2;
        float textY = bounds.y + (bounds.height + textHeight) / 2;
        font.draw(batch, text, textX, textY);
    }

    private void drawBorder(SpriteBatch batch, Rectangle bounds, int thickness) {
        // Top
        batch.draw(createWhitePixel(), bounds.x, bounds.y + bounds.height - thickness, bounds.width, thickness);
        // Bottom
        batch.draw(createWhitePixel(), bounds.x, bounds.y, bounds.width, thickness);
        // Left
        batch.draw(createWhitePixel(), bounds.x, bounds.y, thickness, bounds.height);
        // Right
        batch.draw(createWhitePixel(), bounds.x + bounds.width - thickness, bounds.y, thickness, bounds.height);
    }

    private com.badlogic.gdx.graphics.Texture createWhitePixel() {
        // Tạo một texture 1x1 pixel màu trắng
        com.badlogic.gdx.graphics.Pixmap pixmap = new com.badlogic.gdx.graphics.Pixmap(1, 1, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 1, 1, 1);
        pixmap.fill();
        com.badlogic.gdx.graphics.Texture texture = new com.badlogic.gdx.graphics.Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    public void show() {
        visible = true;
    }

    public void hide() {
        visible = false;
    }

    public boolean isVisible() {
        return visible;
    }

    public void dispose() {
        // Texture sẽ được dispose trong các phương thức vẽ
    }
}
