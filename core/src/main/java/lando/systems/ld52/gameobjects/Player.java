package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lando.systems.ld52.Assets;

public class Player implements GameObject {

    private Animation<TextureRegion> _front;
    private Animation<TextureRegion> _back;
    private Animation<TextureRegion> _side;

    private float _imageSize;

    private float _animTime = 0;
    private float _moveTime = 1;

    public Player(Assets assets, float tileSize) {
        _front = assets.playerFront;
        _back = assets.playerBack;
        _side = assets.playerSide;

        _imageSize = tileSize;
    }

    @Override
    public void update(float dt) {
        _animTime += dt;
        if (_animTime > _moveTime) {
            _animTime = 0;
            movePlayer();
        }
    }

    private void movePlayer() { }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(_front.getKeyFrame(_animTime), 0, 0, _imageSize, _imageSize);
    }
}
