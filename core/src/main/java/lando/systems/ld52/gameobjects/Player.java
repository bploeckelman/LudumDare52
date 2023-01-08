package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.CpuSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld52.Assets;

public class Player implements GameObject {

    public enum MoveDirection {
        clockwise,
        counterclockwise;
    }

    public enum Side { top, right, bottom, left}

    public final GameBoard gameBoard;
    private final Animation<TextureRegion> _front;
    private final Animation<TextureRegion> _back;
    private final Animation<TextureRegion> _side;

    private final Animation<TextureRegion> _scythe;

    private Animation<TextureRegion> _current;
    private boolean _flipped;

    private float _animTime = 0;
    private float _moveTime = 0.86f;

    private MoveDirection _moveDirection = MoveDirection.clockwise;

    public int boardPosition;

    private Vector2 _renderPosition = new Vector2();
    private Vector2 _nextPosition = new Vector2();
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
        _flipped = false;
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
        boardPosition += increment;
        if (boardPosition == -1) {
            boardPosition += perimeterTiles;
        } else if (boardPosition == perimeterTiles) {
            boardPosition = 0;
        }

        if (boardPosition < GameBoard.gridSize){
            currentRow = GameBoard.gridSize;
            currentCol = boardPosition;
        } else if (boardPosition < GameBoard.gridSize * 2) {
            currentCol = GameBoard.gridSize;
            currentRow = GameBoard.gridSize - (boardPosition - GameBoard.gridSize);
        } else if (boardPosition < GameBoard.gridSize * 3) {
            currentRow = -1;
            currentCol = (GameBoard.gridSize) - (boardPosition - (GameBoard.gridSize * 2));
        } else {
            currentCol = -1;
            currentRow = boardPosition - (GameBoard.gridSize *3);
        }

        int side = boardPosition / GameBoard.gridSize;
        _flipped = false;
        switch (side) {
            case 1: // right
                currentSide = Side.right;
                _flipped = true;
            case 3: // left
                currentSide = Side.left;
                _current = _side;
                break;
            case 2: // bottom
                currentSide = Side.bottom;
                _flipped = true;
                _current = _back;
                break;
            default:
                // top
                _current = _front;
                currentSide = Side.top;
                break;
        }
        setPosition(false);
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
        float xScale = _flipped ? -1 : 1;

        if (!_flipped) {
            renderScythe(batch);
        }

        batch.draw(_current.getKeyFrame(_animTime),
                _renderPosition.x, _renderPosition.y,
                GameBoard.tileSize / 2, 0,
                GameBoard.tileSize, GameBoard.tileSize,
                xScale, 1, 0);

        if (_flipped) {
            renderScythe(batch);
        }

        harvestZone.render(batch);
    }

    private void renderScythe(SpriteBatch batch) {
        float xScale = _flipped ? -1 : 1;
        batch.draw(_scythe.getKeyFrame(_animTime),
                _renderPosition.x, _renderPosition.y,
                GameBoard.tileSize / 2, 0,
                GameBoard.tileSize, GameBoard.tileSize,
                xScale, 1, 0);
    }
}
