package com.uet.arkanoid.paddle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.uet.arkanoid.ball.Ball;

public class PaddleNormal extends Paddle {

    private static final float MAX_BOUNCE_ANGLE_DEGREES = 75f;
    private float speed;

    private boolean expanded = false;
    private float expandTimer = 0f;
    private final float NORMAL_WIDTH;
    private final float MAX_WIDTH;

    public PaddleNormal(float x, float y) {
        super("paddle.png");
        this.speed = 600f;

        float desiredWidth = 130;
        float aspectRatio = (float) texture.getHeight() / texture.getWidth();
        float desiredHeight = desiredWidth * aspectRatio;
        this.bounds.set(x, y, desiredWidth, desiredHeight);

        this.NORMAL_WIDTH = desiredWidth;
        this.MAX_WIDTH = desiredWidth * 1.5f; // mở rộng 1.5 lần
    }

    @Override
    public void update(float delta) {
        // Di chuyển paddle
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            bounds.x -= speed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            bounds.x += speed * delta;
        }

        // Giới hạn trong khung chơi
        float LEFT_WALL = 32f;
        float RIGHT_WALL = 1008f;
        if (bounds.x < LEFT_WALL) bounds.x = LEFT_WALL;
        if (bounds.x + bounds.width > RIGHT_WALL) bounds.x = RIGHT_WALL - bounds.width;

        // Nếu đang mở rộng — giảm thời gian và tự thu nhỏ lại
        if (expanded) {
            expandTimer -= delta;
            if (expandTimer <= 0) {
                bounds.width = NORMAL_WIDTH;
                expanded = false;
            }
        }
    }

    public void resetPosition() {
        bounds.x = (Gdx.graphics.getWidth() - bounds.width - 200) / 2f;
        bounds.y = 50;
    }

    public void checkCollision(Ball ball) {
        if (ball.isActive() && ball.getVelocity().y < 0 && this.getBounds().overlaps(ball.getBounds())) {

            float ballCenterX = ball.getX();
            float paddleCenterX = this.getX() + this.getWidth() / 2;
            float hitPosition = ballCenterX - paddleCenterX;

            float normalizedPosition = hitPosition / (this.getWidth() / 2);
            normalizedPosition = Math.max(-1f, Math.min(1f, normalizedPosition));

            float bounceAngle = normalizedPosition * MAX_BOUNCE_ANGLE_DEGREES;

            float currentSpeed = ball.getSpeed();
            double newAngleRad = Math.toRadians(90 - bounceAngle);

            float newVx = (float) (currentSpeed * Math.cos(newAngleRad));
            float newVy = (float) (currentSpeed * Math.sin(newAngleRad));
            ball.setVelocity(newVx, newVy);

            float newBallY = this.getY() + this.getHeight() + ball.getRadius();
            ball.setPosition(ball.getX(), newBallY);
        }
    }

    /**
     * ✅ Gọi khi ăn power-up mở rộng paddle
     */
    public void expand() {
        if (!expanded) {
            bounds.width = MAX_WIDTH;
            expanded = true;
            expandTimer = 30f;
        } else {
            expandTimer = 30f;
        }

        // tránh vượt khỏi màn hình
        if (bounds.x + bounds.width > 1008f) {
            bounds.x = 1008f - bounds.width;
        }
    }
}
