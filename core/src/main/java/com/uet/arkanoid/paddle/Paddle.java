package com.uet.arkanoid.paddle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Paddle {
    private Texture texture;
    private Rectangle bounds;
    private float speed;


    public Paddle(float x, float y) {
        // Tải ảnh paddle
        texture = new Texture(Gdx.files.internal("paddle.png"));

        // Kích thước paddle
        float width = texture.getWidth();
        float height = texture.getHeight();

        // Tạo vùng va chạm
        bounds = new Rectangle(x, y, width, height);

        // Tốc độ di chuyển
        speed = 500f;

        float desiredWidth = 130;  // chiều rộng mong muốn (px)
        float aspectRatio = (float) texture.getHeight() / texture.getWidth();
        float desiredHeight = desiredWidth * aspectRatio;

        bounds = new Rectangle(x, y, desiredWidth, desiredHeight);

    }

    public void update(float delta) {
        // Điều khiển paddle bằng bàn phím
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT)) {
            bounds.x -= speed * delta;
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT)) {
            bounds.x += speed * delta;
        }

        // Giới hạn không cho paddle ra khỏi màn hình
        if (bounds.x < 0) bounds.x = 0;
        if (bounds.x + bounds.width > Gdx.graphics.getWidth())
            bounds.x = Gdx.graphics.getWidth() - bounds.width;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public void dispose() {
        texture.dispose();
    }

    // Getter cho vị trí paddle
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

    public void resetPosition() {
        bounds.x = (Gdx.graphics.getWidth() - bounds.width) / 2f;
        bounds.y = 50; // khoảng cách từ đáy lên
    }
}
