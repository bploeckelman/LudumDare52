package lando.systems.ld52.data;

import lando.systems.ld52.gameobjects.GameBoard;

public class RoundData {
    public TileData[][] tileData;

    public RoundData() {
        int size = GameBoard.gridSize;
        tileData = new TileData[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                tileData[x][y] = new TileData();
            }
        }
    }
}
