package lando.systems.ld52.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import lando.systems.ld52.Config;
import lando.systems.ld52.utils.typinglabel.TypingLabel;

public class CreditScreen extends BaseScreen {

    private final TypingLabel titleLabel;
    private final TypingLabel themeLabel;
    private final TypingLabel leftCreditLabel;
    private final TypingLabel rightCreditLabel;
    private final TypingLabel thanksLabel;
    private final TypingLabel disclaimerLabel;
    private final TypingLabel rossmanLabel;

    private boolean isCreditOver = false;
    private boolean exitingScreen = false;
    private final TextureRegion background;

    private final String title = "{RAINBOW}Reapo Man: Death Who?{ENDRAINBOW}";
    private final String theme = "Made for Ludum Dare 52: Harvest";

    private final String thanks = "{GRADIENT=purple;cyan}Thanks for playing our game!{ENDGRADIENT}";
    private final String developers = "{COLOR=gray}Development:{COLOR=white}\n {GRADIENT=white;gray}Brian Ploeckelman{ENDGRADIENT} \n {GRADIENT=white;gray}Doug Graham{ENDGRADIENT} \n {GRADIENT=white;gray}Brian Rossman{ENDGRADIENT} \n {GRADIENT=white;gray}Jeffrey Hwang{ENDGRADIENT}";
    private final String rossman = "{GRADIENT=gray;black}...also Brian Rossman\n(in our memories){ENDGRADIENT}";
    private final String artists = "{COLOR=gray}Art:{COLOR=white}\n {GRADIENT=white;gray}Matt Neumann{ENDGRADIENT}";
    private final String emotionalSupport = "{COLOR=cyan}Emotional Support:{COLOR=white}\n  Asuka, Osha,\n  Cherry,\n  Obi, Yoda";
    private final String music = "{COLOR=gray}Music, writing, & other stuff:{COLOR=white}\n {GRADIENT=white;gray}Pete Valeo{ENDGRADIENT}";
    private final String libgdx = "Made with {COLOR=red}<3{COLOR=white}\nand LibGDX";
    private final String disclaimer = "{GRADIENT=black;gray}Disclaimer:{ENDGRADIENT}  {GRADIENT=gold;yellow}{JUMP=.2}{WAVE=0.8;1.1;1.1}No moons were harvested in the making of this game{ENDWAVE}{ENDJUMP}{ENDGRADIENT}";

    private float accum = 0f;

    public CreditScreen() {
        super();
        titleLabel = new TypingLabel(game.assets.font, title.toLowerCase(), 0f, Config.Screen.window_height - 15f);
        titleLabel.setWidth(Config.Screen.window_width);
        titleLabel.setFontScale(1f);

        themeLabel = new TypingLabel(game.assets.smallFont, theme.toLowerCase(), 0f, Config.Screen.window_height - 70f);
        themeLabel.setWidth(Config.Screen.window_width);
        themeLabel.setFontScale(1f);

        leftCreditLabel = new TypingLabel(game.assets.smallFont, developers.toLowerCase() + "\n\n" + emotionalSupport.toLowerCase() + "\n\n", 75f, Config.Screen.window_height / 2f + 135f);
        leftCreditLabel.setWidth(Config.Screen.window_width / 2f - 150f);
        leftCreditLabel.setLineAlign(Align.left);
        leftCreditLabel.setFontScale(1f);

        rossmanLabel = new TypingLabel(game.assets.smallFont, rossman.toLowerCase(), 200f, 165f);
        rossmanLabel.setWidth(Config.Screen.window_width / 2f - 150f);
        rossmanLabel.setLineAlign(Align.left);
        rossmanLabel.setFontScale(0.4f);

        background = game.assets.pixelRegion;

        rightCreditLabel = new TypingLabel(game.assets.smallFont, artists.toLowerCase() + "\n\n" + music.toLowerCase() + "\n\n" + libgdx.toLowerCase(), Config.Screen.window_width / 2 + 75f, Config.Screen.window_height / 2f + 135f);
        rightCreditLabel.setWidth(Config.Screen.window_width / 2f - 150f);
        rightCreditLabel.setLineAlign(Align.left);
        rightCreditLabel.setFontScale(1f);

        thanksLabel = new TypingLabel(game.assets.smallFont, thanks.toLowerCase(), 0f, 115f);
        thanksLabel.setWidth(Config.Screen.window_width);
        thanksLabel.setLineAlign(Align.center);
        thanksLabel.setFontScale(1f);

        disclaimerLabel = new TypingLabel(game.assets.smallFont, disclaimer, 0f, 50f);
        disclaimerLabel.setWidth(Config.Screen.window_width);
        thanksLabel.setLineAlign(Align.center);
        disclaimerLabel.setFontScale(.6f);
    }

    @Override
    public void hide() {
        isCreditOver = false;
        exitingScreen = false;
    }

    @Override
    public void update(float dt) {
        accum += dt;
        titleLabel.update(dt);
        themeLabel.update(dt);
        leftCreditLabel.update(dt);
        rightCreditLabel.update(dt);
        if (leftCreditLabel.hasEnded()) {
            rossmanLabel.update(dt);
        }
        thanksLabel.update(dt);
        disclaimerLabel.update(dt);

        if (rossmanLabel.hasEnded()) {
            isCreditOver = true;
        }

        if (!exitingScreen && isCreditOver && Gdx.input.justTouched()){
            exitingScreen = true;
            game.getScreenManager().pushScreen("title", TransitionManager.TransitionType.BLEND.name());
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        batch.setProjectionMatrix(windowCamera.combined);
        batch.begin();
        {
            batch.draw(background, 0, 0, Config.Screen.window_width, Config.Screen.window_height);

            batch.setColor(0f, 0f, 0f, 0.6f);
            batch.draw(game.assets.pixelRegion, 25f, 130f, Config.Screen.window_width / 2f - 50f, 450f);
            batch.draw(game.assets.pixelRegion, Config.Screen.window_width / 2f + 25f, 130f, Config.Screen.window_width / 2f - 50f, 450f);

            batch.setColor(Color.WHITE);
            titleLabel.render(batch);
            themeLabel.render(batch);
            leftCreditLabel.render(batch);
            if (leftCreditLabel.hasEnded()) {
                TextureRegion catTexture = game.assets.cherry.getKeyFrame(accum);
                TextureRegion dogTexture = game.assets.asuka.getKeyFrame(accum);
                TextureRegion kittenTexture = game.assets.osha.getKeyFrame(accum);
                TextureRegion rossDogTexture = game.assets.rossDog.getKeyFrame(accum);
                TextureRegion whiteLabTexture = game.assets.whiteLab.getKeyFrame(accum);
                batch.draw(catTexture, 60f, 215f);
                batch.draw(dogTexture, 60f, 245f);
                batch.draw(kittenTexture, 270f, 245f);
                batch.draw(rossDogTexture, 230f, 185f);
                batch.draw(whiteLabTexture, 50f, 175f);
                //rossmanLabel.render(batch);
            }
            rightCreditLabel.render(batch);
            thanksLabel.render(batch);
            disclaimerLabel.render(batch);
            batch.setColor(Color.WHITE);
        }
        batch.end();
    }

}
