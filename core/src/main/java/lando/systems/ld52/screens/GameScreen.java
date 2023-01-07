package lando.systems.ld52.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld52.Config;
import lando.systems.ld52.gameobjects.*;

public class GameScreen extends BaseScreen {

    private TextureRegion background;
    public GameBoard gameboard;

    public Player player;

    private float stateTime = 0;

    @Override
    protected void create() {
        super.create();

        OrthographicCamera worldCam = (OrthographicCamera) worldCamera;
        worldCam.setToOrtho(false, Config.Screen.window_width, Config.Screen.window_height);
        worldCam.update();
        gameboard = new GameBoard();
        background = assets.atlas.findRegion("fire-color-gradient");

        player = new Player(assets, gameboard.gridDelta);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        // ...
        stateTime += delta;
        gameboard.update(delta);
        player.update(delta);
    }

    @Override
    public void render(float delta) {
        update(delta);

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        batch.setProjectionMatrix(worldCamera.combined);
        batch.begin();
        {
            batch.draw(background, 0, 0, worldCamera.viewportWidth, worldCamera.viewportHeight);
            gameboard.render(batch);
            player.render(batch);
        }
        batch.end();
    }

}
