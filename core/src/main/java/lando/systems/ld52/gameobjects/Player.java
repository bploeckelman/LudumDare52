package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld52.Assets;

public class Player implements GameObject {

    public enum MoveDirection {
        clockwise,
        counterclockwise;
    }

    public final GameBoard gameBoard;
    private final Animation<TextureRegion> _front;
    private final Animation<TextureRegion> _back;
    private final Animation<TextureRegion> _side;

    private Animation<TextureRegion> _current;
    private boolean _flipped;

    private float _animTime = 0;
    private float _moveTime = 1;

    private MoveDirection _moveDirection = MoveDirection.clockwise;

    public int _boardPosition;

    private Vector2 _renderPosition = new Vector2();
    public HarvestZone harvestZone;

    public Player(Assets assets, GameBoard gameBoard) {
        _front = assets.playerFront;
        _back = assets.playerBack;
        _side = assets.playerSide;

        this.gameBoard = gameBoard;

        reset();
        this.harvestZone = new HarvestZone(this);
    }

    private void reset() {
        _boardPosition = 0;
        _moveDirection = MoveDirection.clockwise;
        _current = _front;
        _flipped = false;
        _animTime = 0;

        setPosition();
    }

    @Override
    public void update(float dt) {
        _animTime += dt;
        if (Gdx.input.justTouched()){
            harvestZone.handleClick();
        }
        if (_animTime > _moveTime) {
            _animTime = 0;
            if (harvestZone.currentPhase == HarvestZone.HarvestPhase.cycle) {
                movePlayer();
            }
        }
        harvestZone.update(dt);
    }

    private void movePlayer() {
        int increment = _moveDirection == MoveDirection.clockwise ? 1 : -1;

        // position 0 is top, left, incrementing around the corners
        //     0   1
        // 7 |---|---| 2
        // 6 |---|---| 3
        //     5   4

        int perimeterTiles = gameBoard.gridSize * 4;
        _boardPosition += increment;
        if (_boardPosition == -1) {
            _boardPosition += perimeterTiles;
        } else if (_boardPosition == perimeterTiles) {
            _boardPosition = 0;
        }

        int side = _boardPosition / gameBoard.gridSize;
        _flipped = false;
        switch (side) {
            case 1: // right
                _flipped = true;
            case 3: // left
                _current = _side;
                break;
            case 2: // bottom
                _flipped = true;
                _current = _back;
                break;
            default:
                // top
                _current = _front;
                break;
        }
        setPosition();
    }

    public void switchDirections() {
        _moveDirection = _moveDirection == MoveDirection.clockwise ? MoveDirection.counterclockwise : MoveDirection.clockwise;
    }

    private void setPosition() {
        float tileSize = GameBoard.tileSize;
        int gridSize = GameBoard.gridSize;
        float moveLaneSize = 88f;

        int sidePosition = (_boardPosition % gridSize);
        float sideOffset = sidePosition * tileSize;

        float xPos, yPos;

        int side = _boardPosition / gridSize;

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
        _renderPosition.set(xPos, yPos);
    }

    @Override
    public void render(SpriteBatch batch) {
        float xScale = _flipped ? -1 : 1;
        batch.draw(_current.getKeyFrame(_animTime),
                _renderPosition.x, _renderPosition.y,
                GameBoard.tileSize / 2, 0,
                GameBoard.tileSize, GameBoard.tileSize,
                xScale, 1, 0);

        harvestZone.render(batch);
    }
}
