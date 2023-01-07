package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld52.Assets;
import lando.systems.ld52.assets.Feature;
import lando.systems.ld52.assets.Head;

public class TileHead extends TileObject {


//    private final Animation<TextureRegion> animation;
    private float stateTime;

    // TODO: face slot
    // Hair Color
    // Hair Style? whatever we come up with needs to be stored here
    private final Head head;
    private final Array<Feature> features;
    private final Animation<TextureRegion> headAnim;
    private final Array<Animation<TextureRegion>> featureAnims;

    public TileHead(Assets assets, Tile tile) {
        super(tile);
//        animation = new Animation<>(0.1f, assets.atlas.findRegion("faces/test-face"));
//        animation.setPlayMode(Animation.PlayMode.LOOP);
        stateTime = 0f;

        // TODO - this is temporary, need a robust way to pick a set of features based on some criteria
        //   one that doesn't generate two 'hair' features for example... see note in Feature
        head = Head.a;
        headAnim = Head.get(assets, head);
        features = new Array<>();
        featureAnims = new Array<>();
        for (Feature feature : Feature.values()) {
            features.add(feature);
            featureAnims.add(Feature.get(assets, feature));
        }
    }

    @Override
    public void update(float dt) {
        stateTime += dt;
    }

    @Override
    public void render(SpriteBatch batch) {
        // take out margin to try to
        float margin = 0f;

        TextureRegion headKeyframe = headAnim.getKeyFrame(stateTime);
        batch.draw(headKeyframe,
                tile.bounds.x + margin,
                tile.bounds.y + margin,
                tile.bounds.width - margin * 2f,
                tile.bounds.height - margin * 2f);

        for (Animation<TextureRegion> featureAnim : featureAnims) {
            TextureRegion featureKeyframe = featureAnim.getKeyFrame(stateTime);
            batch.draw(featureKeyframe,
                    tile.bounds.x + margin,
                    tile.bounds.y + margin,
                    tile.bounds.width - margin * 2f,
                    tile.bounds.height - margin * 2f);
        }
    }

}
