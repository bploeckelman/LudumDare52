package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld52.Assets;

enum MoveDirection {
    clockwise,
    counterclockwise;
}

public class Player implements GameObject {
    private final GameBoard _gameBoard;
    private Animation<TextureRegion> _front;
    private Animation<TextureRegion> _back;
    private Animation<TextureRegion> _side;

    private Animation<TextureRegion> _current;
    private boolean _flipped;

    private float _animTime = 0;
    private float _moveTime = 1;

    private MoveDirection _moveDirection = MoveDirection.clockwise;

    private int _boardPosition;

    private Vector2 _renderPosition = new Vector2();

    public Player(Assets assets, GameBoard gameBoard) {
        _front = assets.playerFront;
        _back = assets.playerBack;
        _side = assets.playerSide;

        _gameBoard = gameBoard;

        reset();
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
        if (_animTime > _moveTime) {
            _animTime = 0;
            movePlayer();
        }
    }

    private void movePlayer() {
        int increment = _moveDirection == MoveDirection.clockwise ? 1 : -1;

        // position 0 is top, left, incrementing around the corners
        //     0   1
        // 7 |---|---| 2
        // 6 |---|---| 3
        //     5   4

        int perimeterTiles = _gameBoard.gridSize * 4;
        _boardPosition += increment;
        if (_boardPosition == -1) {
            _boardPosition += perimeterTiles;
        } else if (_boardPosition == perimeterTiles) {
            _boardPosition = 0;
        }

        int side = _boardPosition / _gameBoard.gridSize;
        _flipped = false;
        switch (side) {
            case 1: // right
                _flipped = true;
            case 3: // left
                _current = _side;
                break;
            case 2: // bottom
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
        Rectangle board = _gameBoard.boardArea;
        float tileSize = _gameBoard.tileSize;
        int gridSize = _gameBoard.gridSize;

        float sideOffset = (_boardPosition % gridSize) * tileSize;

        float xPos, yPos;

        int side = _boardPosition / gridSize;

        switch (side) {
            case 1: // right
                xPos = board.x + board.width;
                yPos = board.y + board.height - (tileSize + sideOffset);
                break;
            case 2: // bottom
                xPos = board.x + board.width - (tileSize + sideOffset);
                yPos = board.y - tileSize;
                break;
            case 3: // left
                xPos = board.x - tileSize;
                yPos = board.y + sideOffset;
                break;
            case 0: // top
            default:
                xPos = board.x + sideOffset;
                yPos = board.y + board.height;
                break;
        }
        _renderPosition.set(xPos, yPos);
    }

    @Override
    public void render(SpriteBatch batch) {
        float xScale = _flipped ? -1 : 1;
        batch.draw(_current.getKeyFrame(_animTime), _renderPosition.x, _renderPosition.y, _gameBoard.tileSize / 2,
                0, _gameBoard.tileSize, _gameBoard.tileSize, xScale, 1, 0);
    }
}
