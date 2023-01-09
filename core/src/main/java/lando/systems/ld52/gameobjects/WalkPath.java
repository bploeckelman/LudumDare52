package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld52.Assets;
import text.formic.Stringf;

public class WalkPath implements GameObject {

    private final String side;
    private final Animation<TextureRegion> baseAnim;
    private final Animation<TextureRegion> topAnim;
    private final Texture clouds;
    private final Rectangle bounds;
    private float scroll;
    private float stateTime;

    // TODO - add an 'active' flag for when the player is in this section

    public WalkPath(Assets assets, String side, float x, float y) {
        this.side = side;
        String animRegionsPrefix = Stringf.format("world/walkpath-%s/walkpath-%s-", side, side);
        String baseRegionsPath = animRegionsPrefix + "base";
        String topRegionsPath = animRegionsPrefix + "topper";
        baseAnim = new Animation<>(0.5f, assets.atlas.findRegions(baseRegionsPath), Animation.PlayMode.LOOP_PINGPONG);
        topAnim = new Animation<>(0.5f, assets.atlas.findRegions(topRegionsPath), Animation.PlayMode.LOOP_PINGPONG);
        String texturePath = Stringf.format("images/walkpath-%s-clouds_00.png", side);
        clouds = assets.mgr.get(texturePath, Texture.class);
        bounds = new Rectangle(x, y, clouds.getWidth(), clouds.getHeight());
        scroll = 0f;
        stateTime = 0f;
    }

    @Override
    public void update(float dt) {
        // TODO - update animations (if 'active')

        stateTime += dt;

        float speed = 0.1f;
        scroll += speed * dt;
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion baseKeyframe = baseAnim.getKeyFrame(stateTime);
        TextureRegion topKeyframe = topAnim.getKeyFrame(stateTime);

        float u = 0;
        float v = 0;
        float u2 = 1;
        float v2 = 1;

        // apply scroll to u's or v's based on which side this is
        if (side.equalsIgnoreCase("top")) {
            u += scroll;
            u2 += scroll;
        } else if (side.equalsIgnoreCase("bottom")) {
            u -= scroll;
            u2 -= scroll;
        } else if (side.equalsIgnoreCase("left")) {
            v -= scroll;
            v2 -=  scroll;
        } else if (side.equalsIgnoreCase("right")) {
            v += scroll;
            v2 += scroll;
        }

        batch.draw(baseKeyframe, bounds.x, bounds.y, bounds.width, bounds.height);
        batch.draw(clouds, bounds.x, bounds.y, bounds.width, bounds.height, u, v, u2, v2);
        batch.draw(topKeyframe, bounds.x, bounds.y, bounds.width, bounds.height);
    }

}
