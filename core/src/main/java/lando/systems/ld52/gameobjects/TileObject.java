package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld52.screens.GameScreen;

public class TileObject implements GameObject{
    protected float margin = 5f;

    Tile tile;

    public TileObject(Tile tile) {
        this.tile = tile;
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch batch) {

    }

    /**
     * Called when this tile object was collected with a click
     * @return
     */
    public boolean collect(GameScreen gameScreen) {
        return false;
    }
}
