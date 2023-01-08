package lando.systems.ld52.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kotcrab.vis.ui.util.ToastManager;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.toast.Toast;
import lando.systems.ld52.Config;
import lando.systems.ld52.Main;
import lando.systems.ld52.assets.Feature;
import lando.systems.ld52.audio.AudioManager;
import lando.systems.ld52.gameobjects.*;
import lando.systems.ld52.ui.GameScreenUI;
import lando.systems.ld52.ui.QuotaListUI;

public class GameScreen extends BaseScreen {

    private static final Color clearColor = new Color(0.15f, 0.15f, 0.2f, 1f);

    public GameBoard gameboard;
    public Player player;
    public Hourglass hourglass;
    public PlayerUI playerUI;
    private Music currentMusic;
    public GameScreenUI gameScreenUI;

    private TextureRegion background;
    public Quota heavenQuota;
    public Quota hellQuota;
    private boolean quotaToastShown;

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

        heavenQuota = new Quota(Quota.Source.heaven,
                  Feature.getRandomFrom(Feature.Category.hair_head)
                , Feature.getRandomFrom(Feature.Category.nose)
                , Feature.getRandomFrom(Feature.Category.mouth)
        );
        hellQuota = new Quota(Quota.Source.hell,
                  Feature.getRandomFrom(Feature.Category.hair_face)
                , Feature.getRandomFrom(Feature.Category.eye)
                , Feature.getRandomFrom(Feature.Category.mouth)
        );
        QuotaListUI quotaListUI = gameScreenUI.rightSideUI.quotaListUI;
        quotaListUI.setQuotas(heavenQuota, hellQuota);
        quotaToastShown = false;
    }

    @Override
    public void show(){
        super.show();

        game.getInputMultiplexer().addProcessor(uiStage);
//        Gdx.app.log("currentMusicPositionGameScreenOnShow()", String.valueOf(Main.game.currentMusicPosition));
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
        playerUI.update(delta);
        if (quotaToastShown) {
            return;
        }
        if (heavenQuota.isSatisfied()) {
            showToast("You've filled heaven's quota!", ToastManager.UNTIL_CLOSED);
            quotaToastShown = true;
        } else if (hellQuota.isSatisfied()) {
            showToast("You've filled hell's quota!", ToastManager.UNTIL_CLOSED);
            quotaToastShown = true;
        }

        gameboard.update(delta);
        player.update(delta);
        hourglass.update(delta);

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

    public void showToast(String text, float duration) {
        VisTable table = new VisTable();
        table.defaults().pad(8f).grow();
        table.add(new VisLabel(text, "outfit-medium-40px"));
        Toast toast = new Toast(table);
        toasts.show(toast, duration);
    }

}
