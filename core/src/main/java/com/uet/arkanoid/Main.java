package com.uet.arkanoid;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {

    // MỚI: Enum để quản lý trạng thái của game
    private enum GameState {
        MENU,
        PLAYING,
        GAME_OVER
    }
    private GameState gameState = GameState.MENU; // Trạng thái ban đầu là MENU

    // MỚI: Biến cho màn hình Menu
    private Texture menuBackground;
    private Texture button1PlayerTexture;
    private Texture button2PlayerTexture;
    private Texture buttonDifficultyTexture;
    private Rectangle button1PlayerBounds;
    private Rectangle button2PlayerBounds;
    private Rectangle buttonDifficultyBounds;
    private Difficulty currentDifficulty = Difficulty.NORMAL;

    // THAY ĐỔI: Các đối tượng game cũ, giữ nguyên
    private Texture background;
    private SpriteBatch batch;
    private BitmapFont font;

    private BrickManager brickManager;
    private Ball ball;
    private Paddle paddle;
    private PaddleCollision paddleCollision;
    private ScoreSystem scoreSystem;
    private Lives livesSystem;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2);

        // MỚI: Load tài nguyên và thiết lập cho Menu
        loadMenuAssets();
    }

    // MỚI: Hàm này chứa code khởi tạo game, sẽ được gọi khi người chơi nhấn nút "Start"
    private void startGame() {
        // --- TOÀN BỘ CODE KHỞI TẠO GAME TỪ HÀM create() CŨ ĐƯỢC CHUYỂN VÀO ĐÂY ---
        background = new Texture(Gdx.files.internal("background.png"));

        paddle = new Paddle(
            (Gdx.graphics.getWidth() - 128) / 2f,
            50
        );
        paddleCollision = new PaddleCollision();
        scoreSystem = new ScoreSystem(50, Gdx.graphics.getHeight() - 50);
        livesSystem = new Lives(Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 70);
        brickManager = new BrickManager("Level1.tmx");

        Texture ballTexture = new Texture(Gdx.files.internal("ball.png"));
        float startX = Gdx.graphics.getWidth() / 2f;
        float startY = paddle.getY() + 20;

        // MỚI: Điều chỉnh tốc độ bóng dựa vào độ khó
        float ballSpeed;
        switch (currentDifficulty) {
            case EASY:   ballSpeed = 400; break;
            case HARD:   ballSpeed = 650; break;
            default:     ballSpeed = 500; break; // NORMAL
        }
        ball = new Ball(startX, startY, 10, ballSpeed, ballTexture);
        ball.launch(60);

        // MỚI: Chuyển trạng thái sang đang chơi
        gameState = GameState.PLAYING;
    }

    @Override
    public void render() {
        // Xóa màn hình
        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // THAY ĐỔI: Render dựa trên trạng thái của game
        switch (gameState) {
            case MENU:
                handleMenuInput();
                renderMenu();
                break;
            case PLAYING:
                renderGame();
                break;
            case GAME_OVER:
                // (Bạn có thể thêm logic cho màn hình game over ở đây)
                // Ví dụ: renderGameOver();
                break;
        }
    }

    // MỚI: Hàm chứa toàn bộ logic render game cũ của bạn
    private void renderGame() {
        float delta = Gdx.graphics.getDeltaTime();

        // update logic
        paddle.update(delta);
        ball.update(delta);

        brickManager.checkCollision(ball,scoreSystem);
        paddleCollision.checkCollision(paddle, ball);
        checkWallCollision();

        scoreSystem.update(delta);
        livesSystem.update(delta);

        // render
        batch.begin();
        batch.draw(background, 0, 0);
        paddle.render(batch);
        brickManager.render(batch);
        ball.render(batch);
        scoreSystem.render(batch);
        livesSystem.render(batch);
        batch.end();
    }

    // MỚI: Hàm vẽ màn hình Menu
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

    // MỚI: Hàm xử lý input cho Menu
    private void handleMenuInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            // Cần chuyển đổi tọa độ vì hệ tọa độ của input và camera khác nhau
            // Trong ApplicationAdapter mặc định, không cần camera.unproject nếu không dùng camera riêng
            // Tuy nhiên, tọa độ Y của input bị ngược (0 ở trên cùng)
            touchPos.y = Gdx.graphics.getHeight() - touchPos.y;

            if (button1PlayerBounds.contains(touchPos.x, touchPos.y)) {
                System.out.println("Starting 1 Player game!");
                startGame(); // Bắt đầu game
            }
            if (button2PlayerBounds.contains(touchPos.x, touchPos.y)) {
                // Logic cho 2 người chơi bạn sẽ cần tự cài đặt
                System.out.println("Starting 2 Player game! (Not implemented)");
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

    // MỚI: Hàm load tài nguyên cho Menu
    private void loadMenuAssets() {
        // Bạn cần chuẩn bị các file ảnh này trong thư mục assets
        menuBackground = new Texture(Gdx.files.internal("menu_background2.png"));
        button1PlayerTexture = new Texture(Gdx.files.internal("singlePlayer_button.png"));
        button2PlayerTexture = new Texture(Gdx.files.internal("multiPlayer_button.png"));
        buttonDifficultyTexture = new Texture(Gdx.files.internal("Difficult_button.png"));

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float buttonWidth = 400;
        float buttonHeight = 90;
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
            // Thêm màn hình Endgame ở đây
            // Có thể đổi gameState = GameState.GAME_OVER;
        }

        if (ball.getY() < -ball.getRadius()) {
            boolean stillAlive = livesSystem.loseLife();
            if (stillAlive) {
                ball.reset(paddle.getX() + 65f , paddle.getY() + 20);
                ball.launch(60);
            } else {
                System.out.println("GAME OVER");
                // Thêm màn hình endgame ở đây
                // Có thể đổi gameState = GameState.MENU; để quay về menu chính
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

        // Hủy tài nguyên menu
        menuBackground.dispose();
        button1PlayerTexture.dispose();
        button2PlayerTexture.dispose();
        buttonDifficultyTexture.dispose();
    }
}
