package lando.systems.ld52.ui;

import com.kotcrab.vis.ui.widget.VisTable;
import lando.systems.ld52.Assets;

public class LeftSideUI extends VisTable {
    private final float WIDTH = 256f;
    private final float SCOREBOX_HEIGHT = 80f;
    private final float CHARACTER_STAT_HEIGHT = 250f;
    private final float MARGIN = 12f;
    public LeftSideUI(Assets assets, float x, float y, float width, float height) {
        setSize(width, height);
        setPosition(x, y);
        setDebug(true);
        ScoreBoxUI scoreBoxUI = new ScoreBoxUI();
        add(scoreBoxUI).pad(MARGIN).growX().size(WIDTH, SCOREBOX_HEIGHT);
        row();
        HarvestedSoulUI harvestedSoulUI = new HarvestedSoulUI();
        add(harvestedSoulUI).padLeft(MARGIN).padRight(MARGIN).padBottom(MARGIN).growX().size(WIDTH, CHARACTER_STAT_HEIGHT);
        row();
        PlayerStatUI playerStatUI = new PlayerStatUI(assets);
        add(playerStatUI).padLeft(MARGIN).padRight(MARGIN).padBottom(MARGIN).growX().width(WIDTH).growY();
    }
}
