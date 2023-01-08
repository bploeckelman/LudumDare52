package lando.systems.ld52.ui;

import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisTable;
import lando.systems.ld52.Assets;

public class RightSideUI extends VisTable {
    private final float WIDTH = 220f;
    private final float QUOTA_HEIGHT = 450f;
    private final float MARGIN = 12f;
    public QuotaListUI quotaListUI;
    public HourGlassUI hourGlassUI;
    public RightSideUI(Assets assets, float x, float y, float width, float height) {
        align(Align.top);
        setSize(width, height);
        setPosition(x, y);
        quotaListUI = new QuotaListUI(assets);
        add(quotaListUI).pad(MARGIN).growX().size(WIDTH, QUOTA_HEIGHT);
    }
}
