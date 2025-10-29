package com.uet.arkanoid.ball;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.uet.arkanoid.brick.Brick;
import com.uet.arkanoid.ui.Lives;

public class Ball {
    public Vector2 position;      // Vị trí của bóng
    private Vector2 velocity;      // Vận tốc của bóng
    private float radius;          // Bán kính của bóng (cho logic game)
    private Texture texture;       // Texture hình ảnh
    private float width;           // Chiều rộng hiển thị
    private float height;          // Chiều cao hiển thị
    public float speed;           // Tốc độ cơ bản
    private boolean active;        // Trạng thái hoạt động
    private Rectangle bounds;      // Hình chữ nhật bao quanh (cho collision)

    // Constructor
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

    // Khởi động bóng với góc ban đầu (độ)
    public void launch(float angleDegrees) {
        float angleRad = (float) Math.toRadians(angleDegrees);
        velocity.x = (float) Math.cos(angleRad) * speed;
        velocity.y = (float) Math.sin(angleRad) * speed;
        active = true;
    }

    // Cập nhật vị trí bóng
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

    // Vẽ bóng
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

    // Tăng tốc độ
    public void increaseSpeed(float amount) {
        speed += amount;
        // Cập nhật velocity theo tỷ lệ
        float currentSpeed = velocity.len();
        if (currentSpeed > 0) {
            velocity.scl(speed / currentSpeed);
        }
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

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    // Kiểm tra bóng có ra khỏi màn hình không
    public boolean isOutOfBounds(float screenWidth, float screenHeight) {
        return position.x < -radius || position.x > screenWidth + radius ||
            position.y < -radius || position.y > screenHeight + radius;
    }

    // Kiem tra bong ra khoi canh duoi
    public boolean isFallenOffScreen(float screenHeight) {
        return position.y < -radius;
    }

    // Kiem tra bong roi = mat mang
    public boolean checkAndHandleLostLife(float screenWidth, float screenHeight,
                                          Lives lives, float resetX, float resetY) {
        // Chỉ kiểm tra khi bóng rơi xuống dưới màn hình
        if (position.y < -radius && active) {
            active = false;
            boolean stillAlive = lives.loseLife();

            if (stillAlive) {
                // Reset bóng về vị trí ban đầu để chơi tiếp
                reset(resetX, resetY);
                return true;
            } else {
                // Game Over
                return false;
            }
        }
        return true; // Bóng vẫn đang chơi bình thường
    }

    // Giải phóng tài nguyên
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}
