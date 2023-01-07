package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld52.Assets;
import lando.systems.ld52.utils.Coord;

public class Tile {

    public Coord coord;
    public Rectangle bounds;
    public TileObject object;
    public TextureRegion texture;

    // Debug probably
    public Color color;

    public Tile(Assets assets, int x, int y, float tileSize, GameBoard board) {
        texture = assets.atlas.findRegion("tiles/tile");
        coord = Coord.at(x, y);
        bounds = new Rectangle(
                board.bounds.x + GameBoard.margin + x * (tileSize + GameBoard.margin),
                board.bounds.y + GameBoard.margin + y * (tileSize + GameBoard.margin),
                tileSize, tileSize);
        color = new Color(1f, 1f, 1f, 1f);
        color.fromHsv(MathUtils.random(360f), .3f, .5f);
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
        batch.setColor(color);
        batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
        batch.setColor(Color.WHITE);
        if (object != null){
            object.render(batch);
        }
    }
}
