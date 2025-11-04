package com.uet.arkanoid.powerup;

import com.badlogic.gdx.graphics.Texture;
import com.uet.arkanoid.ui.Lives;

public class ExtraLifePowerUp extends PowerUp {

    public ExtraLifePowerUp(Texture texture, float x, float y, float width, float height) {
        super(texture, x, y, width, height, 1); // Type 1 = Extra Life
    }

    /**
     * Định nghĩa logic hiệu ứng: Chỉ áp dụng nếu đối tượng nhận là 'Lives'.
     */
    @Override
    public void applyEffect(Object receiver) {
        if (receiver instanceof Lives livesInstance) {
            livesInstance.addLife();
        }
    }
}
