package lando.systems.ld52.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
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
import lando.systems.ld52.levels.Level;
import lando.systems.ld52.particles.Particles;
import lando.systems.ld52.serialization.RoundDto;
import lando.systems.ld52.serialization.TileDto;
import lando.systems.ld52.tutorial.TutorialManager;
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
    public TutorialManager tutorialManager;

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
        game.audioManager.playSound(AudioManager.Sounds.bell, .5f);


        tutorialManager = new TutorialManager();
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
        RoundDto roundDto = Level.getLevel(round);

        heavenQuota = new Quota(roundDto.heaven);
        hellQuota = new Quota(roundDto.hell);
        RoundData roundData = getRoundData(roundDto, heavenQuota, hellQuota);

        QuotaListUI quotaListUI = gameScreenUI.rightSideUI.quotaListUI;
        quotaListUI.setQuotas(heavenQuota, hellQuota);
        quotaToastShown = false;
        hideToast();
        gameboard.resetTimer();

        gameboard.setupBoard(assets, roundData);
        player.reset();
        gameScreenUI.leftSideUI.harvestedSoulUI.clear();
    }

    private RoundData getRoundData(RoundDto roundDto, Quota heavenQuota, Quota hellQuota) {
        RoundData roundData = new RoundData();

        // TODO - to ensure random characters don't generate heaven/hell character

        for (TileDto tileDto : roundDto.tileDtos) {

            TileData tileData = new TileData();

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
                    tileData.type = TileType.powerUp;
                    break;
                case powerDown_type1:
                    tileData.type = TileType.powerDown;
                    break;
            }

            roundData.tileData[tileDto.x][tileDto.y] = tileData;
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
//                case nose:
//                    tileData.nose = feature;
//                    break;
                case hair_face:
                    tileData.hair_face = feature;
                    break;
                case hair_head:
                    tileData.hair_head = feature;
                    break;
                case mouth:
                    tileData.mouth = feature;
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
//        currentMusic = game.audioManager.playMusic(AudioManager.Musics.mainTheme);
//        game.audioManager.playSound(AudioManager.Sounds.bell, .5f);
//        currentMusic.setPosition(Main.game.currentMusicPosition);
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
        tutorialManager.update(delta);

        // TODO: remove me, this is debug
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)){
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            windowCamera.unproject(mousePos);
            Gdx.app.log("MousePos", "X:" + mousePos.x + "  Y:" + mousePos.y);
        }

        if (tutorialManager.tutorialActive()) return;

        playerUI.update(delta);

        // TODO: remove me I am debug
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            nextRound();
            return;
        }


        if (quotaToastShown) {
            if (Gdx.input.isTouched() && _roundNumber < MAX_ROUND_NUMBER) {
                game.getScreenManager().pushScreen("mid-story", TransitionManager.getRandomTransition().name());
            }
            else if (Gdx.input.isTouched() && _roundNumber >= MAX_ROUND_NUMBER) {
                game.getScreenManager().pushScreen("end-story", TransitionManager.getRandomTransition().name());
            }
            return;
        }
        if (heavenQuota.isSatisfied()) {
            showToast("Heaven Quota: Fully Reaped!", ToastManager.UNTIL_CLOSED);
            Stats.last_quota_reached = Quota.Source.heaven;
            quotaToastShown = true;
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {
                    Rectangle bounds = gameboard.tiles[i][j].bounds;
                    particles.flyUp(assets.angel, bounds.getX() + bounds.width / 2, bounds.getY() + bounds.width / 2);
                }
            }
            score += 1000 + Math.pow(gameboard.getSecondsLeft(), 1.2) * 20;
            Stats.heavenQuotaMet++;
        } else if (hellQuota.isSatisfied()) {
            showToast("Hell's Quota: Fully Reaped!", ToastManager.UNTIL_CLOSED);
            Stats.last_quota_reached = Quota.Source.hell;
            quotaToastShown = true;
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {
                    Rectangle bounds = gameboard.tiles[i][j].bounds;
                    particles.flyUp(assets.devil, bounds.getX() + bounds.width / 2, bounds.getY() + bounds.width / 2);
                }
            }
            score += 1000 + Math.pow(gameboard.getSecondsLeft(), 1.2) * 20;
            Stats.hellQuotaMet++;
        } else if (gameboard.getSecondsLeft() <= 0) {
            showToast("You failed to meet either quota!", ToastManager.UNTIL_CLOSED);
            Stats.last_quota_reached = null;
            quotaToastShown = true;
            Stats.noQuotaMet++;
        }

        gameboard.update(delta);
        player.update(delta);
        hourglass.update(delta);
        gameScreenUI.leftSideUI.scoreBoxUI.setScoreLabel(score);


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
        batch.setProjectionMatrix(screenShaker.getCombinedMatrix());
        batch.begin();
        {
            particles.draw(batch, Particles.Layer.foreground);
        }
        batch.end();


        if (tutorialManager.tutorialActive()) {
            batch.begin();
            tutorialManager.render(batch);
            batch.end();
        }
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
