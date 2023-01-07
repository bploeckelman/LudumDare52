package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld52.Config;
import lando.systems.ld52.Main;

public class Gameboard {

    public static int gridSize = 8;
    public Rectangle boardArea;
    private float boardWidth = 500;

    public Gameboard() {
        boardArea = new Rectangle(Config.Screen.window_width/2f - boardWidth/2f, Config.Screen.window_height/2f - boardWidth/2f, boardWidth, boardWidth);
    }

    public void update(float dt) {

    }

    public void render(SpriteBatch batch) {
        float gridDelta = boardWidth / gridSize;
        for (int x = 0; x < gridSize; x++){
            for (int y = 0; y < gridSize; y++) {
                batch.setColor((float)x/gridSize,(float)y/gridSize, (x+y)/(2f*gridSize), 1.0f);
                batch.draw(Main.game.assets.pixelRegion, boardArea.x + (x * gridDelta), boardArea.y + (y * gridDelta), gridDelta, gridDelta);
            }
        }
    }
}
