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
    private final Animation<TextureRegion> _front;
    private final Animation<TextureRegion> _back;
    private final Animation<TextureRegion> _side;

    private final Animation<TextureRegion> _scythe;

    private Animation<TextureRegion> _current;
    public boolean flipped;

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

    public Player(Assets assets, GameBoard gameBoard) {
        _front = assets.playerFront;
        _back = assets.playerBack;
        _side = assets.playerSide;

        _scythe = assets.playerScythe;

        this.gameBoard = gameBoard;

        reset();
        this.harvestZone = new HarvestZone(this);
    }

    private void reset() {
        boardPosition = 0;
        _moveDirection = MoveDirection.clockwise;
        _current = _front;
        flipped = false;
        _animTime = 0;

        setPosition(true);
    }

    @Override
    public void update(float dt) {
        _animTime += dt;
        if (_animTime > _moveTime) {
            _animTime = 0;
            if (harvestZone.currentPhase == HarvestZone.HarvestPhase.cycle) {
                movePlayer();

            }
        }

        harvestZone.update(dt);
        _renderPosition.lerp(_nextPosition, dt * 10);
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

        flipped = false;
        switch (side) {
            case 1: // right
                currentSide = Side.right;
                flipped = true;
                break;
            case 3: // left
                currentSide = Side.left;
                _current = _side;
                break;
            case 2: // bottom
                currentSide = Side.bottom;
                flipped = true;
                _current = _back;
                break;
            default:
                // top
                _current = _front;
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
                break;
            case 2: // bottom
                xPos = gameBoard.right() - (tileSize + sideOffset) - ((sidePosition + 1) * GameBoard.margin);
                yPos = gameBoard.bottom() - tileSize - (moveLaneSize - tileSize) / 2f;
                break;
            case 3: // left
                xPos = gameBoard.left() - tileSize - (moveLaneSize - tileSize) / 2f;
                yPos = gameBoard.bottom() + sideOffset + ((sidePosition + 1) * GameBoard.margin);
                break;
            case 0: // top
            default:
                xPos = gameBoard.left() + sideOffset + ((sidePosition + 1) * GameBoard.margin);
                yPos = gameBoard.top() + (moveLaneSize - tileSize) / 2f;
                break;
        }
        _nextPosition.set(xPos, yPos);

        if (immediate) {
            _renderPosition.set(_nextPosition);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (gameBoard.cornerTransition != GameBoard.CornerTransition.none) return;

        float xScale = flipped ? -1 : 1;

        if (!flipped) {
            renderScythe(batch);
        }

        batch.draw(_current.getKeyFrame(_animTime),
                _renderPosition.x, _renderPosition.y,
                GameBoard.tileSize / 2, 0,
                GameBoard.tileSize, GameBoard.tileSize,
                xScale, 1, 0);

        if (flipped) {
            renderScythe(batch);
        }

        harvestZone.render(batch);
    }

    private void renderScythe(SpriteBatch batch) {
        if (harvestZone.currentPhase == HarvestZone.HarvestPhase.collection) return;
        float xScale = flipped ? -1 : 1;
        batch.draw(_scythe.getKeyFrame(_animTime),
                _renderPosition.x, _renderPosition.y,
                GameBoard.tileSize / 2, 0,
                GameBoard.tileSize, GameBoard.tileSize,
                xScale, 1, 0);
    }

    public boolean inCorner() {
        return gameBoard.cornerTransition != GameBoard.CornerTransition.none;
    }
}
