package lando.systems.ld52.ui;

import com.kotcrab.vis.ui.widget.VisTable;
import lando.systems.ld52.Assets;

public class RightSideUI extends VisTable {
    private final float WIDTH = 256f;
    private final float HOURGLASS_HEIGHT = 240f;
    private final float MARGIN = 12f;
    public QuotaListUI quotaListUI;
    public HourGlassUI hourGlassUI;
    public RightSideUI(Assets assets, float x, float y, float width, float height) {
        setSize(width, height);
        setPosition(x, y);
        quotaListUI = new QuotaListUI(assets);
        add(quotaListUI).pad(MARGIN).growX().growY().width(WIDTH);
        row();
        hourGlassUI = new HourGlassUI();
        add(hourGlassUI).padLeft(MARGIN).padRight(MARGIN).padBottom(MARGIN).size(WIDTH, HOURGLASS_HEIGHT);
    }
}
