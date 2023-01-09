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
    private final int MAX_ROUND_NUMBER = 5;
    public int score = 0;
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
    public boolean isFreshStart = true;

    @Override
    protected void create() {
        super.create();

        OrthographicCamera worldCam = (OrthographicCamera) worldCamera;
        worldCam.setToOrtho(false, Config.Screen.window_width, Config.Screen.window_height);
        worldCam.update();

        gameboard = new GameBoard(this, assets);

        background = new TextureRegion(assets.gameScreenLayout);

        player = new Player(assets, gameboard);
        hourglass = new Hourglass(assets, gameboard);
        playerUI = new PlayerUI(player, assets);
        this.particles = Main.game.particles;

        initializeUI();
        gameScreenUI = new GameScreenUI(windowCamera, assets);
        uiStage.addActor(gameScreenUI);

        currentMusic = game.audioManager.playMusic(AudioManager.Musics.mainTheme);


        setRound(0);
    }

    private int _roundNumber = 0;

    public int getroundNumber() {
        return _roundNumber;
    }
    public void nextRound() {
        if (++_roundNumber > MAX_ROUND_NUMBER) {
            _roundNumber = 0;
        }
        setRound(_roundNumber);
    }

    public void resetGame() {
        _roundNumber = 0;
        setRound(0);
    }

    public void setRound(int round) {
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

        QuotaListUI quotaListUI = gameScreenUI.rightSideUI.quotaListUI;
        quotaListUI.setQuotas(heavenQuota, hellQuota);
        quotaToastShown = false;
        hideToast();
        gameboard.resetTimer();

        // RoundDto roundDto = RoundDto.fromJson("{\"tileDtos\":[[{},{},{},{},{},{}],[{\"tileType\":\"obstacle\"},{\"tileType\":\"character_hell\",\"quotaIndex\":1},{},{},{\"tileType\":\"character_rando\"},{\"tileType\":\"obstacle\"}],[{},{},{},{},{},{}],[{},{},{\"tileType\":\"obstacle\"},{\"tileType\":\"character_heaven\",\"quotaIndex\":1},{},{}],[{},{\"tileType\":\"character_rando\"},{},{\"tileType\":\"obstacle\"},{},{}],[{},{},{},{},{},{\"tileType\":\"character_rando\"}]]}");
        RoundDto roundDto = new RoundDto();
        RoundData roundData = getRoundData(roundDto, heavenQuota, hellQuota);
        gameboard.setupBoard(assets, roundData);
        player.reset();
    }

    private RoundData getRoundData(RoundDto roundDto, Quota heavenQuota, Quota hellQuota) {
        RoundData roundData = new RoundData();

        // TODO - to ensure random characters don't generate heaven/hell character

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
        isFreshStart = false;
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            nextRound();
            return;
        }

        if (quotaToastShown) {
            if (Gdx.input.isTouched() && _roundNumber < MAX_ROUND_NUMBER) {
                game.getScreenManager().pushScreen("mid-story", TransitionManager.TransitionType.CROSSHATCH.name());
            }
            else if (Gdx.input.isTouched() && _roundNumber >= MAX_ROUND_NUMBER) {
                game.getScreenManager().pushScreen("end-story", TransitionManager.TransitionType.CROSSHATCH.name());
            }
            return;
        }
        if (heavenQuota.isSatisfied()) {
            showToast("Heaven Quota: Fully Reaped!", ToastManager.UNTIL_CLOSED);
            Stats.last_quota_reached = Quota.Source.heaven;
            quotaToastShown = true;
            particles.flyUp(assets.angel, 350f, 300f);
            particles.flyUp(assets.angel, 950f, 300f);
            score += 1000 + gameboard.getSecondsLeft() * 20;
        } else if (hellQuota.isSatisfied()) {
            showToast("Hell's Quota: Fully Reaped!", ToastManager.UNTIL_CLOSED);
            Stats.last_quota_reached = Quota.Source.hell;
            quotaToastShown = true;
            particles.flyUp(assets.devil, 350f, 300f);
            particles.flyUp(assets.devil, 950f, 300f);
            score += 1000 + gameboard.getSecondsLeft() * 20;
        } else if (gameboard.getSecondsLeft() <= 0) {
            showToast("You've failed to meet quota!", ToastManager.UNTIL_CLOSED);
            Stats.last_quota_reached = null;
            quotaToastShown = true;
        }

        gameboard.update(delta);
        player.update(delta);
        hourglass.update(delta);
        gameScreenUI.leftSideUI.scoreBoxUI.setScoreLabel(score);

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

    public void hideToast() {
        toasts.clear();
    }

}
