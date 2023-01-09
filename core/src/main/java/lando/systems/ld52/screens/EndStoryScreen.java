package lando.systems.ld52.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
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
    private float rotationDT;
    private Vector2 tvPos = new Vector2(900, 200);
    private Vector2 tvVelocity = new Vector2(-0.5f, 0.5f);
    private float tvRotation = 15;
    private Vector2 sixPackPos = new Vector2(245, 200);
    private Vector2 sixPackVelocity = new Vector2(-1.5f, -1.5f);
    private float sixPackRotation = 25;
    private Vector2 chairPos = new Vector2(50, 200);
    private Vector2 chairVelocity = new Vector2(2f, 2f);
    private float chairRotation = 20;


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
        rotationDT = MathUtils.random(-45, 45);
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

    private float rotationCalc(Vector2 itemPosition, Vector2 itemVelocity, float itemRotation, float delta) {
        itemRotation += rotationDT * delta;
        itemPosition.add(itemVelocity.x * delta * 100, itemVelocity.y * delta * 100);
        if (itemPosition.x < 0){
            rotationDT = MathUtils.random(-45, 45);
            itemPosition.x = 0;
            itemVelocity.x *= -1;
        }
        if (itemPosition.x > windowCamera.viewportWidth - 200){
            rotationDT = MathUtils.random(-45, 45);
            itemPosition.x = windowCamera.viewportWidth - 200;
            itemVelocity.x *= -1;
        }
        if (itemPosition.y < 150){
            rotationDT = MathUtils.random(-45, 45);
            itemPosition.y = 150;
            itemVelocity.y *= -1;
        }
        if (itemPosition.y > windowCamera.viewportHeight - 200){
            rotationDT = MathUtils.random(-45, 45);
            itemPosition.y = windowCamera.viewportHeight - 200;
            itemVelocity.y *= -1;
        }
        return itemRotation;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        phaseAccum += delta;

        if (((Gdx.input.justTouched() && phaseAccum > .2f) || phaseAccum > 30.25F)&& !isStoryOver) {

            storyAccum += delta * 10;

            if (((Gdx.input.justTouched() && phaseAccum > .2f) || phaseAccum > 36F) && !isStoryOver) {

                // todo cancel playing sounds
                game.audioManager.stopAllSounds();

                phaseAccum = 0;
                clickPhase++;
                switch (clickPhase) {
                    case 1:
                        subtitles = "Can't wait to enjoy some of my favorite non-reaping-related leisure activities in my afterlife!\n" +
                                "Thanks for keeping me company during my shifts this week.\n\n" +
                                "For additional fun, see if you can pick out these references in the music:";
                        showBeer = true;
                        break;
                    case 2:
                        subtitles =
                                "Don't Fear The Reaper (Blue Oyster Cult)               Stairway To Heaven (Led Zeppelin)\n" +
                                        "Another One Bites The Dust (Queen)             Highway To Hell (AC/DC) Tears In Heaven (Eric Clapton)         \nHeaven (Talking Heads)              Death Sound (Legend of Zelda)          " +
                                "\nHeaven On Their Minds (Jesus Christ Superstar)            Dies Irae (Verdi)";

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
//<<<<<<< Updated upstream
//=======
//                assets.layout.setText(assets.largeFont, "PURGATORY", whiteWithAlpha, camera.viewportWidth, Align.center, false);
//                assets.largeFont.draw(batch, assets.layout, 10, camera.viewportHeight * 6 / 7f + assets.layout.height);
//>>>>>>> Stashed changes
            } else {
                batch.draw(backgroundTexture, 0, 200, windowCamera.viewportWidth, 800);
            }
//            batch.draw(assets.chair, 50, 200, 200f, 200f);
            chairRotation = rotationCalc(chairPos, chairVelocity, chairRotation, delta);
            batch.draw(assets.chair, chairPos.x, chairPos.y, assets.chair.getRegionWidth() / 2, assets.chair.getRegionHeight() / 2, 200, 200, 1, 1, chairRotation);
            //batch.draw(assets.beerPack, 245, 200, 100f, 100f);
            sixPackRotation = rotationCalc(sixPackPos, sixPackVelocity, sixPackRotation, delta);
            batch.draw(assets.beerPack, sixPackPos.x, sixPackPos.y, 100f, 100f, 100, 100, 1, 1, sixPackRotation);
            tvRotation = rotationCalc(tvPos, tvVelocity, tvRotation, delta);
            if (clickPhase < 1) {
//                batch.draw(assets.tvOff, 900, 200, 300f, 300f);
                batch.draw(assets.tvOff, tvPos.x, tvPos.y, 100f, 100f, 300, 300, 1, 1, tvRotation);
            } else {
//                batch.draw(assets.tvOn.getKeyFrame(phaseAccum), 900, 200, 300f, 300f);
                batch.draw(assets.tvOn.getKeyFrame(phaseAccum), tvPos.x, tvPos.y, 100f, 100f, 300, 300, 1, 1, tvRotation);
            }
            if (Stats.heavenQuotaMet > Stats.hellQuotaMet) {
                batch.draw(assets.halo.getKeyFrame(phaseAccum), 240, 400, 200, 300);
            } else if (Stats.heavenQuotaMet < Stats.hellQuotaMet) {
                batch.draw(assets.horns.getKeyFrame(phaseAccum), 240, 400, 200, 325);
            }
            batch.draw(assets.playerNoScythe.getKeyFrame(phaseAccum), 250, 300, 250f, 250f);
            if (Stats.heavenQuotaMet == Stats.hellQuotaMet) {
                assets.layout.setText(assets.largeFont, "PURGATORY", whiteWithAlpha, camera.viewportWidth, Align.center, false);
                assets.largeFont.draw(batch, assets.layout, 0, camera.viewportHeight * 6 / 7f + assets.layout.height);
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
//<<<<<<< Updated upstream
//            assets.largeFont.draw(batch, assets.layout, 100, camera.viewportHeight / 7f + assets.layout.height);
//=======
            assets.largeFont.draw(batch, assets.layout, 10, camera.viewportHeight / 10f + assets.layout.height);

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

//        TextButton startGameButton = new TextButton("Skip", titleScreenButtonStyle);
////        Gdx.app.log("startbuttonwidth&height", "width: " + startGameButton.getWidth() + " & height: " + startGameButton.getHeight());
//        startGameButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
//        startGameButton.setPosition(left, top);
//        startGameButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                game.getScreenManager().pushScreen("credit", TransitionManager.TransitionType.CROSSHATCH.name());
//            }
//        });
//        uiStage.addActor(startGameButton);
    }
}
