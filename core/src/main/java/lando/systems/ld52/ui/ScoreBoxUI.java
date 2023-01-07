package lando.systems.ld52.ui;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisWindow;
import lando.systems.ld52.Assets;

public class ScoreBoxUI extends VisWindow {

    private VisLabel scoreLabel;

    public ScoreBoxUI() {
        super("");
        setTouchable(Touchable.disabled);
        setBackground(Assets.Patch.metal.drawable);
        VisLabel label = new VisLabel("Score Box");
        add(label).align(Align.top);
        row();
        scoreLabel = new VisLabel("0");
        add(scoreLabel).align(Align.center);
    }

    public void setScoreLabel(int score) {
        scoreLabel.setText(score);
    }
}
