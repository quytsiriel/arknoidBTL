package com.uet.arkanoid.brick;

import com.badlogic.gdx.graphics.Texture;

public class PowerUpBrick extends Brick {
    private int powerType;

    public PowerUpBrick(Texture texture, float x, float y, float width, float height, int powerType) {
        super(texture, x, y, width, height);
        this.powerType = powerType;
    }

    @Override
    public int destroy() {
        if (!deleted) {
            deleted = true;
            return powerType; // trả về mã power-up
        }
        return -1;
    }
}
