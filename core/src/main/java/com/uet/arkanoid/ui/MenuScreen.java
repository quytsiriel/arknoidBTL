package com.uet.arkanoid.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.uet.arkanoid.Main;

public class MenuScreen {
    private final Main game;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private Music BackgroundSound;

    private Texture menuBackground;
    private Texture button1PlayerTexture;
    private Texture button2PlayerTexture;
    private Texture buttonDifficultyTexture;

    private Rectangle button1PlayerBounds;
    private Rectangle button2PlayerBounds;
    private SoundManager sound;



    public MenuScreen(Main game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.font.setColor(Color.WHITE);
        this.font.getData().setScale(2);
        loadMenuAssets();
    }

    public void render() {
        handleInput();

        batch.begin();
        batch.draw(menuBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(button1PlayerTexture, button1PlayerBounds.x, button1PlayerBounds.y, button1PlayerBounds.width, button1PlayerBounds.height);
        batch.draw(button2PlayerTexture, button2PlayerBounds.x, button2PlayerBounds.y, button2PlayerBounds.width, button2PlayerBounds.height);
        batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            touchPos.y = Gdx.graphics.getHeight() - touchPos.y;

            if (button1PlayerBounds.contains(touchPos.x, touchPos.y)) {
                sound.stopBackgroundMusic();
                sound.playGameMusic();
                game.startGame();

            }
            if (button2PlayerBounds.contains(touchPos.x, touchPos.y)) {
                // Tạm thời dùng chung
                game.startGame();
            }

        }
    }

    private void loadMenuAssets() {
        menuBackground = new Texture(Gdx.files.internal("menu_background2.png"));
        button1PlayerTexture = new Texture(Gdx.files.internal("singlePlayer_button.png"));
        button2PlayerTexture = new Texture(Gdx.files.internal("multiPlayer_button.png"));
        buttonDifficultyTexture = new Texture(Gdx.files.internal("Difficult_button.png"));

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        float bw = 300, bh = 100;
        float bx = (w - bw) / 2;

        button1PlayerBounds = new Rectangle(bx, 362, bw, bh);
        button2PlayerBounds = new Rectangle(bx+ 7, 240, bw-10, bh-10);
        sound = new SoundManager();
        sound.playBackgroundMusic();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
        menuBackground.dispose();
        button1PlayerTexture.dispose();
        button2PlayerTexture.dispose();
        buttonDifficultyTexture.dispose();


    }
}
