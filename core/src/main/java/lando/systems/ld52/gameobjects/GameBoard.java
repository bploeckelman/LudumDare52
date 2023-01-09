package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld52.Assets;
import lando.systems.ld52.Config;
import lando.systems.ld52.Main;
import lando.systems.ld52.data.RoundData;
import lando.systems.ld52.screens.GameScreen;

import static lando.systems.ld52.gameobjects.GameBoard.CornerTransition.*;

public class GameBoard {

//    public final static int gridSize = 7;
//    public final static float tileSize = 72;
//    public final static float margin = 6;
//    public final static float boardSize = margin + (tileSize + margin) * gridSize;

    // 6x6 grid instead of 7x7
    public final static float boardSize = 550;
    public final static int gridSize = 6;
    public final static float margin = 6;
    public final static float tileSize = (boardSize - (gridSize + 1) * margin) / (gridSize);
    public final static float MAX_TIME_IN_SECONDS = 45;
    public final static float EXTRA_TIME_PER_PERSON = 15;

    public enum CornerTransition { none, top_left, top_right, bottom_right, bottom_left }
    public CornerTransition cornerTransition = CornerTransition.none;
    private boolean wasInCorner;

    private final Vector2 cornerPosTopLeft = new Vector2(280, 590);
    private final Vector2 cornerPosTopRight = new Vector2(868, 590);
    private final Vector2 cornerPosBottomLeft = new Vector2(280, 0);
    private final Vector2 cornerPosBottomRight = new Vector2(870, 0);

    private final Animation<TextureRegion> cornerIdleAnim;
    private final Animation<TextureRegion> cornerActionAnim;
    private float cornerStateTime;

    private final WalkPath walkPathTop;
    private final WalkPath walkPathBottom;
    private final WalkPath walkPathLeft;
    private final WalkPath walkPathRight;

    public Tile[][] tiles;

    public Rectangle bounds;
    public GameScreen screen;
    public float timer;
    public float maxTime;
    public float elapsed;
    public Player player;

    public GameBoard(GameScreen screen, Assets assets) {
        this.screen = screen;
        timer = MAX_TIME_IN_SECONDS;
        maxTime = MAX_TIME_IN_SECONDS;
        elapsed = 0;
        bounds = new Rectangle(
                (Config.Screen.window_width - boardSize) / 2f,
                (Config.Screen.window_height - boardSize) / 2f,
                boardSize, boardSize);
     
        cornerIdleAnim = assets.cornerIdle;
        cornerActionAnim = assets.cornerAction;
        cornerStateTime = 0f;
        wasInCorner = false;

        walkPathTop    = new WalkPath(assets, "top",    380, 632);
        walkPathRight  = new WalkPath(assets, "right",  912, 100);
        walkPathBottom = new WalkPath(assets, "bottom", 380, 0);
        walkPathLeft   = new WalkPath(assets, "left",   280, 100);
    }
    
    public void setupBoard(Assets assets, RoundData roundData) {
        tiles = new Tile[gridSize][];
        for (int x = 0; x < gridSize; x++){
            tiles[x] = new Tile[gridSize];
            for (int y = 0; y < gridSize; y++) {
                tiles[x][y] = new Tile(assets, x, y, tileSize, this, roundData.tileData[x][y]);
            }
        }
    }

    public float left() {
        return bounds.x;
    }

    public float right() {
        return bounds.x + bounds.width;
    }

    public float top() {
        return bounds.y + bounds.height;
    }

    public float bottom() {
        return bounds.y;
    }

    public void resetTimer() {
        Stats.timeTaken += elapsed;
        maxTime = MAX_TIME_IN_SECONDS + EXTRA_TIME_PER_PERSON * screen.heavenQuota.people.size;
        timer = maxTime;
        elapsed = 0;
    }

