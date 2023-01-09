package lando.systems.ld52.serialization;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import lando.systems.ld52.gameobjects.GameBoard;

public class RoundDto {
    private QuotaDto heaven;
    private QuotaDto hell;
    public TileDto[] tileDtos;

    public QuotaDto getHeaven() {
        return heaven;
    }

    public void setHeaven(QuotaDto heaven) {
        this.heaven = heaven;
    }

    public QuotaDto getHell() {
        return hell;
    }

    public void setHell(QuotaDto hell) {
        this.hell = hell;
    }

    public TileDto[] getTileDtos() {
        return tileDtos;
    }

    public void setTileDtos(TileDto[] tileDtos) {
        this.tileDtos = tileDtos;
    }

    //    public RoundDto() {
//        tileDtos = new TileDto[GameBoard.gridSize][GameBoard.gridSize];
//        for (int x = 0; x < GameBoard.gridSize; x++) {
//            for (int y = 0; y < GameBoard.gridSize; y++) {
//                tileDtos[x][y] = new TileDto();
//            }
//        }
//
//        // 0,0 bottom left
//        // temporary - will de/serialize
//        tileDtos[1][0].tileType = TileDto.TileType.obstacle;
//
//        tileDtos[1][1].tileType = TileDto.TileType.character_hell;
//        tileDtos[1][1].quotaIndex = 1;
//
//        tileDtos[4][1].tileType = TileDto.TileType.character_rando;
//
//        tileDtos[3][2].tileType = TileDto.TileType.obstacle;
//
//        tileDtos[3][3].tileType = TileDto.TileType.character_heaven;
//        tileDtos[3][3].quotaIndex = 1;
//
//        tileDtos[4][3].tileType = TileDto.TileType.obstacle;
//
//        tileDtos[1][4].tileType = TileDto.TileType.character_rando;
//
//        tileDtos[1][5].tileType = TileDto.TileType.obstacle;
//
//        tileDtos[5][5].tileType = TileDto.TileType.character_rando;
//    }
//
//
    public static RoundDto fromJson(String roundData) {
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        return json.fromJson(RoundDto.class, roundData);
    }
//
//    // helper to create json
//    public static String toJson(Object object) {
//        Json json = new Json();
//        json.setOutputType(JsonWriter.OutputType.json);
//        return json.toJson(object);
//    }
}
