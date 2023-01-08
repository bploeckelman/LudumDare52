package lando.systems.ld52.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Json;
import com.kotcrab.vis.ui.util.ToastManager;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.toast.Toast;
import lando.systems.ld52.Config;
import lando.systems.ld52.Main;
import lando.systems.ld52.assets.Feature;
import lando.systems.ld52.audio.AudioManager;
import lando.systems.ld52.data.RoundData;
import lando.systems.ld52.data.TileData;
import lando.systems.ld52.data.TileType;
import lando.systems.ld52.gameobjects.*;
import lando.systems.ld52.particles.Particles;
import lando.systems.ld52.serialization.RoundDto;
import lando.systems.ld52.serialization.TileDto;
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
    public Particles particles;

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

        heavenQuota = new Quota(Quota.Source.heaven);
        hellQuota = new Quota(Quota.Source.hell);

        heavenQuota.addPerson(
                Feature.getRandomFrom(Feature.Category.hair_head)
                , Feature.getRandomFrom(Feature.Category.eye)
                , Feature.getRandomFrom(Feature.Category.nose)
        );
        hellQuota.addPerson(
                Feature.getRandomFrom(Feature.Category.hair_head)
                , Feature.getRandomFrom(Feature.Category.eye)
                , Feature.getRandomFrom(Feature.Category.hair_face)
        );

        RoundDto roundDto = new RoundDto();
        RoundData roundData = getRoundData(roundDto, heavenQuota, hellQuota);

        gameboard = new GameBoard(assets, this, roundData);
        background = new TextureRegion(assets.gameScreenLayout);

        player = new Player(assets, gameboard);
        hourglass = new Hourglass(assets, gameboard);
        playerUI = new PlayerUI(player, assets);
        this.particles = Main.game.particles;

        initializeUI();
        gameScreenUI = new GameScreenUI(windowCamera, assets);
        uiStage.addActor(gameScreenUI);

        currentMusic = game.audioManager.playMusic(AudioManager.Musics.mainTheme);
        Gdx.app.log("Creating GameScreen", "Music");

        QuotaListUI quotaListUI = gameScreenUI.rightSideUI.quotaListUI;
        quotaListUI.setQuotas(heavenQuota, hellQuota);
        quotaToastShown = false;
    }

    private RoundData getRoundData(RoundDto roundDto, Quota heavenQuota, Quota hellQuota) {
        RoundData roundData = new RoundData();

        for (int x = 0; x < roundDto.tileDtos.length; x++) {
            for (int y = 0; y < roundDto.tileDtos[x].length; y++) {
                TileData tileData = new TileData();

                TileDto tileDto = roundDto.tileDtos[x][y];
                switch (tileDto.tileType) {
                    case obstacle:
                        tileData.type = TileType.obstacle;
                        break;
                    case character_hell:
                        tileData.type = TileType.character;
                        Quota.Person charHell = hellQuota.people.get(tileDto.quotaIndex - 1);
                        mapFeatures(tileData, charHell);
                        break;
                    case character_heaven:
                        tileData.type = TileType.character;
                        Quota.Person charHeaven = heavenQuota.people.get(tileDto.quotaIndex - 1);
                        mapFeatures(tileData, charHeaven);
                        break;
                    case character_rando:
                        tileData.type = TileType.character;
                        // remove features? - this could gen a match in heaven/hell
                        break;
                    case powerUp_type1:
                        break;
                }

                roundData.tileData[x][y] = tileData;
            }
        }

        return roundData;
    }

    private void mapFeatures(TileData tileData, Quota.Person character) {
        for (Feature feature : character.features.keySet()) {
            switch (feature.category) {
                case clothes:
                    tileData.clothes = feature;
                    break;
                case eye:
                    tileData.eye = feature;
                    break;
                case nose:
                    tileData.nose = feature;
                    break;
                case hair_face:
                    tileData.hair_face = feature;
                    break;
                case hair_head:
                    tileData.hair_head = feature;
                    break;
                // TODO
                // case hat:
                //    tileData.hat = feature;
                //    break;
                // case neck:
                //    tileData.neck = feature;
                //    break;
            }

        }
    }

    @Override
    public void show(){
        super.show();
        // TODO: we need to reset gameboard here
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

        batch.setProjectionMatrix(screenShaker.getCombinedMatrix());
        batch.begin();
        {
            batch.draw(background, 0, 0, worldCamera.viewportWidth, worldCamera.viewportHeight);
            particles.draw(batch, Particles.Layer.background);
            gameboard.render(batch);
            particles.draw(batch, Particles.Layer.middle);
            player.render(batch);
            particles.draw(batch, Particles.Layer.foreground);
        }
        batch.end();
        batch.setProjectionMatrix(windowCamera.combined);
        batch.begin();
        {
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
