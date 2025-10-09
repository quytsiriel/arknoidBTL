package com.uet.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Panel {

    private Texture background;

    public Panel() {
        background = new Texture(Gdx.files.internal("background.png"));
    }

    public void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(background, 0, 0);
        batch.end();
    }

    public void dispose() {
        background.dispose();
    }
}
