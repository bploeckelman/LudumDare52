package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld52.Assets;
import lando.systems.ld52.Config;
import lando.systems.ld52.audio.AudioManager;
import lando.systems.ld52.screens.GameScreen;

public class TilePowerdown extends TileObject {

    private final Animation<TextureRegion> animation;
    private float stateTime;

    public TilePowerdown(Assets assets, Tile tile) {
        super(tile);
        animation = assets.powerdown;
        stateTime = 0f;
    }


    @Override
    public void update(float dt) {
        stateTime += dt;
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion keyframe = animation.getKeyFrame(stateTime);
        batch.draw(keyframe,
                tile.bounds.x + margin,
                tile.bounds.y + margin,
                tile.bounds.width - margin * 2f,
                tile.bounds.height - margin * 2f);
    }

    public boolean collect(GameScreen gameScreen) {
        Stats.numPowerdownsReaped++;

        gameScreen.game.particles.lightning(new Vector2(tile.bounds.x + MathUtils.random(-150, 150), Config.Screen.window_height), new Vector2(tile.bounds.x + tile.bounds.width / 2, tile.bounds.y + tile.bounds.height / 2));
        gameScreen.game.particles.explode(tile.bounds.x + tile.bounds.width / 2, tile.bounds.y + tile.bounds.height / 2, tile.bounds.width);
        gameScreen.screenShaker.addDamage(25f);
        gameScreen.score += 10;
        gameScreen.player.harvestZone.adjustRange(-1);

        // TODO: need power down sound
        gameScreen.audioManager.playSound(AudioManager.Sounds.thud);
        return false;
    }
}
