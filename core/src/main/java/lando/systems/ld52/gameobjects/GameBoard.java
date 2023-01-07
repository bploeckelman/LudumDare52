package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld52.Assets;
import lando.systems.ld52.Config;
import lando.systems.ld52.Main;

public class GameBoard {

    public final static int gridSize = 7;
    public final static float tileSize = 64;
    public final static float margin = 12;
    public final static float boardSize = margin + (tileSize + margin) * gridSize;

    private final Tile[][] tiles;

    public Rectangle bounds;

    public GameBoard(Assets assets) {
        bounds = new Rectangle(
                (Config.Screen.window_width  - boardSize) / 2f,
                (Config.Screen.window_height - boardSize) / 2f,
                boardSize, boardSize);

        tiles = new Tile[gridSize][];
        for (int x = 0; x < gridSize; x++){
            tiles[x] = new Tile[gridSize];
            for (int y = 0; y < gridSize; y++) {
                tiles[x][y] = new Tile(assets, x, y, tileSize, this);
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
                boolean highlighted = (x == 3 && y == 4);
                tiles[x][y].render(batch, highlighted);
            }
        }
    }
}
