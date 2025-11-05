package com.uet.arkanoid.ball;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.uet.arkanoid.brick.Brick;
import com.uet.arkanoid.ui.Lives;

/**
 * Đại diện cho quả bóng trong trò chơi Arkanoid, quản lý vị trí, vận tốc và va chạm cơ bản.
 */
public class Ball {
    public Vector2 position;
    private Vector2 velocity;
    private float radius;          // Bán kính của bóng (cho logic game)
    private Texture texture;       // Texture hình ảnh
    private float width;           // Chiều rộng hiển thị
    private float height;          // Chiều cao hiển thị
    public float speed;           // Tốc độ cơ bản
    private boolean active;        // Trạng thái hoạt động
    /** Hình chữ nhật bao quanh (Bounding Box) dùng để kiểm tra va chạm. */
    private Rectangle bounds;

    public Ball(float x, float y, float radius, float speed, Texture texture) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0, 0);
        this.radius = radius;
        this.speed = speed;
        this.texture = texture;
        this.width = radius * 2;
        this.height = radius * 2;
        this.active = false;
        this.bounds = new Rectangle(x - radius, y - radius, width, height);
    }

    // Constructor (để thêm ảnh quả bóng)
    public Ball(float x, float y, float radius, float speed, String texturePath) {
        this(x, y, radius, speed, new Texture(texturePath));
    }

    /**
     * Khởi động bóng, đặt vận tốc ban đầu theo một góc.
     */
    public void launch(float angleDegrees) {
        float angleRad = (float) Math.toRadians(angleDegrees);
        velocity.x = (float) Math.cos(angleRad) * speed;
        velocity.y = (float) Math.sin(angleRad) * speed;
        active = true;
    }

    /**
     * Cập nhật vị trí của bóng trong mỗi khung hình dựa trên vận tốc.
     */
    public void update(float delta) {
        if (active) {
            position.x += velocity.x * delta;
            position.y += velocity.y * delta;
            updateBounds();
        }
    }

    // Cập nhật bounds theo vị trí hiện tại
    private void updateBounds() {
        bounds.setPosition(position.x - width / 2, position.y - height / 2);
    }

    /**
     * Vẽ bóng lên màn hình bằng SpriteBatch.
     *
     * @param batch SpriteBatch dùng để vẽ.
     */
    public void render(SpriteBatch batch) {
        if (texture != null) {
            // Vẽ texture với tâm ở vị trí position
            batch.draw(texture,
                position.x - width / 2,   // x (góc dưới trái)
                position.y - height / 2,  // y (góc dưới trái)
                width,                     // width
                height);                   // height
        }
    }

    // Đảo chiều theo trục X (va chạm tường trái/phải)
    public void reverseX() {
        velocity.x = -velocity.x;
    }

    // Đảo chiều theo trục Y (va chạm tường trên/dưới)
    public void reverseY() {
        velocity.y = -velocity.y;
    }

    // Xử lý va chạm sơ bộ với gạch (Brick)
    public void Nay(Brick brick) {
        if (position.y > brick.getY() + 30f || position.y  < brick.getY()) {
            velocity.y *= -1;
        }
        else if(position.x <= brick.getX() || position.x  >= brick.getX() + 80f) {
            velocity.x *= -1;
        }
    }
    // Reset bóng về vị trí ban đầu
    public void reset(float x, float y) {
        position.set(x, y);
        velocity.set(0, 0);
        active = false;
    }


    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getRadius() {
        return radius;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getSpeed() {
        return speed;
    }

    public boolean isActive() {
        return active;
    }

    public Texture getTexture() {
        return texture;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
        updateBounds();
    }

    public void setVelocity(float vx, float vy) {
        velocity.set(vx, vy);
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    // Kiem tra bong ra khoi canh duoi
    public boolean isFallenOffScreen() {
        return position.y < -radius;
    }

}
