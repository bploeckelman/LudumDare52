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
}
