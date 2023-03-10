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

      collar_a         (Category.clothes,   "collar-a-idle",   "Collar", "Won't get into heaven OR hell with that boring shirt.")
    , buttonup_purple         (Category.clothes,   "clothes-buttonup-purple",   "Purple Buttonup", "Don't try to make a purple shirt work unless you're Prince. Which you're not.")
    , buttonup_blue         (Category.clothes,   "clothes-buttonup-blue",   "Blue Buttonup", "Is that a track jacket? \n\nWas this one in the Russian mafia?")
    , sweatshirt_red         (Category.clothes,   "clothes-sweatshirt-red",   "Red Hoodie", "Nice sweatshirt. More like Little DEAD Riding Hood, amirite?")
    , sweatshirt_blue         (Category.clothes,   "clothes-sweatshirt-blue",   "Blue Hoodie", "Blue sweatshirt. \nWe all see it, right? \nUnabomber? No?")
    , plain_green         (Category.clothes,   "clothes-plain-green",   "Green Tee", "Green shirt! \n\nGreen means go (to hell and/or heaven)")
    , hawaiian_red         (Category.clothes,   "clothes-hawaiian-red",   "Red Hawaiian", "Red Hawaiian shirt? \nBig fat Party animal in the house!")
    , hawaiian_blue         (Category.clothes,   "clothes-hawaiian-blue",   "Blue Hawaiian", "Blue Hawaiian shirt? \nBig fat  party animal in the house!")
//    , clothes_         (Category.clothes,   "",   "")
//    , clothes_         (Category.clothes,   "",   "")


    // TODO - eyes should be a separate thing? are we doing eye color vs eye-wear?
    , eyes_a           (Category.eye,       "eyes-a-idle",     "Plain Eyes", "It's not just me, right? \n\nThese eyes look super weird? ")
//    , glasses_a        (Category.eye,       "glasses-a",       "Glasses")
    , glasses_b        (Category.eye,       "glasses-b",       "Glasses", "This person has glasses. That's it. That's the joke.")
    , glasses_big        (Category.eye,       "glasses-big-a",       "Haute Couture", "The bigger the sunglasses, the colder the heart")
    , glasses_star        (Category.eye,       "glasses-star",       "Elton John Shades", "Elton John Shades? I guess THAT's why they call it the blues.\nBecause they're dead.")
    , glasses_cop        (Category.eye,       "glasses-reflective",       "Cop Shades", "Reflective sunglasses? I know what THIS one was doing on January 6")
    , glasses_aviator        (Category.eye,       "glasses-aviator",       "Aviators", "Aviators? \n\nI guess even cool people have to die sometime")
//    , eyepatch_a       (Category.eye,       "eyepatch-a",      "Yarrr")

//    , nose_normal      (Category.nose,      "feature-blank",   "Nose", "The nose knows.")
//    , nose_clown       (Category.nose,      "nose-clown",      "Clown", "")

//    , tongue           (Category.mouth,     "tongue",          "Tongue")
    , lips_blue           (Category.mouth,     "mouth-lips-blue",          "Blue Lips", "Man - blue lips make me uncomfortable. Not natural.")
    , lips_green           (Category.mouth,     "mouth-lips-green",          "Green Lips", "This lipstick is *extremely* unpleasant.")
    , lips_red           (Category.mouth,     "mouth-lips-red",          "Red Lips", "Strong \"Rocky Horror intro\" vibes from these lips.")
    , lips_plain           (Category.mouth,     "mouth-lips-plain",          "Plain Lips", "Hey, it's a normal-looking mouth for once!")

    , clean_shaven     (Category.hair_face, "feature-blank",   "No facial hair", "They won't need to shave ever again.")
    , mustache_a       (Category.hair_face, "mustache-a",      "Creeper 'stache", "With a 'stache like that, it's probably for the best he's gone.")
    , beard_beard       (Category.hair_face, "beard-beard",      "Full Beard", "With that beard, I hope they have IPAs where this guy's going.")
    , beard_goatee       (Category.hair_face, "beard-goatee",      "Goatee", "Look at that goatee! \n\nI guess the 90s really ARE coming back")
    , beard_soulpatch       (Category.hair_face, "beard-soulpatch",      "\"Soul\"-patch (ew)", "Unironic soulpatch? Yikes. Glad this one's gone.")
