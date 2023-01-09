package lando.systems.ld52.tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import lando.systems.ld52.Assets;
import lando.systems.ld52.Config;
import lando.systems.ld52.Main;

public class TutorialPart {

    public boolean complete;
    public float alpha;
    public float targetAlpha;
    public Rectangle highlightBounds;
    private Assets assets;
    private String text;
    private Rectangle textBounds;
    private Color textColor;
    private float flash = 0;
    private Color flashColor;
    private float accum;
    private String continueText = "click to continue\npress escape to skip tutorial";

    public TutorialPart (float x, float y, float w, float h, String text) {
        this.assets = Main.game.assets;
        this.complete = false;
        this.alpha = 0;
        this.targetAlpha = 1f;
        this.flashColor = new Color(Color.WHITE);
        this.highlightBounds = new Rectangle(x, y, w, h);
        this.text = text;
        this.textColor = new Color();
        float textY = 0;
        float textHeight = 0;
        float textX = 200;
        float textWidth = Config.Screen.window_width - 400;
        if (y < Config.Screen.window_height - (y + h)) {
            // show on top of highlight
            float blankHeight = Config.Screen.window_height - (y + h);
            textHeight = Math.min(blankHeight - 40, 500);
            textY = y + h + (blankHeight - textHeight) /2f;
        } else {
            // show below
            float blankHeight = y;
            textHeight = Math.min(blankHeight - 40, 500);
            textY = (blankHeight - textHeight) /2f;
        }
        if (textHeight < 300) {
            textHeight = Config.Screen.window_height - 200;
            textY = 100;
            // my brother in christ... push it to a wing
            if (x < Config.Screen.window_width - (x + w)) {
                // show on right
                float blankWidth = Config.Screen.window_width - (x + w);
                textWidth = Math.min(blankWidth - 40, 600);
                textX = x + w + (blankWidth - textWidth)/2f;
            } else {
                // show on left
                float blankWidth = x;
                textWidth = Math.min(blankWidth - 40, 600);
                textX = (blankWidth - textWidth)/2f;
            }
        }
        textBounds = new Rectangle(textX, textY, textWidth, textHeight);
    }

    public void update(float dt) {
        flash += dt * 6f;
        accum += dt * 220f;
        flashColor.fromHsv(accum, 1f, 1f);

        float alphaDif = targetAlpha - alpha;
        if (alphaDif != 0){
            float dir = Math.signum(alphaDif);
            float alphaAmount = 2f * dt;
            if (alphaAmount >= Math.abs(alphaDif)){
                alpha = targetAlpha;
            } else {
                alpha += (alphaAmount * dir);
            }
        } else {
            // transition done
            if (targetAlpha == 0){
                complete = true;
            } else {
                if (Gdx.input.justTouched()) {
                    targetAlpha = 0;
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
                    TutorialManager.SHOW_TUTORIAL = false;
                }
            }
        }
    }

    public void render(SpriteBatch batch) {
        batch.setColor(0f, 0f, 0f, alpha*.9f);
        float lastWidth = Config.Screen.window_width - (highlightBounds.x + highlightBounds.width);
        float lastHeight = Config.Screen.window_height - (highlightBounds.y + highlightBounds.height);
        batch.draw(assets.pixelRegion, 0, 0, highlightBounds.x, highlightBounds.y);
        batch.draw(assets.pixelRegion, highlightBounds.x, 0, highlightBounds.width, highlightBounds.y);
        batch.draw(assets.pixelRegion, highlightBounds.x + highlightBounds.width, 0, lastWidth, highlightBounds.y);

        batch.draw(assets.pixelRegion, 0, highlightBounds.y, highlightBounds.x, highlightBounds.height);
        batch.draw(assets.pixelRegion, highlightBounds.x + highlightBounds.width, highlightBounds.y, lastWidth, highlightBounds.height );

        batch.draw(assets.pixelRegion, 0, highlightBounds.y + highlightBounds.height, highlightBounds.x, lastHeight);
        batch.draw(assets.pixelRegion, highlightBounds.x, highlightBounds.y + highlightBounds.height, highlightBounds.width, lastHeight);
        batch.draw(assets.pixelRegion, highlightBounds.x + highlightBounds.width, highlightBounds.y + highlightBounds.height, lastWidth, lastHeight);

        batch.setColor(flashColor.r, flashColor.g, flashColor.b, alpha);
        Assets.Patch.debug.ninePatch.draw(batch, highlightBounds.x, highlightBounds.y, highlightBounds.width, highlightBounds.height);
        batch.setColor(1f, 1f, 1f, alpha);
        textColor.set(1f, 1f, 1f, alpha);
        Assets.NinePatches.glass_blue.draw(batch, textBounds.x - 10, textBounds.y - 10, textBounds.width + 20, textBounds.height + 20);
        assets.font.getData().setScale(.5f);
        assets.layout.setText(assets.font, text, textColor, textBounds.width, Align.center, true);
        assets.font.draw(batch, assets.layout, textBounds.x, textBounds.y + textBounds.height/2f + assets.layout.height/2f);
        assets.font.getData().setScale(.3f);

        assets.layout.setText(assets.font, continueText, textColor, textBounds.width, Align.center, true);
        assets.font.draw(batch, assets.layout, textBounds.x, textBounds.y + assets.layout.height + 5);

        assets.font.getData().setScale(1f);
        batch.setColor(Color.WHITE);
    }
}
