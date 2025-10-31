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

    public GameScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
    }

    public void startNewGame() {
        background = new Texture(Gdx.files.internal("background.png"));

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
        // Vị trí reset bóng (theo Paddle)
        float resetX = paddle.getX() + paddle.getWidth() / 2;
        float resetY = paddle.getY() + paddle.getHeight() + 10; // 10 là bán kính bóng

        ballManager = new BallManager(ballTexture, ballSpeed, resetX, resetY);

        // Tạo quả bóng đầu tiên và thêm vào Manager
        Ball initialBall = new NormalBall(resetX, resetY, 10, ballSpeed, ballTexture);
        initialBall.setActive(false); // Chờ phóng
        ballManager.addBall(initialBall);
    }

    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        float delta = Gdx.graphics.getDeltaTime();

        // 1. Xử lý Input
        handleInput();

        // 2. Cập nhật (Update) các hệ thống
        paddle.update(delta);

        // BallManager cập nhật TẤT CẢ bóng và va chạm (gạch, paddle, tường, mất mạng)
        ballManager.update(delta, paddle, brickManager, scoreSystem, livesSystem);

        // BrickManager cập nhật vật phẩm rơi và va chạm (với paddle)
        brickManager.update(delta, paddle, livesSystem, ballManager);

        scoreSystem.update(delta);
        livesSystem.update(delta);

        // 3. Vẽ (Render)
        batch.begin();
        batch.draw(background, 0, 0);
        paddle.render(batch);
        brickManager.render(batch); // Vẽ gạch VÀ vật phẩm rơi
        ballManager.render(batch);  // Vẽ TẤT CẢ bóng
        scoreSystem.render(batch);
        livesSystem.render(batch);
        batch.end();

        // (Kiểm tra game over)
        if (livesSystem.getCurrentLives() <= 0) {
            // game.returnToMenu();
        }
    }

    /**
     * Xử lý input (ví dụ: phóng bóng)
     */
    private void handleInput() {
        // Phóng bóng bằng phím Space
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            ballManager.launchFirstBall();
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
    }
}