//    ,beard_beard       (Category.hair_face, "beard-",      "")
                                                                                        // Very funny jokes go here, need to figure out what the max line length is at the normal text size"
    , hair_bald        (Category.hair_head, "feature-blank",   "Shaved", "What a beautifully smooth dome!")
    , hair_long_brown  (Category.hair_head, "hair-long-brown",  "Long Brown", "This hair looks kinda like Mitch Hedberg, right?")
    , hair_long_londe  (Category.hair_head, "hair-long-blonde",  "Long Brown", "Are there influencers in the afterlife?")
    , hair_short_black (Category.hair_head, "hair-short-black", "Short Black", "Nothing funny about this dorky-ass haircut.")
    , hair_ponytail (Category.hair_head, "hair-ariana-ponytail", "Ari's Pony", "An Ariana Grande ponytail? In THIS economy?")
    , hair_balding (Category.hair_head, "hair-balding", "Balding", "He lost his hair, but at least he kept his dignity")
    , hair_curly_black (Category.hair_head, "hair-curly-black", "Natural", "That hair is bold and beautiful.")
    , hair_curly_red (Category.hair_head, "hair-curly-red", "Rocking the Red", "Red hair is SO in this season.")
    , hair_leia (Category.hair_head, "hair-leia-brown", "Princess Buns", "Aren't you a little short for a reaper?")
//    , hair_long_blue (Category.hair_head, "hair-long-blue", "Long Blue")
//    , hair_long_darkteal (Category.hair_head, "hair-long-darkteal", "Long Teal")
    , hair_manbun (Category.hair_head, "hair-manbun", "Man-bun (ew)","Manbun? Christ.\nI hope he won't miss kombucha")
    , hair_mohawk_blue (Category.hair_head, "hair-mohawk-blue", "Blue Hawk", "Mohawk? Guess it's Skate AND Die!")
//    , hair_mohawk_green (Category.hair_head, "hair-mohawk-green", "Green Hawk")
    , hair_mohawk_red (Category.hair_head, "hair-mohawk-red", "Red Hawk", "Mohawk? Guess it's Skate AND Die!")
    , hair_powdered_wig (Category.hair_head, "hair-powdered-wig", "Hamilton Cosplay","A powdered wig? Straight to hell for this Hamilton-cosplaying motherfucker")
//    , hair_ (Category.hair_head, "hair-", "hair")
//    , hair_ (Category.hair_head, "hair-", "hair")

    , hat_beret_green (Category.hair_head, "hat-beret-green", "Green Beret", "Green beret, huh? Must be ex-military. \n\nREALLY ex, now.")
    , hat_beret_red (Category.hair_head, "hat-beret-red", "Raspberry Beret", "Red beret? I guess THIS is what happened to the Guardian Angels")
    , hat_fez (Category.hair_head, "hat-fez", "Fez", "This hat looks 2D, but it's actually 3D.")
    , hat_hardhat (Category.hair_head, "hat-hardhat", "Hardhat", "Hard hat wasn't hard enough, I guess.")
    , hat_heisenberg (Category.hair_head, "hat-heisenberg", "Heisenberg", "*I* am the one who reaps")
    , hat_lombardi (Category.hair_head, "hat-lombardi", "Disheveled Hat", "This hat looks like something a bum would wear.")
    , hat_sheriff (Category.hair_head, "hat-sheriff", "Sheriff", "Sheriff's hat? \n\nRootin', tootin', no more shootin''")
    , hat_summer (Category.hair_head, "hat-summer", "White Lady Hat", "Did this hat belong to a farmer or a white girl? We may never know")
//    , hat_ (Category.hat, "hat-", "Hat")
//    , cigarette        (Category.mouth, "cigarette", "cigarette") // NOTE - not legible onscreen so removing it as a feature option
    ;

    public enum Category {
          clothes   (0)
        , eye       (1)
//        , nose      (2)
        , mouth     (3)
        , hair_face (4)
        , hair_head (5)
//        , hat       (6) // TODO - no 'hat' features, these were moved to hair
        , neck      (6) // TODO - no 'neck' features yet (like necklace or tie or something)
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
    public final String jokeText;

    Feature(Category category, String regionsName, String displayName, String jokeText) {
        this.category = category;
        this.regionsName = prefix + regionsName;
        this.displayName = displayName;
        this.jokeText = jokeText;
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

    // ------------------------------------------------------------------------
    // Fun names

    public static String getRandomCharacterName() {
        return characterNames[MathUtils.random(0, characterNames.length - 1)];
    }

    private static final String[] characterNames = new String[] {
              "Bill Windows"
            , "Steve Apple"
            , "Elon Stank"
            , "Mark Sugarberg"
            , "Sam Bankrun-Fired"
            , "Elizabeth Sherlock"
            , "Snorin Buffet"
            , "Tom Bones"
            , "Liam McPoyle"
            , "Big Richard"
            , "Walter Heisenberg"
            , "Max Power"
            , "Tiny Tom"
            , "Urist Dwarvenson"
            , "Geoff Bezers"
            , "Estelle Deddy"
            , "Dua Reapa"
            , "Queen Elizabeth"
            , "Princess Die-ana"
            , "JKLOL Rowling"
            , "Noprah"
            , "Halle Buried"
            , "Beyond Knowles"
            // TODO - moar....
    };

}
