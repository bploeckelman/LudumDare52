package lando.systems.ld52.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld52.Config;
import lando.systems.ld52.Main;

public class LaunchScreen extends BaseScreen {

    boolean exitingScreen;
    float accum;
    ShaderProgram shader;

    public LaunchScreen() {
        exitingScreen = false;
        if (MathUtils.randomBoolean(.1f)){
            shader = Main.game.assets.cityShader2;
        } else {
            shader = Main.game.assets.cityShader;
        }
    }

    public void update(float dt) {
        accum += dt;
        if (!exitingScreen && Gdx.input.justTouched()) {
            game.getScreenManager().pushScreen("title", TransitionManager.TransitionType.BLEND.name());
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(Color.PURPLE);
        batch.setProjectionMatrix(windowCamera.combined);
        batch.setShader(shader);
        batch.begin();
        {
            shader.setUniformf("iTime", accum);
            shader.setUniformf("iResolution", windowCamera.viewportWidth, windowCamera.viewportHeight);
            batch.draw(assets.pixel, 0, 0, windowCamera.viewportWidth, windowCamera.viewportHeight, -.5f, -.5f, windowCamera.viewportWidth-.5f, windowCamera.viewportHeight - .5f);
        }
        batch.end();
        batch.setShader(null);
        batch.begin();
        {
            assets.layout.setText(assets.font, "Click To Start", Color.WHITE, Config.Screen.window_width, Align.center, false);
            assets.font.draw(batch, assets.layout, 0, 80);

            assets.font.getData().setScale(.4f);
            assets.layout.setText(assets.font, "Chrome needs this for audio to work... Thanks Obama", Color.WHITE, Config.Screen.window_width, Align.center, false);
            assets.font.draw(batch, assets.layout, 0, 40);
            assets.font.getData().setScale(1f);
        }
        batch.end();
    }
}
