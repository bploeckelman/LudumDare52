package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld52.Assets;
import lando.systems.ld52.Main;

public class HarvestZone {

    public enum HarvestPhase { cycle, golf, collection}

    private final float golfIndicatorSize = 8f;
    private final float golfMaxTime = 1f;
    private final Interpolation golfInterpolation = Interpolation.slowFast;

    private Player player;
    public HarvestPhase currentPhase;
    public int tilesLong;
    private Vector2 startPos;
    private float rotation;
    private float golfTimer;
    private float golfPosition;

    public HarvestZone(Player player) {
        this.player = player;
        this.currentPhase = HarvestPhase.cycle;
        this.tilesLong = 5;
        this.startPos = new Vector2();
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
    }

    public void render(SpriteBatch batch) {
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
        Assets.Patch.debug.ninePatch.draw(batch, startPos.x, startPos.y - (GameBoard.tileSize + GameBoard.margin)/2f, 0, (GameBoard.tileSize + GameBoard.margin)/2f, maxWidth, (GameBoard.tileSize + GameBoard.margin), 1f, 1f, rotation);

    }

    public void handleClick() {
        if (currentPhase == HarvestPhase.cycle){
            currentPhase = HarvestPhase.golf;
            golfTimer = 0;
        } else if(currentPhase == HarvestPhase.golf) {
            currentPhase = HarvestPhase.cycle;
        }
    }
}
