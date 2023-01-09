package lando.systems.ld52.serialization;

import lando.systems.ld52.gameobjects.Quota;

public class QuotaDto {
    public Quota.Source source;

    public PersonDto[] people;

    public QuotaDto() {}

    public QuotaDto(Quota.Source source, PersonDto... people) {
        this.source = source;
        this.people = people;
    }

    public Quota.Source getSource() {
        return source;
    }

    public void setSource(Quota.Source source) {
        this.source = source;
    }

    public PersonDto[] getPeople() {
        return people;
    }

    public void setPeople(PersonDto[] people) {
        this.people = people;
    }
}
