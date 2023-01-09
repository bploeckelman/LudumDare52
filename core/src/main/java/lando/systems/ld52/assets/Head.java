package lando.systems.ld52.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld52.Assets;

import java.util.HashMap;
import java.util.Map;

public enum Head {

      a ("head-a-idle")
      , a1 ("head-a-1")
      , a2 ("head-a-2")
      , a3 ("head-a-3")
      , a4 ("head-a-4")
      , b1 ("head-b-1")
      , b2 ("head-b-2")
      , b3 ("head-b-3")
      , b4 ("head-b-4")
      , blank ("head-blank")
//    , ...
    ;

    public static final String prefix = "faces/test/";
    public final String regionsName;

    Head(String regionsName) {
        this.regionsName = prefix + regionsName;
    }

    // TODO: Randomize head chooser, a la random feature chooser
//    private static final Map<Head, String> allHeadsMap = new HashMap<>();
//    private static final Array<Head> allHeadsArray= new Array<Head>();
//
    public static Head getRandom() {
        Head[] headsArray = Head.values();
//        Gdx.app.log("Head", String.valueOf(headsArray[MathUtils.random(0, headsArray.length - 1)]));
        Head headToGet = headsArray[MathUtils.random(0, headsArray.length - 1)];
        if( headToGet.name() == "blank") {
            headToGet = Head.b2;

        }
        return headToGet;

    }

    public static Animation<TextureRegion> get(Assets assets, Head head) {
        return assets.heads.get(head);
    }

}
