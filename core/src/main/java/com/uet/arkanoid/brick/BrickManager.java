package com.uet.arkanoid.brick;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import com.uet.arkanoid.ball.Ball;
import com.uet.arkanoid.ball.BallManager;
import com.uet.arkanoid.level.LevelLoader;
import com.uet.arkanoid.paddle.Paddle;
import com.uet.arkanoid.ui.Lives;
import com.uet.arkanoid.ui.ScoreSystem;

// Import các lớp PowerUp cụ thể
import com.uet.arkanoid.powerup.PowerUp;
import com.uet.arkanoid.powerup.ExtraLifePowerUp;
import com.uet.arkanoid.powerup.MultiBallPowerUp;
import com.uet.arkanoid.powerup.ExpandPaddlePowerUp;

import java.util.ArrayList;

public class BrickManager {

    private final ArrayList<Brick> bricks;
    // Danh sách các vật phẩm PowerUp đang rơi
    private final ArrayList<PowerUp> fallingPowerUps;

    private final Texture[] brickTextures;
    private final Texture[] powerUpTextures;
    private final Sound hitSound;
    private boolean allCleared = false;

    public BrickManager(String mapPath) {
        // Load textures gạch (ví dụ: 0-6)
        brickTextures = new Texture[]{
            new Texture("Brick0.png"), new Texture("Brick1.png"), new Texture("Brick2.png"),
            new Texture("Brick3.png"), new Texture("Brick4.png"), new Texture("Brick5.png"),
            new Texture("Brick6.png")
        };
        NormalBrick.setAllTextures(brickTextures);

        // Load textures vật phẩm (powerup1.png, powerup2.png, powerup3.png)
        powerUpTextures = new Texture[]{
            new Texture("powerup1.png"), // [0] Dành cho ExtraLife (Loại gạch 4)
            new Texture("powerup2.png"), // [1] Dành cho MultiBall (Loại gạch 6)
            new Texture("powerup3.png")  // [2] Dành cho ExpandPaddle (Loại gạch 5)
        };

        hitSound = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));

        // Load bricks (bao gồm cả PowerUpBrick 4, 5, 6)
        bricks = LevelLoader.loadLevel(mapPath, brickTextures);

        // Khởi tạo danh sách vật phẩm rơi
        fallingPowerUps = new ArrayList<>();
    }

    /**
     * Cập nhật vật phẩm rơi: kiểm tra va chạm và áp dụng hiệu ứng.
     */
    public void update(float delta, Paddle paddle, Lives lives, BallManager ballManager) {

        // Lặp qua các vật phẩm đang rơi
        for (PowerUp powerUp : fallingPowerUps) {

            // 1. Chỉ cập nhật vị trí rơi
            powerUp.update(delta);

            // 2. BrickManager tự kiểm tra va chạm
            if (powerUp.checkCollision(paddle)) {

                // 3. Quyết định đối tượng nào nhận hiệu ứng
                switch (powerUp.getType()) {
                    case 1: // Extra Life
                        powerUp.applyEffect(lives);
                        break;
                    case 2: // MultiBall
                        powerUp.applyEffect(ballManager);
                        break;
                    case 3: // Expand Paddle
                        powerUp.applyEffect(paddle);
                        break;
                }

                powerUp.deactivate();
            }
        }

        // 4. Dọn dẹp danh sách
        fallingPowerUps.removeIf(p -> !p.isActive());
    }

    public void render(SpriteBatch batch) {
        // 1. Vẽ gạch
        for (Brick b : bricks) {
            b.render(batch);
        }
        // 2. Vẽ vật phẩm đang rơi
        for (PowerUp p : fallingPowerUps) {
            p.render(batch);
        }
    }

    /**
     * Kiểm tra va chạm của Bóng với Gạch.
     * ĐÃ SỬA LẠI LOGIC SINH POWERUP THEO ĐÚNG LOẠI GẠCH (4, 5, 6).
     */
    public void checkCollision(Ball ball, ScoreSystem scoreSystem) {
        for (Brick brick : bricks) {
            if (!brick.isDeleted() && brick.getBounds().overlaps(ball.getBounds())) {

                // brick.destroy() sẽ trả về loại gạch (ví dụ: 0, 1, 2, 3, 4, 5, 6)
                int brickType = brick.destroy();
                ball.Nay(brick);

                int scoreValue = (brick instanceof PowerUpBrick) ? 200 : 100;
                scoreSystem.addScore(scoreValue, new Vector2(brick.getX(), brick.getY()));
                hitSound.play(0.3f);
                switch (brickType) {

                    case 4: // Gạch loại 4 -> ExtraLife (dùng powerup1.png, type 1)
                        fallingPowerUps.add(new ExtraLifePowerUp(
                            powerUpTextures[0],
                            brick.getX() + brick.getWidth() / 2f - 12, brick.getY(), 25, 25
                        ));
                        break;

                    case 5: // Gạch loại 5 -> ExpandPaddle (dùng powerup3.png, type 3)
                        fallingPowerUps.add(new ExpandPaddlePowerUp(
                            powerUpTextures[2],
                            brick.getX() + brick.getWidth() / 2f - 15, brick.getY(), 40, 20
                        ));
                        break;

                    case 6: // Gạch loại 6 -> MultiBall (dùng powerup2.png, type 2)
                        fallingPowerUps.add(new MultiBallPowerUp(
                            powerUpTextures[1],
                            brick.getX() + brick.getWidth() / 2f - 20, brick.getY(), 30, 30
                        ));
                        break;

                    default:
                        break;
                }

                if (isAllCleared()) allCleared = true;
            }
        }
    }

    public boolean isAllCleared() {
        for (Brick b : bricks)
            if (!b.isDeleted()) return false;
        return true;
    }

    public void dispose() {
        for (Texture t : brickTextures) if (t != null) t.dispose();
        for (Texture t : powerUpTextures) if (t != null) t.dispose();
        if (hitSound != null) hitSound.dispose();
    }
}
