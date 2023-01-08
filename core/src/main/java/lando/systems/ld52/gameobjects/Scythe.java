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

    private final float lifetime = 2f;
    private float elapsed = 0f;
    private final float pausetime = 1f;
    private float paused = 0f;
    private int direction = 1;

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
    public void update(float dt) {
//        stateTime += dt;
//
//        paused -= dt;
//        if (paused <= 0) {
//            paused = 0;
//        }
//
//        if (paused == 0) {
//            elapsed += direction * dt;
//            if (elapsed <= 0 || elapsed >= lifetime) {
//                direction *= -1;
//                paused = pausetime;
//            }
//            elapsed = MathUtils.clamp(elapsed, 0, lifetime);
//        }
//
//        float progress = Calc.min(elapsed / lifetime, 1f); // 0 <-> 1
//        float t = Interpolation.fastSlow.apply(progress);
//
//        float speed = 1000;
//        rotation -= (t * speed) * dt;
//
//        float minPos = (88 - size.x) / 2f;
//        float maxPos = 560;
//        position.y = minPos + t * (maxPos - minPos);
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion keyframe = animation.getKeyFrame(stateTime);
        batch.draw(keyframe,
                position.x - size.x/2f, position.y - size.y/2f,
                keyframe.getRegionWidth() / 2f,
                keyframe.getRegionHeight() / 2f,
                size.x, size.y,
                scale.x, scale.y,
                rotation.floatValue()
        );
    }

}
