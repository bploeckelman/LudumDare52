package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld52.Assets;
import lando.systems.ld52.audio.AudioManager;

import static lando.systems.ld52.Main.game;

public class Player implements GameObject {

    public enum MoveDirection {
        clockwise,
        counterclockwise
    }

    public enum Side { top, right, bottom, left}

    public final GameBoard gameBoard;
    private final Animation<TextureRegion> playerWithScythe;
    private final Animation<TextureRegion> playerNoScythe;
    private Animation<TextureRegion> currentPlayerAnimation;

    private float _animTime = 0;
    private final float _moveTime = 0.88f;

    private MoveDirection _moveDirection = MoveDirection.clockwise;

    public int boardPosition;

    private final Vector2 _renderPosition = new Vector2();
    private final Vector2 _nextPosition = new Vector2();
    public HarvestZone harvestZone;
    public Side currentSide = Side.left;
    public int currentCol;
    public int currentRow;
    // this is for rotating the sprite on side
    private float spriteRotation = 0;
    private Vector2 floatOffset = new Vector2();

    public Player(Assets assets, GameBoard gameBoard) {
        playerWithScythe = assets.playerWithScythe;
        playerNoScythe = assets.playerNoScythe;

        this.gameBoard = gameBoard;
        gameBoard.player = this;

        reset();
        this.harvestZone = new HarvestZone(this);
    }

    public void reset() {
        boardPosition = 0;
        // hack because player starts on the left, so floats right,left before first moving,
        // if starting on top side, harvestZone update causes arrayoutofbounds
        gameBoard.cornerTransition = GameBoard.CornerTransition.top_left;
        _moveDirection = MoveDirection.clockwise;
        currentPlayerAnimation = playerWithScythe;
        _animTime = 0;
        currentRow = GameBoard.gridSize;
        currentCol = boardPosition;
        currentSide = Side.top;

        setPosition(true);
    }

    @Override
    public void update(float dt) {
        _animTime += dt;
        float offset = 3 * (float)Math.sin(_animTime * 15);
        setOffset(offset);

        if (_animTime > _moveTime) {
            _animTime = 0;
            if (harvestZone.currentPhase == HarvestZone.HarvestPhase.cycle) {
                movePlayer();
            }
        }

        harvestZone.update(dt);

        boolean noScythe = (harvestZone.currentPhase == HarvestZone.HarvestPhase.collection) || harvestZone.throwCooldown > 0;
        currentPlayerAnimation = noScythe ? playerNoScythe : playerWithScythe;

        _renderPosition.lerp(_nextPosition, dt * 10);
    }

    private void setOffset(float offset) {
        float dx = 0, dy = 0;
        switch (currentSide) {
            case top:
                dy = offset;
                break;
            case bottom:
                dy = -offset;
                break;
            case left:
                dx = offset;
                break;
            case right:
                dx = -offset;
                break;
        }
        floatOffset.set(dx, dy);

    }

