package lando.systems.ld52.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lando.systems.ld52.Config;
import lando.systems.ld52.gameobjects.GameBoard;
import lando.systems.ld52.gameobjects.Player;
import lando.systems.ld52.gameobjects.Scythe;
import lando.systems.ld52.ui.GameScreenUI;

public class GameScreen extends BaseScreen {

    private static final Color clearColor = new Color(0.15f, 0.15f, 0.2f, 1f);

    private TextureRegion background;
    public GameBoard gameboard;

    public Player player;
    public Scythe scythe;

    private float stateTime = 0;
    private GameScreenUI gameScreenUI;

    @Override
    protected void create() {
        super.create();

        OrthographicCamera worldCam = (OrthographicCamera) worldCamera;
        worldCam.setToOrtho(false, Config.Screen.window_width, Config.Screen.window_height);
        worldCam.update();
        gameboard = new GameBoard(assets);
        background = new TextureRegion(assets.gameScreenLayout);

        player = new Player(assets, gameboard);
        scythe = new Scythe(assets);

        initializeUI();
        gameScreenUI = new GameScreenUI(windowCamera, assets);
        uiStage.addActor(gameScreenUI);
    }

    @Override
    public void show(){
        super.show();
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
        gameboard.update(delta);
        player.update(delta);
        scythe.update(delta);
    }

    @Override
    public void render(float delta) {
        update(delta);

        batch.setProjectionMatrix(worldCamera.combined);
        batch.begin();
        {
            batch.draw(background, 0, 0, worldCamera.viewportWidth, worldCamera.viewportHeight);
            gameboard.render(batch);
            player.render(batch);
            scythe.render(batch);
        }
        batch.end();
        uiStage.draw();
    }

    @Override
    public Color getClearColor() {
        return clearColor;
    }

}
