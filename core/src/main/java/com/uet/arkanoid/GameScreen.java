package com.uet.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class GameScreen {
    private Texture background;
    private Texture pressSpaceTexture;

    private BrickManager brickManager;
    private Ball ball;
    private Paddle paddle;
    private PaddleCollision paddleCollision;
    private ScoreSystem scoreSystem;
    private Lives livesSystem;
    private BallLauncher ballLauncher;

    private float screenWidth;
    private float screenHeight;

    private float blinkTimer = 0f;
    private boolean showPressSpace = true;
    private final float BLINK_INTERVAL = 0.4f;

    public GameScreen(Difficulty difficulty) {
        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();

        background = new Texture(Gdx.files.internal("background.png"));
        pressSpaceTexture = new Texture(Gdx.files.internal("press_space.png"));

        // Khởi tạo các đối tượng game
        paddle = new Paddle(
            (screenWidth - 128) / 2f,
            50
        );

        paddleCollision = new PaddleCollision();
        scoreSystem = new ScoreSystem(50, screenHeight - 50);
        livesSystem = new Lives(screenWidth - 200, screenHeight - 70);
        brickManager = new BrickManager("Level1.tmx");



        Texture ballTexture = new Texture(Gdx.files.internal("ball.png"));
        float startX = screenWidth / 2f;
        float startY = paddle.getY() + 20;

        // Điều chỉnh tốc độ bóng dựa vào độ khó
        float ballSpeed;
        switch (difficulty) {
            case EASY:   ballSpeed = 400; break;
            case HARD:   ballSpeed = 650; break;
            default:     ballSpeed = 500; break;
        }

        ball = new Ball(startX, startY + 10, 10, ballSpeed, ballTexture);
        ballLauncher = new BallLauncher(ball, paddle);
    }

    public void update(float delta) {
        paddle.update(delta);
        ballLauncher.update();
        ball.update(delta);

        if (ball.isActive() && !ballLauncher.isBallOnPaddle()) {
            brickManager.checkCollision(ball, scoreSystem);
            paddleCollision.checkCollision(paddle, ball);
            checkWallCollision();
        }

        scoreSystem.update(delta);
        livesSystem.update(delta);
        updateBlinkEffect(delta);
    }

    /**
     * Blink 'Press Space' effect.
     */
    private void updateBlinkEffect(float delta) {
        blinkTimer += delta;
        if (blinkTimer >= BLINK_INTERVAL) {
            blinkTimer = 0f;
            showPressSpace = !showPressSpace;
        }
    }


    public void render(SpriteBatch batch) {
        batch.draw(background, 0, 0);
        paddle.render(batch);
        brickManager.render(batch);
        ball.render(batch);
        scoreSystem.render(batch);
        livesSystem.render(batch);


        if (ballLauncher.isWaitingForLaunch() && pressSpaceTexture != null && showPressSpace) {
            float imageWidth = 650f;
            float imageHeight = 320f;
            float x = (screenWidth - imageWidth + 40) / 2f;
            float y = (screenHeight - imageHeight - 600) / 2f;
            batch.draw(pressSpaceTexture, x, y, imageWidth, imageHeight);
        }
    }

    private void checkWallCollision() {
        if (!ball.isActive() || ballLauncher.isBallOnPaddle()) return;

        if (ball.getX() - ball.getRadius() <= 32 || ball.getX() + ball.getRadius() >= 1008)
            ball.reverseX();

        if (ball.getY() + ball.getRadius() >= 778)
            ball.reverseY();

        if (brickManager.isAllCleared()) {
            // Xử lý khi hoàn thành màn chơi
        }

        if (ball.getY() < -ball.getRadius()) {
            boolean stillAlive = livesSystem.loseLife();
            if (stillAlive) {
                ballLauncher.fullReset();
            } else {
                // Game over - sẽ được xử lý trong Main
            }
        }
    }

    public void dispose() {
        if (background != null) background.dispose();
        if (pressSpaceTexture != null) pressSpaceTexture.dispose();
        if (ball != null) ball.dispose();
        if (paddle != null) paddle.dispose();
        if (brickManager != null) brickManager.dispose();
        if (scoreSystem != null) scoreSystem.dispose();
        if (livesSystem != null) livesSystem.dispose();
    }

    // Các phương thức getter để Main có thể kiểm tra trạng thái game
    public boolean isGameOver() {
        return livesSystem.getCurrentLives() <= 0;
    }

    public boolean isLevelComplete() {
        return brickManager.isAllCleared();
    }

    public BallLauncher getBallLauncher() {
        return ballLauncher;
    }

    public Lives getLivesSystem() {
        return livesSystem;
    }

    public ScoreSystem getScoreSystem() {
        return scoreSystem;
    }
}
