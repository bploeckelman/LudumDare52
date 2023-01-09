package lando.systems.ld52.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld52.Config;

public class LaunchScreen extends BaseScreen {

    boolean exitingScreen;

    public LaunchScreen() {
        exitingScreen = false;
    }

    public void update(float dt) {
        if (!exitingScreen && Gdx.input.justTouched()) {
            game.getScreenManager().pushScreen("title", TransitionManager.TransitionType.BLEND.name());
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(Color.PURPLE);
        batch.setProjectionMatrix(worldCamera.combined);
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
