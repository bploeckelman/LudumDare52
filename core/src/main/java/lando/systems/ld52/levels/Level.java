package lando.systems.ld52.levels;

import lando.systems.ld52.assets.Feature;
import lando.systems.ld52.gameobjects.Quota;
import lando.systems.ld52.serialization.PersonDto;
import lando.systems.ld52.serialization.QuotaDto;
import lando.systems.ld52.serialization.RoundDto;
import lando.systems.ld52.serialization.TileDto;

public class Level {

    public static RoundDto getLevel(int round) {
        switch (round) {
            case 1:
                return level2;
            default:
                return level1;
        }
    }


    public static RoundDto level1 = new RoundDto(
            new QuotaDto(
                    Quota.Source.heaven,
                    new PersonDto(1,
                            Feature.beard_beard
                    )
            ),
            new QuotaDto(
                    Quota.Source.hell,
                    new PersonDto(1,
                            Feature.beard_soulpatch
                    )
            ),
            // y, x (for easier visualization)
            new TileDto(0, 1, TileDto.TileType.obstacle),
            new TileDto(1, 1, TileDto.TileType.character_hell, 1),
            new TileDto(1, 4, TileDto.TileType.character_rando),
            new TileDto(2, 3, TileDto.TileType.obstacle),
            new TileDto(3, 3, TileDto.TileType.character_heaven, 1),
            new TileDto(3, 4, TileDto.TileType.obstacle),
            new TileDto(4, 1, TileDto.TileType.character_rando),
            new TileDto(5, 1, TileDto.TileType.obstacle),
            new TileDto(5, 5, TileDto.TileType.character_rando)
    );
    public static RoundDto level2 = new RoundDto(
            new QuotaDto(
                    Quota.Source.heaven,
                    new PersonDto(1,
                            Feature.Category.eye,
                            Feature.Category.nose,
                            Feature.Category.mouth
                    )
            ),
            new QuotaDto(
                    Quota.Source.hell,
                    new PersonDto(1,
                            Feature.Category.hair_head,
                            Feature.Category.hair_face
                    )
            ),
            // y, x (for easier visualization)
            new TileDto(0, 5, TileDto.TileType.character_heaven, 1),
            new TileDto(1, 2, TileDto.TileType.character_rando),
            new TileDto(2, 0, TileDto.TileType.character_rando),
            new TileDto(3, 1, TileDto.TileType.obstacle),
            new TileDto(4, 1, TileDto.TileType.character_hell, 1),
            new TileDto(4, 2, TileDto.TileType.obstacle),
            new TileDto(5, 1, TileDto.TileType.obstacle)
    );
}
