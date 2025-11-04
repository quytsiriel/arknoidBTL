package com.uet.arkanoid.powerup;

import com.badlogic.gdx.graphics.Texture;
import com.uet.arkanoid.ball.BallManager;

public class MultiBallPowerUp extends PowerUp {

    public MultiBallPowerUp(Texture texture, float x, float y, float width, float height) {
        super(texture, x, y, width, height, 2); // Type 2 = MultiBall
    }

    /**
     * Áp dụng hiệu ứng: Gọi BallManager để nhân TẤT CẢ các bóng đang bay.
     */
    @Override
    public void applyEffect(Object receiver) {
        if (receiver instanceof BallManager ballManager) {
            if (ballManager.isBallInPlay()) {
                ballManager.multiplyAllActiveBalls();
            }
        }
    }
}
