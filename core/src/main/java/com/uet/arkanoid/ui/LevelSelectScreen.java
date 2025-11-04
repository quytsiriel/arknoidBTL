package com.uet.arkanoid.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.uet.arkanoid.Main;

public class LevelSelectScreen {
    private final Main game;
    private final SpriteBatch batch;
    private final Texture level1ButtonTexture;
    private final Texture level2ButtonTexture;
    private final Texture level3ButtonTexture;
    private final Texture level4ButtonTexture;
    private final Texture Background;


    // Vùng nhấn cho các nút
    private final Rectangle level1Bounds;
    private final Rectangle level2Bounds;
    private final Rectangle level3Bounds;
    private final Rectangle level4Bounds;

    public LevelSelectScreen(Main game) {
        this.game = game;
        this.batch = new SpriteBatch();

        // Tải asset cho màn hình này
        level1ButtonTexture = new Texture(Gdx.files.internal("level1.png"));
        level2ButtonTexture = new Texture(Gdx.files.internal("level2.png"));
        level3ButtonTexture = new Texture(Gdx.files.internal("level3.png"));
        level4ButtonTexture = new Texture(Gdx.files.internal("level4.png"));
        Background = new Texture(Gdx.files.internal("menu_background3.png"));


        float buttonX = (Gdx.graphics.getWidth() - level1ButtonTexture.getWidth()) / 2f;
        level1Bounds = new Rectangle(300, 450, level1ButtonTexture.getWidth(), level1ButtonTexture.getHeight());
        level2Bounds = new Rectangle(650, 450, level2ButtonTexture.getWidth(), level2ButtonTexture.getHeight());
        level3Bounds = new Rectangle(300, 200, level3ButtonTexture.getWidth(), level3ButtonTexture.getHeight());
        level4Bounds = new Rectangle(650, 200, level4ButtonTexture.getWidth(), level4ButtonTexture.getHeight());


    }

    public void render() {
        // Xử lý input
        handleInput();

        // Vẽ

        batch.begin();
        // Vẽ các nút
        batch.draw(Background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(level1ButtonTexture, level1Bounds.x, level1Bounds.y);
        batch.draw(level2ButtonTexture, level2Bounds.x, level2Bounds.y);
        batch.draw(level3ButtonTexture, level3Bounds.x, level3Bounds.y);
        batch.draw(level4ButtonTexture, level4Bounds.x, level4Bounds.y);
        batch.end();
    }

    private void handleInput() {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            // Lấy tọa độ chuột (libGDX có gốc (0,0) ở dưới cùng bên trái)
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY();
            int player = game.getCurrentMode();

            int playerMode = game.getCurrentMode();

            String selectedLevel = null;

            // 1. Xác định xem level nào được nhấn
            if (level1Bounds.contains(x, y)) {
                selectedLevel = "Level1.tmx";
            } else if (level2Bounds.contains(x, y)) {
                selectedLevel = "Level2.tmx";
            } else if (level3Bounds.contains(x, y)) {
                selectedLevel = "Level3.tmx";
            } else if (level4Bounds.contains(x, y)) {
                selectedLevel = "Level4.tmx";
            }

            // 2. Nếu một level đã được chọn, hãy bắt đầu game
            if (selectedLevel != null) {
                if (playerMode == 1) {
                    game.startGame(selectedLevel);
                } else {
                    game.startTwoPlayerGame(selectedLevel);
                }
            }
        }
    }

    public void dispose() {
        batch.dispose();
        level1ButtonTexture.dispose();
        level2ButtonTexture.dispose();
    }
}
