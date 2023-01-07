package lando.systems.ld52.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lando.systems.ld52.Assets;

public enum Head {

      a ("head-a-idle")
//    , ...
    ;

    public static final String prefix = "faces/test/";
    public final String regionsName;

    Head(String regionsName) {
        this.regionsName = prefix + regionsName;
    }

    public static Animation<TextureRegion> get(Assets assets, Head head) {
        return assets.heads.get(head);
    }

}
