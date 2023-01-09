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
        clickPhase = 1;
        phaseAccum = 0;

        storyAccum = 0;

        subtitles = "Another day, another shift at Reapo Depot...\n\n " +
                "Not the most satisfying job in the world, but hey - it's a living!\n\n";
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


                        subtitles = " Reaping, crossing over, ferrying spirits... Doesn't really matter WHAT you call it.\n\n " +
                                "Bottom line, lot of souls need to be harvested these days. \n\n" +
                                "Too many, if you ask me."
                               ;

                        break;
                        case 4:
                        cutsceneTexture = game.assets.cutscene2;


                        subtitles = "How many people we got on Earth these days, anyway? " +
                                    "Seven, eight thousand?\n\n";

                        break;
                    case 5:
                        subtitles = "BILLION? \n\n" +
                                "With a B?? \n\n" +
                                "Jesus. No wonder they're \"encouraging\" us to work overtime.";
                        break;
                    case 6:
                        subtitles = "Honestly, you ask me, this whole gig's wearing a bit thin.\n\n"+
                        "I've barely got it in me to meet the upstairs quota... \n\n" +
                                "and don't even get me STARTED on all the people going downstairs";
                        break;
                        case 7:
                        subtitles = "Ya know, I probably don't have to get ALL the souls on my list.\n\n" +
                                "Enough to hit a quota, maybe get the boss to notice... Easy peasy.\n\n";

                        break;
                        case 8:
                        cutsceneTexture = game.assets.cutscene3;
                        subtitles = "Anyway, we better get this show on the road - can't risk another write-up.\n\n" +
                                "You're late with the scythe on ONE royal matriarch and Management won't let you hear the end of it. \n\n" +
                                "Let's do this.";

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
            batch.draw(backgroundTexture, 0, 200, windowCamera.viewportWidth, 800);
            batch.draw(cutsceneTexture, windowCamera.viewportWidth / 2 - 500f / 2, 220f, 500f, 500f);

            assets.largeFont.getData().setScale(.3f);
            assets.largeFont.setColor(whiteWithAlpha);
            assets.layout.setText(assets.largeFont, subtitles, whiteWithAlpha, camera.viewportWidth, Align.left, true);
            assets.largeFont.draw(batch, assets.layout, 100, camera.viewportHeight * .05f + assets.layout.height);

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
