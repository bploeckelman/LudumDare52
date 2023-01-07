package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld52.Main;

public class TileHead extends TileObject {



    // TODO: face slot
    // Hair Color
    // Hair Style? whatever we come up with needs to be stored here

    public TileHead(Tile tile) {
        super(tile);
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(Main.game.assets.circleTex, tile.tileArea.x+margin, tile.tileArea.y+margin, tile.tileArea.width - margin*2f, tile.tileArea.height - margin *2f);
    }

}
