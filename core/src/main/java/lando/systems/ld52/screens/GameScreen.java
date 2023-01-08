package lando.systems.ld52.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lando.systems.ld52.Config;
import lando.systems.ld52.assets.Feature;
import lando.systems.ld52.audio.AudioManager;
import lando.systems.ld52.gameobjects.*;
import lando.systems.ld52.ui.GameScreenUI;

import java.util.ArrayList;
import java.util.List;

public class GameScreen extends BaseScreen {

    private static final Color clearColor = new Color(0.15f, 0.15f, 0.2f, 1f);

    private TextureRegion background;
    public GameBoard gameboard;

    public Player player;
    public Scythe scythe;

    private float stateTime = 0;
    private GameScreenUI gameScreenUI;
    public Hourglass hourglass;
    public PlayerUI playerUI;
    private List<Feature> quotaFeatureList;

    @Override
    protected void create() {
        super.create();

        OrthographicCamera worldCam = (OrthographicCamera) worldCamera;
        worldCam.setToOrtho(false, Config.Screen.window_width, Config.Screen.window_height);
        worldCam.update();
        gameboard = new GameBoard(assets, this);
        background = new TextureRegion(assets.gameScreenLayout);

        player = new Player(assets, gameboard);
        scythe = new Scythe(assets);
        hourglass = new Hourglass(assets);
        playerUI = new PlayerUI(assets);

        initializeUI();
        gameScreenUI = new GameScreenUI(windowCamera, assets);
        uiStage.addActor(gameScreenUI);
        game.audioManager.playMusic(AudioManager.Musics.mainTheme);

        quotaFeatureList = new ArrayList<>();
        quotaFeatureList.add(Feature.eyepatch_a);
        quotaFeatureList.add(Feature.tongue);
        quotaFeatureList.add(Feature.nose_clown);
        setQuota(quotaFeatureList);
    }

    public void setQuota(List<Feature> quotaFeatureList) {
        gameScreenUI.rightSideUI.quotaListUI.resetQuota();
        gameScreenUI.rightSideUI.quotaListUI.setQuotaList(quotaFeatureList);

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
        stateTime += delta;
        gameboard.update(delta);
        player.update(delta);
        scythe.update(delta);
        hourglass.update(delta);
        playerUI.update(delta);
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
            hourglass.render(batch);
            playerUI.render(batch);
        }
        batch.end();
        uiStage.draw();
    }

    @Override
    public Color getClearColor() {
        return clearColor;
    }

}
