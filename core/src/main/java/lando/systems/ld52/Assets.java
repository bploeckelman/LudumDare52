package lando.systems.ld52;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.*;
import lando.systems.ld52.assets.EffectAnims;
import lando.systems.ld52.assets.Feature;
import lando.systems.ld52.assets.Head;
import lando.systems.ld52.assets.InputPrompts;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Assets implements Disposable {

    public enum Load { ASYNC, SYNC }

    public boolean initialized;

    public SpriteBatch batch;
    public ShapeDrawer shapes;
    public GlyphLayout layout;
    public AssetManager mgr;
    public TextureAtlas atlas;
    public Particles particles;
    public EffectAnims effectAnims;

    public I18NBundle strings;
    public InputPrompts inputPrompts;

    public BitmapFont font;
    public BitmapFont smallFont;
    public BitmapFont largeFont;

    public Texture pixel;
    public Texture gameScreenLayout;
    public Texture cutsceneBackground;
    public Texture cutscene0;
    public Texture cutscene1;
    public Texture cutscene2;
    public Texture cutscene3;
    public TextureRegion circleTex;
    public TextureRegion pixelRegion;
    public TextureRegion hourglassTex;
    public TextureRegion skull;

    public Animation<TextureRegion> cherry;
    public Animation<TextureRegion> asuka;
    public Animation<TextureRegion> osha;
    public Animation<TextureRegion> rossDog;
    public Animation<TextureRegion> whiteLab;

    public Animation<TextureRegion> playerWithScythe;
    public Animation<TextureRegion> playerNoScythe;
    public Animation<TextureRegion> scythe;
    public Animation<TextureRegion> tombstone;
    public Animation<TextureRegion> powerup;
    public Animation<TextureRegion> powerdown;
    public Animation<TextureRegion> cornerIdle;
    public Animation<TextureRegion> cornerAction;
    public Animation<TextureRegion> angel;
    public Animation<TextureRegion> devil;
    public Animation<TextureRegion> beer;
    public Animation<TextureRegion> halo;
    public Animation<TextureRegion> horns;
    public Animation<TextureRegion> tvOn;
    public TextureRegion chair;
    public TextureRegion tvOff;
    public TextureRegion beerPack;


    public Array<Animation<TextureRegion>> numberParticles;

    public ObjectMap<Head, Animation<TextureRegion>> heads;
    public ObjectMap<Feature, Animation<TextureRegion>> features;

    public Sound settingSound;
    public Sound swoosh1;
    public Sound charge1;
    public Sound poof1;
    public Sound clock1;
    public Sound clock2;
    public Sound clock3;
    public Sound clock4;
    public Sound soulReap1;
    public Sound thud1;
    public Sound bell1;
    public Sound heaven1;
    public Sound hell1;

    public Music mainTheme;
    public Music mutedMainTheme;

    public ShaderProgram hourglassShader;
    public ShaderProgram harvestShader;

    public enum Patch {
        debug, panel, metal, glass,
        glass_green, glass_yellow, glass_dim, glass_active;
        public NinePatch ninePatch;
        public NinePatchDrawable drawable;
    }

    public static class Particles {
        public TextureRegion circle;
        public TextureRegion sparkle;
        public TextureRegion smoke;
        public TextureRegion ring;
        public TextureRegion dollar;
        public TextureRegion blood;
        public TextureRegion sparks;
        public TextureRegion line;
    }


    public static class NinePatches {
        public static NinePatch plain;
        public static NinePatch plain_dim;
        public static NinePatch plain_gradient;
        public static NinePatch plain_gradient_highlight_yellow;
        public static NinePatch plain_gradient_highlight_green;
        public static NinePatch plain_gradient_highlight_red;
        public static NinePatch glass;
        public static NinePatch glass_active;
        public static NinePatch glass_blue;
        public static NinePatch glass_light_blue;
        public static NinePatch glass_corner_bl;
        public static NinePatch glass_corner_br;
        public static NinePatch glass_corner_tl;
        public static NinePatch glass_corner_tr;
        public static NinePatch glass_corners;
        public static NinePatch glass_red;
        public static NinePatch glass_yellow;
        public static NinePatch glass_green;
        public static NinePatch glass_tab;
        public static NinePatch glass_dim;
        public static NinePatch metal;
        public static NinePatch metal_blue;
        public static NinePatch metal_green;
        public static NinePatch metal_yellow;
        public static NinePatch panel_grey;
        public static NinePatch shear;
    }

    public Assets() {
        this(Load.SYNC);
    }

    public Assets(Load load) {
        initialized = false;

        // create a single pixel texture and associated region
        Pixmap pixmap = new Pixmap(2, 2, Pixmap.Format.RGBA8888);
        {
            pixmap.setColor(Color.WHITE);
            pixmap.drawPixel(0, 0);
            pixmap.drawPixel(1, 0);
            pixmap.drawPixel(0, 1);
            pixmap.drawPixel(1, 1);
            pixel = new Texture(pixmap);
        }
        pixmap.dispose();
        pixelRegion = new TextureRegion(pixel);

        batch = new SpriteBatch();
        shapes = new ShapeDrawer(batch, pixelRegion);
        layout = new GlyphLayout();

        TextureLoader.TextureParameter walkpathTexParams = new TextureLoader.TextureParameter();
        walkpathTexParams.minFilter = Texture.TextureFilter.Linear;
        walkpathTexParams.magFilter = Texture.TextureFilter.Linear;
        walkpathTexParams.wrapU = Texture.TextureWrap.Repeat;
        walkpathTexParams.wrapV = Texture.TextureWrap.Repeat;

        TextureLoader.TextureParameter linearTexParams = new TextureLoader.TextureParameter();
        linearTexParams.minFilter = Texture.TextureFilter.Linear;
        linearTexParams.magFilter = Texture.TextureFilter.Linear;

        mgr = new AssetManager();
        {
            mgr.load("sprites/sprites.atlas", TextureAtlas.class);
            mgr.load("ui/uiskin.json", Skin.class);
            mgr.load("i18n/strings", I18NBundle.class);

            mgr.load("fonts/outfit-medium-20px.fnt", BitmapFont.class);
            mgr.load("fonts/outfit-medium-40px.fnt", BitmapFont.class);
            mgr.load("fonts/outfit-medium-80px.fnt", BitmapFont.class);

            mgr.load("images/walkpath-left-clouds_00.png", Texture.class, walkpathTexParams);
            mgr.load("images/walkpath-right-clouds_00.png", Texture.class, walkpathTexParams);
            mgr.load("images/walkpath-top-clouds_00.png", Texture.class, walkpathTexParams);
            mgr.load("images/walkpath-bottom-clouds_00.png", Texture.class, walkpathTexParams);

            mgr.load("images/layout-alpha-1.png", Texture.class);

            mgr.load("audio/sounds/settingSound.ogg", Sound.class);
            mgr.load("audio/sounds/swoosh1.ogg", Sound.class);
            mgr.load("audio/sounds/charge1.ogg", Sound.class);
            mgr.load("audio/sounds/poof1.ogg", Sound.class);
            mgr.load("audio/sounds/clock1.ogg", Sound.class);
            mgr.load("audio/sounds/clock2.ogg", Sound.class);
            mgr.load("audio/sounds/clock3.ogg", Sound.class);
            mgr.load("audio/sounds/clock4.ogg", Sound.class);
            mgr.load("audio/sounds/soulreap1.ogg", Sound.class);
            mgr.load("audio/sounds/thud1.ogg", Sound.class);
            mgr.load("audio/sounds/bell1.ogg", Sound.class);
            mgr.load("audio/sounds/heaven1.ogg", Sound.class);
            mgr.load("audio/sounds/hell1.ogg", Sound.class);
//            mgr.load("audio/sounds/.ogg", Sound.class);
//            mgr.load("audio/sounds/.ogg", Sound.class);
//            mgr.load("audio/sounds/.ogg", Sound.class);

            mgr.load("audio/music/maintheme.ogg", Music.class);
            mgr.load("audio/music/mainthemeLowpass.ogg", Music.class);

            mgr.load("images/story-background_00.png", Texture.class, linearTexParams);
            mgr.load("images/cutscene_0.png", Texture.class, linearTexParams);
            mgr.load("images/cutscene_1.png", Texture.class, linearTexParams);
            mgr.load("images/cutscene_2.png", Texture.class, linearTexParams);
            mgr.load("images/cutscene_3.png", Texture.class, linearTexParams);
        }

        if (load == Load.SYNC) {
            mgr.finishLoading();
            updateLoading();
        }
    }

    public float updateLoading() {
        if (!mgr.update()) return mgr.getProgress();
        if (initialized) return 1;

        hourglassShader = loadShader("shaders/default.vert", "shaders/hourglass.frag");
        harvestShader = loadShader("shaders/default.vert", "shaders/harvest.frag");

        gameScreenLayout = mgr.get("images/layout-alpha-1.png", Texture.class);

        atlas = mgr.get("sprites/sprites.atlas");
        strings = mgr.get("i18n/strings", I18NBundle.class);
        inputPrompts = new InputPrompts(this);
        effectAnims = new EffectAnims(this);

        smallFont = mgr.get("fonts/outfit-medium-20px.fnt");
        font      = mgr.get("fonts/outfit-medium-40px.fnt");
        font.setUseIntegerPositions(false);
        largeFont = mgr.get("fonts/outfit-medium-80px.fnt");

        circleTex = atlas.findRegion("particles/circle");

        cherry = new Animation<>(0.1f, atlas.findRegions("pets/cat"), Animation.PlayMode.LOOP);
        asuka = new Animation<>(0.1f, atlas.findRegions("pets/dog"), Animation.PlayMode.LOOP);
        osha = new Animation<>(.1f, atlas.findRegions("pets/kitten"), Animation.PlayMode.LOOP);
        angel = new Animation<>(.1f, atlas.findRegions("particles/angels/angel"), Animation.PlayMode.LOOP);
        devil = new Animation<>(.1f, atlas.findRegions("particles/devils/devil"), Animation.PlayMode.LOOP);
        rossDog = new Animation<>(.1f, atlas.findRegions("pets/ross-dog"), Animation.PlayMode.LOOP);
        whiteLab = new Animation<>(.1f, atlas.findRegions("pets/white-lab-dog"), Animation.PlayMode.LOOP);

        beer = new Animation<>(.1f, atlas.findRegions("objects/items-beer/items-beer/items-beer"), Animation.PlayMode.LOOP);
        chair = atlas.findRegion("objects/items-chair/items-chair", 0);
        horns = new Animation<>(.1f, atlas.findRegions("objects/items-horns-halo/items-horns"), Animation.PlayMode.LOOP);
        halo = new Animation<>(.1f, atlas.findRegions("objects/items-horns-halo/items-halo"), Animation.PlayMode.LOOP);
        tvOn = new Animation<>(.1f, atlas.findRegions("objects/items-tv/items-tv-on/items-tv-on"), Animation.PlayMode.LOOP);
        tvOff = atlas.findRegion("objects/items-tv/items-tv-idle", 0);
        beerPack = atlas.findRegion("objects/items-beer/items-beer-6pack/items-6pack", 0);

        cutsceneBackground = mgr.get("images/story-background_00.png");
        cutscene0 = mgr.get("images/cutscene_0.png");
        cutscene1 = mgr.get("images/cutscene_1.png");
        cutscene2 = mgr.get("images/cutscene_2.png");
        cutscene3 = mgr.get("images/cutscene_3.png");
        skull = atlas.findRegion("skull", 0);

        playerWithScythe = new Animation<>(.2f, atlas.findRegions("player/reapo-idle-a"), Animation.PlayMode.LOOP);
        playerNoScythe = new Animation<>(.2f, atlas.findRegions("player/reapo-idle-noweapon"), Animation.PlayMode.LOOP);
        
        scythe = new Animation<>(.2f, atlas.findRegions("player/scythe"), Animation.PlayMode.LOOP);
        tombstone = new Animation<>(.2f, atlas.findRegions("objects/tombstone"), Animation.PlayMode.LOOP);
        powerup = new Animation<>(.2f, atlas.findRegions("objects/powerup"), Animation.PlayMode.LOOP);
        powerdown = new Animation<>(.2f, atlas.findRegions("objects/powerdown"), Animation.PlayMode.LOOP);
        cornerIdle = new Animation<>(0.1f, atlas.findRegions("world/walkpath-corner-a-idle/walkpath-corner-a-idle"), Animation.PlayMode.LOOP);
        cornerAction = new Animation<>(0.06f, atlas.findRegions("world/walkpath-corner-a-action/walkpath-corner-a-action"), Animation.PlayMode.NORMAL);

        heads = new ObjectMap<>();
        for (Head head : Head.values()) {
            Animation<TextureRegion> animation = new Animation<>(0.1f, atlas.findRegions(head.regionsName), Animation.PlayMode.LOOP);
            heads.put(head, animation);
        }

        features = new ObjectMap<>();
        for (Feature feature : Feature.values()) {
            Animation<TextureRegion> animation = new Animation<>(0.1f, atlas.findRegions(feature.regionsName), Animation.PlayMode.LOOP);
            features.put(feature, animation);
        }

        // initialize patch values
        Patch.debug.ninePatch        = new NinePatch(atlas.findRegion("ninepatch/debug"), 2, 2, 2, 2);
        Patch.panel.ninePatch        = new NinePatch(atlas.findRegion("ninepatch/panel"), 15, 15, 15, 15);
        Patch.glass.ninePatch        = new NinePatch(atlas.findRegion("ninepatch/glass"), 8, 8, 8, 8);
        Patch.glass_green.ninePatch  = new NinePatch(atlas.findRegion("ninepatch/glass-green"), 8, 8, 8, 8);
        Patch.glass_yellow.ninePatch = new NinePatch(atlas.findRegion("ninepatch/glass-yellow"), 8, 8, 8, 8);
        Patch.glass_dim.ninePatch    = new NinePatch(atlas.findRegion("ninepatch/glass-dim"), 8, 8, 8, 8);
        Patch.glass_active.ninePatch = new NinePatch(atlas.findRegion("ninepatch/glass-active"), 8, 8, 8, 8);
        Patch.metal.ninePatch        = new NinePatch(atlas.findRegion("ninepatch/metal"), 12, 12, 12, 12);

        Patch.debug.drawable        = new NinePatchDrawable(Patch.debug.ninePatch);
        Patch.panel.drawable        = new NinePatchDrawable(Patch.panel.ninePatch);
        Patch.glass.drawable        = new NinePatchDrawable(Patch.glass.ninePatch);
        Patch.glass_green.drawable  = new NinePatchDrawable(Patch.glass_green.ninePatch);
        Patch.glass_yellow.drawable = new NinePatchDrawable(Patch.glass_yellow.ninePatch);
        Patch.glass_dim.drawable    = new NinePatchDrawable(Patch.glass_dim.ninePatch);
        Patch.glass_active.drawable = new NinePatchDrawable(Patch.glass_active.ninePatch);
        Patch.metal.drawable        = new NinePatchDrawable(Patch.metal.ninePatch);

        NinePatches.plain_dim                       = new NinePatch(atlas.findRegion("ninepatch/plain-dim"),               12, 12, 12, 12);
        NinePatches.plain_gradient                  = new NinePatch(atlas.findRegion("ninepatch/plain-gradient"),           2,  2,  2,  2);
        NinePatches.plain_gradient_highlight_yellow = new NinePatch(atlas.findRegion("ninepatch/plain-gradient-highlight-yellow"), 2,  2,  2,  2);
        NinePatches.plain_gradient_highlight_green  = new NinePatch(atlas.findRegion("ninepatch/plain-gradient-highlight-green"), 2,  2,  2,  2);
        NinePatches.plain_gradient_highlight_red    = new NinePatch(atlas.findRegion("ninepatch/plain-gradient-highlight-red"), 2,  2,  2,  2);
        NinePatches.glass                           = new NinePatch(atlas.findRegion("ninepatch/glass"),                   12, 12, 12, 12);
        NinePatches.glass_blue                      = new NinePatch(atlas.findRegion("ninepatch/glass-blue"),              12, 12, 12, 12);
        NinePatches.glass_light_blue                = new NinePatch(atlas.findRegion("ninepatch/glass"),                   12, 12, 12, 12);
        NinePatches.glass_active                    = new NinePatch(atlas.findRegion("ninepatch/glass-active"),            12, 12, 12, 12);
        NinePatches.glass_corner_bl                 = new NinePatch(atlas.findRegion("ninepatch/glass-corner-bl"),         12, 12, 12, 12);
        NinePatches.glass_corner_br                 = new NinePatch(atlas.findRegion("ninepatch/glass-corner-br"),         12, 12, 12, 12);
        NinePatches.glass_corner_tl                 = new NinePatch(atlas.findRegion("ninepatch/glass-corner-tl"),         12, 12, 12, 12);
        NinePatches.glass_corner_tr                 = new NinePatch(atlas.findRegion("ninepatch/glass-corner-tr"),         12, 12, 12, 12);
        NinePatches.glass_corners                   = new NinePatch(atlas.findRegion("ninepatch/glass-corners"),           12, 12, 12, 12);
        NinePatches.glass_red                       = new NinePatch(atlas.findRegion("ninepatch/glass-red"),               12, 12, 12, 12);
        NinePatches.glass_yellow                    = new NinePatch(atlas.findRegion("ninepatch/glass-yellow"),            12, 12, 12, 12);
        NinePatches.glass_green                     = new NinePatch(atlas.findRegion("ninepatch/glass-green"),             12, 12, 12, 12);
        NinePatches.glass_tab                       = new NinePatch(atlas.findRegion("ninepatch/glass-tab"),               12, 12, 22, 12);
        NinePatches.glass_dim                       = new NinePatch(atlas.findRegion("ninepatch/glass-dim"),               12, 12, 22, 12);
        NinePatches.metal                           = new NinePatch(atlas.findRegion("ninepatch/metal"),                   12, 12, 12, 12);
        NinePatches.metal_blue                      = new NinePatch(atlas.findRegion("ninepatch/metal-blue"),              12, 12, 12, 12);
        NinePatches.metal_green                     = new NinePatch(atlas.findRegion("ninepatch/metal-green"),             12, 12, 12, 12);
        NinePatches.metal_yellow                    = new NinePatch(atlas.findRegion("ninepatch/metal-yellow"),            12, 12, 12, 12);
        NinePatches.panel_grey                      = new NinePatch(atlas.findRegion("kenney-uipack/grey_panel"),           8,  8,  8,  8);
        NinePatches.shear                           = new NinePatch(atlas.findRegion("ninepatch/shear"),                   75, 75, 12, 12);

        // initialize particle images
        particles = new Particles();
        particles.circle  = atlas.findRegion("particles/circle");
        particles.ring    = atlas.findRegion("particles/ring");
        particles.smoke   = atlas.findRegion("particles/smoke");
        particles.sparkle = atlas.findRegion("particles/sparkle");
        particles.dollar  = atlas.findRegion("particles/dollars");
        particles.blood   = atlas.findRegion("characters/blood-stain");
        particles.sparks  = atlas.findRegion("particles/sparks");
        particles.line    = atlas.findRegion("particles/line");
        numberParticles = new Array<>();
        for (int i = 0; i <= 9; ++i) {
            numberParticles.add(new Animation<>(0.1f, atlas.findRegions("particles/font-points-" + i)));
        }

        hourglassTex = atlas.findRegion("ui/hourglass-shader");

        settingSound = mgr.get("audio/sounds/settingSound.ogg", Sound.class);
         swoosh1 = mgr.get("audio/sounds/swoosh1.ogg", Sound.class);
         charge1 = mgr.get("audio/sounds/charge1.ogg", Sound.class);
         poof1 = mgr.get("audio/sounds/poof1.ogg", Sound.class);
        clock1 = mgr.get("audio/sounds/clock1.ogg", Sound.class);
        clock2 = mgr.get("audio/sounds/clock2.ogg", Sound.class);
        clock3 = mgr.get("audio/sounds/clock3.ogg", Sound.class);
        clock4 = mgr.get("audio/sounds/clock4.ogg", Sound.class);
         soulReap1 = mgr.get("audio/sounds/soulreap1.ogg", Sound.class);
         thud1 = mgr.get("audio/sounds/thud1.ogg", Sound.class);
         bell1 = mgr.get("audio/sounds/bell1.ogg", Sound.class);
         heaven1 = mgr.get("audio/sounds/heaven1.ogg", Sound.class);
         hell1 = mgr.get("audio/sounds/hell1.ogg", Sound.class);
//         = mgr.get("audio/sounds/.ogg", Sound.class);

        mainTheme = mgr.get("audio/music/maintheme.ogg", Music.class);
        mutedMainTheme = mgr.get("audio/music/mainthemeLowpass.ogg", Music.class);

        initialized = true;
        return 1;
    }

    @Override
    public void dispose() {
        mgr.dispose();
        batch.dispose();
        pixel.dispose();
    }

    public static ShaderProgram loadShader(String vertSourcePath, String fragSourcePath) {
        ShaderProgram.pedantic = false;
        ShaderProgram shaderProgram = new ShaderProgram(
                Gdx.files.internal(vertSourcePath),
                Gdx.files.internal(fragSourcePath));
        String log = shaderProgram.getLog();

        if (!shaderProgram.isCompiled()) {
            Gdx.app.error("LoadShader", "compilation failed:\n" + log);
            throw new GdxRuntimeException("LoadShader: compilation failed:\n" + log);
        } else if (Config.Debug.shaders) {
            Gdx.app.debug("LoadShader", "ShaderProgram compilation log: " + log);
        }

        return shaderProgram;
    }

}
