package lando.systems.ld52.levels;

import lando.systems.ld52.assets.Feature;
import lando.systems.ld52.gameobjects.Quota;
import lando.systems.ld52.serialization.PersonDto;
import lando.systems.ld52.serialization.QuotaDto;
import lando.systems.ld52.serialization.RoundDto;
import lando.systems.ld52.serialization.TileDto;

public class Level {

    public static RoundDto getLevel(int round) {
        // rounds 0 based
        switch (round) {
            case 1:
                return level2;
            case 2:
                return level3;
            case 3:
                return level4;
            case 4:
                return level5;
            default:
                return level1;
        }
    }


    public static RoundDto level1 = new RoundDto(
            new QuotaDto(
                    Quota.Source.heaven,
                    new PersonDto(1,
                            Feature.hat_beret_red
                    ),
                    new PersonDto(1,
                            Feature.beard_beard
                    )
            ),
            new QuotaDto(
                    Quota.Source.hell,
                    new PersonDto(1,
                            Feature.hat_fez
                    ),
                    new PersonDto(1,
                            Feature.beard_soulpatch
                    )
            ),
            // y, x (for easier visualization)
            new TileDto(0, 0, TileDto.TileType.character_hell, 1),
            new TileDto(0, 2, TileDto.TileType.obstacle),
            new TileDto(1, 1, TileDto.TileType.character_rando),
            new TileDto(1, 3, TileDto.TileType.character_rando),
            new TileDto(1, 4, TileDto.TileType.character_heaven, 2),
            new TileDto(1, 5, TileDto.TileType.obstacle),
            new TileDto(2, 0, TileDto.TileType.obstacle),
            new TileDto(2, 2, TileDto.TileType.character_rando),
            new TileDto(2, 3, TileDto.TileType.character_rando),
            new TileDto(2, 5, TileDto.TileType.obstacle),
            new TileDto(3, 2, TileDto.TileType.character_heaven, 1),
            new TileDto(3, 3, TileDto.TileType.obstacle),
            new TileDto(3, 4, TileDto.TileType.character_rando),
            new TileDto(3, 5, TileDto.TileType.character_hell, 2),
            new TileDto(4, 1, TileDto.TileType.character_rando),
            new TileDto(4, 4, TileDto.TileType.character_rando),
            new TileDto(5, 3, TileDto.TileType.obstacle)
    );

    public static RoundDto level2 = new RoundDto(
            new QuotaDto(
                    Quota.Source.heaven,
                    new PersonDto(1,
                            Feature.Category.eye,
                            Feature.Category.mouth
                    ),
                    new PersonDto(1,
                            Feature.Category.hair_head,
                            Feature.Category.mouth
                    ),
                    new PersonDto(1,
                            Feature.Category.hair_face,
                            Feature.Category.eye
                    )
            ),
            new QuotaDto(
                    Quota.Source.hell,
                    new PersonDto(1,
                            Feature.Category.mouth,
                            Feature.Category.hair_face
                    ),
                    new PersonDto(1,
                            Feature.Category.eye,
                            Feature.Category.hair_head
                    ),
                    new PersonDto(1,
                            Feature.Category.mouth,
                            Feature.Category.eye
                    )
            ),
            // y, x (for easier visualization)
            new TileDto(0, 2, TileDto.TileType.powerUp_type1),
            new TileDto(0, 4, TileDto.TileType.character_rando),
            new TileDto(1,0, TileDto.TileType.obstacle),
            new TileDto(1,1, TileDto.TileType.character_hell, 1),
            new TileDto(1, 3, TileDto.TileType.character_heaven, 1),
            new TileDto(2, 1, TileDto.TileType.character_rando),
            new TileDto(2, 2, TileDto.TileType.character_rando),
            new TileDto(2, 4, TileDto.TileType.character_rando),
            new TileDto(2, 5, TileDto.TileType.character_heaven, 3),
            new TileDto(3, 0, TileDto.TileType.character_hell, 3),
            new TileDto(3, 2, TileDto.TileType.obstacle),
            new TileDto(3, 3, TileDto.TileType.character_hell, 2),
            new TileDto(3, 5, TileDto.TileType.obstacle),
            new TileDto(4, 2, TileDto.TileType.character_heaven, 2),
            new TileDto(4, 3, TileDto.TileType.obstacle),
            new TileDto(4, 4, TileDto.TileType.powerUp_type1)
    );

