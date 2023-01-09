package lando.systems.ld52.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld52.Assets;
import lando.systems.ld52.Config;
import lando.systems.ld52.audio.AudioManager;
import lando.systems.ld52.ui.SettingsUI;

public class IntroStoryScreen extends BaseScreen {

    private boolean exitingScreen = false;
    private int clickPhase;
    private float phaseAccum;
    private float storyAccum;
    private String subtitles;
    private Texture cutsceneTexture;
    private Texture backgroundTexture;
    private Color whiteWithAlpha;
    private boolean isStoryOver = false;
    private boolean isTutorialShown = false;
    private String clickToAdvance;

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

        subtitles = " ";
        clickToAdvance = "(click to advance)";
        backgroundTexture = game.assets.cutsceneBackground;
        cutsceneTexture = game.assets.cutscene0;

        ChangeListener listener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getScreenManager().pushScreen("game", TransitionManager.TransitionType.PAGE_CURL.name());
            }
        };
    }

    @Override
    public void show() {
        super.show();
        game.getInputMultiplexer().addProcessor(uiStage);
        game.audioManager.playMusic(AudioManager.Musics.mutedMainTheme);
    }

    @Override
    public void hide() {
        game.getInputMultiplexer().removeProcessor(uiStage);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        phaseAccum += delta;

        if (((Gdx.input.justTouched() && phaseAccum > .2f) || phaseAccum > .25F)&& !isStoryOver) {

            storyAccum += delta * 3;

            if (((Gdx.input.justTouched() && phaseAccum > .2f)) && !isStoryOver) {

                // todo cancel playing sounds
                game.audioManager.stopAllSounds();

                phaseAccum = 0;


                switch (clickPhase) {
                    case 0:
                        subtitles = "Another day, another shift at Reapo Depot...\n\n " +
                        "Not the most satisfying job in the world, but hey - it's a living!\n\n";
                        break;
                    case 1:
                        cutsceneTexture = game.assets.cutscene0;
                        subtitles =
                                "Not a literal living, of course. \n\n" +
                                "Very little of that going on here.\n\n";
                        break;
                    case 2:
                        cutsceneTexture = game.assets.cutscene1;
                        subtitles = "What we do, kind of the exact opposite, really. More like, un-living.\n\n" +
                                "Unliving humans, that's the deal here.";

                        break;
                    case 3:
                        cutsceneTexture = game.assets.cutscene2;


                        subtitles = "Reaping, crossing over, harvesting souls... Doesn't really matter WHAT you call it. " +
                                "Bottom line is, we got a lot of souls need be harvested these days. Too many, if you ask me.\n\n" +
                                "How many people on Earth these days? Numbers we're seeing, must be two, three thousand at least, huh?";

                        break;
                    case 4:
                        cutsceneTexture = game.assets.cutscene3;
                        subtitles = "8 BILLION? Jeez. Okay\n\n"+
                        "No wonder they've been \"encouraging\" us to work overtime.";
                        break;
                    case 5:
                        cutsceneTexture = game.assets.cutscene3;
                        subtitles = "Honestly, whole gig's wearing a bit thin if you ask me.\n\n"+
                        "Honestly? I don't even ";
                        break;
                    default:
                        isStoryOver = true;
                        game.getScreenManager().pushScreen("game", TransitionManager.TransitionType.PAGE_CURL.name());
                        break;
                }
                clickPhase++;
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
            batch.draw(backgroundTexture, 0, 300, windowCamera.viewportWidth, 600);
            batch.draw(cutsceneTexture, windowCamera.viewportWidth / 2 - 500f / 2, 220f, 500f, 500f);

            assets.largeFont.getData().setScale(.3f);
            assets.largeFont.setColor(whiteWithAlpha);
            assets.layout.setText(assets.largeFont, subtitles, whiteWithAlpha, camera.viewportWidth, Align.left, true);
            assets.largeFont.draw(batch, assets.layout, 120, camera.viewportHeight * .05f + assets.layout.height);

            assets.largeFont.getData().setScale(.23f);
            assets.largeFont.setColor(whiteWithAlpha);
            assets.layout.setText(assets.largeFont, clickToAdvance, Color.DARK_GRAY, camera.viewportWidth, Align.right, true);
            assets.largeFont.draw(batch, assets.layout, -20, camera.viewportHeight * .02f + assets.layout.height);

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

        SettingsUI settingsUI = new SettingsUI(assets, skin, audioManager, windowCamera);

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
                game.getScreenManager().pushScreen("game", TransitionManager.TransitionType.PAGE_CURL.name());
            }
        });
        uiStage.addActor(startGameButton);
    }
}
