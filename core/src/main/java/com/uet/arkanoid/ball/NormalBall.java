package com.uet.arkanoid.ball;

import com.badlogic.gdx.graphics.Texture;
import com.uet.arkanoid.brick.Brick;

/**
 * Lớp con triển khai quả bóng tiêu chuẩn.
 * BẮT BUỘC triển khai phương thức handleBrickCollision()
 */
public class NormalBall extends Ball {

    public NormalBall(float x, float y, float radius, float speed, String texturePath) {
        super(x, y, radius, speed, new Texture(texturePath));
    }

    public NormalBall(float x, float y, float radius, float speed, Texture texture) {
        super(x, y, radius, speed, texture);
    }

    @Override
    public void handleBrickCollision(Brick brick) {
        if (position.y > brick.getY() + 30f || position.y  < brick.getY()) {
            velocity.y *= -1;
        }
        else if(position.x <= brick.getX() || position.x  >= brick.getX() + 80f) {
            velocity.x *= -1;
        }
    }
}