    public static RoundDto level3 = new RoundDto(
            new QuotaDto(
                    Quota.Source.heaven,
                    new PersonDto(1,
                            Feature.Category.hair_face,
                            Feature.Category.mouth
                    ),
                    new PersonDto(1,
                            Feature.Category.mouth,
                            Feature.Category.eye
                    )
            ),
            new QuotaDto(
                    Quota.Source.hell,
                    new PersonDto(1,
                            Feature.Category.hair_head,
                            Feature.Category.eye
                    ),
                    new PersonDto(1,
                            Feature.Category.mouth,
                            Feature.Category.hair_head
                    )
            ),
            new TileDto(0, 3, TileDto.TileType.character_rando),
            new TileDto(0, 4, TileDto.TileType.powerUp_type1),
            new TileDto(1, 0, TileDto.TileType.powerUp_type1),
            new TileDto(1,1, TileDto.TileType.powerDown_type1),
            new TileDto(1, 2, TileDto.TileType.obstacle),
            new TileDto(1, 3, TileDto.TileType.obstacle),
            new TileDto(1, 4, TileDto.TileType.powerDown_type1),
            new TileDto(2, 0, TileDto.TileType.character_rando),
            new TileDto(2, 1, TileDto.TileType.obstacle),
            new TileDto(2, 2, TileDto.TileType.character_heaven, 2),
            new TileDto(2, 3, TileDto.TileType.character_hell, 2),
            new TileDto(2, 4, TileDto.TileType.obstacle),
            new TileDto(3, 1, TileDto.TileType.obstacle),
            new TileDto(3, 2, TileDto.TileType.character_hell, 1),
            new TileDto(3, 3, TileDto.TileType.character_heaven, 1),
            new TileDto(3, 4, TileDto.TileType.obstacle),
            new TileDto(3, 5, TileDto.TileType.character_rando),
            new TileDto(4, 1, TileDto.TileType.powerDown_type1),
            new TileDto(4, 2, TileDto.TileType.obstacle),
            new TileDto(4, 3, TileDto.TileType.obstacle),
            new TileDto(4, 4, TileDto.TileType.powerDown_type1),
            new TileDto(4, 5, TileDto.TileType.powerUp_type1),
            new TileDto(5, 1, TileDto.TileType.powerUp_type1),
            new TileDto(5, 2, TileDto.TileType.character_rando)
    );

    public static RoundDto level4 = new RoundDto(
            new QuotaDto(
                    Quota.Source.heaven,
                    new PersonDto(1,
                            Feature.Category.mouth,
                            Feature.Category.eye,
                            Feature.Category.hair_face
                    )
            ),
            new QuotaDto(
                    Quota.Source.hell,
                    new PersonDto(1,
                            Feature.Category.clothes,
                            Feature.Category.hair_head,
                            Feature.Category.eye
                    )
            ),
            new TileDto(0, 0, TileDto.TileType.character_rando),
            new TileDto(0, 2, TileDto.TileType.character_rando),
            new TileDto(0, 3, TileDto.TileType.character_rando),
            new TileDto(1, 1, TileDto.TileType.powerDown_type1),
            new TileDto(1, 2, TileDto.TileType.obstacle),
            new TileDto(1, 4, TileDto.TileType.obstacle),
            new TileDto(2, 2, TileDto.TileType.character_heaven, 1),
            new TileDto(2, 4, TileDto.TileType.character_rando),
            new TileDto(3, 1, TileDto.TileType.character_rando),
            new TileDto(3, 2, TileDto.TileType.obstacle),
            new TileDto(3, 3, TileDto.TileType.character_hell, 1),
            new TileDto(3, 4, TileDto.TileType.obstacle),
            new TileDto(3, 5, TileDto.TileType.character_rando),
            new TileDto(4, 1, TileDto.TileType.obstacle),
            new TileDto(4, 2, TileDto.TileType.character_rando),
            new TileDto(4, 3, TileDto.TileType.obstacle),
            new TileDto(4, 4, TileDto.TileType.obstacle),
            new TileDto(5, 0, TileDto.TileType.character_rando),
            new TileDto(5, 4, TileDto.TileType.powerUp_type1)
    );

