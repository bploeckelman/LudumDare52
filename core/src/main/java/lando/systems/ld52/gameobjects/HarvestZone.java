package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HarvestZone {

    public enum HarvestPhase { cycle, golf, collection}

    private Player player;
    public HarvestPhase currentPhase;
    public int tilesLong;

    public HarvestZone(Player player) {
        this.player = player;
        this.currentPhase = HarvestPhase.cycle;
    }

    public void update(float dt) {

    }

    public void render(SpriteBatch batch) {

    }

    public void handleClick() {

    }
}
