package lando.systems.ld52.ui;

import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisTable;
import lando.systems.ld52.Assets;

public class LeftSideUI extends VisTable {
    private final float WIDTH = 256f;
    private final float SCOREBOX_HEIGHT = 80f;
    private final float CHARACTER_STAT_HEIGHT = 256f;
    private final float MARGIN = 12f;
    public ScoreBoxUI scoreBoxUI;
    public HarvestedSoulUI harvestedSoulUI;
    public LeftSideUI(Assets assets, float x, float y, float width, float height) {
        setSize(width, height);
        setPosition(x, y);
        align(Align.top);
        scoreBoxUI = new ScoreBoxUI();
        add(scoreBoxUI).pad(MARGIN).growX().size(WIDTH, SCOREBOX_HEIGHT);
        row();
        harvestedSoulUI = new HarvestedSoulUI();
        add(harvestedSoulUI).padLeft(MARGIN).padRight(MARGIN).padBottom(MARGIN).growX().size(WIDTH, CHARACTER_STAT_HEIGHT);
        row();
    }
}
