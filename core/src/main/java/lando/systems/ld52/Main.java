package lando.systems.ld52;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.VisUI;
import de.damios.guacamole.gdx.graphics.NestableFrameBuffer;
import de.eskalon.commons.core.ManagedGame;
import de.eskalon.commons.screen.transition.ScreenTransition;
import de.eskalon.commons.utils.BasicInputMultiplexer;
import lando.systems.ld52.audio.AudioManager;
import lando.systems.ld52.particles.Particles;
import lando.systems.ld52.screens.*;
import lando.systems.ld52.utils.Time;
import lando.systems.ld52.utils.accessors.*;

public class Main extends ManagedGame<BaseScreen, ScreenTransition> {

	public static Main game;

	public Assets assets;
	public Engine engine;
	public Particles particles;
	public TweenManager tween;
	public NestableFrameBuffer frameBuffer;
	public TextureRegion frameBufferRegion;
	public OrthographicCamera windowCamera;
	public AudioManager audioManager;
	public float currentMusicPosition;
	public Main() {
		Main.game = this;
	}

	@Override
	public void create() {
		Time.init();

		assets = new Assets();

		VisUI.load(assets.mgr.get("ui/uiskin.json", Skin.class));
		{
			Skin skin = VisUI.getSkin();
			skin.getFont("default")            .setUseIntegerPositions(false);
			skin.getFont("font")               .setUseIntegerPositions(false);
			skin.getFont("list")               .setUseIntegerPositions(false);
			skin.getFont("subtitle")           .setUseIntegerPositions(false);
			skin.getFont("window")             .setUseIntegerPositions(false);
			skin.getFont("outfit-medium-10px") .setUseIntegerPositions(false);
			skin.getFont("outfit-medium-14px") .setUseIntegerPositions(false);
			skin.getFont("outfit-medium-17px") .setUseIntegerPositions(false);
			skin.getFont("outfit-medium-19px") .setUseIntegerPositions(false);
			skin.getFont("outfit-medium-20px") .setUseIntegerPositions(false);
			skin.getFont("outfit-medium-40px") .setUseIntegerPositions(false);
			skin.getFont("outfit-medium-80px") .setUseIntegerPositions(false);
		}

		engine = new Engine();

		tween = new TweenManager();
		{
			Tween.setWaypointsLimit(4);
			Tween.setCombinedAttributesLimit(4);
			Tween.registerAccessor(Color.class, new ColorAccessor());
			Tween.registerAccessor(Rectangle.class, new RectangleAccessor());
			Tween.registerAccessor(Vector2.class, new Vector2Accessor());
			Tween.registerAccessor(Vector3.class, new Vector3Accessor());
			Tween.registerAccessor(OrthographicCamera.class, new CameraAccessor());
		};

		particles = new Particles(assets);
		audioManager = new AudioManager(assets, tween);
		currentMusicPosition = 0f;

		Pixmap.Format format = Pixmap.Format.RGBA8888;
		int width = Config.Screen.framebuffer_width;
		int height = Config.Screen.framebuffer_height;
		boolean hasDepth = true;
		frameBuffer = new NestableFrameBuffer(format, width, height, hasDepth);
		Texture frameBufferTexture = frameBuffer.getColorBufferTexture();
		frameBufferTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		frameBufferRegion = new TextureRegion(frameBufferTexture);
		frameBufferRegion.flip(false, true);

		windowCamera = new OrthographicCamera();
		windowCamera.setToOrtho(false, Config.Screen.window_width, Config.Screen.window_height);
		windowCamera.update();

		BasicInputMultiplexer inputMux = new BasicInputMultiplexer();
		Gdx.input.setInputProcessor(game.getInputMultiplexer());
		//Gdx.input.setInputProcessor(inputMux);

		screenManager.initialize(inputMux, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		screenManager.addScreen("launch", new LaunchScreen());
		screenManager.addScreen("title", new TitleScreen());
		screenManager.addScreen("game", new GameScreen());
		screenManager.addScreen("cutscene", new CutsceneScreen());
		screenManager.addScreen("credit", new CreditScreen());
		screenManager.addScreen("intro-story", new IntroStoryScreen());
		screenManager.addScreen("mid-story", new MidStoryScreen());
		screenManager.addScreen("end-story", new EndStoryScreen());
		TransitionManager.initialize(screenManager);

		if (Gdx.app.getType() == Application.ApplicationType.WebGL) {
			screenManager.pushScreen("launch", TransitionManager.TransitionType.BLEND.name());
		} else {
			screenManager.pushScreen("title", TransitionManager.TransitionType.BLEND.name());
		}
	}


	public void update(float delta) {
		// handle top level input
		// TODO: remove this debug before release
//		{
//			if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) Config.Debug.general = !Config.Debug.general;
//		}

		// update things that must update every tick
		{
			Time.update();
			tween.update(Time.delta);
		}

		// handle a pause
		{
			if (Time.pause_timer > 0) {
				Time.pause_timer -= Time.delta;
				if (Time.pause_timer <= -0.0001f) {
					Time.delta = -Time.pause_timer;
				} else {
					// skip updates if we're paused
					return;
				}
			}
			Time.millis += Time.delta;
			Time.previous_elapsed = Time.elapsed_millis();
		}

		// update systems
		{
			// TODO - need a way to separate 'pausable update' from 'always update' on entity components
			particles.update(Time.delta);
			engine.update(Time.delta);
		}
	}

	@Override
	public void render() {
		update(Time.delta);
		screenManager.render(Time.delta);
	}

	@Override
	public void dispose() {
		screenManager.getScreens().forEach(BaseScreen::dispose);
		frameBuffer.dispose();
		assets.dispose();
		particles.dispose();
	}

}