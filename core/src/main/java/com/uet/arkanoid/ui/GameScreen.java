package com.uet.arkanoid.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.uet.arkanoid.Main;
import com.uet.arkanoid.ball.Ball;
import com.uet.arkanoid.ball.NormalBall;
import com.uet.arkanoid.brick.BrickManager;
import com.uet.arkanoid.paddle.PaddleNormal;
import com.uet.arkanoid.paddle.PlayerStateManager;

public class GameScreen {
    private final Main game;
    private final SpriteBatch batch;
    private Texture background;
    private Ball ball;
    private PaddleNormal paddle;
    private BrickManager brickManager;
    private ScoreSystem scoreSystem;
    private Lives livesSystem;
    private PlayerStateManager playerStateManager;

    public GameScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
    }

    public void startNewGame() {
        background = new Texture(Gdx.files.internal("background.png"));
        paddle = new PaddleNormal((Gdx.graphics.getWidth() - 128) / 2f, 50);
        scoreSystem = new ScoreSystem(50, Gdx.graphics.getHeight() - 50);
        livesSystem = new Lives(1070, 189);
        brickManager = new BrickManager("Level1.tmx");


        Texture ballTexture = new Texture(Gdx.files.internal("ball.png"));
        float ballSpeed = 500;
        ball = new NormalBall((Gdx.graphics.getWidth() - 200)/ 2f, paddle.getY() + 30, 10, ballSpeed, ballTexture);

        playerStateManager = new PlayerStateManager(ball, paddle, livesSystem, game);
    }

    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        float delta = Gdx.graphics.getDeltaTime();

        paddle.update(delta);
        playerStateManager.update(delta);

        if (ball.isActive()) {
            ball.update(delta);
            brickManager.checkCollision(ball, scoreSystem);
            paddle.checkCollision(ball);
            checkWallCollision();
        }

        scoreSystem.update(delta);
        livesSystem.update(delta);

        batch.begin();
        batch.draw(background, 0, 0);
        paddle.render(batch);
        brickManager.render(batch);
        ball.render(batch);
        scoreSystem.render(batch);
        livesSystem.render(batch);
        batch.end();
    }

    private void checkWallCollision() {
        if (ball.getX() - ball.getRadius() <= 32 || ball.getX() + ball.getRadius() >= 1008)
            ball.reverseX();
        if (ball.getY() + ball.getRadius() >= 778)
            ball.reverseY();
        if (brickManager.isAllCleared()) {
            game.returnToMenu();
        }
    }

    public void dispose() {
        batch.dispose();
        background.dispose();
        ball.dispose();
        paddle.dispose();
        brickManager.dispose();
        scoreSystem.dispose();
        livesSystem.dispose();
    }
}
