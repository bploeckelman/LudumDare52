package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld52.Main;

public class Tile {

    public int x;
    public int y;
    public Rectangle tileArea;

    // Debug probably
    public Color tileColor;

    public Tile(int x, int y, float tileSize, Rectangle board) {
        this.x = x;
        this.y = y;
        tileArea = new Rectangle(board.x + x * tileSize, board.y + y * tileSize, tileSize, tileSize);
        tileColor = new Color(Color.WHITE);
        tileColor.fromHsv(MathUtils.random(360f), .5f, .8f);
    }

    public void update(float dt) {

    }

    public void render(SpriteBatch batch) {
        batch.setColor(tileColor);
        batch.draw(Main.game.assets.pixelRegion, tileArea.x, tileArea.y, tileArea.width, tileArea.height);
        batch.setColor(Color.WHITE);
    }
}
