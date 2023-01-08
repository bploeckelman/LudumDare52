package lando.systems.ld52.serialization;

import com.badlogic.gdx.utils.Json;
import lando.systems.ld52.gameobjects.GameBoard;

public class RoundDto {
    public TileDto[][] tileDtos;

    public RoundDto() {
        tileDtos = new TileDto[GameBoard.gridSize][GameBoard.gridSize];
        for (int x = 0; x < GameBoard.gridSize; x++) {
            for (int y = 0; y < GameBoard.gridSize; y++) {
                tileDtos[x][y] = new TileDto();
            }
        }

        // temporary - will de/serialize
        tileDtos[1][0].tileType = TileDto.TileType.obstacle;

        tileDtos[1][1].tileType = TileDto.TileType.character_hell;
        tileDtos[1][1].quotaIndex = 1;

        tileDtos[4][1].tileType = TileDto.TileType.character_rando;

        tileDtos[3][2].tileType = TileDto.TileType.obstacle;

        tileDtos[3][3].tileType = TileDto.TileType.character_heaven;
        tileDtos[3][3].quotaIndex = 1;

        tileDtos[4][3].tileType = TileDto.TileType.obstacle;

        tileDtos[1][4].tileType = TileDto.TileType.character_rando;

        tileDtos[1][5].tileType = TileDto.TileType.obstacle;

        tileDtos[5][5].tileType = TileDto.TileType.character_rando;
    }


    public static RoundDto fromJson(String roundData) {
        Json json = new Json();
        return json.fromJson(RoundDto.class, roundData);
    }
}
