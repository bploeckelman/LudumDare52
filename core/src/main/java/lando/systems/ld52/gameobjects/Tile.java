package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class Tile {

    public int x;
    public int y;

    // Debug probably
    public Color tileColor;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        tileColor = new Color(Color.WHITE);
        tileColor.fromHsv(MathUtils.random(360f), .5f, .8f);
    }
}
