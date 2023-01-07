package lando.systems.ld52.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lando.systems.ld52.Assets;

public enum Feature {
    // TODO - add additional params that can be used elsewhere
    //   like a Category enum (mouth, eyes, facial hair, etc...)

      cigarette        ("cigarette")
    , collar_a         ("collar-a-idle")
    , eyepatch_a       ("eyepatch-a")
    , eyes_a           ("eyes-a-idle")
    , glasses_a        ("glasses-a")
    , hair_long_brown  ("hair-long-brown")
    , hair_short_black ("hair-short-black")
    , mustache_a       ("mustache-a")
    , nose_clown       ("nose-clown")
    , tongue           ("tongue")
    ;

    public static final String prefix = "faces/test/";
    public final String regionsName;

    Feature(String regionsName) {
        this.regionsName = prefix + regionsName;
    }

    public static Animation<TextureRegion> get(Assets assets, Feature feature) {
        return assets.features.get(feature);
    }

}
