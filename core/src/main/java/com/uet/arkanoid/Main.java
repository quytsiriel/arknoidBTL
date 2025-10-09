package com.uet.arkanoid;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {

    private Texture background;
    private SpriteBatch batch;
    private BitmapFont font;

    private BrickManager brickManager;
    private Ball ball;
    private Paddle paddle;
    private ScoreSystem scoreSystem;
    private Lives livesSystem;

    private int score = 0;
    private int lives = 3;

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("background.png"));

        // Paddle
        paddle = new Paddle(
            (Gdx.graphics.getWidth() - 128) / 2f,
            50
        );

        // Score & Lives
        scoreSystem = new ScoreSystem(50, Gdx.graphics.getHeight() - 50);
        livesSystem = new Lives(Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 70);

        // Font
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2);

        // Gạch
        brickManager = new BrickManager("Level1.tmx");

        // ✅ Bóng
        Texture ballTexture = new Texture(Gdx.files.internal("ball.png"));
        float startX = Gdx.graphics.getWidth() / 2f;
        float startY = paddle.getY() + 20;
        ball = new Ball(startX, startY, 10, 300, ballTexture);
        ball.launch(60); // bắn lên 60 độ
    }

    @Override
    public void render() {
        // clear màn hình
        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float delta = Gdx.graphics.getDeltaTime();

        // update logic
        paddle.update(delta);
        ball.update(delta);
        brickManager.checkCollision(ball);
        checkWallCollision();

        scoreSystem.update(delta);
        livesSystem.update(delta);

        // render
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
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        if (ball.getX() - ball.getRadius() <= 0 || ball.getX() + ball.getRadius() >= screenWidth)
            ball.reverseX();

        if (ball.getY() + ball.getRadius() >= screenHeight)
            ball.reverseY();

        if (ball.getY() < -ball.getRadius()) {
            ball.reset(screenWidth / 2f, paddle.getY() + 20);
            ball.launch(60);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        font.dispose();
        ball.dispose();
    }
}
