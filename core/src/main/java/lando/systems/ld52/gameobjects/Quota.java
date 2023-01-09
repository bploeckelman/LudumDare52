package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.utils.Array;
import lando.systems.ld52.assets.Feature;
import lando.systems.ld52.serialization.PersonDto;
import lando.systems.ld52.serialization.QuotaDto;

import java.util.HashMap;

public class Quota {

    public enum Source {heaven, hell}

    public static class Person {
        public final HashMap<Feature, Boolean> features = new HashMap<>();
        public boolean didJustSatisfy = false;

        // TODO - probably best to limit number of features to max of 3 so UI doesn't get nuts
        public void addAll(Feature... features) {
            for (Feature feature : features) {
                this.features.put(feature, false);
            }
        }

        public void satisfy(Array<Feature> features) {
            for (Feature feature : features) {
                if (this.features.containsKey(feature)) {
                    this.features.put(feature, true);
                    didJustSatisfy = true;
                }
            }
        }

        public boolean isSatisfied() {
            boolean satisfied = true;
            for (HashMap.Entry<Feature, Boolean> entry : features.entrySet()) {
                boolean featureSatisfied = entry.getValue();
                if (!featureSatisfied) {
                    satisfied = false;
                    break;
                }
            }
            return satisfied;
        }
    }

    public final Source source;
    // TODO - add a quantity modifier for each person (so we can say you need 2x or 3x people like this to fill the quota not just one)
    public final Array<Person> people = new Array<>();
    public boolean didJustSatisfy = false;

    public Quota(QuotaDto quotaDto) {
        this.source = quotaDto.source;

        for (PersonDto personDto : quotaDto.people) {
            Feature[] features = personDto.features;
            if (features == null) {
                Feature.Category[] categories = personDto.categories;
                features = new Feature[categories.length];
                for (int i = 0; i < categories.length; i++) {
                    features[i] = Feature.getRandomFrom(categories[i]);
                }
            }
            addPerson(features);
        }
    }

    public void addPerson(Feature... features) {
        Person person = new Person();
        person.addAll(features);
        people.add(person);
    }

    public void satisfy(Array<Feature> features) {
        for (Person person : people) {
            person.satisfy(features);
            if (person.didJustSatisfy) {
                didJustSatisfy = true;
            }
        }
    }

    public boolean isSatisfied() {
        for (Person person : people) {
            if (!person.isSatisfied()) {
                return false;
            }
        }
        return true;
    }

}
