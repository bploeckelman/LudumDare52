package lando.systems.ld52.screens;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.kotcrab.vis.ui.VisUI;
import de.eskalon.commons.screen.ManagedScreen;
import lando.systems.ld52.Assets;
import lando.systems.ld52.Config;
import lando.systems.ld52.Main;
import lando.systems.ld52.audio.AudioManager;
import lando.systems.ld52.utils.screenshake.ScreenShakeCameraController;

public abstract class BaseScreen extends ManagedScreen implements Disposable {

    public final Main game;
    public final Assets assets;
    public final Engine engine;
    public final TweenManager tween;
    public final SpriteBatch batch;
    public final OrthographicCamera windowCamera;
    public final Vector3 pointerPos;
    public ScreenShakeCameraController screenShaker;
    public Camera worldCamera;
    public AudioManager audioManager;
    protected Stage uiStage;
    protected Skin skin;

    protected boolean active;

    public BaseScreen() {
        this.game = Main.game;
        this.assets = game.assets;
        this.engine = game.engine;
        this.tween = game.tween;
        this.windowCamera = game.windowCamera;
        this.batch = assets.batch;
        this.pointerPos = new Vector3();
        this.active = false;
        this.audioManager = game.audioManager;
    }

    @Override
    protected void create() {
        worldCamera = new OrthographicCamera();
        ((OrthographicCamera) worldCamera).setToOrtho(false, Config.Screen.window_width, Config.Screen.window_height);
        worldCamera.update();
        screenShaker = new ScreenShakeCameraController(worldCamera);
        initializeUI();
    }

    @Override
    public void show() {
        super.show();
        active = true;
    }

    @Override
    public void hide() {
        active = false;
    }

    public void update(float delta) {
        windowCamera.update();
        if (worldCamera != null) {
            worldCamera.update();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            Config.Debug.general = !Config.Debug.general;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            Config.Debug.ui = !Config.Debug.ui;
        }
        screenShaker.update(delta);
        audioManager.update(delta);
        uiStage.act(delta);
    }

    @Override
    public void resize(int width, int height) {
        windowCamera.setToOrtho(false, width, height);
        windowCamera.update();
    }

    @Override
    public void dispose() {

    }

    public void resetWorldCamera() {}

    protected void initializeUI() {
        // reset the stage in case it hasn't already been set to the current window camera orientation
        // NOTE - doesn't seem to be a way to directly set the stage camera as the window camera
        //  could go in the other direction, create the uiStage and set windowCam = stage.cam
        skin = VisUI.getSkin();
        StretchViewport viewport = new StretchViewport(windowCamera.viewportWidth, windowCamera.viewportHeight);
        uiStage = new Stage(viewport, batch);
    }

}
