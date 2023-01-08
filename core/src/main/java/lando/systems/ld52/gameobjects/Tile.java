package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld52.Assets;
import lando.systems.ld52.Config;
import lando.systems.ld52.screens.GameScreen;
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
        } else if (MathUtils.randomBoolean(.33f)) {
            object = new TileTombstone(assets, this);
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
            gameScreen.game.particles.lightning(new Vector2(bounds.x + MathUtils.random(-150, 150), Config.Screen.window_height), new Vector2(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2));
            if (object.getClass() == TileTombstone.class) {
                gameScreen.game.particles.explode(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2, bounds.width);
                gameScreen.screenShaker.addDamage(25f);
            } else if (object.getClass() == TileHead.class) {
                gameScreen.game.particles.bleed(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);
                gameScreen.screenShaker.addDamage(100f);
            }
            // TODO: remove me when we actually do things
            object = null;
        }
    }
}
