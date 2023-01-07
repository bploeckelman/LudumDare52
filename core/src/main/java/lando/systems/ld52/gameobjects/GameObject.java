package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface GameObject {
    public void update(float dt);
    public void render(SpriteBatch batch);
}
