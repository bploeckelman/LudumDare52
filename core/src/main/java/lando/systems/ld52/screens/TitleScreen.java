package lando.systems.ld52.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld52.Config;

public class TitleScreen extends BaseScreen {

    private TextureRegion background;
    private TextureRegion logo;
    private TextureRegion dog;
    private TextureRegion cat;
    private TextureRegion kitten;
    private float stateTime = 0;
    private boolean exiting;

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

        if (Gdx.input.justTouched() && !exiting ){
            exiting = true;
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
    }

}
