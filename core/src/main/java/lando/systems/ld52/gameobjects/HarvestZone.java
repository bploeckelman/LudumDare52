package lando.systems.ld52.gameobjects;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld52.Assets;
import lando.systems.ld52.audio.AudioManager;
import lando.systems.ld52.screens.BaseScreen;
import lando.systems.ld52.screens.GameScreen;
import lando.systems.ld52.utils.accessors.Vector2Accessor;

import static lando.systems.ld52.Main.game;

public class HarvestZone {

    public enum HarvestPhase { cycle, golf, collection}

    private static final float golfIndicatorSize = 50f;
    private static final float golfMaxTime = .215f; // Time it takes to go one tile
    private static final int tilesStart = 3;
    private final Interpolation golfInterpolation = Interpolation.slowFast;

    private final Player player;
    public Scythe scythe;

    public HarvestPhase currentPhase;
    public int tilesLong;
    public int currentPathLength;
    private final Vector2 startPos;
    private float rotation;
    private float golfTimer;
    public float golfPosition;
    public Tile tileToHarvest;
    private boolean touchLastFrame;
    private int scytheSpinTimeMultiplier;
    private ShaderProgram harvestShader;
    private TextureRegion texture;
    public float throwCooldown;

    public HarvestZone(Player player) {
        this.player = player;
        this.currentPhase = HarvestPhase.cycle;
        this.tilesLong = tilesStart;
        this.startPos = new Vector2();
        this.scythe = new Scythe(game.assets);
        this.scytheSpinTimeMultiplier = 1;
        tileToHarvest = null;
        touchLastFrame = false;
        harvestShader = player.gameBoard.screen.assets.harvestShader;
        this.texture = new TextureRegion(player.gameBoard.screen.assets.pixel);
        this.throwCooldown = .1f;
    }

    public void update(float dt) {
        throwCooldown = Math.max(throwCooldown - dt, 0);
        handleInput();
        currentPathLength = tilesLong;
        // TODO: if hits something along the way, change currentPathLength

        float tileSize = GameBoard.tileSize;
        int gridSize = GameBoard.gridSize;

        int sidePosition = (player.boardPosition % gridSize);
        float sideOffset = sidePosition * tileSize;

        float xPos, yPos;

        int side = player.boardPosition / gridSize;
        int xDelta = 0;
        int yDelta = 0;

        switch (player.currentSide) {
            case right: // right
                rotation = 180;
                xDelta = -1;
                xPos = player.gameBoard.right();
                yPos = player.gameBoard.top() - (tileSize + sideOffset) - ((sidePosition + 1) * GameBoard.margin) + tileSize/2f;
                break;
            case bottom: // bottom
                rotation = 90;
                yDelta = 1;
                xPos = player.gameBoard.right() - (tileSize + sideOffset) - ((sidePosition + 1) * GameBoard.margin) + tileSize/2f;
                yPos = player.gameBoard.bottom();
                break;
            case left: // left
                rotation = 0;
                xDelta = 1;
                xPos = player.gameBoard.left();
                yPos = player.gameBoard.bottom() + sideOffset + ((sidePosition + 1) * GameBoard.margin) + tileSize/2f;
                break;
            case top: // top
            default:
                rotation = 270;
                yDelta = -1;
                xPos = player.gameBoard.left() + sideOffset + ((sidePosition + 1) * GameBoard.margin) + tileSize/2f;
                yPos = player.gameBoard.top();
                break;
        }
        startPos.set(xPos, yPos);

        for (int i = 1; i <= tilesLong; i++) {
            currentPathLength = i;
            if(player.gameBoard.tiles[player.currentCol + (i*xDelta)][player.currentRow + (i*yDelta)].blocks()) {
                break;
            }
        }

        if (currentPhase == HarvestPhase.golf){
            float maxGolfTime = golfMaxTime * currentPathLength;
            golfTimer += dt;
            if (golfTimer > 2*maxGolfTime) {
                golfTimer -= 2*maxGolfTime;
            }
            float tempGolfPos = golfTimer/maxGolfTime;
            if (tempGolfPos > 1f) {
                tempGolfPos = 2f - tempGolfPos;
            }
            golfPosition = golfInterpolation.apply(tempGolfPos);
            int tileIndex = MathUtils.clamp((int)(currentPathLength * golfPosition), 0, currentPathLength);
            tileIndex++; // don't want it 0 indexed
            switch (player.currentSide) {
                case top:
                    tileToHarvest = player.gameBoard.tiles[player.currentCol][player.currentRow - tileIndex];
                    break;
                case right:
                    tileToHarvest = player.gameBoard.tiles[player.currentCol - tileIndex][player.currentRow];
                    break;
                case bottom:
                    tileToHarvest = player.gameBoard.tiles[player.currentCol][player.currentRow + tileIndex];
                    break;
                case left:
                    tileToHarvest = player.gameBoard.tiles[player.currentCol + tileIndex][player.currentRow];
                    break;
            }
        } else {
            golfPosition = 0;
        }

        scythe.update(dt);
    }

