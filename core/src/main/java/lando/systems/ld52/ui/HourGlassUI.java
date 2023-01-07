package lando.systems.ld52.ui;

import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisWindow;
import lando.systems.ld52.Assets;

public class HourGlassUI extends VisWindow {
    public HourGlassUI() {
        super("");
        setBackground(Assets.Patch.metal.drawable);
        align(Align.top);
        VisLabel label = new VisLabel("Hourglass", "large");
        add(label).growX().align(Align.center);
    }
}
