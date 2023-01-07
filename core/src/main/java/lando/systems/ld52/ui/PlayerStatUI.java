package lando.systems.ld52.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisProgressBar;
import com.kotcrab.vis.ui.widget.VisTable;
import lando.systems.ld52.Assets;
import lando.systems.ld52.utils.Utils;


public class PlayerStatUI extends VisTable {
    public PlayerStatUI(Assets assets) {
        VisLabel playerLabel = new VisLabel("Death", "large");
        playerLabel.setAlignment(Align.center);
        add(playerLabel).colspan(2).growX().align(Align.center);
        row();
        setBackground(Assets.Patch.metal.drawable);
        Image image = new Image(new TextureRegionDrawable(assets.playerFront.getKeyFrame(0f)));
        add(image).growX().growY();
        VisProgressBar.ProgressBarStyle horizontalProgressBarStyle = getSkin().get("default-vertical", VisProgressBar.ProgressBarStyle.class);
        VisProgressBar.ProgressBarStyle scytheProgressBarStyle = new VisProgressBar.ProgressBarStyle(horizontalProgressBarStyle);
        scytheProgressBarStyle.knobAfter =  new TextureRegionDrawable(Utils.getColoredTextureRegion(Color.BLACK));
        scytheProgressBarStyle.knobBefore =  new TextureRegionDrawable(Utils.getColoredTextureRegion(Color.GREEN));
        scytheProgressBarStyle.knob = new TextureRegionDrawable(assets.scythe.getKeyFrame(0f));
        VisProgressBar scytheProgressBar = new VisProgressBar(0f, 100f, 1f, true);
        scytheProgressBar.setStyle(scytheProgressBarStyle);
        scytheProgressBar.setWidth(50f);
        scytheProgressBar.setValue(50f);
        add(scytheProgressBar).width(50f).maxWidth(50f).growY();
        setDebug(true);
    }
}
