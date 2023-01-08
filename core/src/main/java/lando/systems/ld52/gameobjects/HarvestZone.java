package lando.systems.ld52.gameobjects;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld52.Assets;
import lando.systems.ld52.audio.AudioManager;
import lando.systems.ld52.utils.accessors.Vector2Accessor;

import static lando.systems.ld52.Main.game;

public class HarvestZone {

    public enum HarvestPhase { cycle, golf, collection}

    private static final float golfIndicatorSize = 8f;
    private static final float golfMaxTime = .25f; // Time it takes to go one tile
    private static final int tilesStart = 4;
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

    public HarvestZone(Player player) {
        this.player = player;
        this.currentPhase = HarvestPhase.cycle;
        this.tilesLong = tilesStart;
        this.startPos = new Vector2();
        this.scythe = new Scythe(game.assets);
        tileToHarvest = null;
        touchLastFrame = false;
    }

    public void update(float dt) {
        handleInput();
        currentPathLength = tilesLong;
        // TODO: if hits something along the way, change currentPathLength

        float tileSize = GameBoard.tileSize;
        int gridSize = GameBoard.gridSize;

        int sidePosition = (player.boardPosition % gridSize);
        float sideOffset = sidePosition * tileSize;

        float xPos, yPos;

        int side = player.boardPosition / gridSize;

        switch (side) {
            case 1: // right
                rotation = 180;
                xPos = player.gameBoard.right();
                yPos = player.gameBoard.top() - (tileSize + sideOffset) - ((sidePosition + 1) * GameBoard.margin) + tileSize/2f;
                break;
            case 2: // bottom
                rotation = 90;
                xPos = player.gameBoard.right() - (tileSize + sideOffset) - ((sidePosition + 1) * GameBoard.margin) + tileSize/2f;
                yPos = player.gameBoard.bottom();
                break;
            case 3: // left
                rotation = 0;
                xPos = player.gameBoard.left();
                yPos = player.gameBoard.bottom() + sideOffset + ((sidePosition + 1) * GameBoard.margin) + tileSize/2f;
                break;
            case 0: // top
            default:
                rotation = 270;
                xPos = player.gameBoard.left() + sideOffset + ((sidePosition + 1) * GameBoard.margin) + tileSize/2f;
                yPos = player.gameBoard.top();
                break;
        }
        startPos.set(xPos, yPos);

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
        }

        scythe.update(dt);
    }

    public void render(SpriteBatch batch) {
        // TODO: this will need to be stored if we are going to check for gravestones, etc along the path
        float maxWidth = (GameBoard.tileSize+GameBoard.margin) * currentPathLength;
        if (currentPhase == HarvestPhase.cycle) {
            batch.setColor(1f, 1f, 1f, .5f);
        }
        if (currentPhase == HarvestPhase.golf) {
            batch.setColor(1f, 1f, .5f, 1f);
            float golfWidth = golfPosition * maxWidth;
            float golfX = startPos.x + MathUtils.cosDeg(rotation) * golfWidth;
            float golfY = startPos.y + MathUtils.sinDeg(rotation) * golfWidth;
            batch.draw(game.assets.circleTex, golfX - golfIndicatorSize/2f, golfY- golfIndicatorSize/2f, golfIndicatorSize, golfIndicatorSize);
        }
        if (currentPhase == HarvestPhase.collection) {
            scythe.render(batch);
        }
        Assets.Patch.debug.ninePatch.draw(batch, startPos.x, startPos.y - (GameBoard.tileSize + GameBoard.margin)/2f, 0, (GameBoard.tileSize + GameBoard.margin)/2f, maxWidth, (GameBoard.tileSize + GameBoard.margin), 1f, 1f, rotation);
        batch.setColor(Color.WHITE);
    }

    public void adjustRange(int amount) {
        tilesLong += amount;
        tilesLong = MathUtils.clamp(tilesLong, 1, GameBoard.gridSize);
    }

    public void handleInput() {
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
            golfTimer = 0;
        } else if (currentPhase == HarvestPhase.golf && (!touched && touchLastFrame)) {
            currentPhase = HarvestPhase.collection;
            game.audioManager.loopSound(AudioManager.Sounds.swoosh1, game.audioManager.musicVolume.floatValue());
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
                Timeline.createSequence().push(
                        Tween.set(scythe.position, Vector2Accessor.XY).target(startPos.x, startPos.y))
                        .push(Timeline.createParallel().push(Tween.to(scythe.position, Vector2Accessor.XY, 1f).target(tileToHarvest.getCenter().x, tileToHarvest.getCenter().y))
                                .push(Tween.to(scythe.rotation, 0, 1f).target(-720)))
                        .push(Tween.call((type, source) -> tileToHarvest.collect()))
                        .push(Timeline.createParallel().push(Tween.to(scythe.position, Vector2Accessor.XY, 1f).target(startPos.x, startPos.y))
                                .push(Tween.to(scythe.rotation, 0, 1f).target(0)))
                        .push(Tween.call((type, source) -> {
                            currentPhase = HarvestPhase.cycle;
                            tileToHarvest = null;
                            game.audioManager.stopSound(AudioManager.Sounds.swoosh1);
                        }))
                        .start(game.tween);
            }

        }
        touchLastFrame = touched;
    }
}
