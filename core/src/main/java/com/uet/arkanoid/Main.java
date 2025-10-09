package com.uet.arkanoid;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;


/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {

    public Texture background;
    public BitmapFont font;
    BrickManager brickManager;
    public SpriteBatch batch;


    //Paddle
    private Paddle paddle;
    // ScoreSystem && Lives
    private ScoreSystem scoreSystem;
    private Lives livesSystem;


    private int score = 0;
    private int lives = 3;

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("background.png"));


        paddle = new Paddle(
            (Gdx.graphics.getWidth() - 128) / 2f,  // căn giữa theo chiều ngang
            50                                     // cách đáy 50px
        );

        scoreSystem = new ScoreSystem(50, Gdx.graphics.getHeight() - 50);
        livesSystem = new Lives(Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 70);

        // khởi tạo font
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2);
        background = new Texture(Gdx.files.internal("background.png"));
        brickManager = new BrickManager("Level1.tmx");
    }

    @Override
    public void render() {
        // screen clear
        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //update
        float delta = Gdx.graphics.getDeltaTime();

        // update paddle
        paddle.update(delta);

        // update score, lives
        scoreSystem.update(delta);
        livesSystem.update(delta);


        batch.begin();
        batch.draw(background, 0, 0);

        // vẽ paddle
        paddle.render(batch);

        // vẽ điểm, vẽ mạng sống
        scoreSystem.render(batch);
        livesSystem.render(batch);


        ball.update(delta);

        //Hàm check va chạm - truyền class ball vào
        brickManager.checkCollision(ball);


        batch.begin();
        batch.draw(background,0,0);
        brickManager.render(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        font.dispose();
    }

    @Override
    public void resize(int width, int height) {

    }
}
