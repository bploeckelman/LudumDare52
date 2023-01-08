package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld52.Assets;
import lando.systems.ld52.Config;
import lando.systems.ld52.Main;
import lando.systems.ld52.assets.Feature;
import lando.systems.ld52.data.RoundData;
import lando.systems.ld52.data.TileData;
import lando.systems.ld52.data.TileType;
import lando.systems.ld52.screens.GameScreen;

public class GameBoard {

//    public final static int gridSize = 7;
//    public final static float tileSize = 72;
//    public final static float margin = 6;
//    public final static float boardSize = margin + (tileSize + margin) * gridSize;

    // 6x6 grid instead of 7x7
    public final static float boardSize = 550;
    public final static int gridSize = 6;
    public final static float margin = 6;
    public final static float tileSize = (boardSize - (gridSize + 1) * margin) / (gridSize);

    public final Tile[][] tiles;

    public Rectangle bounds;
    public GameScreen screen;

    public GameBoard(Assets assets, GameScreen screen) {
        this.screen = screen;
        bounds = new Rectangle(
                (Config.Screen.window_width  - boardSize) / 2f,
                (Config.Screen.window_height - boardSize) / 2f,
                boardSize, boardSize);

        // this will get passed in
        RoundData roundData = new RoundData();

        tiles = new Tile[gridSize][];
        for (int x = 0; x < gridSize; x++){
            tiles[x] = new Tile[gridSize];
            for (int y = 0; y < gridSize; y++) {
                tiles[x][y] = new Tile(assets, x, y, tileSize, this, roundData.tileData[x][y]);
            }
        }
    }

    public float left() {
        return bounds.x;
    }

    public float right() {
        return bounds.x + bounds.width;
    }

    public float top() {
        return bounds.y + bounds.height;
    }

    public float bottom() {
        return bounds.y;
    }

    public void update(float dt) {
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                tiles[x][y].update(dt);
            }
        }
    }

    public void render(SpriteBatch batch) {
        if (Config.Debug.general) {
            batch.setColor(0, 1, 1, 0.25f);
            batch.draw(Main.game.assets.pixelRegion, bounds.x, bounds.y, bounds.width, bounds.height);
            batch.setColor(1, 1, 1, 1);
        }

        for (int x = 0; x < gridSize; x++){
            for (int y = 0; y < gridSize; y++) {
                Tile t = tiles[x][y];
                boolean highlighted = t.equals(screen.player.harvestZone.tileToHarvest);
                t.render(batch, highlighted);
            }
        }
    }

    public Tile getTileAt(float worldX, float worldY) {
        if (worldX < left() || worldX >= right() || worldY < bottom() || worldY >= top()) {
            return null;
        }
        try {
            int xIndex = (int) ((worldX - left()) / bounds.width * gridSize);
            int yIndex = (int) ((worldY - bottom()) / bounds.height * gridSize);

            return tiles[xIndex][yIndex];
        } catch (Exception ex) {
            // something went wrong?
            return null;
        }
    }
}
