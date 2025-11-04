package com.uet.arkanoid.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class Lives implements Disposable {
    private int currentLives;
    private final int maxLives;
    private final int initialLives;

    private Vector2 position;

    private Texture heartTexture;
    private final float heartSize = 42f;
    private final float heartSpacing = 50f;

    private float loseLifeTimer = 0f;
    private final float LOSE_LIFE_ANIMATION_TIME = 0.5f;
    private boolean isAnimatingLoss = false;
    private int animatingHeartIndex = -1;


    public Lives(float x, float y) {
        this(3, x, y);
    }

    public Lives(int initialLives, float x, float y) {
        this.initialLives = initialLives;
        this.currentLives = initialLives;
        this.maxLives = initialLives + 2;
        this.position = new Vector2(x, y);

        heartTexture = new Texture(Gdx.files.internal("lives.png"));
    }

    public void update(float delta) {
        if (isAnimatingLoss) {
            loseLifeTimer += delta;
            if (loseLifeTimer >= LOSE_LIFE_ANIMATION_TIME) {
                isAnimatingLoss = false;
                loseLifeTimer = 0f;
                animatingHeartIndex = -1;
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < currentLives; i++) {
            float x = position.x + (i * heartSpacing);
            float y = position.y;
            batch.draw(heartTexture, x, y, heartSize, heartSize);
        }

        if (isAnimatingLoss && animatingHeartIndex >= 0) {
            float progress = loseLifeTimer / LOSE_LIFE_ANIMATION_TIME;
            if (progress > 1f) progress = 1f;

            float alpha = 1f - progress;
            float scale = 1f + progress * 0.5f;
            float x = position.x + (animatingHeartIndex * heartSpacing);
            float y = position.y;
            float offset = (heartSize * (scale - 1f)) / 2f;

            batch.setColor(1, 1, 1, alpha);
            batch.draw(heartTexture,
                x - offset, y - offset,
                heartSize * scale, heartSize * scale);
        }

        batch.setColor(1, 1, 1, 1f);
    }

    public boolean loseLife() {
        if (currentLives > 0) {
            animatingHeartIndex = currentLives - 1;
            currentLives--;
            isAnimatingLoss = true;
            loseLifeTimer = 0f;
            return currentLives > 0;
        }
        return false;
    }

    public boolean addLife() {
        if (currentLives < maxLives) {
            if(currentLives == 3) {
                currentLives++;
                return true;
            }
        }
        return false;
    }

    public int getCurrentLives() {
        return currentLives;
    }

    public boolean hasLives() {
        return currentLives > 0;
    }

    public boolean isMaxLives() {
        return currentLives >= maxLives;
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void reset() {
        currentLives = initialLives;
        isAnimatingLoss = false;
        loseLifeTimer = 0f;
    }

    @Override
    public void dispose() {
        if (heartTexture != null) heartTexture.dispose();
    }
}
