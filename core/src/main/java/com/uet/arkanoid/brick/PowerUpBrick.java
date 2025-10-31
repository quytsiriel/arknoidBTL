package com.uet.arkanoid.brick;

import com.badlogic.gdx.graphics.Texture;

/**
 * Lớp GẠCH (Brick) có chứa PowerUp.
 * Chỉ chịu trách nhiệm trả về 'powerType' (1, 2, hoặc 3) khi bị phá hủy.
 */
public class PowerUpBrick extends Brick {

    // powerType (1, 2, 3) tương ứng với loại PowerUp nó sẽ sinh ra
    private final int powerType;

    public PowerUpBrick(Texture texture, float x, float y, float width, float height, int powerType) {
        super(texture, x, y, width, height);
        this.powerType = powerType;
    }

    /**
     * Khi bị phá hủy, trả về mã PowerUp (1, 2, hoặc 3).
     * @return Mã loại PowerUp.
     */
    @Override
    public int destroy() {
        if (!deleted) {
            deleted = true;
            return powerType; // Trả về mã power-up (1, 2, hoặc 3)
        }
        return -1;
    }
}
