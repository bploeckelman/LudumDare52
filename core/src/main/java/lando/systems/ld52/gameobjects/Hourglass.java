package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lando.systems.ld52.Assets;

public class Hourglass implements GameObject {

    private final TextureRegion background;
    private final Animation<TextureRegion> animation;
    private float stateTime;

    public Hourglass(Assets assets) {
        background = assets.pixelRegion;
        animation = new Animation<>(0.1f, assets.atlas.findRegion("ui/hourglass-skull"));
        stateTime = 0f;
    }

    @Override
    public void update(float dt) {
        stateTime += dt;
    }

    @Override
    public void render(SpriteBatch batch) {
        // hard coded layout for now...
        float x = 1030;
        float y = 32;
        float w = 220;
        float h = 220;

        // TODO - pick patch based on time remaining... (?green ->) plain -> yellow -> red
        NinePatch patch = Assets.NinePatches.plain_gradient;
        patch.draw(batch, x, y, w, h);

        TextureRegion keyframe = animation.getKeyFrame(stateTime);
        batch.draw(keyframe,
                x + (w - keyframe.getRegionWidth()) / 2f,
                y + (h - keyframe.getRegionHeight()) / 2f);
    }

}

