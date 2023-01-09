package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import lando.systems.ld52.Assets;

public class Hourglass implements GameObject {

    private final TextureRegion background;
    private final TextureRegion hourglass;
    private final GlyphLayout layout;
    private final BitmapFont font;
    private float stateTime;
    private final GameBoard gameBoard;
    private final Rectangle hourglassBounds;
    private final ShaderProgram hourglassShader;
    private final Color outsideColorDark = new Color(.1f, .1f, .1f, 1f);
    private final Color outsideColorLight = new Color(.7f, .5f, .1f, 1f);
    private final Color insideColorDark = new Color(.3f, .3f, .6f, 1f);
    private final Color insideColorLight = new Color(.9f, .9f, .3f, 1f);


    public Hourglass(Assets assets, GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        hourglassBounds = new Rectangle(1055,32, 170, 170);
        background = assets.pixelRegion;
        hourglass = assets.hourglassTex;
        hourglassShader = assets.hourglassShader;
        font = assets.font;
        this.layout = assets.layout;
        stateTime = 0f;
    }

    @Override
    public void update(float dt) {
        stateTime += dt*.3f;
        if (stateTime >1f){
            stateTime = 0;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        // hard coded layout for now...
        float x = 1030;
        float y = 32;
        float w = 220;
        float h = 220;

        // TODO - pick patch based on time remaining... (?green ->) plain -> yellow -> red
        NinePatch patch = Assets.NinePatches.plain_gradient;
        patch.draw(batch, x, y, w, h);

        font.getData().setScale(.5f);
        int secondsLeft = gameBoard.getSecondsLeft();
        String timerText = secondsLeft + " Seconds";

        float percent = gameBoard.getTimerPercent();
        Color timerColor = Color.WHITE;
        if (percent < .5f) {
            timerColor = (percent <= .1f || secondsLeft <= 5) ? Color.RED : Color.YELLOW;
        }

        layout.setText(font, timerText, timerColor, w, Align.center, false);
        font.draw(batch, layout, 1030, 230);
        font.getData().setScale(1f);
        batch.setShader(hourglassShader);

//        hourglassShader.setUniformf("u_time", stateTime);
        hourglassShader.setUniformf("u_time", gameBoard.getTimerPercent());
        hourglassShader.setUniformf("u_color1", outsideColorDark);
        hourglassShader.setUniformf("u_color2", outsideColorLight);
        hourglassShader.setUniformf("u_color3", insideColorDark);
        hourglassShader.setUniformf("u_color4", insideColorLight);

        batch.draw(hourglass, hourglassBounds.x, hourglassBounds.y, hourglassBounds.width, hourglassBounds.height);

        batch.setShader(null);
    }

}

