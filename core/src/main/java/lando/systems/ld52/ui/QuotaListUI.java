package lando.systems.ld52.ui;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisWindow;
import lando.systems.ld52.Assets;
import lando.systems.ld52.assets.Feature;

import java.util.ArrayList;
import java.util.List;

public class QuotaListUI extends VisWindow {

    private Image quotaImage1;
    private VisLabel quotaLabel1;
    private Image quotaImage2;
    private VisLabel quotaLabel2;
    private Image quotaImage3;
    private VisLabel quotaLabel3;
    private Image quotaImage4;
    private VisLabel quotaLabel4;
    private Image quotaImage5;
    private VisLabel quotaLabel5;
    private List<VisLabel> quotaLabels = new ArrayList<>();
    private List<Image> quotaImages = new ArrayList<>();
    private Assets assets;

    public QuotaListUI(Assets assets) {
        super("");
        this.assets = assets;
        align(Align.top);
        setTouchable(Touchable.disabled);
        setBackground(Assets.Patch.metal.drawable);
        VisLabel label = new VisLabel("Quota List", "large");
        add(label).colspan(2).align(Align.top);
        row();
        quotaImage1 = new Image();
        add(quotaImage1);
        quotaLabel1 = new VisLabel("None");
        add(quotaLabel1).align(Align.center);
        row();
        quotaImage2 = new Image();
        add(quotaImage2);
        quotaLabel2 = new VisLabel("");
        add(quotaLabel2).align(Align.center);
        row();
        quotaImage3 = new Image();
        add(quotaImage3);
        quotaLabel3 = new VisLabel("");
        add(quotaLabel3).align(Align.center);
        row();
        quotaImage4 = new Image();
        add(quotaImage4);
        quotaLabel4 = new VisLabel("");
        add(quotaLabel4).align(Align.center);
        row();
        quotaImage5 = new Image();
        add(quotaImage1);
        quotaLabel5 = new VisLabel("");
        add(quotaLabel5).align(Align.center);

        quotaImages.add(quotaImage1);
        quotaLabels.add(quotaLabel1);
        quotaImages.add(quotaImage2);
        quotaLabels.add(quotaLabel2);
        quotaImages.add(quotaImage3);
        quotaLabels.add(quotaLabel3);
        quotaImages.add(quotaImage4);
        quotaLabels.add(quotaLabel4);
        quotaImages.add(quotaImage5);
        quotaLabels.add(quotaLabel5);
    }

    public void resetQuota() {
        for (Image image : quotaImages) {
            image.setDrawable(null);
        }
        for (VisLabel quotaLabel : quotaLabels) {
            quotaLabel.setText("");
        }
    }

    public void setQuotaList(List<Feature> features) {
        for (int i = 0; i < features.size(); i++) {
            quotaImages.get(i).setDrawable(new TextureRegionDrawable(assets.features.get(features.get(i)).getKeyFrame(0f)));
            quotaLabels.get(i).setText(features.get(i).displayName);
        }
    }

    public void setQuota1(Feature feature) {
        quotaLabel1.setText(feature.displayName);
        quotaImage1.setDrawable(new TextureRegionDrawable(assets.features.get(feature).getKeyFrame(0f)));
    }
}
