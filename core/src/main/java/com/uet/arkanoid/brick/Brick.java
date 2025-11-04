package com.uet.arkanoid.brick;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Brick {
    protected Texture texture;
    protected Rectangle bounds;
    protected boolean deleted = false;

    public Brick(Texture texture, float x, float y, float width, float height) {
        this.texture = texture;
        this.bounds = new Rectangle(x, y, width, height);
    }
    public Brick(int type,Texture texture, float x, float y, float width, float height) {
        this.texture = texture;
        this.bounds = new Rectangle(x, y, width, height);
    }

    public void render(SpriteBatch batch) {
        if (!deleted) {
            batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public float getX() { return bounds.x; }

    public float getY() { return bounds.y; }

    public float getWidth() {
        return getBounds().getWidth();
    }

    public float getHeight() {
        return getBounds().getHeight();
    }

    public boolean isDeleted() { return deleted; }

    public abstract int destroy();
}
