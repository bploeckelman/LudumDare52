package lando.systems.ld52.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;
import lando.systems.ld52.Assets;
import lando.systems.ld52.assets.Feature;
import lando.systems.ld52.assets.Head;
import lando.systems.ld52.gameobjects.Quota;

import java.util.Comparator;

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
        heavenQuotaTable.align(Align.top);
        hellQuotaTable.align(Align.top);

        VisLabel label = new VisLabel("Quotas", "outfit-medium-40px");

        add(label).colspan(2).align(Align.top);
        row();
        add(heavenQuotaTable).grow().padTop(5);
        row();
        add(hellQuotaTable).grow().padTop(5);
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
        TextureRegionDrawable imageBackground  = new TextureRegionDrawable(assets.atlas.findRegion("ninepatch/plain-gradient"));//.tint(imageBackgroundTint);
        imageBackground.setMinSize(60, 60);

        heavenQuotaTable.clear();
        heavenQuotaTable.defaults().align(Align.top);
        heavenQuotaTable.setBackground(heavenBackground);
        heavenQuotaTable.add(new VisLabel("Heaven", "outfit-medium-20px")).colspan(2).align(Align.top);
        heavenQuotaTable.row();
        if (heaven != null) {
            for (Quota.Person person : heaven.people) {
                // initialize a table for the labels for each feature
                // TODO - maybe limit to 3 features max so we don't have huge rows
                VisTable labelTable = new VisTable();

                // initialize the image stack with the appropriate backgrounds
                Stack imageStack = new Stack(
                          new Container<>(new VisImage(imageBackground))
                        , new Container<>(new VisImage(Head.get(assets, Head.blank).getKeyFrame(0)))
                );

                // add  all this person's features to an array that we can sort by layer
                Array<Feature> features = new Array<>();
                features.addAll(person.features.keySet().toArray(new Feature[0]));
                features.sort(Comparator.comparingInt(feature -> feature.category.layer));

                // add feature images to the stack in the proper order
                // and also add the feature labels to that table
                for (Feature feature : features) {
                    TextureRegion featureTexture = assets.features.get(feature).getKeyFrame(0);
                    VisImage featureImage = new VisImage(new TextureRegionDrawable(featureTexture));
                    imageStack.add(featureImage);

                    VisLabel featureLabel = new VisLabel(feature.displayName, "outfit-medium-14px");
                    labelTable.add(featureLabel).growX();
                    labelTable.row();
                }

                // initialize the background that indicates whether this person is considered 'satisfied'
                Drawable background = new TextureRegionDrawable(assets.pixelRegion)
                        .tint(person.isSatisfied() ? satisfiedBackgroundTint : backgroundTint);

                // initialize the inner table for this row
                VisTable innerTable = new VisTable();
                innerTable.setBackground(background);
                innerTable.add(imageStack).padRight(10);
                innerTable.add(labelTable).grow();

                heavenQuotaTable.add(innerTable).growX();
                heavenQuotaTable.row();
            }
        }

        hellQuotaTable.clear();
        hellQuotaTable.setBackground(hellBackground);
        hellQuotaTable.add(new VisLabel("Hell", "outfit-medium-20px")).colspan(2).align(Align.top);
        hellQuotaTable.row();
        if (hell != null) {
            for (Quota.Person person : hell.people) {
                // initialize a table for the labels for each feature
                // TODO - maybe limit to 3 features max so we don't have huge rows
                VisTable labelTable = new VisTable();

                // initialize the image stack with the appropriate backgrounds
                Stack imageStack = new Stack(
                          new Container<>(new VisImage(imageBackground))
                        , new Container<>(new VisImage(Head.get(assets, Head.blank).getKeyFrame(0)))
                );

                // add  all this person's features to an array that we can sort by layer
                Array<Feature> features = new Array<>();
                features.addAll(person.features.keySet().toArray(new Feature[0]));
                features.sort(Comparator.comparingInt(feature -> feature.category.layer));

                // add feature images to the stack in the proper order
                // and also add the feature labels to that table
                for (Feature feature : features) {
                    TextureRegion featureTexture = assets.features.get(feature).getKeyFrame(0);
                    VisImage featureImage = new VisImage(new TextureRegionDrawable(featureTexture));
                    imageStack.add(featureImage);

                    VisLabel featureLabel = new VisLabel(feature.displayName, "outfit-medium-14px");
                    labelTable.add(featureLabel).growX();
                    labelTable.row();
                }

                // initialize the background that indicates whether this person is considered 'satisfied'
                Drawable background = new TextureRegionDrawable(assets.pixelRegion)
                        .tint(person.isSatisfied() ? satisfiedBackgroundTint : backgroundTint);

                // initialize the inner table for this row
                VisTable innerTable = new VisTable();
                innerTable.setBackground(background);
                innerTable.add(imageStack).padRight(10);
                innerTable.add(labelTable).grow();

                hellQuotaTable.add(innerTable).growX();
                hellQuotaTable.row();
            }
        }
    }

}
