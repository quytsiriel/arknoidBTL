package com.uet.arkanoid.ball;

import com.badlogic.gdx.graphics.Texture;
import com.uet.arkanoid.brick.Brick;

/**
 * Lớp con triển khai quả bóng tiêu chuẩn.
 */
public class NormalBall extends Ball {

    public NormalBall(float x, float y, float radius, float speed, String texturePath) {
        super(x, y, radius, speed, new Texture(texturePath));
    }

    public NormalBall(float x, float y, float radius, float speed, Texture texture) {
        super(x, y, radius, speed, texture);
    }
}
