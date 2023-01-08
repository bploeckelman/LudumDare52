package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lando.systems.ld52.Assets;

public class PlayerUI implements GameObject {

    private final Player player;
    private final TextureRegion background;
    private final Animation<TextureRegion> idle;
    private final Animation<TextureRegion> backswing;
    private final Animation<TextureRegion> swing;

    private Animation<TextureRegion> current;
    private float stateTime;

    public PlayerUI(Player player, Assets assets) {
        this.player = player;
        background = assets.pixelRegion;
        current = idle = new Animation<>(.2f, assets.atlas.findRegions("ui/player/death-swing-idle/death-swing-idle"), Animation.PlayMode.LOOP);
        backswing = new Animation<>(0.1f, assets.atlas.findRegions("ui/player/death-swing-back/death-swing-back"),Animation.PlayMode.NORMAL);
        swing = new Animation<>(0.1f, assets.atlas.findRegions("ui/player/death-swing-thru/death-swing-thru"), Animation.PlayMode.NORMAL);
        stateTime = 0f;
    }

    private boolean collect = false;
    @Override
    public void update(float dt) {
        stateTime += dt;

        HarvestZone harvestZone = player.harvestZone;

        switch (harvestZone.currentPhase) {
            case cycle:
                current = idle;
                break;
            case golf:
                collect = false;
                current = backswing;
                stateTime = backswing.getAnimationDuration() * harvestZone.golfPosition;
                break;
            case collection:
                if (!collect) {
                    stateTime = 0;
                    collect = true;
                }
                current = swing;
                break;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        // hard coded layout for now...
        float x = 32;
        float y = 32;
        float w = 220;
        float h = 220;

        // TODO - pick patch based on time remaining... (?green ->) plain -> yellow -> red
        NinePatch patch = Assets.NinePatches.plain_gradient;
        patch.draw(batch, x, y, w, h);

        TextureRegion keyframe = current.getKeyFrame(stateTime);
        batch.draw(keyframe,
                x + (w - keyframe.getRegionWidth()) / 2f,
                y + (h - keyframe.getRegionHeight()) / 2f);
    }
}
