package lando.systems.ld52.ui;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisWindow;
import lando.systems.ld52.Assets;
import lando.systems.ld52.assets.Feature;

public class QuotaListUI extends VisWindow {

    private Image quotaImage1;
    private VisLabel quotaLabel1;
    private Assets assets;

    public QuotaListUI(Assets assets) {
        super("");
        this.assets = assets;
        setTouchable(Touchable.disabled);
        setBackground(Assets.Patch.metal.drawable);
        VisLabel label = new VisLabel("Quota List", "large");
        add(label).colspan(2).align(Align.top);
        row();
        quotaImage1 = new Image();
        add(quotaImage1);
        quotaLabel1 = new VisLabel("quota 1");
        add(quotaLabel1).align(Align.center);
        setQuota1(Feature.cigarette);
    }

    public void setQuota1(Feature feature) {
        quotaLabel1.setText(feature.displayName);
        quotaImage1.setDrawable(new TextureRegionDrawable(assets.features.get(feature).getKeyFrame(0f)));
    }
}
