package com.uet.arkanoid.level;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.uet.arkanoid.brick.*;

import java.util.ArrayList;

public class LevelLoader {

    public static ArrayList<Brick> loadLevel(String mapPath, Texture[] brickTextures) {
        ArrayList<Brick> bricks = new ArrayList<>();

        // Load bản đồ từ file TMX
        TiledMap map = new TmxMapLoader().load(mapPath);
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("Bricks");
        if (layer == null)
            layer = (TiledMapTileLayer) map.getLayers().get("bricks");

        int tileWidth = (int) layer.getTileWidth();
        int tileHeight = (int) layer.getTileHeight();

        for (int x = 0; x < layer.getWidth(); x++) {
            for (int y = 0; y < layer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                if (cell == null || cell.getTile() == null) continue;

                int type = cell.getTile().getId() - 1;
                Texture tex = brickTextures[type];
                float px = x * tileWidth;
                float py = y * tileHeight;

                Brick brick;
                if (type >= 4 && type <= 6) {
                    brick = new PowerUpBrick(tex, px, py, tileWidth, tileHeight, type);
                } else {
                    int hitPoints = Math.max(1, type);
                    brick = new NormalBrick(type, tex, px, py, tileWidth, tileHeight, hitPoints);
                }
                bricks.add(brick);
            }
        }

        return bricks;
    }
}
