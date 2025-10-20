package com.uet.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class BallLauncher {
    private Ball ball;
    private Paddle paddle;
    private boolean isBallOnPaddle;

    public BallLauncher(Ball ball, Paddle paddle) {
        this.ball = ball;
        this.paddle = paddle;
        this.isBallOnPaddle = true;
        attachBallToPaddle(); // Gắn bóng lên paddle ngay khi khởi tạo
    }

    public void update() {
        // Nếu bóng đang trên paddle, cập nhật vị trí bóng theo paddle
        if (isBallOnPaddle) {
            attachBallToPaddle();

            // Kiểm tra nếu nhấn SPACE thì launch
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                launchBall();
            }
        }
    }

    // Gắn bóng lên paddle (ở giữa và phía trên paddle)
    private void attachBallToPaddle() {
        float ballX = paddle.getX() + paddle.getWidth() / 2;
        float ballY = paddle.getY() + paddle.getHeight() + ball.getRadius();
        ball.setPosition(ballX, ballY);
        ball.setVelocity(0, 0); // Đảm bảo bóng không di chuyển
        ball.setActive(false); // Đảm bảo bóng không active
    }

    // Launch bóng từ vị trí hiện tại của paddle
    private void launchBall() {
        isBallOnPaddle = false;
        ball.setActive(true);
        ball.launch(90); // Launch với góc 90 độ (thẳng lên)
    }

    // Reset bóng về trạng thái trên paddle
    public void resetBall() {
        isBallOnPaddle = true;
        ball.setActive(false);
        attachBallToPaddle();
    }

    public void fullReset() {
        // Reset paddle về vị trí giữa
        paddle.resetPosition();

        // Reset bóng lên trên paddle
        resetBall();
    }

    public boolean isWaitingForLaunch() {
        return isBallOnPaddle;
    }

    public boolean isBallOnPaddle() {
        return isBallOnPaddle;
    }
}
