package com.uet.arkanoid.paddle;

import com.badlogic.gdx.math.Rectangle;
import com.uet.arkanoid.ball.Ball;

public class PaddleCollision {

    private static final float MAX_BOUNCE_ANGLE_DEGREES = 75f;

    public void checkCollision(Paddle paddle, Ball ball) {
        // Chỉ kiểm tra khi bóng đang hoạt động và đi xuống
        if (ball.isActive() && ball.getVelocity().y < 0 && paddle.getBounds().overlaps(ball.getBounds())) {

            // 1. Tính toán vị trí va chạm
            float ballCenterX = ball.getX(); // Lấy từ ball.getPosition().x
            float paddleCenterX = paddle.getX() + paddle.getWidth() / 2;
            float hitPosition = ballCenterX - paddleCenterX;

            // 2. Chuẩn hóa vị trí va chạm về khoảng [-1, 1]
            float normalizedPosition = hitPosition / (paddle.getWidth() / 2);
            normalizedPosition = Math.max(-1f, Math.min(1f, normalizedPosition));

            // 3. Tính toán góc nảy mới
            float bounceAngle = normalizedPosition * MAX_BOUNCE_ANGLE_DEGREES;

            // 4. Cập nhật vận tốc của bóng
            float currentSpeed = ball.getSpeed(); // Sử dụng tốc độ đã lưu trong ball
            double newAngleRad = Math.toRadians(90 - bounceAngle); // 90 độ là thẳng đứng

            float newVx = (float) (currentSpeed * Math.cos(newAngleRad));
            float newVy = (float) (currentSpeed * Math.sin(newAngleRad));
            ball.setVelocity(newVx, newVy);

            // **THAY ĐỔI QUAN TRỌNG**
            // Đặt lại vị trí của bóng ngay phía trên paddle để tránh bị kẹt.
            // Vì position là tâm, nên ta phải đặt tâm của bóng
            // ở vị trí: (đỉnh paddle + bán kính của bóng).
            float newBallY = paddle.getY() + paddle.getHeight() + ball.getRadius();
            ball.setPosition(ball.getX(), newBallY);
        }
    }
}
