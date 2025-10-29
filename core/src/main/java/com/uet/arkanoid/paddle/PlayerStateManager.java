// Đặt trong package com.uet.arkanoid.ui
package com.uet.arkanoid.paddle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.uet.arkanoid.Main;
import com.uet.arkanoid.ball.Ball;
import com.uet.arkanoid.paddle.PaddleNormal; // Sử dụng lớp con PaddleNormal
import com.uet.arkanoid.ui.Lives;

/**
 * Lớp này quản lý trạng thái của người chơi, bao gồm:
 * - Xử lý việc mất mạng khi bóng rơi.
 * - Reset bóng và paddle về vị trí chờ.
 * - Điều khiển việc launch bóng bằng phím SPACE.
 */
public class PlayerStateManager {

    private Ball ball;
    private PaddleNormal paddle;
    private Lives livesSystem;
    private Main game; // Để quay về menu

    // Biến lưu vị trí Y của bóng so với paddle
    private float ballYOffset;

    public PlayerStateManager(Ball ball, PaddleNormal paddle, Lives livesSystem, Main game) {
        this.ball = ball;
        this.paddle = paddle;
        this.livesSystem = livesSystem;
        this.game = game;

        // Tính toán offset một lần (dựa trên vị trí khởi tạo của bóng)
        this.ballYOffset = ball.getY() - paddle.getY();

        // Bắt đầu game ở trạng thái chờ launch
        resetForLaunch();
    }

    /**
     * Được gọi mỗi frame từ GameScreen.render()
     * Quản lý trạng thái của bóng (chờ hoặc đang chơi).
     */
    public void update(float delta) {
        if (ball.isActive()) {
            checkLifeLost();
        } else {
            updateWaitingToLaunch(delta);
        }
    }

    /**
     * Cập nhật logic khi bóng đang ở trên paddle và chờ được launch.
     */
    private void updateWaitingToLaunch(float delta) {
        float followX = paddle.getX() + (paddle.getWidth() / 2) - (ball.getRadius()) + 10;
        float followY = paddle.getY() + ballYOffset;
        ball.setPosition(followX, followY);

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            ball.launch(90);
        }
    }

    /**
     * Kiểm tra xem bóng có rơi ra khỏi màn hình không.
     */
    private void checkLifeLost() {
        if (ball.getY() < -ball.getRadius()) {
            boolean stillAlive = livesSystem.loseLife();

            if (stillAlive) {
                resetForLaunch();
            } else {
                game.returnToMenu();
            }
        }
    }

    /**
     * Đặt lại paddle và bóng về trạng thái chờ (giữa màn hình).
     */
    private void resetForLaunch() {
        paddle.resetPosition();

        // 2. Reset bóng về vị trí trên paddle (và đặt isActive = false)
        // Cần cập nhật X của bóng sau khi paddle đã reset
        float resetBallX = paddle.getX() + (paddle.getWidth() / 2) - (ball.getRadius()) + 30;
        float resetBallY = paddle.getY() + ballYOffset + 15;

        ball.reset(resetBallX, resetBallY);
    }
}
