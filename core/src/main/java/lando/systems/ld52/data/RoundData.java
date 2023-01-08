package lando.systems.ld52.data;

import com.badlogic.gdx.math.MathUtils;
import lando.systems.ld52.assets.Feature;
import lando.systems.ld52.gameobjects.GameBoard;

public class RoundData {
    public TileData[][] tileData;

    public RoundData() {
        tileData = new TileData[GameBoard.gridSize][GameBoard.gridSize];
        for (int x = 0; x < GameBoard.gridSize; x++) {
            for (int y = 0; y < GameBoard.gridSize; y++) {

                // will come from files
                TileData td = new TileData();

                if(MathUtils.randomBoolean(.5f)) {
                    td.type = TileType.character;
                    if(MathUtils.randomBoolean(.5f)) {
                        td.eye = Feature.eyepatch_a;
                        td.nose = Feature.nose_clown;
                        td.hair_face = Feature.hair_bald;
                    }
                    // rest completely random
                } else if (MathUtils.randomBoolean(.33f)) {
                    td.type = TileType.obstacle;
                } // TODO powerup type

                tileData[x][y] = td;
            }
        }
    }
}
