package com.uet.arkanoid.brick;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.uet.arkanoid.ball.Ball;
import com.uet.arkanoid.level.LevelLoader;
import com.uet.arkanoid.ui.ScoreSystem;

import java.util.ArrayList;

public class BrickManager {

    private ArrayList<Brick> bricks;
    private Texture[] brickTextures;
    private boolean allCleared = false;

    public BrickManager(String mapPath) {
        // Load texture
        brickTextures = new Texture[]{
            new Texture("Brick0.png"),
            new Texture("Brick1.png"),
            new Texture("Brick2.png"),
            new Texture("Brick3.png"),
            new Texture("Brick4.png"),
            new Texture("Brick5.png"),
            new Texture("Brick6.png")
        };
        NormalBrick.setAllTextures(brickTextures);

        // Load bricks từ level
        bricks = LevelLoader.loadLevel(mapPath, brickTextures);
    }

    public void render(SpriteBatch batch) {
        for (Brick b : bricks) b.render(batch);
    }

    public boolean isAllCleared() {
        for (Brick b : bricks)
            if (!b.isDeleted()) return false;
        return true;
    }

    public int checkCollision(Ball ball, ScoreSystem scoreSystem) {
        for (Brick brick : bricks) {
            if (!brick.isDeleted() && brick.getBounds().overlaps(ball.getBounds())) {
                int brickType = brick.destroy();
                ball.Nay(brick);

                int scoreValue = (brick instanceof PowerUpBrick) ? 200 : 100;
                scoreSystem.addScore(scoreValue, new Vector2(brick.getX(), brick.getY()));

                if (isAllCleared()) allCleared = true;
                return brickType;
            }
        }
        return -1;
    }

    public boolean isGameOver() {
        return allCleared;
    }

    public void dispose() {
        for (Texture t : brickTextures) t.dispose();
    }
}
