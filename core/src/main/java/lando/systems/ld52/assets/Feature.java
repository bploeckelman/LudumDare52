package lando.systems.ld52.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld52.Assets;

import java.util.HashMap;
import java.util.Map;

public enum Feature {

      collar_a         (Category.clothes,   "collar-a-idle",   "collar")
    // TODO - eyes should be a separate thing? are we doing eye color vs eye-wear?
    , eyes_a           (Category.eye,       "eyes-a-idle",     "eyes")
    , glasses_a        (Category.eye,       "glasses-a",       "glasses")
    , eyepatch_a       (Category.eye,       "eyepatch-a",      "eye patch")
    , nose_normal      (Category.nose,      "feature-blank",   "normal nose")
    , nose_clown       (Category.nose,      "nose-clown",      "clown nose")
    , tongue           (Category.mouth,     "tongue",          "tongue out")
    , clean_shaven     (Category.hair_face, "feature-blank",   "clean shaven")
    , mustache_a       (Category.hair_face, "mustache-a",      "mustache")
    , hair_bald        (Category.hair_head, "feature-blank",   "bald")
    , hair_long_brown  (Category.hair_head, "hair-long-brown",  "long brown hair")
    , hair_short_black (Category.hair_head, "hair-short-black", "short black hair")
//    , cigarette        (Category.mouth, "cigarette", "cigarette") // NOTE - not legible onscreen so removing it as a feature option
    ;

    public enum Category {
          clothes   (0)
        , eye       (1)
        , nose      (2)
        , mouth     (3)
        , hair_face (4)
        , hair_head (5)
//        , hat       (6) // TODO - no 'hat' features yet
//        , neck      (7) // TODO - no 'neck' features yet (like necklace or tie or something)
        ;
        public final int layer;
        Category(int layer) {
            this.layer = layer;
        }
    }

    public static final String prefix = "faces/test/";
    public final Category category;
    public final String regionsName;
    public final String displayName;

    Feature(Category category, String regionsName, String displayName) {
        this.category = category;
        this.regionsName = prefix + regionsName;
        this.displayName = displayName;
    }

    public static Animation<TextureRegion> get(Assets assets, Feature feature) {
        return assets.features.get(feature);
    }

    private static final Map<Category, Array<Feature>> featuresByCategory = new HashMap<>();
    public static Feature getRandomFrom(Category category) {
        if (featuresByCategory.isEmpty()) {
            for (Feature feature : Feature.values()) {
                featuresByCategory.putIfAbsent(feature.category, new Array<>());
                featuresByCategory.get(feature.category).add(feature);
            }
        }
        Array<Feature> features = featuresByCategory.get(category);
        if (features == null || features.isEmpty()) {
            Gdx.app.log("feature", "no features for category: " + category.name());
        }
        return features.get(MathUtils.random(0, features.size - 1));
    }

    public static Feature getFeature(Feature feature, Category category) {
        return feature != null ? feature : getRandomFrom(category);
    }
}