    private void movePlayer() {
        int increment = _moveDirection == MoveDirection.clockwise ? 1 : -1;

        // position 0 is top, left, incrementing around the corners
        //     0   1
        // 7 |---|---| 2
        // 6 |---|---| 3
        //     5   4

        int perimeterTiles = GameBoard.gridSize * 4;

        // this is getting crazy, but I'm too lazy to simplify - add another variable
        int previousSide = boardPosition / GameBoard.gridSize;

        if (!inCorner()) {
            boardPosition += increment;
            if (boardPosition == -1) {
                boardPosition += perimeterTiles;
            } else if (boardPosition == perimeterTiles) {
                boardPosition = 0;
            }
        }
        int side = boardPosition / GameBoard.gridSize;

        if (previousSide != side) {
            switch (side) {
                case 1:
                    gameBoard.cornerTransition = GameBoard.CornerTransition.top_right;
                    break;
                case 2:
                    gameBoard.cornerTransition = GameBoard.CornerTransition.bottom_right;
                    break;
                case 3:
                    gameBoard.cornerTransition = GameBoard.CornerTransition.bottom_left;
                    break;
                default:
                    gameBoard.cornerTransition = GameBoard.CornerTransition.top_left;
                    break;
            }
            // set corner transition
            return;
        }

        if (boardPosition < GameBoard.gridSize) {
            currentRow = GameBoard.gridSize;
            currentCol = boardPosition;
        } else if (boardPosition < GameBoard.gridSize * 2) {
            currentCol = GameBoard.gridSize;
            currentRow = GameBoard.gridSize - (boardPosition - GameBoard.gridSize) - 1;
        } else if (boardPosition < GameBoard.gridSize * 3) {
            currentRow = -1;
            currentCol = (GameBoard.gridSize) - (boardPosition - (GameBoard.gridSize * 2)) - 1;
        } else {
            currentCol = -1;
            currentRow = boardPosition - (GameBoard.gridSize *3);
        }

        switch (side) {
            case 1: // right
                currentSide = Side.right;
                break;
            case 3: // left
                currentSide = Side.left;
                break;
            case 2: // bottom
                currentSide = Side.bottom;
                break;
            default:
                // top
                currentSide = Side.top;
                break;
        }

        // last 'move' was in the corner
        if (inCorner()) {
            gameBoard.cornerTransition = GameBoard.CornerTransition.none;
            setPosition(true);
        } else {
            setPosition(false);
        }
        game.audioManager.playSound(AudioManager.Sounds.clock, .35f);
    }

    public void switchDirections() {
        _moveDirection = _moveDirection == MoveDirection.clockwise ? MoveDirection.counterclockwise : MoveDirection.clockwise;
    }

    private void setPosition(boolean immediate) {
        float tileSize = GameBoard.tileSize;
        int gridSize = GameBoard.gridSize;
        float moveLaneSize = 88f;

        int sidePosition = (boardPosition % gridSize);
        float sideOffset = sidePosition * tileSize;

        float xPos, yPos;

        int side = boardPosition / gridSize;

        switch (side) {
            case 1: // right
                xPos = gameBoard.right() + (moveLaneSize - tileSize) / 2f;
                yPos = gameBoard.top() - (tileSize + sideOffset) - ((sidePosition + 1) * GameBoard.margin);
                spriteRotation = 270;
                break;
            case 2: // bottom
                xPos = gameBoard.right() - (tileSize + sideOffset) - ((sidePosition + 1) * GameBoard.margin);
                yPos = gameBoard.bottom() - tileSize - (moveLaneSize - tileSize) / 2f;
                spriteRotation = 180;
                break;
            case 3: // left
                xPos = gameBoard.left() - tileSize - (moveLaneSize - tileSize) / 2f;
                yPos = gameBoard.bottom() + sideOffset + ((sidePosition + 1) * GameBoard.margin);
                spriteRotation = 90;
                break;
            case 0: // top
            default:
                xPos = gameBoard.left() + sideOffset + ((sidePosition + 1) * GameBoard.margin);
                yPos = gameBoard.top() + (moveLaneSize - tileSize) / 2f;
                spriteRotation = 0;
                break;
        }
        _nextPosition.set(xPos, yPos);

        if (immediate) {
            _renderPosition.set(_nextPosition);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (inCorner()) return;

        TextureRegion playerImage = currentPlayerAnimation.getKeyFrame(_animTime);
        batch.draw(playerImage,
                _renderPosition.x + floatOffset.x, _renderPosition.y + floatOffset.y,
                GameBoard.tileSize / 2, GameBoard.tileSize / 2,
                GameBoard.tileSize- 15, GameBoard.tileSize -15,
                1, 1, spriteRotation);

        harvestZone.render(batch);
    }

    public boolean inCorner() {
        return gameBoard.cornerTransition != GameBoard.CornerTransition.none;
    }
}
