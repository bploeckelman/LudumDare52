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

public class TileCoin extends TileObject {
    private final Animation<TextureRegion> animation;
    private float stateTime;

    public TileCoin(Assets assets, Tile tile) {
        super(tile);
        if (MathUtils.randomBoolean()) {
            animation = assets.btcCoin;
        } else {
            animation = assets.dogeCoin;
        }
//        Main.game.audioManager.playSound(AudioManager.Sounds.chargeUp);
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
        gameScreen.game.particles.lightning(new Vector2(tile.bounds.x + MathUtils.random(-150, 150), Config.Screen.window_height), new Vector2(tile.bounds.x + tile.bounds.width / 2, tile.bounds.y + tile.bounds.height / 2));
        gameScreen.game.particles.explode(tile.bounds.x + tile.bounds.width / 2, tile.bounds.y + tile.bounds.height / 2, tile.bounds.width);
        gameScreen.screenShaker.addDamage(25f);
        gameScreen.score += 1337;

        // TODO: need coin sound
        gameScreen.audioManager.playSound(AudioManager.Sounds.coin, .5f);
        return false;
    }
}
