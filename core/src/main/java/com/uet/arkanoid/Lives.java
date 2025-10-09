package com.uet.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

/**
 * Class Lives - Quản lý và hiển thị mạng sống trong game Arkanoid với LibGDX
 */
public class Lives implements Disposable {
    private int currentLives;
    private final int maxLives;
    private final int initialLives;

    // Vị trí hiển thị
    private Vector2 position;

    // Graphics
    private Texture heartTexture;
    private BitmapFont font;
    private final float heartSize = 48f;
    private final float heartSpacing = 40f;

    // Animation khi mất mạng
    private float loseLifeTimer = 0f;
    private final float LOSE_LIFE_ANIMATION_TIME = 0.5f;
    private boolean isAnimatingLoss = false;
    private int animatingHeartIndex = -1;

    /**
     * Constructor khởi tạo với số mạng mặc định
     * @param x Vị trí x hiển thị
     * @param y Vị trí y hiển thị
     */
    public Lives(float x, float y) {
        this(3, x, y);
    }

    /**
     * Constructor khởi tạo với số mạng tùy chỉnh
     * @param initialLives Số mạng ban đầu
     * @param x Vị trí x hiển thị
     * @param y Vị trí y hiển thị
     */
    public Lives(int initialLives, float x, float y) {
        this.initialLives = initialLives;
        this.currentLives = initialLives;
        this.maxLives = initialLives + 2;
        this.position = new Vector2(x, y);

        // Khởi tạo font
        font = new BitmapFont();
        font.getData().setScale(1.5f);
        font.setColor(Color.WHITE);

        // Load ảnh từ file (đặt file heart.png trong thư mục assets/)
        heartTexture = new Texture(Gdx.files.internal("heart.png"));
    }

    /**
     * Thay ảnh trái tim bằng file khác (tuỳ chọn)
     * @param texturePath đường dẫn tới file ảnh
     */
    public void setHeartTexture(String texturePath) {
        if (heartTexture != null) {
            heartTexture.dispose();
        }
        heartTexture = new Texture(Gdx.files.internal(texturePath));
    }

    /**
     * Cập nhật logic (animation)
     */
    public void update(float delta) {
        if (isAnimatingLoss) {
            loseLifeTimer += delta;
            if (loseLifeTimer >= LOSE_LIFE_ANIMATION_TIME) {
                isAnimatingLoss = false;
                loseLifeTimer = 0f;
                animatingHeartIndex = -1;
            }
        }
    }

    /**
     * Hiển thị mạng sống
     */
    public void render(SpriteBatch batch) {
        // Vẽ các tim còn sống
        for (int i = 0; i < currentLives; i++) {
            float x = position.x + (i * heartSpacing);
            float y = position.y;
            batch.setColor(1, 1, 1, 1);
            batch.draw(heartTexture, x, y, heartSize, heartSize);
        }

        // Hiệu ứng tim mất dần
        if (isAnimatingLoss && animatingHeartIndex >= 0) {
            float progress = loseLifeTimer / LOSE_LIFE_ANIMATION_TIME;
            if (progress > 1f) progress = 1f;

            float alpha = 1f - progress; // mờ dần
            float scale = 1f + progress * 0.5f; // nổ nhẹ
            float x = position.x + (animatingHeartIndex * heartSpacing);
            float y = position.y;
            float offset = (heartSize * (scale - 1f)) / 2f;

            batch.setColor(1, 1, 1, alpha);
            batch.draw(heartTexture,
                x - offset, y - offset,
                heartSize * scale, heartSize * scale);
        }

        // Reset màu để không ảnh hưởng phần khác
        batch.setColor(1, 1, 1, 1f);

        // Vẽ chữ hiển thị số mạng
        font.draw(batch, "x" + currentLives,
            position.x + (maxLives * heartSpacing) + 10,
            position.y + heartSize / 2 + 8);
    }


    /**
     * Vẽ đơn giản bằng ShapeRenderer (debug)
     */
    public void renderShapes(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        for (int i = 0; i < currentLives; i++) {
            float x = position.x + (i * heartSpacing) + heartSize / 2;
            float y = position.y + heartSize / 2;
            shapeRenderer.circle(x, y, heartSize / 2);
        }
        shapeRenderer.end();
    }

    /**
     * Mất một mạng
     */
    public boolean loseLife() {
        if (currentLives > 0) {
            animatingHeartIndex = currentLives - 1;  // ← Lưu index trái tim bị mất
            currentLives--;                           // ← Trừ mạng
            isAnimatingLoss = true;                   // ← Bật animation
            loseLifeTimer = 0f;
            return currentLives > 0;
        }
        return false;
    }

    /**
     * Thêm một mạng
     */
    public boolean addLife() {
        if (currentLives < maxLives) {
            currentLives++;
            return true;
        }
        return false;
    }

    public int getCurrentLives() { return currentLives; }
    public boolean hasLives() { return currentLives > 0; }
    public boolean isMaxLives() { return currentLives >= maxLives; }

    public void reset() {
        currentLives = initialLives;
        isAnimatingLoss = false;
        loseLifeTimer = 0f;
    }

    public void setPosition(float x, float y) { position.set(x, y); }
    public Vector2 getPosition() { return position; }

    @Override
    public void dispose() {
        if (heartTexture != null) heartTexture.dispose();
        if (font != null) font.dispose();
    }
}
