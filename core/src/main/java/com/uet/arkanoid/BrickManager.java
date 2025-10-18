package com.uet.arkanoid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class BrickManager {

    public ArrayList<Brick> bricks;
    private Texture[] BrickTextures;
    private boolean allCleared = false;

    public BrickManager(String mapPath) {
        bricks = new ArrayList<>();

        // Load texture cho các loại gạch
        BrickTextures = new Texture[]{
            new Texture("Brick0.png"),
            new Texture("Brick1.png"),
            new Texture("Brick2.png"),
            new Texture("Brick3.png"),
            new Texture("Brick4.png"),
            new Texture("Brick5.png"),
            new Texture("Brick6.png")
        };

        Brick.setAllTextures(BrickTextures);

        // Load bản đồ từ Tiled (.tmx)
        TiledMap map = new TmxMapLoader().load(mapPath);

        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("Bricks");
        if (layer == null)
            layer = (TiledMapTileLayer) map.getLayers().get("bricks");

        int tileWidth = (int) layer.getTileWidth();
        int tileHeight = (int) layer.getTileHeight();

        // Duyệt từng cell trong layer
        for (int x = 0; x < layer.getWidth(); x++) {
            for (int y = 0; y < layer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                if (cell != null && cell.getTile() != null) {
                    int tileID = cell.getTile().getId();

                    int type = tileID - 1 ;
                    int hit = type;
                    switch (type) {
                        case 4:
                            hit = 1;
                            break;
                        case 5:
                            hit = 1;
                            break;
                        case 6:
                            hit = 1;
                            break;
                    }

                    Texture tex = BrickTextures[type];
                    int px = x * tileWidth;
                    int py = y * tileHeight;

                    bricks.add(new Brick(type, px, py, tileHeight, tileWidth, tex, hit));
                }
            }
        }
    }

    // (render, isAllCleared giữ nguyên...)
    public void render(SpriteBatch batch) {
        for (Brick b : bricks) {
            b.render(batch);
        }
    }
    public boolean isAllCleared() {
        for (Brick b : bricks) {
            if (!b.isDeleted()) return false;
        }
        return true;
    }

    // THAY ĐỔI: Chỉnh sửa hàm checkCollision để nhận ScoreSystem
    public int checkCollision(Ball ball, ScoreSystem scoreSystem) {
        for (Brick brick : bricks) {
            if (!brick.isDeleted() && brick.getBrickRectangle().overlaps(ball.bounds)) {
                // Lấy loại gạch trước khi phá hủy để tính điểm
                int brickType = brick.getType();

                // Phá gạch
                int result = brick.destroy();

                // Nảy lại bóng
                ball.Nay(brick);


                // MỚI: Bổ sung phần cộng điểm
                int scoreValue = 0;
                switch (brickType) {
                    case 0: // Brick0.png
                        scoreValue = 50;
                        break;
                    case 1: // Brick1.png
                        scoreValue = 80;
                        break;
                    case 2: // Brick2.png
                        scoreValue = 100;
                        break;
                    case 3: // Brick3.png
                        scoreValue = 120;
                        break;
                }

                // Gọi ScoreSystem để cộng điểm và tạo hiệu ứng popup
                if (scoreValue > 0) {
                    scoreSystem.addScore(scoreValue, new Vector2(brick.getX(), brick.getY()));
                }

                if (isAllCleared()) {
                    allCleared = true;
                }

                return brickType;

                // Thoát khỏi vòng lặp sau khi xử lý một va chạm để tránh lỗi
            }
        }
        return -1;
    }

    public boolean isGameOver() {
        return allCleared;
    }

    public void dispose() {
        // ...
    }
}
