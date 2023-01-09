package lando.systems.ld52.gameobjects;

import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld52.Assets;

public class Scythe implements GameObject {

    private final Animation<TextureRegion> animation;
    private float stateTime;

    public MutableFloat rotation;
    private final Vector2 size;
    private final Vector2 scale;
    public Vector2 position;

    public Scythe(Assets assets) {
        this.animation = assets.scythe;
        this.stateTime = 0;
        this.rotation = new MutableFloat(0);
        this.size = new Vector2(64, 64);
        this.scale = new Vector2(1, 1);
        TextureRegion keyframe = animation.getKeyFrame(stateTime);
        this.position = new Vector2(
                (Gdx.graphics.getWidth()  - keyframe.getRegionWidth())  / 2f,
                (Gdx.graphics.getHeight() - keyframe.getRegionHeight()) / 2f);
    }

    @Override
    public void update(float dt) { }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion keyframe = animation.getKeyFrame(stateTime);

        float xScale = scale.x;
        float curRotation = rotation.floatValue();
        batch.draw(keyframe,
                position.x - size.x/2f, position.y - size.y/2f,
                keyframe.getRegionWidth() / 2f,
                keyframe.getRegionHeight() / 2f,
                size.x, size.y,
                xScale, scale.y,
                curRotation
        );
    }

}
