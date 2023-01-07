package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld52.Assets;
import lando.systems.ld52.Main;

public class Tile {

    public int x;
    public int y;
    public Rectangle tileArea;
    public TileObject object;

    // Debug probably
    public Color tileColor;

    public Tile(Assets assets, int x, int y, float tileSize, Rectangle board) {
        this.x = x;
        this.y = y;
        tileArea = new Rectangle(board.x + x * tileSize, board.y + y * tileSize, tileSize, tileSize);
        tileColor = new Color(Color.WHITE);
        tileColor.fromHsv(MathUtils.random(360f), .3f, .5f);
        if(MathUtils.randomBoolean(.5f)){
            object = new TileHead(assets, this);
        }
    }

    public void update(float dt) {
        if (object != null){
            object.update(dt);
        }
    }

    public void render(SpriteBatch batch) {
        // TODO - make tiles fit layout (add margin/spacing)
//        batch.setColor(tileColor);
//        batch.draw(Main.game.assets.pixelRegion, tileArea.x, tileArea.y, tileArea.width, tileArea.height);
        batch.setColor(Color.WHITE);
        if (object != null){
            object.render(batch);
        }
    }
}