    public void update(float dt) {
        timer -= dt;
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                tiles[x][y].update(dt);
            }
        }

        cornerStateTime += dt;

        // set the walk path active
        walkPathTop.active = false;
        walkPathRight.active = false;
        walkPathBottom.active = false;
        walkPathLeft.active = false;
        if (cornerTransition == CornerTransition.none) {
            switch (player.currentSide) {
                case top: walkPathTop.active = true; break;
                case right: walkPathRight.active = true; break;
                case bottom: walkPathBottom.active = true; break;
                case left: walkPathLeft.active = true; break;
            }
            wasInCorner = false;
        } else {
            if (!wasInCorner) {
                wasInCorner = true;
                cornerStateTime = 0f;
            }
        }

        walkPathTop.update(dt);
        walkPathRight.update(dt);
        walkPathBottom.update(dt);
        walkPathLeft.update(dt);
    }

    public void render(SpriteBatch batch) {
        if (Config.Debug.general) {
            batch.setColor(0, 1, 1, 0.25f);
            batch.draw(Main.game.assets.pixelRegion, bounds.x, bounds.y, bounds.width, bounds.height);
            batch.setColor(1, 1, 1, 1);
        }

        for (int x = 0; x < gridSize; x++){
            for (int y = 0; y < gridSize; y++) {
                Tile t = tiles[x][y];
                boolean highlighted = t.equals(screen.player.harvestZone.tileToHarvest);
                t.render(batch, highlighted);
            }
        }

        TextureRegion keyframe;

        // draw edge animations
        walkPathTop.render(batch);
        walkPathRight.render(batch);
        walkPathBottom.render(batch);
        walkPathLeft.render(batch);

        // render corner animations
        Animation<TextureRegion> cornerAnim;

        // top left
        cornerAnim = (cornerTransition == top_left) ? cornerActionAnim : cornerIdleAnim;
        keyframe = cornerAnim.getKeyFrame(cornerStateTime);
        batch.draw(keyframe, cornerPosTopLeft.x, cornerPosTopLeft.y);

        // top right
        cornerAnim = (cornerTransition == top_right) ? cornerActionAnim : cornerIdleAnim;
        keyframe = cornerAnim.getKeyFrame(cornerStateTime);
        batch.draw(keyframe,
                cornerPosTopRight.x, cornerPosTopRight.y,
                keyframe.getRegionWidth() / 2f,
                keyframe.getRegionHeight() / 2f,
                keyframe.getRegionWidth(),
                keyframe.getRegionHeight(),
                1f, 1f,
                -90
        );

        // bottom right
        cornerAnim = (cornerTransition == bottom_right) ? cornerActionAnim : cornerIdleAnim;
        keyframe = cornerAnim.getKeyFrame(cornerStateTime);
        batch.draw(keyframe,
                cornerPosBottomRight.x, cornerPosBottomRight.y,
                keyframe.getRegionWidth() / 2f,
                keyframe.getRegionHeight() / 2f,
                keyframe.getRegionWidth(),
                keyframe.getRegionHeight(),
                1f, 1f,
                -180
        );

        // bottom left
        cornerAnim = (cornerTransition == bottom_left) ? cornerActionAnim : cornerIdleAnim;
        keyframe = cornerAnim.getKeyFrame(cornerStateTime);
        batch.draw(keyframe,
                cornerPosBottomLeft.x, cornerPosBottomLeft.y,
                keyframe.getRegionWidth() / 2f,
                keyframe.getRegionHeight() / 2f,
                keyframe.getRegionWidth(),
                keyframe.getRegionHeight(),
                1f, 1f,
                -270
        );
    }

    public int getSecondsLeft() {
        int displaySeconds = (int)Math.ceil(timer);
        if (displaySeconds < 0){
            displaySeconds = 0;
        }
        return displaySeconds;
    }

    public float getTimerPercent() {
        return MathUtils.clamp(timer/ maxTime,0, 1f);
    }

    public Tile getTileAt(float worldX, float worldY) {
        if (worldX < left() || worldX >= right() || worldY < bottom() || worldY >= top()) {
            return null;
        }
        try {
            int xIndex = (int) ((worldX - left()) / bounds.width * gridSize);
            int yIndex = (int) ((worldY - bottom()) / bounds.height * gridSize);

            return tiles[xIndex][yIndex];
        } catch (Exception ex) {
            // something went wrong?
            return null;
        }
    }
}
