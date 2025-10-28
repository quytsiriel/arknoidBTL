package com.uet.arkanoid.brick;

import com.badlogic.gdx.graphics.Texture;

public class NormalBrick extends Brick {
    private int hitPoints;
    private int type;
    private static Texture[] allTextures;

    public NormalBrick(int type, Texture texture, float x, float y, float width, float height, int hitPoints) {
        super(texture, x, y, width, height);
        this.hitPoints = hitPoints;
        this.type = type;
    }

    @Override
    public int destroy() {
        if (deleted) return -1;

        hitPoints--;
        if (hitPoints > 0) {
            // ↓↓↓ đổi texture của lớp cha ↓↓↓
            if (type > 0 && allTextures != null) {
                type--;
                this.texture = allTextures[type];
            }
            return -1; // chưa vỡ hẳn
        } else {
            deleted = true;
            return type; // gạch vỡ hẳn
        }
    }

    // Gọi 1 lần trong BrickManager sau khi load ảnh
    public static void setAllTextures(Texture[] textures) {
        allTextures = textures;
    }
}
