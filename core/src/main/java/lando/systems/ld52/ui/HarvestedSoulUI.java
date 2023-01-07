package lando.systems.ld52.ui;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisWindow;
import lando.systems.ld52.Assets;

public class HarvestedSoulUI extends VisWindow {

    VisLabel nameLabel;
    VisLabel occupationLabel;
    VisLabel funFactLabel;

    public HarvestedSoulUI() {
        super("");
        setTouchable(Touchable.disabled);
        setBackground(Assets.Patch.metal.drawable);
        align(Align.top);
        nameLabel = new VisLabel("Elon Musk", "large");
        add(nameLabel).align(Align.center);
        row();
        VisLabel label = new VisLabel("Occupation: ");
        label.setFontScale(1f);
        add(label).align(Align.left);;
        row();
        occupationLabel = new VisLabel("Professional Tweeter");
        occupationLabel.setFontScale(.75f);
        add(occupationLabel).align(Align.left);
        row();
        label = new VisLabel("Fun Facts:");
        label.setFontScale(1f);
        add(label).align(Align.left);;
        row();
        funFactLabel = new VisLabel("Father of X Æ A-12");
        funFactLabel.setFontScale(.75f);
        add(funFactLabel).align(Align.left).growX();
        setDebug(true);
    }

    public void setSoul(String name, String occupation, String funFact) {
        nameLabel.setText(name);
        occupationLabel.setText(occupation);
        funFactLabel.setText(funFact);
    }
}
