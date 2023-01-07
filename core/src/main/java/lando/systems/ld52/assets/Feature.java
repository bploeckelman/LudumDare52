package lando.systems.ld52.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lando.systems.ld52.Assets;

public enum Feature {
    // TODO - add additional params that can be used elsewhere
    //   like a Category enum (mouth, eyes, facial hair, etc...)

      cigarette        ("cigarette", "cigarette")
    , collar_a         ("collar-a-idle", "collor type a")
    , eyepatch_a       ("eyepatch-a", "eye patch")
    , eyes_a           ("eyes-a-idle", "normal eye")
    , glasses_a        ("glasses-a", "glass type a")
    , hair_long_brown  ("hair-long-brown", "brown long hair")
    , hair_short_black ("hair-short-black", "brown short hair")
    , mustache_a       ("mustache-a", "mustache type a")
    , nose_clown       ("nose-clown", "clown nose")
    , tongue           ("tongue", "tongue out")
    ;



    public static final String prefix = "faces/test/";
    public final String regionsName;
    public final String displayName;

    Feature(String regionsName, String displayName) {
        this.regionsName = prefix + regionsName;
        this.displayName = displayName;
    }

    public static Animation<TextureRegion> get(Assets assets, Feature feature) {
        return assets.features.get(feature);
    }

}
