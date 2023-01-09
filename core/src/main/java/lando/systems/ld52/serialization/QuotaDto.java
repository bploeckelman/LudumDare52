package lando.systems.ld52.serialization;

import lando.systems.ld52.gameobjects.Quota;

public class QuotaDto {
    public Quota.Source source;

    public PersonDto[] people;

    public QuotaDto(Quota.Source source, PersonDto... people) {
        this.source = source;
        this.people = people;
    }
}
