package com.uet.arkanoid.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.uet.arkanoid.Main;
import com.uet.arkanoid.ball.Ball;
import com.uet.arkanoid.brick.BrickManager;
import com.uet.arkanoid.paddle.Paddle;
import com.uet.arkanoid.paddle.PaddleCollision;

public class GameScreen {
    private final Main game;
    private final SpriteBatch batch;
    private Texture background;
    private Ball ball;
    private Paddle paddle;
    private BrickManager brickManager;
    private PaddleCollision paddleCollision;
    private ScoreSystem scoreSystem;
    private Lives livesSystem;

    public GameScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
    }

    public void startNewGame(Difficulty difficulty) {
        background = new Texture(Gdx.files.internal("background.png"));
        paddle = new Paddle((Gdx.graphics.getWidth() - 128) / 2f, 50);
        paddleCollision = new PaddleCollision();
        scoreSystem = new ScoreSystem(50, Gdx.graphics.getHeight() - 50);
        livesSystem = new Lives(Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 70);
        brickManager = new BrickManager("Level1.tmx");

        Texture ballTexture = new Texture(Gdx.files.internal("ball.png"));
        float ballSpeed = switch (difficulty) {
            case EASY -> 400;
            case HARD -> 650;
            default -> 500;
        };
        ball = new Ball(Gdx.graphics.getWidth() / 2f, paddle.getY() + 20, 10, ballSpeed, ballTexture);
        ball.launch(60);
    }

    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        float delta = Gdx.graphics.getDeltaTime();

        paddle.update(delta);
        ball.update(delta);
        brickManager.checkCollision(ball, scoreSystem);
        paddleCollision.checkCollision(paddle, ball);
        checkWallCollision();

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
        if (ball.getY() < -ball.getRadius()) {
            boolean stillAlive = livesSystem.loseLife();
            if (stillAlive) {
                ball.reset(paddle.getX() + 65f , paddle.getY() + 20);
                ball.launch(60);
            } else {
                game.returnToMenu();
            }
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
