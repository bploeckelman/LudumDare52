package lando.systems.ld52.gameobjects;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld52.Assets;
import lando.systems.ld52.Main;
import lando.systems.ld52.utils.accessors.Vector2Accessor;

public class HarvestZone {

    public enum HarvestPhase { cycle, golf, collection}

    private final float golfIndicatorSize = 8f;
    private final float golfMaxTime = 1f;
    private final Interpolation golfInterpolation = Interpolation.slowFast;

    private Player player;
    public Scythe scythe;

    public HarvestPhase currentPhase;
    public int tilesLong;
    private Vector2 startPos;
    private float rotation;
    private float golfTimer;
    public float golfPosition;
    public Tile tileToHarvest;

    public HarvestZone(Player player) {
        this.player = player;
        this.currentPhase = HarvestPhase.cycle;
        this.tilesLong = 5;
        this.startPos = new Vector2();
        this.scythe = new Scythe(Main.game.assets);
        tileToHarvest = null;
    }

    public void update(float dt) {
        float tileSize = GameBoard.tileSize;
        int gridSize = GameBoard.gridSize;
        float moveLaneSize = 88f;

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
            golfTimer += dt;
            if (golfTimer > 2*golfMaxTime) {
                golfTimer -= 2*golfMaxTime;
            }
            float tempgolfpos = golfTimer/golfMaxTime;
            if (tempgolfpos > 1f) {
                tempgolfpos = 2f - tempgolfpos;
            }
            golfPosition = golfInterpolation.apply(tempgolfpos);
        }

        scythe.update(dt);
    }

    public void render(SpriteBatch batch) {
        // TODO: this will need to be stored if we are going to check for gravestones, etc along the path
        float maxWidth = (GameBoard.tileSize+GameBoard.margin) * tilesLong;
        if (currentPhase == HarvestPhase.cycle) {
            batch.setColor(1f, 1f, 1f, .5f);
        }
        if (currentPhase == HarvestPhase.golf) {
            batch.setColor(1f, 1f, .5f, 1f);
            float golfWidth = golfPosition * maxWidth;
            float golfX = startPos.x + MathUtils.cosDeg(rotation) * golfWidth;
            float golfY = startPos.y + MathUtils.sinDeg(rotation) * golfWidth;
            batch.draw(Main.game.assets.circleTex, golfX - golfIndicatorSize/2f, golfY- golfIndicatorSize/2f, golfIndicatorSize, golfIndicatorSize);
        }
        if (currentPhase == HarvestPhase.collection) {
            scythe.render(batch);
        }
        Assets.Patch.debug.ninePatch.draw(batch, startPos.x, startPos.y - (GameBoard.tileSize + GameBoard.margin)/2f, 0, (GameBoard.tileSize + GameBoard.margin)/2f, maxWidth, (GameBoard.tileSize + GameBoard.margin), 1f, 1f, rotation);
        batch.setColor(Color.WHITE);
    }

    public void handleClick() {
        if (currentPhase == HarvestPhase.cycle){
            currentPhase = HarvestPhase.golf;
            golfTimer = 0;
        } else if(currentPhase == HarvestPhase.golf) {
            currentPhase = HarvestPhase.collection;
            // TODO: when we store this for gravestones, check this later
            float maxWidth = (GameBoard.tileSize+GameBoard.margin) * tilesLong;
            float golfWidth = golfPosition * maxWidth;
            float golfX = startPos.x + MathUtils.cosDeg(rotation) * golfWidth;
            float golfY = startPos.y + MathUtils.sinDeg(rotation) * golfWidth;
            tileToHarvest = player.gameBoard.getTileAt(golfX, golfY);
            if (tileToHarvest == null) { // Sorry Brian P
                tileToHarvest = player.gameBoard.getTileAt(golfX - 20, golfY);
                if (tileToHarvest == null) {
                    tileToHarvest = player.gameBoard.getTileAt(golfX + 20, golfY);
                    if (tileToHarvest == null) {
                        tileToHarvest = player.gameBoard.getTileAt(golfX, golfY - 20);
                        if (tileToHarvest == null) {
                            tileToHarvest = player.gameBoard.getTileAt(golfX, golfY + 20);
                        }
                    }
                }
            }
            if (tileToHarvest == null) {
                // WTF? This should never happen, I am not going to throw an exception just in case though
            } else {
                Timeline.createSequence().push(
                        Tween.set(scythe.position, Vector2Accessor.XY).target(startPos.x, startPos.y))
                        .push(Timeline.createParallel().push(Tween.to(scythe.position, Vector2Accessor.XY, 1f).target(golfX, golfY))
                                .push(Tween.to(scythe.rotation, 0, 1f).target(720)))
                        .push(Tween.call((type, source) -> tileToHarvest.collect()))
                        .push(Timeline.createParallel().push(Tween.to(scythe.position, Vector2Accessor.XY, 1f).target(startPos.x, startPos.y))
                                .push(Tween.to(scythe.rotation, 0, 1f).target(0)))
                        .push(Tween.call((type, source) -> {
                            currentPhase = HarvestPhase.cycle;
                            tileToHarvest = null;
                        }))
                        .start(Main.game.tween);
            }

        }
    }
}
