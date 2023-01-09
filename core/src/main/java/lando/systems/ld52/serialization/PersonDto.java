package lando.systems.ld52.serialization;

import lando.systems.ld52.assets.Feature;

public class PersonDto {
    private int count = 1;
    // choose specific features in a category or random feature of a category
    private Feature[] features;
    private Feature.Category[] categories;

    public PersonDto() {}

    public PersonDto(int count, Feature... features) {
        this.count = count;
        this.features = features;
    }

    public PersonDto(int count, Feature.Category... categories) {
        this.count = count;
        this.categories = categories;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Feature[] getFeatures() {
        return features;
    }

    public void setFeatures(Feature[] features) {
        this.features = features;
    }

    public Feature.Category[] getCategories() {
        return categories;
    }

    public void setCategories(Feature.Category[] categories) {
        this.categories = categories;
    }
}
