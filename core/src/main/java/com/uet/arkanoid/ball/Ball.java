package com.uet.arkanoid.ball;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.uet.arkanoid.brick.Brick;

/**
 * Lớp trừu tượng (Abstract Class) cho tất cả các loại bóng.
 * Cung cấp thuộc tính chung và buộc lớp con phải triển khai logic va chạm.
 */
public abstract class Ball {
    // (Giữ nguyên các thuộc tính protected)
    protected Vector2 position;
    protected Vector2 velocity;
    protected float radius;
    protected Texture texture;
    protected float width;
    protected float height;
    public float speed;
    protected boolean active;
    protected Rectangle bounds;

    // Constructor vẫn giữ nguyên
    public Ball(float x, float y, float radius, float speed, Texture texture) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0, 0);
        this.radius = radius;
        this.speed = speed;
        this.texture = texture;
        this.width = radius * 2;
        this.height = radius * 2;
        this.active = false;
        // Khởi tạo bounds
        this.bounds = new Rectangle(x - radius, y - radius, width, height);
    }

    // Phương thức bắt buộc lớp con phải triển khai
    public abstract void handleBrickCollision(Brick brick);

    // Các phương thức cụ thể khác vẫn giữ nguyên logic đã có
    /**
     * Thiết lập vận tốc ban đầu và phóng bóng đi.
     * Góc 90 độ là đi thẳng lên.
     * @param angleDegrees Góc phóng so với trục X (ví dụ: 90 độ là thẳng đứng).
     */
    public void launch(float angleDegrees) {
        if (active) return;
        double angleRad = Math.toRadians(angleDegrees);
        float newVx = (float) (speed * Math.cos(angleRad));
        float newVy = (float) (speed * Math.sin(angleRad));
        setVelocity(newVx, newVy);
        this.active = true;
    }
    /**
     * Cập nhật vị trí bóng dựa trên vận tốc và thời gian trôi qua.
     * @param delta Thời gian trôi qua kể từ khung hình trước (giây).
     */
    public void update(float delta) {
        if (active) {
            position.x += velocity.x * delta;
            position.y += velocity.y * delta;
            updateBounds();
        }
    }

    // Cập nhật bounds theo vị trí hiện tại
    protected void updateBounds() {
        // Cập nhật bounds: vị trí x, y là góc dưới trái của hình chữ nhật bao quanh
        bounds.setPosition(position.x - width / 2, position.y - height / 2);
    }

    // Phương thức vẽ bóng
    public void render(SpriteBatch batch) {
        if (texture != null) {
            batch.draw(texture,
                position.x - width / 2,
                position.y - height / 2,
                width,
                height);
        }
    }

    public void reverseX() {
        velocity.x = -velocity.x;
    }

    public void reverseY() {
        velocity.y = -velocity.y;
    }

    public void reset(float x, float y) {
        position.set(x, y);
        velocity.set(0, 0);
        active = false;
        updateBounds();
    }

    public boolean isActive() { return active; }
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }

    /** Trả về vị trí tâm của bóng (Vector2). */
    public Vector2 getPosition() {
        return position;
    }

    /** Trả về tọa độ X của tâm bóng. */
    public float getX() {
        return position.x;
    }

    /** Trả về tọa độ Y của tâm bóng. */
    public float getY() {
        return position.y;
    }

    /** Trả về bán kính của bóng. */
    public float getRadius() {
        return radius;
    }

    /** Trả về vận tốc hiện tại (Vector2). */
    public Vector2 getVelocity() {
        return velocity;
    }

    /** Trả về vùng giới hạn va chạm (Rectangle). */
    public Rectangle getBounds() {
        return bounds;
    }

    /** Trả về tốc độ hiện tại của bóng (độ lớn vector vận tốc). */
    public float getSpeed() {
        // Dùng thuộc tính speed đã lưu, hoặc có thể tính lại bằng velocity.len();
        return speed;
    }

    /** * Thiết lập vị trí mới cho bóng và cập nhật bounds.
     * @param x Tọa độ X mới.
     * @param y Tọa độ Y mới.
     */
    public void setPosition(float x, float y) {
        this.position.set(x, y);
        updateBounds();
    }

    /** * Thiết lập vận tốc mới cho bóng và cập nhật speed.
     * @param vx Vận tốc X mới.
     * @param vy Vận tốc Y mới.
     */
    public void setVelocity(float vx, float vy) {
        this.velocity.set(vx, vy);
        // Cập nhật speed sau khi vận tốc thay đổi
        this.speed = velocity.len();
    }

    /**
     * Thiết lập trạng thái hoạt động của bóng.
     * @param active True nếu bóng đang bay, false nếu bóng đang gắn với paddle.
     */
    public void setActive(boolean active) {
        this.active = active;
    }
}
