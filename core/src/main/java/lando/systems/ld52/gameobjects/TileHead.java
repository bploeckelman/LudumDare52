package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import lando.systems.ld52.Assets;
import lando.systems.ld52.assets.Feature;
import lando.systems.ld52.assets.Head;

import java.util.Comparator;
import java.util.function.ToIntFunction;

public class TileHead extends TileObject {

    private float stateTime;

    private final Head head;
    private final Animation<TextureRegion> headAnim;
    private final OrderedMap<Feature, Animation<TextureRegion>> featureAnims;

    public TileHead(Assets assets, Tile tile) {
        super(tile);
        stateTime = 0f;

        head = Head.a;
        headAnim = Head.get(assets, head);

        // pick random features from each category in category layer order
        Array<Feature> features = new Array<>();
        for (Feature.Category category : Feature.Category.values()) {
            Feature feature = Feature.getRandomFrom(category);
            features.add(feature);
        }

        // populate feature -> anim map based on the selected features
        featureAnims = new OrderedMap<>();
        for (Feature feature : features) {
            featureAnims.put(feature, Feature.get(assets, feature));
        }
    }

    @Override
    public void update(float dt) {
        stateTime += dt;
    }

    @Override
    public void render(SpriteBatch batch) {
        // NOTE - margin is zero here to make the characters larger for legibility,
        //  tiles are bigger now so it might be ok to restore the margin
        float margin = 0f;

        Array<Feature> keys = featureAnims.orderedKeys();
        for (int i = 0; i < keys.size; i++) {
            Feature feature = keys.get(i);

            // draw the head after the 'clothes' category (which is shoulders essentially)
            if (feature.category.layer == 1) {
                TextureRegion headKeyframe = headAnim.getKeyFrame(stateTime);
                batch.draw(headKeyframe,
                        tile.bounds.x + margin,
                        tile.bounds.y + margin,
                        tile.bounds.width - margin * 2f,
                        tile.bounds.height - margin * 2f);
            }

            // draw this feature
            Animation<TextureRegion> animation = featureAnims.get(feature);
            TextureRegion featureKeyframe = animation.getKeyFrame(stateTime);
            batch.draw(featureKeyframe,
                    tile.bounds.x + margin,
                    tile.bounds.y + margin,
                    tile.bounds.width - margin * 2f,
                    tile.bounds.height - margin * 2f);
        }
    }

}
