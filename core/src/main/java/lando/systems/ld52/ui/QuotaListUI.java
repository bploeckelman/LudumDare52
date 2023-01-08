package lando.systems.ld52.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;
import lando.systems.ld52.Assets;
import lando.systems.ld52.assets.Feature;
import lando.systems.ld52.assets.Head;
import lando.systems.ld52.gameobjects.Quota;

import java.util.List;

public class QuotaListUI extends VisWindow {

    private final Assets assets;
    private final VisTable heavenQuotaTable;
    private final VisTable hellQuotaTable;
    private final Color backgroundTint = new Color(0.4f, 0.4f, 0.4f, 0.5f);
    private final Color satisfiedBackgroundTint = new Color(0.1f, 0.6f, 0.0f, 0.1f);
    private final Color imageBackgroundTint = new Color(0.2f, 0.2f, 0.2f, 0.5f);

    public QuotaListUI(Assets assets) {
        super("");
        this.assets = assets;
        align(Align.top);
        setTouchable(Touchable.disabled);
        setBackground(new NinePatchDrawable(Assets.NinePatches.plain_gradient));

        heavenQuotaTable = new VisTable();
        hellQuotaTable = new VisTable();

        VisLabel label = new VisLabel("Quotas", "outfit-medium-40px");

        add(label).colspan(2).align(Align.top);
        row();
        add(heavenQuotaTable).fill().padTop(5);
        row();
        add(hellQuotaTable).fill().padTop(5);
    }

    public void setQuotas(Quota quota1, Quota quota2) {
        Quota heaven = null;
        Quota hell = null;
        if      (quota1.source == Quota.Source.heaven) heaven = quota1;
        else if (quota2.source == Quota.Source.heaven) heaven = quota2;
        if      (quota1.source == Quota.Source.hell)   hell   = quota1;
        else if (quota2.source == Quota.Source.hell)   hell   = quota2;

        Drawable heavenBackground = new TextureRegionDrawable(assets.atlas.findRegion("ui/background-heaven")).tint(backgroundTint);
        Drawable hellBackground   = new TextureRegionDrawable(assets.atlas.findRegion("ui/background-hell")).tint(backgroundTint);
        Drawable imageBackground  = new TextureRegionDrawable(assets.pixelRegion).tint(imageBackgroundTint);

        heavenQuotaTable.clear();
        heavenQuotaTable.setBackground(heavenBackground);
        heavenQuotaTable.add(new VisLabel("Heaven", "outfit-medium-20px")).colspan(2).align(Align.top);
        heavenQuotaTable.row();
        if (heaven != null) {
            for (Feature feature : heaven.features.keySet()) {
                VisImage featureImage = new VisImage(new TextureRegionDrawable(assets.features.get(feature).getKeyFrame(0)));
                VisLabel featureLabel = new VisLabel(feature.displayName, "outfit-medium-14px");
                Stack imageStack = new Stack(
                          new Container<>(new VisImage(imageBackground))
                        , new Container<>(new VisImage(Head.get(assets, Head.a).getKeyFrame(0)))
                        , new Container<>(featureImage)
                );

                Drawable background = new TextureRegionDrawable(assets.pixelRegion)
                        .tint(heaven.features.get(feature) ? satisfiedBackgroundTint : backgroundTint);
                VisTable innerTable = new VisTable();
                innerTable.setBackground(background);
                innerTable.add(imageStack).fill();
                innerTable.add(featureLabel).fill().align(Align.center);

                heavenQuotaTable.add(innerTable).fillX();
                heavenQuotaTable.row();
            }
        }

        hellQuotaTable.clear();
        hellQuotaTable.setBackground(hellBackground);
        hellQuotaTable.add(new VisLabel("Hell", "outfit-medium-20px")).colspan(2).align(Align.top);
        hellQuotaTable.row();
        if (hell != null) {
            for (Feature feature : hell.features.keySet()) {
                VisImage featureImage = new VisImage(new TextureRegionDrawable(assets.features.get(feature).getKeyFrame(0)));
                VisLabel featureLabel = new VisLabel(feature.displayName, "outfit-medium-14px");
                Stack imageStack = new Stack(
                          new Container<>(new VisImage(imageBackground))
                        , new Container<>(new VisImage(Head.get(assets, Head.a).getKeyFrame(0)))
                        , new Container<>(featureImage)
                );

                Drawable background = new TextureRegionDrawable(assets.pixelRegion)
                        .tint(hell.features.get(feature) ? satisfiedBackgroundTint : backgroundTint);
                VisTable innerTable = new VisTable();
                innerTable.setBackground(background);
                innerTable.add(imageStack).fill();
                innerTable.add(featureLabel).fill().align(Align.center);

                hellQuotaTable.add(innerTable).fillX();
                hellQuotaTable.row();
            }
        }
    }

}
