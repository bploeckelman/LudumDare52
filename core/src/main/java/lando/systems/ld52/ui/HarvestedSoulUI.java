package lando.systems.ld52.ui;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisWindow;
import lando.systems.ld52.Assets;

public class HarvestedSoulUI extends VisWindow {

    VisLabel occupationHeaderLabel;
    VisLabel funFactHeaderLabel;
    VisLabel nameLabel;
    VisLabel occupationLabel;
    VisLabel funFactLabel;

    public HarvestedSoulUI() {
        super("");
        setTouchable(Touchable.disabled);
        setBackground(Assets.Patch.metal.drawable);
        align(Align.top);
        nameLabel = new VisLabel("", "large");
        add(nameLabel).align(Align.center);
        row();
        occupationHeaderLabel = new VisLabel("Occupation: ");
        occupationHeaderLabel.setFontScale(1f);
        occupationHeaderLabel.setVisible(false);
        add(occupationHeaderLabel).align(Align.left);;
        row();
        occupationLabel = new VisLabel("");
        occupationLabel.setFontScale(.75f);
        add(occupationLabel).align(Align.left);
        row();
        funFactHeaderLabel = new VisLabel("Fun Facts:");
        funFactHeaderLabel.setFontScale(1f);
        funFactHeaderLabel.setVisible(false);
        add(funFactHeaderLabel).align(Align.left);;
        row();
        funFactLabel = new VisLabel("");
        funFactLabel.setFontScale(.75f);
        add(funFactLabel).align(Align.left).growX();
    }

    public void setSoul(String name, String occupation, String funFact) {
        nameLabel.setText(name);
        occupationHeaderLabel.setVisible(true);
        occupationLabel.setText(occupation);
        funFactHeaderLabel.setVisible(true);
        funFactLabel.setText(funFact);
    }
}
