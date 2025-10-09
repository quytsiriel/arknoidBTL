package com.uet.arkanoid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import java.util.ArrayList;

public class BrickManager {

    public ArrayList<Brick> bricks;
    private Texture[] BrickTextures;

    public BrickManager(String mapPath) {
        bricks = new ArrayList<>();

        // Load texture cho các loại gạch
        BrickTextures = new Texture[]{
            new Texture("Brick0.png"),
            new Texture("Brick1.png"),
            new Texture("Brick2.png"),
            new Texture("Brick3.png")
        };

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

                    int type = (tileID) % BrickTextures.length;
                    int hit = type ;

                    Texture tex = BrickTextures[type];
                    int px = x * tileWidth;
                    int py = y * tileHeight;

                    bricks.add(new Brick(type, px, py, tileHeight, tileWidth, tex, hit));
                }
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (Brick b : bricks) {
            b.render(batch);
        }
    }

    public void checkCollision(Ball ball) {
        for (Brick brick : bricks) {
            if (!brick.isDeleted() && brick.getBrickRectangle().overlaps(ball.bounds)) {
                // Gạch bị trúng
                brick.destroy();

                // Nảy lại bóng
                ball.velocity.y *= -1;
                break; // tránh xử lý trùng
            }
        }
    }

    public void dispose() {

    }
}
