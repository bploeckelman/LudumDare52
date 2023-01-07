package lando.systems.ld52;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.ObjectMap;
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

    public I18NBundle strings;
    public InputPrompts inputPrompts;

    public BitmapFont font;
    public BitmapFont smallFont;
    public BitmapFont largeFont;

    public Texture pixel;
    public Texture gameScreenLayout;
    public TextureRegion circleTex;
    public TextureRegion pixelRegion;


    public Animation<TextureRegion> cat;
    public Animation<TextureRegion> dog;
    public Animation<TextureRegion> kitten;

    public Animation<TextureRegion> playerFront;
    public Animation<TextureRegion> playerSide;
    public Animation<TextureRegion> playerBack;
    public Animation<TextureRegion> scythe;

    public ObjectMap<Head, Animation<TextureRegion>> heads;
    public ObjectMap<Feature, Animation<TextureRegion>> features;

    public Sound settingSound;

    public enum Patch {
        debug, panel, metal, glass,
        glass_green, glass_yellow, glass_dim, glass_active;
        public NinePatch ninePatch;
        public NinePatchDrawable drawable;
    }

    public Assets() {
        this(Load.SYNC);
    }

    public Assets(Load load) {
        initialized = false;

        // create a single pixel texture and associated region
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        {
            pixmap.setColor(Color.WHITE);
            pixmap.drawPixel(0, 0);
            pixel = new Texture(pixmap);
        }
        pixmap.dispose();
        pixelRegion = new TextureRegion(pixel);

        batch = new SpriteBatch();
        shapes = new ShapeDrawer(batch, pixelRegion);
        layout = new GlyphLayout();

        mgr = new AssetManager();
        {
            mgr.load("sprites/sprites.atlas", TextureAtlas.class);
            mgr.load("ui/uiskin.json", Skin.class);
            mgr.load("i18n/strings", I18NBundle.class);

            mgr.load("fonts/outfit-medium-20px.fnt", BitmapFont.class);
            mgr.load("fonts/outfit-medium-40px.fnt", BitmapFont.class);
            mgr.load("fonts/outfit-medium-80px.fnt", BitmapFont.class);

            mgr.load("audio/sounds/settingSound.ogg", Sound.class);

            mgr.load("images/layout-alpha-1.png", Texture.class);
        }

        if (load == Load.SYNC) {
            mgr.finishLoading();
            updateLoading();
        }
    }

    public float updateLoading() {
        if (!mgr.update()) return mgr.getProgress();
        if (initialized) return 1;

        gameScreenLayout = mgr.get("images/layout-alpha-1.png", Texture.class);

        atlas = mgr.get("sprites/sprites.atlas");
        strings = mgr.get("i18n/strings", I18NBundle.class);
        inputPrompts = new InputPrompts(this);

        smallFont = mgr.get("fonts/outfit-medium-20px.fnt");
        font      = mgr.get("fonts/outfit-medium-40px.fnt");
        largeFont = mgr.get("fonts/outfit-medium-80px.fnt");

        circleTex = atlas.findRegion("particles/circle");

        cat = new Animation<>(0.1f, atlas.findRegions("pets/cat"), Animation.PlayMode.LOOP);
        dog = new Animation<>(0.1f, atlas.findRegions("pets/dog"), Animation.PlayMode.LOOP);
        kitten = new Animation<>(.1f, atlas.findRegions("pets/kitten"), Animation.PlayMode.LOOP);

        playerFront = new Animation<>(.2f, atlas.findRegions("player/reaper"), Animation.PlayMode.LOOP);
        playerSide = new Animation<>(.2f, atlas.findRegions("player/reaper"), Animation.PlayMode.LOOP);
        playerBack = new Animation<>(.2f, atlas.findRegions("player/reaper"), Animation.PlayMode.LOOP);
        scythe = new Animation<>(.2f, atlas.findRegions("player/scythe"), Animation.PlayMode.LOOP);

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

        settingSound = mgr.get("audio/sounds/settingSound.ogg", Sound.class);

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
