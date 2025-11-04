package com.uet.arkanoid.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.uet.arkanoid.Main;
import com.uet.arkanoid.ball.Ball;
import com.uet.arkanoid.ball.BallManager;
import com.uet.arkanoid.ball.NormalBall;
import com.uet.arkanoid.brick.BrickManager;
import com.uet.arkanoid.paddle.Paddle;
import com.uet.arkanoid.paddle.PaddleNormal;

public class GameScreen {
    private final Main game;
    private final SpriteBatch batch;
    private Texture background;

    // Các trình quản lý (Managers)
    private BallManager ballManager;
    private PaddleNormal paddle; // (Paddle có thể coi là một Manager)
    private BrickManager brickManager;
    private ScoreSystem scoreSystem;
    private Lives livesSystem;
    private PlayerStateManager playerStateManager;
    private boolean paused;
    private Texture spaceToLaunchTexture;
    private boolean waitingForLaunch = true;
    private float launchTextTimer = 0f;


    public GameScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
    }

    public void startNewGame() {
        background = new Texture(Gdx.files.internal("background.png"));
        paddle = new PaddleNormal((Gdx.graphics.getWidth() - 128) / 2f, 50);
        scoreSystem = new ScoreSystem(1080, 580);
        livesSystem = new Lives(1070, 189);
        brickManager = new BrickManager("Level1.tmx");
        spaceToLaunchTexture = new Texture(Gdx.files.internal("launching_text.png"));

        // 1. Khởi tạo các hệ thống UI
        scoreSystem = new ScoreSystem(50, Gdx.graphics.getHeight() - 50);
        livesSystem = new Lives(1070, 189);

        // 2. Khởi tạo Paddle
        paddle = new PaddleNormal((Gdx.graphics.getWidth() - 128) / 2f, 50);

        // 3. Khởi tạo BrickManager
        brickManager = new BrickManager("Level1.tmx");

        // 4. Khởi tạo BallManager
        Texture ballTexture = new Texture(Gdx.files.internal("ball.png"));
        float ballSpeed = 500;
        ball = new NormalBall((Gdx.graphics.getWidth() - 200) / 2f, paddle.getY() + 30, 10, ballSpeed, ballTexture);

        playerStateManager = new PlayerStateManager(ball, paddle, livesSystem, scoreSystem, game);

        scoreSystem.reset();
        // Vị trí reset bóng (theo Paddle)
        float resetX = paddle.getX() + paddle.getWidth() / 2;
        float resetY = paddle.getY() + paddle.getHeight() + 10; // ban kinh la 10gg

        ballManager = new BallManager(ballTexture, ballSpeed, resetX, resetY);

        // Tạo quả bóng đầu tiên và thêm vào Manager
        Ball initialBall = new NormalBall(resetX, resetY, 10, ballSpeed, ballTexture);
        initialBall.setActive(false); // Chờ phóng
        ballManager.addBall(initialBall);
    }

    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        float delta = Gdx.graphics.getDeltaTime();
        ball.handleInput();

        if (waitingForLaunch) {
            if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.SPACE)) {
                waitingForLaunch = false;
                ball.launch(90);
            }
        }

        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            game.pauseGame();
            setPaused(true);
            return;
        }
        // 1. Xử lý Input
        handleInput();

        // 2. Cập nhật (Update) các hệ thống
        paddle.update(delta);

        // BallManager cập nhật TẤT CẢ bóng và va chạm (gạch, paddle, tường, mất mạng)
        ballManager.update(delta, paddle, brickManager, scoreSystem, livesSystem);

        // BrickManager cập nhật vật phẩm rơi và va chạm (với paddle)
        brickManager.update(delta, paddle, livesSystem, ballManager);

        if (!paused) {
            scoreSystem.update(delta);
            livesSystem.update(delta);
            paddle.update(delta);
            playerStateManager.update(delta);

            if (ball.isActive()) {
                ball.update(delta);
                brickManager.checkCollision(ball, scoreSystem);
                paddle.checkCollision(ball);
                checkWallCollision();
            }
        }

        launchTextTimer += Gdx.graphics.getDeltaTime();

        // 3. Vẽ (Render)
        batch.begin();
        batch.draw(background, 0, 0);
        paddle.render(batch);
        brickManager.render(batch); // Vẽ gạch VÀ vật phẩm rơi
        ballManager.render(batch);  // Vẽ TẤT CẢ bóng
        scoreSystem.render(batch);
        livesSystem.render(batch);

        if (!paused && ball.isWaitingForLaunch() && livesSystem.getCurrentLives() > 0) {

            // hieu ung
            float alpha = 0.5f + 0.5f * (float)Math.sin(launchTextTimer * 5f);

            batch.setColor(1f, 1f, 1f, alpha);

            float x = (Gdx.graphics.getWidth() - spaceToLaunchTexture.getWidth()) / 2f - 60;
            float y = Gdx.graphics.getHeight() * 0.25f - 350;
            batch.draw(spaceToLaunchTexture, x, y);

            batch.setColor(1f, 1f, 1f, 1f);
        }

        batch.end();

        // (Kiểm tra game over)
        if (livesSystem.getCurrentLives() <= 0) {
            // game.returnToMenu();
        }
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    private void checkWallCollision() {
        if (ball.getX() - ball.getRadius() <= 32 || ball.getX() + ball.getRadius() >= 1008)
            ball.reverseX();
        if (ball.getY() + ball.getRadius() >= 778)
            ball.reverseY();
        if (brickManager.isAllCleared()) {
            game.returnToMenu();
    /**
     * Xử lý input (ví dụ: phóng bóng)
     */
    private void handleInput() {
        // Phóng bóng bằng phím Space
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            ballManager.launchFirstBall();
        }
        if (livesSystem.getCurrentLives() == 0) {
            game.showGameOver(scoreSystem.getHighScore());
        }
    }

    // Các Getters để BrickManager truy cập
    public Lives getLivesSystem() {
        return livesSystem;
    }

    public BallManager getBallManager() {
        return ballManager;
    }

    public void dispose() {
        batch.dispose();
        background.dispose();
        ballManager.dispose();
        paddle.dispose();
        brickManager.dispose();
        scoreSystem.dispose();
        livesSystem.dispose();
        spaceToLaunchTexture.dispose();
    }
}
