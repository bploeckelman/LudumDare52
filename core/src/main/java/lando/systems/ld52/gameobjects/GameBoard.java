package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld52.Assets;
import lando.systems.ld52.Config;

public class GameBoard {

    private final static float boardWidth = 500;
    public final static int gridSize = 7;
    public final static float tileSize = boardWidth / gridSize;

    private final Assets assets;
    private Tile[][] tiles;

    public Rectangle boardArea;


    public GameBoard(Assets assets) {
        this.assets = assets;

        boardArea = new Rectangle(Config.Screen.window_width/2f - boardWidth/2f, Config.Screen.window_height/2f - boardWidth/2f, boardWidth, boardWidth);
        tiles = new Tile[gridSize][];
        for (int x = 0; x < gridSize; x++){
            tiles[x] = new Tile[gridSize];
            for (int y = 0; y < gridSize; y++) {
                tiles[x][y] = new Tile(assets, x, y, tileSize, boardArea);

            }
        }
    }

    public void update(float dt) {
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                tiles[x][y].update(dt);
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (int x = 0; x < gridSize; x++){
            for (int y = 0; y < gridSize; y++) {
                tiles[x][y].render(batch);
            }
        }
    }
}
