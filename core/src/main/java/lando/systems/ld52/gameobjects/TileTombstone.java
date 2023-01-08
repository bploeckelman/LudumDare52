package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lando.systems.ld52.Assets;

public class TileTombstone extends TileObject {

    private final Animation<TextureRegion> tombstone;
    private float stateTime;

    public TileTombstone(Assets assets, Tile tile) {
        super(tile);
        tombstone = assets.tombstone;
        stateTime = 0f;
    }

    @Override
    public void update(float dt) {
        stateTime += dt;
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion keyframe = tombstone.getKeyFrame(stateTime);
        batch.draw(keyframe,
                tile.bounds.x + margin,
                tile.bounds.y + margin,
                tile.bounds.width - margin * 2f,
                tile.bounds.height - margin * 2f);
    }

    public boolean collect() {
        return false;
    }

}
