package com.uet.arkanoid.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import java.util.ArrayList;

public class ScoreSystem implements Disposable {
    private int currentScore;
    private int highScore;
    private int combo;
    private int maxCombo;
    private final int COMBO_TIMEOUT = 2000;
    private long lastHitTime;

    // Vị trí hiển thị
    private Vector2 scorePosition;
    private Vector2 comboPosition;

    // Graphics
    private BitmapFont scoreFont;
    private BitmapFont comboFont;
    private BitmapFont titleFont;
    private GlyphLayout layout;

    // Animation
    private float comboScale = 1.0f;
    private float comboAlpha = 1.0f;
    private int displayScore = 0; // Điểm hiển thị (tăng dần)
    private final float SCORE_ANIMATION_SPEED = 50f;

    // Popup điểm
    private static class ScorePopup {
        String text;
        Vector2 position;
        float lifetime;
        float alpha;
        Color color;

        ScorePopup(String text, Vector2 pos, Color color) {
            this.text = text;
            this.position = new Vector2(pos);
            this.lifetime = 0f;
            this.alpha = 1.0f;
            this.color = new Color(color);
        }
    }

    private ArrayList<ScorePopup> scorePopups;

    /**
     * Constructor
     * scoreX Vị trí x hiển thị điểm ,scoreY Vị trí y hiển thị điểm
     */
    public ScoreSystem(float scoreX, float scoreY) {
        this.currentScore = 0;
        this.highScore = 0;
        this.combo = 0;
        this.maxCombo = 0;
        this.lastHitTime = 0;
        this.scorePosition = new Vector2(scoreX, scoreY);
        this.comboPosition = new Vector2(scoreX, scoreY - 40);
        this.scorePopups = new ArrayList<>();


        initFonts();

        layout = new GlyphLayout();
    }

    /**
     * Khởi tạo các font
     */
    private void initFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Pixeboy-z8XGD.ttf"));

        // Font cho điểm số chính
        FreeTypeFontGenerator.FreeTypeFontParameter scoreParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        scoreParam.size = 32;
        scoreParam.color = Color.WHITE;
        scoreFont = generator.generateFont(scoreParam);

        // Font cho combo
        FreeTypeFontGenerator.FreeTypeFontParameter comboParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        comboParam.size = 28;
        comboParam.color = Color.YELLOW;
        comboFont = generator.generateFont(comboParam);

        // Font cho tiêu đề ("SCORE", "HIGH")
        FreeTypeFontGenerator.FreeTypeFontParameter titleParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        titleParam.size = 26;
        titleParam.color = Color.YELLOW;
        titleFont = generator.generateFont(titleParam);

