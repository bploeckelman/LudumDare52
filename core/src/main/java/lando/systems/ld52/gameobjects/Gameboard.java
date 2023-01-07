package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld52.Config;
import lando.systems.ld52.Main;

public class Gameboard {

    public static int gridSize = 8;
    public Rectangle boardArea;
    private final float boardWidth = 500;
    Tile[][] tiles;

    public Gameboard() {
        boardArea = new Rectangle(Config.Screen.window_width/2f - boardWidth/2f, Config.Screen.window_height/2f - boardWidth/2f, boardWidth, boardWidth);
        tiles = new Tile[gridSize][];
        for (int x = 0; x < gridSize; x++){
            tiles[x] = new Tile[gridSize];
            for (int y = 0; y < gridSize; y++) {
                tiles[x][y] = new Tile(x, y);
            }
        }
    }

    public void update(float dt) {

    }

    public void render(SpriteBatch batch) {
        float gridDelta = boardWidth / gridSize;
        for (int x = 0; x < gridSize; x++){
            for (int y = 0; y < gridSize; y++) {
                Tile t = tiles[x][y];
                batch.setColor(t.tileColor);
                batch.draw(Main.game.assets.pixelRegion, boardArea.x + (x*gridDelta), boardArea.y + (y * gridDelta), gridDelta, gridDelta);
                batch.setColor(Color.WHITE);
            }
        }
    }
}