    public void render(SpriteBatch batch) {
        // TODO: this will need to be stored if we are going to check for gravestones, etc along the path
        float maxWidth = (GameBoard.tileSize+GameBoard.margin) * currentPathLength;
        if (currentPhase == HarvestPhase.cycle) {
            batch.setColor(.5f, .5f, .5f, .1f);
        }
        if (currentPhase == HarvestPhase.golf) {
            batch.setColor(Color.WHITE);
            float golfWidth = golfPosition * maxWidth;
            float golfX = startPos.x + MathUtils.cosDeg(rotation) * golfWidth;
            float golfY = startPos.y + MathUtils.sinDeg(rotation) * golfWidth;
            batch.draw(game.assets.skull, golfX - golfIndicatorSize/2f, golfY- golfIndicatorSize/2f, golfIndicatorSize, golfIndicatorSize);
            batch.setColor(1f, 1f, .5f, .1f);
        }
        if (currentPhase == HarvestPhase.collection) {
            scythe.render(batch);
        }
        if (currentPhase != HarvestPhase.collection) {
            batch.setShader(harvestShader);
            harvestShader.setUniformf("u_percent", golfPosition);
            harvestShader.setUniformf("u_dimensions", maxWidth, (GameBoard.tileSize + GameBoard.margin));
            batch.draw(texture, startPos.x, startPos.y - (GameBoard.tileSize + GameBoard.margin) / 2f, 0, (GameBoard.tileSize + GameBoard.margin) / 2f, maxWidth, (GameBoard.tileSize + GameBoard.margin), 1f, 1f, rotation);
            batch.setShader(null);
        }
        batch.setColor(Color.WHITE);
    }

    public void adjustRange(int amount) {
        tilesLong += amount;
        tilesLong = MathUtils.clamp(tilesLong, 1, GameBoard.gridSize);
    }

    public void handleInput() {
        // don't remove this
        if (player.inCorner() || throwCooldown > 0) { return; }

        // TODO: remove this before release
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            adjustRange(1);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            adjustRange(-1);
        }


        boolean touched = Gdx.input.isTouched();

        if (currentPhase == HarvestPhase.cycle && (touched && !touchLastFrame)){
            currentPhase = HarvestPhase.golf;
            game.audioManager.loopSound(AudioManager.Sounds.charge1, game.audioManager.soundVolume.floatValue());
            golfTimer = 0;
        } else if (currentPhase == HarvestPhase.golf && (!touched && touchLastFrame)) {
            currentPhase = HarvestPhase.collection;
            game.audioManager.stopSound(AudioManager.Sounds.charge1);
            game.audioManager.playSound(AudioManager.Sounds.poof1);

            game.audioManager.loopSound(AudioManager.Sounds.swoosh1, game.audioManager.soundVolume.floatValue());
            int tileIndex = MathUtils.clamp((int)(currentPathLength * golfPosition), 0, currentPathLength);
            tileIndex++; // don't want it 0 indexed
            switch (player.currentSide) {
                case top:
                    tileToHarvest = player.gameBoard.tiles[player.currentCol][player.currentRow - tileIndex];
                    break;
                case right:
                    tileToHarvest = player.gameBoard.tiles[player.currentCol - tileIndex][player.currentRow];
                    break;
                case bottom:
                    tileToHarvest = player.gameBoard.tiles[player.currentCol][player.currentRow + tileIndex];
                    break;
                case left:
                    tileToHarvest = player.gameBoard.tiles[player.currentCol + tileIndex][player.currentRow];
                    break;
            }
            if (tileToHarvest == null) {
                // WTF? This should never happen, I am not going to throw an exception just in case though
                currentPhase = HarvestPhase.cycle;
            } else {
                switch(player.currentSide) {
                    case right:
                        scytheSpinTimeMultiplier = (7 - tileToHarvest.coord.x()) + 1;
                        break;
                    case left:
                        scytheSpinTimeMultiplier = tileToHarvest.coord.x() + 1;
                        break;
                    case bottom:
                        scytheSpinTimeMultiplier = tileToHarvest.coord.y() + 1;
                        break;
                    case top:
                        scytheSpinTimeMultiplier = (7 - tileToHarvest.coord.y()) + 1;
                        break;
                }


                float spinTime = scytheSpinTimeMultiplier * .1075f;

                GameScreen _gameScreen = null;
                BaseScreen screen = game.getScreenManager().getCurrentScreen();
                if (screen instanceof GameScreen) {
                    _gameScreen = (GameScreen) screen;
                }
                final GameScreen gameScreen = _gameScreen;

                Timeline.createSequence().push(
                        Tween.set(scythe.position, Vector2Accessor.XY).target(startPos.x, startPos.y))
                        .push(Timeline.createParallel().push(Tween.to(scythe.position, Vector2Accessor.XY, spinTime).target(tileToHarvest.getCenter().x, tileToHarvest.getCenter().y))
                                .push(Tween.to(scythe.rotation, 0, spinTime).target(-720)))
                        .push(Tween.call((type, source) -> tileToHarvest.collect(gameScreen)))
                        .push(Timeline.createParallel().push(Tween.to(scythe.position, Vector2Accessor.XY, spinTime).target(startPos.x, startPos.y))
                                .push(Tween.to(scythe.rotation, 0, spinTime).target(0)))
                        .push(Tween.call((type, source) -> {
                            currentPhase = HarvestPhase.cycle;
                            tileToHarvest = null;
                            throwCooldown = .75f;
                            game.audioManager.stopSound(AudioManager.Sounds.swoosh1);
                        }))
                        .start(game.tween);
            }

        }
        touchLastFrame = touched;
    }
}
