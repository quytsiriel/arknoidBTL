package com.uet.arkanoid.powerup;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.uet.arkanoid.paddle.Paddle;

/**
 * Lớp cha TRỪU TƯỢNG cho tất cả vật phẩm rơi.
 * Chỉ xử lý logic di chuyển (rơi) và render.
 * Không tự kiểm tra va chạm Paddle ở đây.
 */
public abstract class PowerUp {
    protected Texture texture;
    protected Rectangle bounds;
    protected boolean active = true;
    protected float speedY = -100f;
    protected int type; // 1=ExtraLife, 2=MultiBall, 3=ExpandPaddle

    public PowerUp(Texture texture, float x, float y, float width, float height, int type) {
        this.texture = texture;
        this.bounds = new Rectangle(x, y, width, height);
        this.type = type;
    }

    /**
     * Cập nhật logic di chuyển (rơi).
     * Đã xóa logic va chạm Paddle khỏi đây.
     */
    public void update(float delta) {
        if (!active) return;

        bounds.y += speedY * delta;

        // Rơi ra ngoài
        if (bounds.y + bounds.height < 0) {
            active = false;
        }
    }

    /**
     * Phương thức mới: Để BrickManager kiểm tra va chạm.
     * @param paddle Paddle để kiểm tra
     * @return true nếu va chạm và đang hoạt động, ngược lại false
     */
    public boolean checkCollision(Paddle paddle) {
        return active && bounds.overlaps(paddle.getBounds());
    }

    public void render(SpriteBatch batch) {
        if (active)
            batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public boolean isActive() {
        return active;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public int getType() {
        return type;
    }

    public void deactivate() {
        active = false;
    }

    /**
     * Lớp con BẮT BUỘC phải định nghĩa hiệu ứng của nó là gì.
     * Dùng 'Object' để nhận bất kỳ đối tượng nào (Paddle, Lives, BallManager...).
     */
    public abstract void applyEffect(Object receiver);
}
