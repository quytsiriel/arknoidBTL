package com.uet.arkanoid;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input; // MỚI: Thêm thư viện Input
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout; // MỚI: Thêm để căn giữa chữ
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {

    private enum GameState {
        MENU,
        PLAYING,
        PAUSED,
        GAME_OVER
    }
    private GameState gameState = GameState.MENU;

    private Texture menuBackground;
    private Texture button1PlayerTexture;
    private Texture button2PlayerTexture;
    private Texture buttonDifficultyTexture;
    private Rectangle button1PlayerBounds;
    private Rectangle button2PlayerBounds;
    private Rectangle buttonDifficultyBounds;
    private Difficulty currentDifficulty = Difficulty.NORMAL;

    private Texture background;
    private Texture pauseImage;
    private SpriteBatch batch;
    private BitmapFont font;

    private BrickManager brickManager;
    private Ball ball;
    private Paddle paddle;
    private PaddleCollision paddleCollision;
    private ScoreSystem scoreSystem;
    private Lives livesSystem;


    // MỚI: Dùng để căn giữa chữ "GAME PAUSED"
    private GlyphLayout pauseLayout;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2);
        pauseImage = new Texture(Gdx.files.internal("paused.png"));

        loadMenuAssets();
    }

    private void startGame() {
        background = new Texture(Gdx.files.internal("background.png"));
        paddle = new Paddle((Gdx.graphics.getWidth() - 128) / 2f, 50);
        paddleCollision = new PaddleCollision();
        scoreSystem = new ScoreSystem(50, Gdx.graphics.getHeight() - 50);
        livesSystem = new Lives(Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 70);
        brickManager = new BrickManager("Level1.tmx");

        Texture ballTexture = new Texture(Gdx.files.internal("ball.png"));
        float startX = Gdx.graphics.getWidth() / 2f;
        float startY = paddle.getY() + 20;

        float ballSpeed;
        switch (currentDifficulty) {
            case EASY:   ballSpeed = 400; break;
            case HARD:   ballSpeed = 650; break;
            default:     ballSpeed = 500; break;
        }
        ball = new Ball(startX, startY, 10, ballSpeed, ballTexture);
        ball.launch(60);
        gameState = GameState.PLAYING;
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // MỚI: Xử lý input cho việc Pause/Resume game
        handlePauseInput();

        switch (gameState) {
            case MENU:
                handleMenuInput();
                renderMenu();
                break;
            case PLAYING:
                // THAY ĐỔI: Tách việc cập nhật và vẽ ra
                updateGame(Gdx.graphics.getDeltaTime());
                drawGame();
                break;
            case PAUSED: // MỚI: Xử lý khi game đang tạm dừng
                drawGame(); // Vẽ lại khung hình game đang đứng im
                renderPauseImage(); // Vẽ chữ "GAME PAUSED" đè lên
                break;
            case GAME_OVER:
                break;
        }
    }

    // MỚI: Hàm xử lý input để bật/tắt pause
    private void handlePauseInput() {
        // Kiểm tra nếu phím ESC vừa được nhấn
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (gameState == GameState.PLAYING) {
                // Nếu đang chơi, chuyển sang PAUSED
                gameState = GameState.PAUSED;
            } else if (gameState == GameState.PAUSED) {
                // Nếu đang PAUSED, quay lại chơi
                gameState = GameState.PLAYING;
            }
        }
    }

    // MỚI: Hàm này chỉ chứa logic cập nhật trạng thái game
    private void updateGame(float delta) {
        paddle.update(delta);
        ball.update(delta);
        brickManager.checkCollision(ball, scoreSystem);
        paddleCollision.checkCollision(paddle, ball);
        checkWallCollision();
        scoreSystem.update(delta);
        livesSystem.update(delta);
    }

    // MỚI: Hàm này chỉ chứa logic vẽ các đối tượng game
    private void drawGame() {
        batch.begin();
        batch.draw(background, 0, 0);
        paddle.render(batch);
        brickManager.render(batch);
        ball.render(batch);
        scoreSystem.render(batch);
        livesSystem.render(batch);
        batch.end();
    }

    // MỚI: Hàm vẽ chữ "GAME PAUSED"
    private void renderPauseImage() {
        batch.begin();
        // Tính toán tọa độ để căn ảnh ra giữa màn hình
        float x = (Gdx.graphics.getWidth() - pauseImage.getWidth()) / 2f;
        float y = (Gdx.graphics.getHeight() - pauseImage.getHeight()) / 2f;
        batch.draw(pauseImage, x, y);
        batch.end();
    }

    private void renderMenu() {
        batch.begin();
        batch.draw(menuBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(button1PlayerTexture, button1PlayerBounds.x, button1PlayerBounds.y, button1PlayerBounds.width, button1PlayerBounds.height);
        batch.draw(button2PlayerTexture, button2PlayerBounds.x, button2PlayerBounds.y, button2PlayerBounds.width, button2PlayerBounds.height);
        batch.draw(buttonDifficultyTexture, buttonDifficultyBounds.x, buttonDifficultyBounds.y, buttonDifficultyBounds.width, buttonDifficultyBounds.height);
        String difficultyText = currentDifficulty.getDisplayName();
        font.draw(batch, difficultyText, buttonDifficultyBounds.x + buttonDifficultyBounds.width + 20, buttonDifficultyBounds.y + 45);
        batch.end();
    }

    // (Các hàm còn lại giữ nguyên...)
    private void handleMenuInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            touchPos.y = Gdx.graphics.getHeight() - touchPos.y;

            if (button1PlayerBounds.contains(touchPos.x, touchPos.y)) {
                startGame();
            }
            if (button2PlayerBounds.contains(touchPos.x, touchPos.y)) {
                startGame();
            }
            if (buttonDifficultyBounds.contains(touchPos.x, touchPos.y)) {
                switch (currentDifficulty) {
                    case EASY:   currentDifficulty = Difficulty.NORMAL; break;
                    case NORMAL: currentDifficulty = Difficulty.HARD;   break;
                    case HARD:   currentDifficulty = Difficulty.EASY;   break;
                }
            }
        }
    }

    private void loadMenuAssets() {
        menuBackground = new Texture(Gdx.files.internal("menu_background2.png"));
        button1PlayerTexture = new Texture(Gdx.files.internal("SinglePlayer_button.png"));
        button2PlayerTexture = new Texture(Gdx.files.internal("MultiPlayer_button.png"));
        buttonDifficultyTexture = new Texture(Gdx.files.internal("Difficulty_button.png"));
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float buttonWidth = 300;
        float buttonHeight = 100;
        float buttonX = (screenWidth - buttonWidth) / 2;
        button1PlayerBounds = new Rectangle(buttonX, screenHeight / 2 + 80, buttonWidth, buttonHeight);
        button2PlayerBounds = new Rectangle(buttonX, screenHeight / 2, buttonWidth, buttonHeight);
        buttonDifficultyBounds = new Rectangle(buttonX, screenHeight / 2 - 80, buttonWidth, buttonHeight);
    }

    private void checkWallCollision() {
        if (ball.getX() - ball.getRadius() <= 32 || ball.getX() + ball.getRadius() >= 1008)
            ball.reverseX();
        if (ball.getY() + ball.getRadius() >= 778)
            ball.reverseY();
        if (brickManager.isAllCleared()) {
            gameState = GameState.MENU; // Hoặc GAME_OVER
        }
        if (ball.getY() < -ball.getRadius()) {
            boolean stillAlive = livesSystem.loseLife();
            if (stillAlive) {
                ball.reset(paddle.getX() + 65f , paddle.getY() + 20);
                ball.launch(60);
            } else {
                System.out.println("GAME OVER");
                gameState = GameState.MENU;
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        if (background != null) background.dispose();
        if (ball != null) ball.dispose();
        if (paddle != null) paddle.dispose();
        if (brickManager != null) brickManager.dispose();
        if (scoreSystem != null) scoreSystem.dispose();
        if (livesSystem != null) livesSystem.dispose();
        menuBackground.dispose();
        button1PlayerTexture.dispose();
        button2PlayerTexture.dispose();
        buttonDifficultyTexture.dispose();
        if (pauseImage != null) pauseImage.dispose();
    }
}
