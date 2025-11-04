    package com.uet.arkanoid.ball;

    import com.badlogic.gdx.graphics.Texture;
    import com.badlogic.gdx.graphics.g2d.SpriteBatch;
    import com.badlogic.gdx.utils.Array;
    import com.uet.arkanoid.Main;
    import com.uet.arkanoid.brick.BrickManager;
    import com.uet.arkanoid.paddle.Paddle;
    import com.uet.arkanoid.paddle.PaddleNormal;
    import com.uet.arkanoid.ui.Lives;
    import com.uet.arkanoid.ui.ScoreSystem;

    // 1
    /**
     * Lớp này quản lý TẤT CẢ các quả bóng đang hoạt động trong game.
     */
    public class BallManager {

        private Array<Ball> balls = new Array<>();
        private Texture ballTexture;
        private float initialBallSpeed;
        private final Main game;
        private BrickManager brickManager;
        private ScoreSystem scoreSystem;
        private Lives livesSystem;

        // Biến để reset bóng
        private float resetX, resetY;

        // Đã mất mạng trong khung hình này chưa (tránh mất 3 mạng cùng lúc)
        private boolean lifeLostThisFrame = false;

        private Array<Ball> newBallsQueue = new Array<>();

        public BallManager(Texture ballTexture, float initialBallSpeed, Main game, float resetX, float resetY) {
            this.ballTexture = ballTexture;
            this.initialBallSpeed = initialBallSpeed;
            this.resetX = resetX;
            this.resetY = resetY;
            this.game = game;
        }

        /**
         * Thêm một quả bóng mới vào trình quản lý (ví dụ: bóng khởi đầu).
         */
        public void addBall(Ball ball) {
            balls.add(ball);
        }

        /**
         * Nhân đôi TẤT CẢ các quả bóng đang hoạt động.
         * Ví dụ: 1 bóng -> 3 bóng (1 cũ + 2 mới)
         * 3 bóng -> 9 bóng (3 cũ + 6 mới)
         */
        public void multiplyAllActiveBalls() {
            // Xóa hàng đợi cũ trước khi bắt đầu
            newBallsQueue.clear();

            // 1. Lặp qua tất cả các bóng hiện có
            for (Ball mainBall : balls) {

                // Chỉ nhân bản những quả bóng đang bay
                if (mainBall.isActive()) {

                    // Tạo 2 quả bóng mới cho mỗi quả bóng đang bay
                    for (int i = 0; i < 2; i++) {
                        Ball newBall = new NormalBall(
                            mainBall.getX(),
                            mainBall.getY(),
                            mainBall.getRadius(),
                            mainBall.getSpeed(),
                            ballTexture
                        );
                        float newVx = mainBall.getVelocity().x * (i == 0 ? 0.9f : -0.9f);
                        float newVy = mainBall.getVelocity().y * 1.1f;
                        if(Math.abs(newVx) < 50) newVx = (i == 0 ? 150f : -150f);

                        newBall.setVelocity(newVx, newVy);
                        newBall.setActive(true);

                        // Thêm bóng mới vào HÀNG ĐỢI (không thêm vào 'balls')
                        newBallsQueue.add(newBall);
                    }
                }
            }

            // 2. Sau khi lặp xong, thêm tất cả bóng từ hàng đợi vào danh sách chính
            balls.addAll(newBallsQueue);
        }

        /**
         * Cập nhật TẤT CẢ các quả bóng và xử lý va chạm, mất mạng 2 người chơi.
         */
        public void update(float delta, Paddle paddle1, Paddle paddle2,
                           BrickManager brickManager, ScoreSystem scoreSystem, Lives lives) {

            lifeLostThisFrame = false;
            this.brickManager = brickManager;
            this.scoreSystem = scoreSystem;
            this.livesSystem = lives;

            for (int i = balls.size - 1; i >= 0; i--) {
                Ball ball = balls.get(i);

                if (ball.isActive()) {
                    ball.update(delta);

                    // ✅ Va chạm với gạch
                    brickManager.checkCollision(ball, scoreSystem);

                    // ✅ Va chạm Paddle 1
                    ((PaddleNormal) paddle1).checkCollision(ball);

                    // ✅ Va chạm Paddle 2
                    ((PaddleNormal) paddle2).checkCollision(ball);

                    // ✅ Va chạm tường
                    checkWallCollision(ball);

                    // ✅ Bóng rơi ra ngoài
                    if (ball.isFallenOffScreen()) {
                        ball.setActive(false);
                        balls.removeIndex(i);
                    }
                } else {
                    // ✅ Bóng đang chờ phóng → nằm trên paddle 1
                    ball.setPosition(
                        paddle1.getX() + paddle1.getWidth() / 2,
                        paddle1.getY() + paddle1.getHeight() + ball.getRadius()
                    );
                }
            }

            // ✅ Nếu hết bóng → trừ mạng
            if (balls.size == 0 && !lifeLostThisFrame) {
                handleLifeLoss(lives);
            }
        }

        /**
         * Hàm update cũ dùng cho chế độ 1 người chơi
         */
        public void update(float delta, Paddle paddle,
                           BrickManager brickManager, ScoreSystem scoreSystem, Lives lives) {

            lifeLostThisFrame = false;
            this.brickManager = brickManager;
            this.scoreSystem = scoreSystem;
            this.livesSystem = lives;

            for (int i = balls.size - 1; i >= 0; i--) {
                Ball ball = balls.get(i);

                if (ball.isActive()) {
                    ball.update(delta);

                    // Va chạm gạch
                    brickManager.checkCollision(ball, scoreSystem);

                    // Va chạm paddle
                    ((PaddleNormal) paddle).checkCollision(ball);

                    // Va chạm tường
                    checkWallCollision(ball);

                    // Rơi ra ngoài -> mất mạng
                    if (ball.isFallenOffScreen()) {
                        ball.setActive(false);
                        balls.removeIndex(i);
                    }
                } else {
                    // Bóng đang chờ phóng → nằm trên paddle
                    ball.setPosition(
                        paddle.getX() + paddle.getWidth() / 2,
                        paddle.getY() + paddle.getHeight() + ball.getRadius()
                    );
                }
            }

            // Nếu hết bóng → trừ mạng
            if (balls.size == 0 && !lifeLostThisFrame) {
                handleLifeLoss(lives);
            }
        }

        /**
         * Xử lý logic mất mạng và reset bóng.
         */
        private void handleLifeLoss(Lives lives) {
            lifeLostThisFrame = true;
            boolean stillAlive = lives.loseLife();

            if (stillAlive) {
                // Tạo lại quả bóng chính
                Ball initialBall = new NormalBall(
                    resetX,
                    resetY,
                    10,
                    initialBallSpeed,
                    ballTexture
                );
                initialBall.setActive(false); // Chờ người chơi phóng
                addBall(initialBall);
            } else {
                System.out.println("GAME OVER");
            }
        }

        /**
         * Vẽ tất cả các quả bóng.
         */
        public void render(SpriteBatch batch) {
            for (Ball ball : balls) {
                ball.render(batch);
            }
        }

        /**
         * Kiểm tra va chạm tường cho một quả bóng.
         */
        private void checkWallCollision(Ball ball) {
            float LEFT_WALL = 32f;
            float RIGHT_WALL = 1008f;
            float TOP_WALL = 778f;

            if (ball.getX() - ball.getRadius() <= LEFT_WALL || ball.getX() + ball.getRadius() >= RIGHT_WALL) {
                ball.reverseX();
            }
            if (ball.getY() + ball.getRadius() >= TOP_WALL ) {
                ball.reverseY();
            }
            if (brickManager.isAllCleared()) {
                game.returnToMenu();
            }
            if (livesSystem.getCurrentLives() == 0) {
                game.showGameOver(scoreSystem.getHighScore());
            }
        }

        /**
         * Dùng để phóng quả bóng đầu tiên (khi chưa active).
         */
        public void launchFirstBall() {
            if(balls.size > 0 && !balls.get(0).isActive()) {
                balls.get(0).launch(85);
            }
        }

        public boolean isBallInPlay() {
            for (Ball ball : balls) {
                if (ball.isActive()) {
                    return true;
                }
            }
            return false;
        }

        public void dispose() {
            if (ballTexture != null) ballTexture.dispose();
        }
    }
