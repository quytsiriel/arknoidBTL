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
import com.uet.arkanoid.paddle.PaddleNormal;

public class GameScreenTwoPlayer {
    private final Main game;
    private final SpriteBatch batch;
    private Texture background;

    private PaddleNormal paddle1;
    private PaddleNormal paddle2;
    private BrickManager brickManager;
    private BallManager ballManager;
    private ScoreSystem scoreSystem;
    private Lives livesSystem;

    private Texture spaceToLaunchTexture;
    private Ball ball;
    private boolean paused;

    public GameScreenTwoPlayer(Main game) {
        this.game = game;
        batch = new SpriteBatch();
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void startNewGame(String level) {
        background = new Texture(Gdx.files.internal("background.png"));
        spaceToLaunchTexture = new Texture(Gdx.files.internal("launching_text.png"));

        scoreSystem = new ScoreSystem(1080, 580);
        livesSystem = new Lives(1070, 189);

        // Paddle 1 (trái) - điều khiển bằng A/D
        paddle1 = new PaddleNormal(200, 50, Input.Keys.A, Input.Keys.D);

        // Paddle 2 (phải) - điều khiển bằng LEFT/RIGHT
        paddle2 = new PaddleNormal(Gdx.graphics.getWidth() - 328, 50, Input.Keys.LEFT, Input.Keys.RIGHT);

        // BrickManager và BallManager như cũ
        brickManager = new BrickManager(level);

        Texture ballTexture = new Texture(Gdx.files.internal("ball.png"));
        float ballSpeed = 500;

        float resetX = Gdx.graphics.getWidth() / 2f;
        float resetY = paddle1.getY() + paddle1.getHeight() + 10;

        ballManager = new BallManager(ballTexture, ballSpeed, game, resetX, resetY);
        Ball initialBall = new NormalBall(resetX, resetY, 10, ballSpeed, ballTexture);
        this.ball = initialBall;
        initialBall.setActive(false);
        ballManager.addBall(initialBall);
    }

    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.pauseGame();
            paused = true;
            return;
        }

        handleInput();

        paddle1.update(delta);
        paddle2.update(delta);

        // Update bóng và kiểm tra va chạm với CẢ HAI paddle
        ballManager.update(delta, paddle1, paddle2, brickManager, scoreSystem, livesSystem);

        brickManager.update(delta, paddle1, livesSystem, ballManager);
        brickManager.update(delta, paddle2, livesSystem, ballManager);

        scoreSystem.update(delta);
        livesSystem.update(delta);

        batch.begin();
        batch.draw(background, 0, 0);
        paddle1.render(batch);
        paddle2.render(batch);
        brickManager.render(batch);
        ballManager.render(batch);
        scoreSystem.render(batch);
        livesSystem.render(batch);


        batch.end();

        if (livesSystem.getCurrentLives() <= 0) {
            // game.returnToMenu();
        }
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            ballManager.launchFirstBall();
        }
    }

    public void dispose() {
        batch.dispose();
        background.dispose();
        ballManager.dispose();
        paddle1.dispose();
        paddle2.dispose();
        brickManager.dispose();
        scoreSystem.dispose();
        livesSystem.dispose();
        spaceToLaunchTexture.dispose();
    }
}
