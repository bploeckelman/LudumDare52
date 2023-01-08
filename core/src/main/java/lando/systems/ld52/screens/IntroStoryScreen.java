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
        backgroundTexture = game.assets.cutsceneBackground;
        cutsceneTexture = game.assets.cutscene0;

        ChangeListener listener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getScreenManager().pushScreen("game", "blend");
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
        Gdx.app.log("click phase", String.valueOf(clickPhase));

        if (((Gdx.input.justTouched() && phaseAccum > .2f) || phaseAccum > 7.25F)&& !isStoryOver) {

            storyAccum += delta * 10;

            if (((Gdx.input.justTouched() && phaseAccum > .2f) || phaseAccum > 6F) && !isStoryOver) {

                // todo cancel playing sounds
                game.audioManager.stopAllSounds();

                phaseAccum = 0;


                switch (clickPhase) {
                    case 0:
                        cutsceneTexture = game.assets.cutscene0;
                        subtitles = "Another shitty work day";
                        break;
                    case 1:
                        cutsceneTexture = game.assets.cutscene1;
                        subtitles = "I can't wait till my stocks go up and quit this job. Let's get punched in.";
                        break;
                    case 2:
                        cutsceneTexture = game.assets.cutscene2;
                        subtitles = "Let's check today's quota.";
                        break;
                    case 3:
                        cutsceneTexture = game.assets.cutscene3;
                        subtitles = "Meh, same thing. Let's get this over with.";
                        break;
                    default:
                        isStoryOver = true;
                        game.getScreenManager().pushScreen("game", TransitionManager.TransitionType.CROSSHATCH.name());
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
            batch.draw(backgroundTexture, 0, 150, windowCamera.viewportWidth, 600);
            batch.draw(cutsceneTexture, windowCamera.viewportWidth / 2 - 500f / 2, 200f, 500f, 500f);

            assets.largeFont.getData().setScale(.4f);
            assets.largeFont.setColor(whiteWithAlpha);
            assets.layout.setText(assets.largeFont, subtitles, whiteWithAlpha, camera.viewportWidth, Align.center, false);
            assets.largeFont.draw(batch, assets.layout, 0, camera.viewportHeight / 7f + assets.layout.height);
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
                game.getScreenManager().pushScreen("game", TransitionManager.TransitionType.CROSSHATCH.name());
            }
        });
        uiStage.addActor(startGameButton);
    }
}
