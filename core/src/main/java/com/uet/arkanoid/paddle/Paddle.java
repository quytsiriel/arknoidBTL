package com.uet.arkanoid.paddle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Gdx;

/**
 * Lớp cha trừu tượng cho các vật thể trong game (Paddle nói chung).
 */
public abstract class Paddle {

    // protected để các lớp con (như Paddle) có thể truy cập
    protected Rectangle bounds;
    protected Texture texture;

    /**
     * Constructor cho một thực thể game.
     * @param texturePath Đường dẫn đến file ảnh.
     */
    public Paddle(String texturePath) {
        if (texturePath != null && !texturePath.isEmpty()) {
            this.texture = new Texture(Gdx.files.internal(texturePath));
        }

        this.bounds = new Rectangle();
    }

    /**
     * Phương thức update logic, được gọi mỗi frame.
     * Lớp con BẮT BUỘC phải định nghĩa (implement).
     */
    public abstract void update(float delta);

    /**
     * Phương thức vẽ chung.
     * Lớp con có thể @Override nếu cần hành vi vẽ đặc biệt.
     */
    public void render(SpriteBatch batch) {
        if (texture != null) {
            batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    /**
     * Giải phóng tài nguyên (texture).
     */
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }

    // --- Các Getter chung ---

    public Rectangle getBounds() {
        return bounds;
    }

    public float getX() {
        return bounds.x;
    }

    public float getY() {
        return bounds.y;
    }

    public float getWidth() {
        return bounds.width;
    }

    public float getHeight() {
        return bounds.height;
    }
}
