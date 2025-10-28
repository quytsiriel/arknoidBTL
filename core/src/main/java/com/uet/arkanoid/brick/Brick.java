package com.uet.arkanoid.brick;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Brick {
    private static Texture[] allTextures;
    public Texture brickTexture;
    private Rectangle brickRectangle;
    int type;
    private boolean deleted = false;
    int hit;
    BrickManager bM;



    public Brick(int t, int x, int y, int h, int w,Texture Brick, int hit) {
        this.type = t;
        this.brickTexture = Brick;
        this.brickRectangle = new Rectangle(x, y, w, h);
        this.hit = hit;
        this.deleted = false;
    }

    public void render(SpriteBatch sb) {
        if (!deleted) {
            sb.draw(brickTexture, brickRectangle.x, brickRectangle.y, brickRectangle.width, brickRectangle.height);
        }
    }
    public static void setAllTextures(Texture[] textures) {
        allTextures = textures;
    }

    public int destroy() {
        if (deleted) return -1; // tránh xử lý lại

        //  Nếu là gạch đặc biệt (power-up)
        if (type == 4 || type == 5 || type == 6) {
            deleted = true;
            return type; // Trả về mã loại đặc biệt
        }

        //  Gạch thường có nhiều "hit"
        hit--;
        if (hit > 0) {
            // Cập nhật texture tương ứng (nếu còn texture)
            if (type > 0) {
                type--;
                brickTexture = allTextures[type];
            }
            return -1; // chưa vỡ hẳn
        } else {
            deleted = true;
            return type; // gạch thường vỡ hẳn
        }

    }

    public boolean isDeleted() {
        return deleted;
    }


    public void setType(int i) {
        type = i;
    }

    public void setHit(int i) {
        hit = i;
    }

    public int getHit() {
        return hit;
    }

    public Rectangle getBrickRectangle() {
        return brickRectangle;
    }

    public int getType() {
        return type;
    }

    public float getX() {
        return brickRectangle.x;
    }
    public float getY() {
        return brickRectangle.y;
    }

}
