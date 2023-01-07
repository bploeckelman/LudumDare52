package lando.systems.ld52.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld52.Config;
import lando.systems.ld52.gameobjects.GameBoard;
import lando.systems.ld52.gameobjects.Player;
import lando.systems.ld52.ui.ScytheMeter;

public class GameScreen extends BaseScreen {

    private TextureRegion background;
    public GameBoard gameboard;

    public Player player;

    private float stateTime = 0;
    // temp oscillation for scythe meter
    private float oscillationSpeed = 2; // controls how fast the oscillation occurs
    private float oscillationRange = 1; // controls the range of the oscillation
    private ScytheMeter scytheMeterVersion1;

    @Override
    protected void create() {
        super.create();

        OrthographicCamera worldCam = (OrthographicCamera) worldCamera;
        worldCam.setToOrtho(false, Config.Screen.window_width, Config.Screen.window_height);
        worldCam.update();
        gameboard = new GameBoard(assets);
        background = assets.atlas.findRegion("fire-color-gradient");

        player = new Player(assets, gameboard);
        scytheMeterVersion1 = new ScytheMeter(100f, 100f, 0, 1f, 0.01f, skin);
        initializeUI();
        uiStage.addActor(scytheMeterVersion1);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        // ...
        stateTime += delta;
        gameboard.update(delta);
        player.update(delta);

        // Probably belongs to player, having it here for testing UI
        float sinValue = MathUtils.sin(stateTime * oscillationSpeed);
        float oscillation = (sinValue + 1) / 2 * oscillationRange;
        scytheMeterVersion1.scytheProgressBar.setValue(oscillation);

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
        uiStage.draw();
    }

}
