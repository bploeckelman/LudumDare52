package lando.systems.ld52.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.github.tommyettinger.textra.TypingLabel;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisWindow;
import lando.systems.ld52.Assets;
import lando.systems.ld52.assets.Feature;
import lando.systems.ld52.assets.Head;
import lando.systems.ld52.gameobjects.TileHead;

public class HarvestedSoulUI extends VisWindow {

    private final Assets assets;

    private final VisLabel name;
    private final TypingLabel afterlife;
    private final TypingLabel jokes;
    private final Stack headStack;
    private final Container<Stack> headContainer;

    public HarvestedSoulUI(Assets assets) {
        super("");
        this.assets = assets;
        align(Align.top);
        setTouchable(Touchable.disabled);
        setBackground(new NinePatchDrawable(Assets.NinePatches.plain_gradient));

        name = new VisLabel("", "outfit-medium-19px");
        afterlife = new TypingLabel("", VisUI.getSkin(), "outfit-medium-17px");
        jokes = new TypingLabel("", VisUI.getSkin(), "outfit-medium-17px");
        jokes.setWrap(true);

        headStack = new Stack();
        headContainer = new Container<>(headStack);
        headContainer.setBackground(new NinePatchDrawable(Assets.NinePatches.glass_active));

        float width = getWidth();
        float height = getHeight();

        VisLabel header = new VisLabel("Recently Deceased", "outfit-medium-19px");
        header.setColor(Color.SKY);

        pad(5);
        add(header).colspan(2).align(Align.center);
        row();
        add(headContainer).size(80, 80).align(Align.center).padLeft(10).padRight(10).padTop(5);
        add(name).size(width, 80).padRight(10);
        row();
        add(afterlife).colspan(2).align(Align.center).padLeft(10).padTop(10);
        row();
        add(jokes).colspan(2).grow().align(Align.left).pad(10);
    }

    public void clear() {
        name.setText("");
        afterlife.setText("");
        jokes.setText("");
        headStack.clear();
    }

    public void setSoul(TileHead tileHead, String afterlifeZone) {
        String charName = Feature.getRandomCharacterName();
        charName = charName.replace(" ", "\n");
        name.setText(charName);
        afterlife.setText(afterlifeZone);
        jokes.setText("Very funny jokes go here, need to figure out what the max line length is at the normal text size");

        headStack.clear();
        headStack.add(new VisImage(new TextureRegionDrawable(Head.get(assets, tileHead.head).getKeyFrame(0))));
        for (Feature feature : tileHead.featureAnims.orderedKeys()) {
            if (feature.category != Feature.Category.clothes) {
                headStack.add(new VisImage(new TextureRegionDrawable(Feature.get(assets, feature).getKeyFrame(0))));
            }
        }
    }
}
