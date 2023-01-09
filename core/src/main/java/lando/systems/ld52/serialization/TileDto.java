package lando.systems.ld52.serialization;

public class TileDto {
    public enum TileType {
        open,
        obstacle,
        character_heaven,
        character_hell,
        character_rando,
        powerUp_type1 // todo, define and add more
    }

    public TileType tileType = TileType.open;
    // only for heaven/hell characters
    public int quotaIndex = 0;
    public int x;
    public int y;

    public TileDto(int y, int x, TileType tileType) {
        this(y, x, tileType, 0);
    }

    public TileDto(int y, int x, TileType tileType, int quotaIndex) {
        this.x = x;
        this.y = y;
        this.tileType = tileType;
        this.quotaIndex = quotaIndex;
    }
}
