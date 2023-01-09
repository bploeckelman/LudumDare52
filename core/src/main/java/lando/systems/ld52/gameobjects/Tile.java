package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld52.Assets;
import lando.systems.ld52.data.TileData;
import lando.systems.ld52.screens.GameScreen;
import lando.systems.ld52.utils.Coord;

public class Tile {

    private final Assets assets;

    public Coord coord;
    public Rectangle bounds;
    public TileObject object;
    public TextureRegion texture;

    // Debug probably
    public Color color;

    public Tile(Assets assets, int x, int y, float tileSize, GameBoard board, TileData tileData) {
        this.assets = assets;
        texture = assets.atlas.findRegion("tiles/tile");
        coord = Coord.at(x, y);
        bounds = new Rectangle(
                board.bounds.x + GameBoard.margin + x * (tileSize + GameBoard.margin),
                board.bounds.y + GameBoard.margin + y * (tileSize + GameBoard.margin),
                tileSize, tileSize);
        color = new Color(1f, 1f, 1f, 1f);
        color.fromHsv(MathUtils.random(360f), .3f, .5f);

        switch (tileData.type) {
            case obstacle:
                object = new TileBoulder(assets, this);
                break;
            case character:
                object = new TileHead(assets, this, tileData);
                break;
        }
    }

    public void update(float dt) {
        if (object != null){
            object.update(dt);
        }
    }

    public void render(SpriteBatch batch, boolean highlighted) {
        batch.setColor(color);
        batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
        batch.setColor(Color.WHITE);
        if (object != null){
            object.render(batch);
        }
        if (highlighted){
            Assets.Patch.debug.ninePatch.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    Vector2 center = new Vector2();
    public Vector2 getCenter() {
        return bounds.getCenter(center);
    }

    public void collect(GameScreen gameScreen) {
        if (object != null) {
            object.collect(gameScreen);
            if (object instanceof TileHead) {
                // create a tombstone in their place
                object = new TileTombstone(assets, this);
            } else {
                object = null;
            }
        }
    }

    public boolean blocks(){
        if (object != null) {
            return object.blocksHarvest;
        }
        return false;
    }
}
