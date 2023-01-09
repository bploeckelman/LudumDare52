package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedMap;
import lando.systems.ld52.Assets;
import lando.systems.ld52.Config;
import lando.systems.ld52.assets.Feature;
import lando.systems.ld52.assets.Head;
import lando.systems.ld52.audio.AudioManager;
import lando.systems.ld52.data.TileData;
import lando.systems.ld52.screens.GameScreen;
import lando.systems.ld52.ui.QuotaListUI;

public class TileHead extends TileObject {

    private float stateTime;

    private final Head head;
    private final Animation<TextureRegion> headAnim;
    private final OrderedMap<Feature, Animation<TextureRegion>> featureAnims;

    public TileHead(Assets assets, Tile tile, TileData tileData) {
        super(tile);
        stateTime = 0f;

        head = Head.getRandom();
        headAnim = Head.get(assets, head);

        // pick random features from each category in category layer order
        Array<Feature> features = new Array<>();
        features.add(Feature.getFeature(tileData.clothes, Feature.Category.clothes));
        features.add(Feature.getFeature(tileData.eye, Feature.Category.eye));
        features.add(Feature.getFeature(tileData.nose, Feature.Category.nose));
        features.add(Feature.getFeature(tileData.mouth, Feature.Category.mouth));
        features.add(Feature.getFeature(tileData.hair_face, Feature.Category.hair_face));
        features.add(Feature.getFeature(tileData.hair_head, Feature.Category.hair_head));
//        features.add(Feature.getFeature(tileData.hat, Feature.Category.hat));
        // features.add(Feature.getFeature(tileData.hat, Feature.Category.hat));
        // features.add(Feature.getFeature(tileData.neck, Feature.Category.neck));

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

    @Override
    public boolean collect(GameScreen gameScreen) {
        if (gameScreen == null) return false;
        QuotaListUI quotaListUI = gameScreen.gameScreenUI.rightSideUI.quotaListUI;
        Quota heavenQuota = gameScreen.heavenQuota;
        Quota hellQuota = gameScreen.hellQuota;
        heavenQuota.satisfy(featureAnims.orderedKeys());
        hellQuota.satisfy(featureAnims.orderedKeys());
        quotaListUI.setQuotas(heavenQuota, hellQuota);
        gameScreen.game.particles.lightning(new Vector2(tile.bounds.x + MathUtils.random(-150, 150), Config.Screen.window_height), new Vector2(tile.bounds.x + tile.bounds.width / 2, tile.bounds.y + tile.bounds.height / 2));
        gameScreen.game.particles.bleed(tile.bounds.x + tile.bounds.width / 2, tile.bounds.y + tile.bounds.height / 2);
        gameScreen.screenShaker.addDamage(100f);
        gameScreen.audioManager.playSound(AudioManager.Sounds.soulReap, .5f);
        gameScreen.score += 200;
        return true;
    }

}
