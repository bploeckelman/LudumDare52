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

    public boolean active;

    public WalkPath(Assets assets, String side, float x, float y) {
        this.side = side;
        String animRegionsPrefix = Stringf.format("world/walkpath-%s/walkpath-%s-", side, side);
        String baseRegionsPath = animRegionsPrefix + "base";
        String topRegionsPath = animRegionsPrefix + "topper";
        baseAnim = new Animation<>(0.5f, assets.atlas.findRegions(baseRegionsPath), Animation.PlayMode.LOOP_PINGPONG);
        topAnim = new Animation<>(0.5f, assets.atlas.findRegions(topRegionsPath), Animation.PlayMode.LOOP_PINGPONG);

        // NOTE - there's a whole deal with these textures
        //  since we want to scroll them with wrap -> repeat
        //  gwt requires power of two textures
        //  so those textures are stretched to be PoT and then squished down to normal size
        String texturePath = Stringf.format("images/walkpath-%s-clouds_00.png", side);
        clouds = new Texture(texturePath);
        clouds.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        clouds.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        boolean isLeftOrRight = side.equalsIgnoreCase("left") || side.equalsIgnoreCase("right");
        boolean isTopOrBottom = side.equalsIgnoreCase("top") || side.equalsIgnoreCase("bottom");
        float w = isLeftOrRight ? 88 : 520;
        float h = isTopOrBottom ? 88 : 520;
        bounds = new Rectangle(x, y, w, h);
        scroll = 0f;
        stateTime = 0f;

        active = false;
    }

    @Override
    public void update(float dt) {
        if (active) {
            stateTime += dt;
            float speed = 0.1f;
            scroll += speed * dt;
        }
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
            v += scroll;
            v2 +=  scroll;
        } else if (side.equalsIgnoreCase("right")) {
            v -= scroll;
            v2 -= scroll;
        }

        batch.draw(baseKeyframe, bounds.x, bounds.y, bounds.width, bounds.height);
        batch.draw(clouds, bounds.x, bounds.y, bounds.width, bounds.height, u, v, u2, v2);
        batch.draw(topKeyframe, bounds.x, bounds.y, bounds.width, bounds.height);
    }

}
