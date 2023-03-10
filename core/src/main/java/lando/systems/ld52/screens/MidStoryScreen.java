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
import lando.systems.ld52.gameobjects.Quota;
import lando.systems.ld52.gameobjects.Stats;

public class MidStoryScreen extends BaseScreen {

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

    public void prepUpGameScreenAndTransition() {
        //prep up the gamescreen
        GameScreen gameScreen = (GameScreen) game.getScreenManager().getScreen("game");
        gameScreen.nextRound();
        game.getScreenManager().pushScreen("game", TransitionManager.TransitionType.CROSSHATCH.name());
    }

    public int getGameScreenRoundNumber() {
        GameScreen gameScreen = (GameScreen) game.getScreenManager().getScreen("game");
        return gameScreen.getroundNumber();
    }

    @Override
    public void show() {
        super.show();
        game.getInputMultiplexer().addProcessor(uiStage);
//        game.audioManager.playMusic(AudioManager.Musics.mutedMainTheme);
        storyAccum = 0;
        phaseAccum = 0;
        clickPhase = 1;
        isStoryOver = false;
        backgroundTexture = game.assets.cutsceneBackground;
        cutsceneTexture = game.assets.cutscene0;
        int displayNumber = getGameScreenRoundNumber() + 1;
        subtitles = "Day " + displayNumber + " complete. Phew! Long one.";
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


                switch (clickPhase) {
                    case 0:
                        cutsceneTexture = game.assets.cutscene0;
                        subtitles = "Whew! Glad that's over!";
                        break;
                    case 1:
                        cutsceneTexture = game.assets.cutscene1;
                        if (Stats.last_quota_reached == null) {
                            subtitles = "Can't believe I didn't make quota. Boss is going to have my scythe for sure";
                        } else {
                            switch (getGameScreenRoundNumber()) {
                                case 0:
                                    subtitles = Stats.last_quota_reached == Quota.Source.heaven ? "Absolutely CRUSHED the Heaven quota yesterday" : "Hell quota: Hell yeah";
                                    break;
                                case 1:
                                    subtitles = Stats.last_quota_reached == Quota.Source.heaven ? "Heaven quota reaped AF" : "Hell quota hella quickly";
                                    break;
                                case 2:
                                    subtitles = Stats.last_quota_reached == Quota.Source.heaven ? "Hey Heaven: Special delivery" : "Hell better watch out for that group";
                                    break;
                                case 3:
                                    subtitles = Stats.last_quota_reached == Quota.Source.heaven ? "Heaven quota complete? Heavenly" : "Hell yeah.";
                                    break;

                                default:
                                    subtitles = Stats.last_quota_reached == Quota.Source.heaven ? "Absolutely CRUSHED my Heaven quota yesterday" : "Hell quota: Hell yeah";
                                    break;

                            }
                        }
                        break;
//                    case 2:
//                        cutsceneTexture = game.assets.cutscene2;
//
//                        if (Stats.last_quota_reached == null) {
//                            subtitles = "Can't win 'em all, I guess...";
//                        } else {
//                            subtitles = Stats.last_quota_reached == Quota.Source.heaven ? "Heaven's quota: Heavenly!" : "Did that Hell quota hella quick.";
//                        }
//                        break;
//                    case 3:
//                        cutsceneTexture = game.assets.cutscene3;
//                        subtitles = "The days are short but the weeks are long, ya know?";
//                        break;
                    default:
                        isStoryOver = true;
                        prepUpGameScreenAndTransition();
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
                prepUpGameScreenAndTransition();
            }
        });
        uiStage.addActor(startGameButton);
    }
}
