package lando.systems.ld52.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lando.systems.ld52.Config;
import lando.systems.ld52.Main;
import lando.systems.ld52.assets.Feature;
import lando.systems.ld52.audio.AudioManager;
import lando.systems.ld52.gameobjects.*;
import lando.systems.ld52.ui.GameScreenUI;

import java.util.ArrayList;
import java.util.List;

import static lando.systems.ld52.Main.*;

public class GameScreen extends BaseScreen {

    private static final Color clearColor = new Color(0.15f, 0.15f, 0.2f, 1f);

    private TextureRegion background;
    public GameBoard gameboard;

    public Player player;

    private float stateTime = 0;
    private GameScreenUI gameScreenUI;
    public Hourglass hourglass;
    public PlayerUI playerUI;
    private List<Feature> quotaFeatureList;
    private Music currentMusic;

    @Override
    protected void create() {
        super.create();

        OrthographicCamera worldCam = (OrthographicCamera) worldCamera;
        worldCam.setToOrtho(false, Config.Screen.window_width, Config.Screen.window_height);
        worldCam.update();
        gameboard = new GameBoard(assets, this);
        background = new TextureRegion(assets.gameScreenLayout);

        player = new Player(assets, gameboard);
        hourglass = new Hourglass(assets);
        playerUI = new PlayerUI(player, assets);

        initializeUI();
        gameScreenUI = new GameScreenUI(windowCamera, assets);
        uiStage.addActor(gameScreenUI);

        currentMusic = game.audioManager.playMusic(AudioManager.Musics.mainTheme);
        Gdx.app.log("Creating GameScreen", "Music");
//        currentMusic.setPosition(14f);
//        Main.game.audioManager.loopSound(AudioManager.Sounds.swoosh1, .5f);

        quotaFeatureList = new ArrayList<>();
        quotaFeatureList.add(Feature.eyepatch_a);
        quotaFeatureList.add(Feature.tongue);
        quotaFeatureList.add(Feature.hair_long_brown);
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
        Gdx.app.log("currentMusicPositionGameScreenOnShow()", String.valueOf(Main.game.currentMusicPosition));
        currentMusic = game.audioManager.playMusic(AudioManager.Musics.mainTheme);
        currentMusic.setPosition(Main.game.currentMusicPosition);
//        currentMusic.setVolume(game.audioManager.musicVolume.floatValue());
    }

    @Override
    public void hide() {

        game.getInputMultiplexer().removeProcessor(uiStage);
//        Main.game.currentMusicPosition = currentMusic.getPosition();

    }

    @Override
    public void update(float delta) {
        super.update(delta);
        stateTime += delta;
        gameboard.update(delta);
        player.update(delta);
        hourglass.update(delta);
        playerUI.update(delta);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            // .hide() happens at the END of transition - can't set the time there (value will be set at 0f)
            Main.game.currentMusicPosition = currentMusic.getPosition();
            game.getScreenManager().pushScreen("cutscene", TransitionManager.TransitionType.CROSSHATCH.name());
        }
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
