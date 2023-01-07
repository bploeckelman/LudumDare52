package lando.systems.ld52.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.kotcrab.vis.ui.widget.VisTable;
import lando.systems.ld52.Assets;

public class RightSideUI extends VisTable {
    private final float WIDTH = 256f;
    private final float BUTTON_HEIGHT = 80f;
    private final float MARGIN = 12f;
    public RightSideUI(Assets assets, float x, float y, float width, float height) {
        setSize(width, height);
        setPosition(x, y);
        QuotaListUI quotaListUI = new QuotaListUI(assets);
        add(quotaListUI).pad(MARGIN).growX().growY().width(WIDTH);
        row();

        TextButton.TextButtonStyle outfitMediumStyle = getSkin().get("text", TextButton.TextButtonStyle.class);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle(outfitMediumStyle);
        buttonStyle.font = assets.smallFont;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.up = Assets.Patch.glass.drawable;
        buttonStyle.down = Assets.Patch.glass_dim.drawable;
        buttonStyle.over = Assets.Patch.glass_dim.drawable;

        TextButton powerUpButton = new TextButton("Power Ups", buttonStyle);
        add(powerUpButton).padLeft(MARGIN).padRight(MARGIN).padBottom(MARGIN).growX().size(WIDTH, BUTTON_HEIGHT);
        row();

        TextButton blah = new TextButton("Blah", buttonStyle);
        add(blah).padLeft(MARGIN).padRight(MARGIN).padBottom(MARGIN).growX().size(WIDTH, BUTTON_HEIGHT);
        row();

        TextButton meh = new TextButton("Meh", buttonStyle);
        add(meh).padLeft(MARGIN).padRight(MARGIN).padBottom(MARGIN).growX().size(WIDTH, BUTTON_HEIGHT);
        row();
    }
}