        generator.dispose();
    }


    /**
     * Cập nhật logic
     * @param delta Delta time
     */
    public void update(float delta) {
        // Animate điểm số tăng dần
        if (displayScore < currentScore) {
            displayScore += (int)(SCORE_ANIMATION_SPEED * delta *
                Math.max(1, (currentScore - displayScore) / 100));
            if (displayScore > currentScore) {
                displayScore = currentScore;
            }
        }

        // Animate combo
        int currentCombo = getCombo();
        if (currentCombo > 1) {
            comboScale = 1.0f + (float)Math.sin(Gdx.graphics.getFrameId() * 0.1f) * 0.1f;
            comboAlpha = 1.0f;
        } else {
            comboScale = 1.0f;
            comboAlpha = Math.max(0, comboAlpha - delta * 2);
        }

        // Cập nhật popup điểm
        for (int i = scorePopups.size() - 1; i >= 0; i--) {
            ScorePopup popup = scorePopups.get(i);
            popup.lifetime += delta;
            popup.position.y += 50 * delta; // Di chuyển lên
            popup.alpha = Math.max(0, 1.0f - popup.lifetime / 1.5f);

            if (popup.lifetime >= 1.5f) {
                scorePopups.remove(i);
            }
        }
    }

    /**
     * Render điểm số
     * @param batch SpriteBatch để vẽ
     */
    public void render(SpriteBatch batch) {
        titleFont.draw(batch, "SCORE", scorePosition.x, scorePosition.y + 30);

        String scoreText = getFormattedScore(displayScore);
        scoreFont.draw(batch, scoreText, scorePosition.x, scorePosition.y);

        titleFont.draw(batch, "HIGH", scorePosition.x , scorePosition.y - 30);
        scoreFont.draw(batch, getFormattedScore(highScore),
            scorePosition.x , scorePosition.y - 60);

        int currentCombo = getCombo();
        if (currentCombo > 1 || comboAlpha > 0) {
            Color originalColor = comboFont.getColor().cpy();
            Color comboColor = getComboColor(currentCombo);
            comboColor.a = comboAlpha;
            comboFont.setColor(comboColor);

            float originalScaleX = comboFont.getData().scaleX;
            float originalScaleY = comboFont.getData().scaleY;
            comboFont.getData().setScale(1.5f * comboScale);

            String comboText = currentCombo + "x COMBO!";
            layout.setText(comboFont, comboText);
            comboFont.draw(batch, comboText,
                comboPosition.x - layout.width / 2 + 60,
                comboPosition.y - 80);

            comboFont.getData().setScale(originalScaleX, originalScaleY);
            comboFont.setColor(originalColor);
        }

        for (ScorePopup popup : scorePopups) {
            Color originalColor = scoreFont.getColor().cpy();
            popup.color.a = popup.alpha;
            scoreFont.setColor(popup.color);
            scoreFont.draw(batch, popup.text, popup.position.x, popup.position.y);
            scoreFont.setColor(originalColor);
        }
    }

    /**
     * Lấy màu combo dựa vào độ lớn
     */
    private Color getComboColor(int combo) {
        if (combo >= 10) return Color.PURPLE;
        if (combo >= 7) return Color.RED;
        if (combo >= 5) return Color.ORANGE;
        if (combo >= 3) return Color.YELLOW;
        return Color.WHITE;
    }

    /**
     * Thêm điểm khi phá gạch
     */
    public void addScore(int brickValue, Vector2 position) {
        long currentTime = System.currentTimeMillis();

        // Kiểm tra combo
        if (currentTime - lastHitTime <= COMBO_TIMEOUT && combo > 0) {
            combo++;
        } else {
            combo = 1;
        }

        if (combo > maxCombo) {
            maxCombo = combo;
        }

        // Tính điểm với combo
        int multiplier = getComboMultiplier();
        int scoreToAdd = brickValue * multiplier;
        currentScore += scoreToAdd;

        if (currentScore > highScore) {
            highScore = currentScore;
        }

        // Tạo popup điểm
        if (position != null) {
            String popupText = "+" + scoreToAdd;
            if (multiplier > 1) {
                popupText += " (x" + multiplier + ")";
            }
            scorePopups.add(new ScorePopup(popupText, position, getComboColor(combo)));
        }

        lastHitTime = currentTime;
    }

    /**
     * Tính hệ số nhân combo
     */
    private int getComboMultiplier() {
        if (combo >= 10) return 5;
        if (combo >= 7) return 4;
        if (combo >= 5) return 3;
        if (combo >= 3) return 2;
        return 1;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getHighScore() {
        return highScore;
    }

    public int getCombo() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastHitTime > COMBO_TIMEOUT) {
            combo = 0;
        }
        return combo;
    }

    public int getMaxCombo() {
        return maxCombo;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    private String getFormattedScore(int score) {
        return String.format("%08d", score);
    }

    public String getFormattedScore() {
        return getFormattedScore(currentScore);
    }

    public String getFormattedHighScore() {
        return getFormattedScore(highScore);
    }

    @Override
    public void dispose() {
        if (scoreFont != null) scoreFont.dispose();
        if (comboFont != null) comboFont.dispose();
        if (titleFont != null) titleFont.dispose();
    }
}
