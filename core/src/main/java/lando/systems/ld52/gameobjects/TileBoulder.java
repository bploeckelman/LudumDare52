package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld52.Assets;
import lando.systems.ld52.Config;
import lando.systems.ld52.audio.AudioManager;
import lando.systems.ld52.screens.GameScreen;

public class TileBoulder extends TileObject {

    private final TextureRegion texture;

    public TileBoulder(Assets assets, Tile tile) {
        super(tile);
        blocksHarvest = true;
        int numBoulders = 5;
        int index = MathUtils.random(0, numBoulders - 1);
        texture = assets.atlas.findRegion("tiles/boulder", index);
    }

    @Override
    public void update(float dt) {
        // nothing to do
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture,
                tile.bounds.x + margin,
                tile.bounds.y + margin,
                tile.bounds.width - margin * 2f,
                tile.bounds.height - margin * 2f);
    }

    public boolean collect(GameScreen gameScreen) {
        gameScreen.game.particles.lightning(new Vector2(tile.bounds.x + MathUtils.random(-150, 150), Config.Screen.window_height), new Vector2(tile.bounds.x + tile.bounds.width / 2, tile.bounds.y + tile.bounds.height / 2));
        gameScreen.game.particles.explode(tile.bounds.x + tile.bounds.width / 2, tile.bounds.y + tile.bounds.height / 2, tile.bounds.width);
        gameScreen.screenShaker.addDamage(15f);
        gameScreen.score += 50;

        gameScreen.audioManager.playSound(AudioManager.Sounds.thud);
        return false;
    }

}
