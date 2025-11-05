package com.uet.arkanoid.powerup;

import com.badlogic.gdx.graphics.Texture;
import com.uet.arkanoid.paddle.PaddleNormal;

public class ExpandPaddlePowerUp extends PowerUp {

    public ExpandPaddlePowerUp(Texture texture, float x, float y, float width, float height) {
        super(texture, x, y, width, height, 3); // Type 3 = Expand
    }

    /**
     * Hiệu ứng với paddle normal.
     */
    @Override
    public void applyEffect(Object receiver) {
        if (receiver instanceof PaddleNormal normalPaddle) {
            normalPaddle.expand();
        }
    }
}