    public static RoundDto level5 = new RoundDto(
            new QuotaDto(
                    Quota.Source.heaven,
                    new PersonDto(1,
                            Feature.Category.clothes,
                            Feature.Category.hair_head,
                            Feature.Category.eye
                    ),
                    new PersonDto(1,
                            Feature.Category.mouth,
                            Feature.Category.hair_head,
                            Feature.Category.eye
                    ),
                    new PersonDto(1,
                            Feature.Category.clothes,
                            Feature.Category.mouth,
                            Feature.Category.eye
                    )
            ),
            new QuotaDto(
                    Quota.Source.hell,
                    new PersonDto(1,
                            Feature.Category.hair_face,
                            Feature.Category.hair_head,
                            Feature.Category.eye
                    ),
                    new PersonDto(1,
                            Feature.Category.hair_face,
                            Feature.Category.mouth,
                            Feature.Category.eye
                    ),
                    new PersonDto(1,
                            Feature.Category.hair_face,
                            Feature.Category.hair_head,
                            Feature.Category.mouth
                    )
            ),
            new TileDto(0, 0, TileDto.TileType.character_heaven, 1),
            new TileDto(0, 1, TileDto.TileType.character_rando),
            new TileDto(0, 2, TileDto.TileType.character_rando),
            new TileDto(0, 3, TileDto.TileType.character_rando),
            new TileDto(0, 4, TileDto.TileType.character_rando),
            new TileDto(0, 5, TileDto.TileType.character_rando),
            new TileDto(1, 0, TileDto.TileType.character_rando),
            new TileDto(1, 1, TileDto.TileType.character_rando),
            new TileDto(1, 2, TileDto.TileType.character_hell, 2),
            new TileDto(1, 3, TileDto.TileType.character_rando),
            new TileDto(1, 4, TileDto.TileType.character_rando),
            new TileDto(1, 5, TileDto.TileType.character_rando),
            new TileDto(2, 0, TileDto.TileType.character_rando),
            new TileDto(2, 1, TileDto.TileType.character_rando),
            new TileDto(2, 2, TileDto.TileType.character_heaven, 3),
            new TileDto(2, 3, TileDto.TileType.character_rando),
            new TileDto(2, 4, TileDto.TileType.character_hell, 3),
            new TileDto(2, 5, TileDto.TileType.character_rando),
            new TileDto(3, 0, TileDto.TileType.character_rando),
            new TileDto(3, 1, TileDto.TileType.character_rando),
            new TileDto(3, 2, TileDto.TileType.character_rando),
            new TileDto(3, 3, TileDto.TileType.character_rando),
            new TileDto(3, 4, TileDto.TileType.character_rando),
            new TileDto(3, 5, TileDto.TileType.character_rando),
            new TileDto(4, 0, TileDto.TileType.character_rando),
            new TileDto(4, 1, TileDto.TileType.character_hell, 1),
            new TileDto(4, 2, TileDto.TileType.character_rando),
            new TileDto(4, 3, TileDto.TileType.character_rando),
            new TileDto(4, 4, TileDto.TileType.character_heaven, 2),
            new TileDto(4, 5, TileDto.TileType.character_rando),
            new TileDto(5, 0, TileDto.TileType.character_rando),
            new TileDto(5, 1, TileDto.TileType.character_rando),
            new TileDto(5, 2, TileDto.TileType.character_rando),
            new TileDto(5, 3, TileDto.TileType.character_rando),
            new TileDto(5, 4, TileDto.TileType.character_rando),
            new TileDto(5, 5, TileDto.TileType.character_rando)
    );
}
