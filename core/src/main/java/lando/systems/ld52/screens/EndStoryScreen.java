package lando.systems.ld52.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld52.Assets;
import lando.systems.ld52.Config;
import lando.systems.ld52.gameobjects.Stats;
import lando.systems.ld52.particles.Particles;
import lando.systems.ld52.utils.Utils;

public class EndStoryScreen extends BaseScreen {

    private boolean exitingScreen = false;
    private int clickPhase;
    private float phaseAccum;
    private float storyAccum;
    private String subtitles;
    private Texture backgroundTexture;
    private Color whiteWithAlpha;
    private boolean isStoryOver = false;
    private boolean showBeer = false;
    private float beerCount = 50;

    private float y = 10;

    @Override
    protected void create() {
        super.create();
        OrthographicCamera worldCam = (OrthographicCamera) worldCamera;
        worldCam.setToOrtho(false, Config.Screen.window_width, Config.Screen.window_height);
        worldCam.update();
        whiteWithAlpha = new Color(Color.WHITE);
        clickPhase = 0;
        phaseAccum = 0;

        storyAccum = 0;

        subtitles = "Finally! Time to kick back and relax!";
        backgroundTexture = game.assets.cutsceneBackground;

    }

    @Override
    public void show() {
        super.show();
        game.getInputMultiplexer().addProcessor(uiStage);
        whiteWithAlpha = new Color(Color.WHITE);
        clickPhase = 0;
        phaseAccum = 0;

        storyAccum = 0;

        subtitles = "Finally! Time to kick back and relax!";
        backgroundTexture = game.assets.cutsceneBackground;
//        game.audioManager.playMusic(AudioManager.Musics.mutedMainTheme);
    }

    @Override
    public void hide() {
        game.getInputMultiplexer().removeProcessor(uiStage);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        phaseAccum += delta;

        if (((Gdx.input.justTouched() && phaseAccum > .2f) || phaseAccum > 7.25F)&& !isStoryOver) {

            storyAccum += delta * 10;

            if (((Gdx.input.justTouched() && phaseAccum > .2f) || phaseAccum > 6F) && !isStoryOver) {

                // todo cancel playing sounds
                game.audioManager.stopAllSounds();

                phaseAccum = 0;
                clickPhase++;
                switch (clickPhase) {
                    case 1:
                        subtitles = "Can't wait to enjoy my favorite reaper leisure activities in my afterlife ";
                        showBeer = true;
                        break;
                    default:
                        isStoryOver = true;
                        game.getScreenManager().pushScreen("credit", TransitionManager.TransitionType.CROSSHATCH.name());
                        break;
                }
            }
        }

    }

    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(Color.BLACK);

        float alpha = MathUtils.clamp(phaseAccum * 3.0f, 0f, 1f );
        whiteWithAlpha.set(alpha, alpha, alpha, alpha);
        OrthographicCamera camera = windowCamera;
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        {
            batch.setColor(whiteWithAlpha);
            if (Stats.heavenQuotaMet == Stats.hellQuotaMet) {
                batch.draw(Utils.getColoredTextureRegion(Color.GRAY), 0, 200, windowCamera.viewportWidth, 800);
                assets.layout.setText(assets.largeFont, "PURGATORY", whiteWithAlpha, camera.viewportWidth, Align.left, false);
                assets.largeFont.draw(batch, assets.layout, 0, camera.viewportHeight * 6 / 7f + assets.layout.height);
            } else {
                batch.draw(backgroundTexture, 0, 200, windowCamera.viewportWidth, 800);
            }
            if (Stats.heavenQuotaMet > Stats.hellQuotaMet) {
                batch.draw(assets.halo.getKeyFrame(phaseAccum), 240, 400, 200, 300);
            } else if (Stats.heavenQuotaMet < Stats.hellQuotaMet) {
                batch.draw(assets.horns.getKeyFrame(phaseAccum), 240, 400, 200, 325);
            }
            batch.draw(assets.chair, 50, 200, 200f, 200f);
            batch.draw(assets.playerNoScythe.getKeyFrame(phaseAccum), 250, 300, 250f, 250f);
            batch.draw(assets.beerPack, 245, 200, 100f, 100f);
            if (clickPhase < 1) {
                batch.draw(assets.tvOff, 900, 200, 300f, 300f);
            } else {
                batch.draw(assets.tvOn.getKeyFrame(phaseAccum), 900, 200, 300f, 300f);
            }
            if (showBeer) {
                for (int i = 0; i < 400; i++) {
                    game.particles.flyUp(assets.beer, MathUtils.random(0, windowCamera.viewportWidth), MathUtils.random(100, windowCamera.viewportHeight));
                }
                showBeer = false;
            }
            game.particles.draw(batch, Particles.Layer.foreground);
            assets.largeFont.getData().setScale(.3f);
            assets.largeFont.setColor(whiteWithAlpha);
            assets.layout.setText(assets.largeFont, subtitles, whiteWithAlpha, camera.viewportWidth, Align.left, false);
            assets.largeFont.draw(batch, assets.layout, 10, camera.viewportHeight / 7f + assets.layout.height);
            assets.largeFont.getData().setScale(1f);
            assets.largeFont.setColor(Color.WHITE);

            batch.setColor(Color.WHITE);
        }
        batch.end();
        uiStage.draw();
    }

    @Override
    public void initializeUI() {
        final float BUTTON_WIDTH = 180f;
        final float BUTTON_HEIGHT = 50f;
        super.initializeUI();
        TextButton.TextButtonStyle outfitMediumStyle = skin.get("text", TextButton.TextButtonStyle.class);
        TextButton.TextButtonStyle titleScreenButtonStyle = new TextButton.TextButtonStyle(outfitMediumStyle);
        titleScreenButtonStyle.font = assets.smallFont;
        titleScreenButtonStyle.fontColor = Color.WHITE;
        titleScreenButtonStyle.up = Assets.Patch.glass.drawable;
        titleScreenButtonStyle.down = Assets.Patch.glass_dim.drawable;
        titleScreenButtonStyle.over = Assets.Patch.glass_dim.drawable;

        float left = windowCamera.viewportWidth - 50f - BUTTON_WIDTH;
        float top = windowCamera.viewportHeight - 50f - BUTTON_HEIGHT;

        TextButton startGameButton = new TextButton("Skip", titleScreenButtonStyle);
//        Gdx.app.log("startbuttonwidth&height", "width: " + startGameButton.getWidth() + " & height: " + startGameButton.getHeight());
        startGameButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        startGameButton.setPosition(left, top);
        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getScreenManager().pushScreen("credit", TransitionManager.TransitionType.CROSSHATCH.name());
            }
        });
        uiStage.addActor(startGameButton);
    }
}
