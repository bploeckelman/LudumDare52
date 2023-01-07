package lando.systems.ld52.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.widget.VisProgressBar;
import com.kotcrab.vis.ui.widget.VisTable;
import lando.systems.ld52.utils.Utils;

public class ScytheMeter extends VisTable {
    public VisProgressBar scytheProgressBar;
    public ScytheMeter(float x, float y, float min, float max, float stepSize, Skin skin) {
        setSize(100f, 10f);
        setPosition(x, y);

        VisProgressBar.ProgressBarStyle horizontalProgressBarStyle = skin.get("default-horizontal", VisProgressBar.ProgressBarStyle.class);
        VisProgressBar.ProgressBarStyle scytheProgressBarStyle = new VisProgressBar.ProgressBarStyle(horizontalProgressBarStyle);
        scytheProgressBarStyle.knobAfter =  new TextureRegionDrawable(Utils.getColoredTextureRegion(Color.YELLOW));
        scytheProgressBarStyle.knobBefore =  new TextureRegionDrawable(Utils.getColoredTextureRegion(Color.GREEN));        scytheProgressBar = new VisProgressBar(min, max, stepSize, false);
        scytheProgressBar.setValue(.5f);
        scytheProgressBar.setStyle(scytheProgressBarStyle);
        addActor(scytheProgressBar);
    }

    public void resetScytheMeter() {
        scytheProgressBar.setValue(0f);
        scytheProgressBar.setVisible(false);
    }

    public void setScytheMeter(float x, float y, float value) {
        scytheProgressBar.setPosition(x, y);
        scytheProgressBar.setValue(value);
        scytheProgressBar.setVisible(true);
    }
}
