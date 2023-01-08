package lando.systems.ld52.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld52.Assets;
import lando.systems.ld52.Config;
import lando.systems.ld52.ui.SettingsUI;

public class TitleScreen extends BaseScreen {

    private TextureRegion background;
    private TextureRegion logo;
    private TextureRegion dog;
    private TextureRegion cat;
    private TextureRegion kitten;
    private TextButton startGameButton;
    private TextButton creditButton;
    private TextButton settingsButton;
    private float stateTime = 0;
    private boolean exiting;
    private final float BUTTON_WIDTH = 180f;
    private final float BUTTON_HEIGHT = 50f;
    private final float BUTTON_PADDING = 10f;

    @Override
    protected void create() {
        super.create();

        OrthographicCamera worldCam = (OrthographicCamera) worldCamera;
        worldCam.setToOrtho(false, Config.Screen.window_width, Config.Screen.window_height);
        worldCam.update();

        background = assets.atlas.findRegion("fire-color-gradient");
        logo = assets.atlas.findRegion("libgdx");
    }

    @Override
    public void show(){
        super.show();
        exiting = false;
        game.getInputMultiplexer().addProcessor(uiStage);
    }

    @Override
    public void hide() {
        game.getInputMultiplexer().removeProcessor(uiStage);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        // ...
        stateTime += delta;
        dog = assets.dog.getKeyFrame(stateTime);
        cat = assets.cat.getKeyFrame(stateTime);
        kitten = assets.kitten.getKeyFrame(stateTime);
        if (!dog.isFlipX()) dog.flip(true, false);
        if (!cat.isFlipX()) cat.flip(true, false);
        if (!kitten.isFlipX()) kitten.flip(true, false);

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            game.getScreenManager().pushScreen("game", TransitionManager.TransitionType.BLEND.name());
        }
    }

    @Override
    public void render(float delta) {
        update(delta);

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        batch.setProjectionMatrix(worldCamera.combined);
        batch.begin();
        {
            batch.draw(background, 0, 0, worldCamera.viewportWidth, worldCamera.viewportHeight);

            float margin = 10;
            float logoX = 0.5f * (Gdx.graphics.getWidth()  - logo.getRegionWidth()) + 80;
            float logoY = 0.5f * (Gdx.graphics.getHeight() - logo.getRegionHeight());
            batch.draw(logo, logoX, logoY);
            batch.draw(dog,
                    margin, margin,
                    2 * dog.getRegionWidth(), 2 * dog.getRegionHeight());
            batch.draw(cat,
                    margin, Gdx.graphics.getHeight() - 2 * cat.getRegionHeight() - 10,
                    2 * cat.getRegionWidth(), 2 * cat.getRegionHeight());
            batch.draw(kitten,
                    logoX + logo.getRegionWidth() * (2f / 3f) - 4 * margin,
                    logoY + logo.getRegionHeight() - 2 * margin,
                    2 * kitten.getRegionWidth(), 2 * kitten.getRegionHeight());
        }
        batch.end();
        uiStage.draw();
    }

    @Override
    public void initializeUI() {
        super.initializeUI();

        SettingsUI settingsUI = new SettingsUI(assets, skin, audioManager, windowCamera);

        TextButton.TextButtonStyle outfitMediumStyle = skin.get("text", TextButton.TextButtonStyle.class);
        TextButton.TextButtonStyle titleScreenButtonStyle = new TextButton.TextButtonStyle(outfitMediumStyle);
        titleScreenButtonStyle.font = assets.smallFont;
        titleScreenButtonStyle.fontColor = Color.WHITE;
        titleScreenButtonStyle.up = Assets.Patch.glass.drawable;
        titleScreenButtonStyle.down = Assets.Patch.glass_dim.drawable;
        titleScreenButtonStyle.over = Assets.Patch.glass_dim.drawable;

        float left = windowCamera.viewportWidth * (5f / 8f);
        float top = windowCamera.viewportHeight * (1f / 2f);

        startGameButton = new TextButton("Start Game", titleScreenButtonStyle);
//        Gdx.app.log("startbuttonwidth&height", "width: " + startGameButton.getWidth() + " & height: " + startGameButton.getHeight());
        startGameButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        startGameButton.setPosition(left, top);
        startGameButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.audioManager.stopAllSounds();
                exiting = true;
                game.getScreenManager().pushScreen("intro-story", TransitionManager.TransitionType.CROSSHATCH.name());
            }
        });

        settingsButton = new TextButton("Settings", titleScreenButtonStyle);
        settingsButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        settingsButton.setPosition(left, startGameButton.getY() - startGameButton.getHeight() - BUTTON_PADDING);
        settingsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                settingsUI.showSettings();
            }
        });


        creditButton = new TextButton("Credits", titleScreenButtonStyle);
        creditButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        creditButton.setPosition(left, settingsButton.getY() - settingsButton.getHeight() - BUTTON_PADDING);
        creditButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                exiting = true;
                game.getScreenManager().pushScreen("credit", TransitionManager.TransitionType.BLEND.name());
            }
        });


        uiStage.addActor(startGameButton);
        uiStage.addActor(settingsButton);
        uiStage.addActor(creditButton);
        uiStage.addActor(settingsUI);
    }

}
