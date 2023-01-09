package lando.systems.ld52.serialization;

import lando.systems.ld52.assets.Feature;

public class PersonDto {
    // number of people with these features
    public int count = 1;
    // choose specific features in a category or random feature of a category
    public Feature[] features;
    public Feature.Category[] categories;

    public PersonDto(int count, Feature... features) {
        this.count = count;
        this.features = features;
    }

    public PersonDto(int count, Feature.Category... categories) {
        this.count = count;
        this.categories = categories;
    }
}
