package com.uet.arkanoid.paddle;

import com.badlogic.gdx.Gdx;
import com.uet.arkanoid.ball.Ball; // <-- Thêm import cho Ball
import com.uet.arkanoid.paddle.Paddle;

public class PaddleNormal extends Paddle {

    private static final float MAX_BOUNCE_ANGLE_DEGREES = 75f;

    private float speed;

    public PaddleNormal(float x, float y) {
        super("paddle.png");
        this.speed = 500f;
        float desiredWidth = 130;
        float aspectRatio = (float) texture.getHeight() / texture.getWidth();
        float desiredHeight = desiredWidth * aspectRatio;
        this.bounds.set(x, y, desiredWidth, desiredHeight);
    }

    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT)) {
            bounds.x -= speed * delta;
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT)) {
            bounds.x += speed * delta;
        }

        float LEFT_WALL = 32f;
        float RIGHT_WALL = 1008f;

        if (bounds.x < LEFT_WALL) {
            bounds.x = LEFT_WALL;
        }
        if (bounds.x + bounds.width > RIGHT_WALL) {
            bounds.x = RIGHT_WALL - bounds.width;
        }
    }

    public void resetPosition() {
        bounds.x = (Gdx.graphics.getWidth() - bounds.width) / 2f;
        bounds.y = 50;
    }

    // --- ĐÂY LÀ PHƯƠNG THỨC MỚI ĐƯỢC CHUYỂN VÀO ---
    /**
     * Kiểm tra va chạm giữa paddle (này) và quả bóng.
     * @param ball Quả bóng để kiểm tra.
     */
    public void checkCollision(Ball ball) {
        // Thay 'paddle.getBounds()' bằng 'this.getBounds()' (hoặc chỉ 'getBounds()')
        if (ball.isActive() && ball.getVelocity().y < 0 && this.getBounds().overlaps(ball.getBounds())) {

            float ballCenterX = ball.getX();
            // Thay 'paddle.getX()' bằng 'this.getX()' (hoặc chỉ 'getX()')
            float paddleCenterX = this.getX() + this.getWidth() / 2;
            float hitPosition = ballCenterX - paddleCenterX;

            // Thay 'paddle.getWidth()' bằng 'this.getWidth()'
            float normalizedPosition = hitPosition / (this.getWidth() / 2);
            normalizedPosition = Math.max(-1f, Math.min(1f, normalizedPosition));

            float bounceAngle = normalizedPosition * MAX_BOUNCE_ANGLE_DEGREES;

            float currentSpeed = ball.getSpeed();
            double newAngleRad = Math.toRadians(90 - bounceAngle);

            float newVx = (float) (currentSpeed * Math.cos(newAngleRad));
            float newVy = (float) (currentSpeed * Math.sin(newAngleRad));
            ball.setVelocity(newVx, newVy);

            // Thay 'paddle.getY()' và 'paddle.getHeight()' bằng 'this'
            float newBallY = this.getY() + this.getHeight() + ball.getRadius();
            ball.setPosition(ball.getX(), newBallY);
        }
    }
}
