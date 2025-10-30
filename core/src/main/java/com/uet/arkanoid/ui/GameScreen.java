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

        Texture ballTexture = new Texture(Gdx.files.internal("ball.png"));
        float ballSpeed = 500;
        ball = new NormalBall((Gdx.graphics.getWidth() - 200) / 2f, paddle.getY() + 30, 10, ballSpeed, ballTexture);

        playerStateManager = new PlayerStateManager(ball, paddle, livesSystem, game);
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

        batch.begin();
        batch.draw(background, 0, 0);
        paddle.render(batch);
        brickManager.render(batch);
        ball.render(batch);
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
        spaceToLaunchTexture.dispose();
    }
}
