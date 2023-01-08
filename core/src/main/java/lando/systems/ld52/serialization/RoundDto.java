package lando.systems.ld52.serialization;

import com.badlogic.gdx.utils.Json;
import lando.systems.ld52.gameobjects.Quota;

public class RoundDto {
    public QuotaDto heaven;
    public QuotaDto hell;

    public static RoundDto fromJson(String roundData) {
        Json json = new Json();
        return json.fromJson(RoundDto.class, roundData);
    }
}
