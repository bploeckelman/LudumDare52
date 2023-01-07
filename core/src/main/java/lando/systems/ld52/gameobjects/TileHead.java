package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lando.systems.ld52.Assets;
import lando.systems.ld52.Main;

public class TileHead extends TileObject {


    private final Animation<TextureRegion> animation;
    private float stateTime;

    // TODO: face slot
    // Hair Color
    // Hair Style? whatever we come up with needs to be stored here

    public TileHead(Assets assets, Tile tile) {
        super(tile);
        animation = new Animation<>(0.1f, assets.atlas.findRegion("faces/test-face"));
        animation.setPlayMode(Animation.PlayMode.LOOP);
        stateTime = 0f;
    }

    @Override
    public void update(float dt) {
        stateTime += dt;
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion keyframe = animation.getKeyFrame(stateTime);
        batch.draw(keyframe,
                tile.tileArea.x+margin,
                tile.tileArea.y+margin,
                tile.tileArea.width - margin*2f,
                tile.tileArea.height - margin *2f);
    }

}
