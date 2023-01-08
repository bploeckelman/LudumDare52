package lando.systems.ld52.gameobjects;

import com.badlogic.gdx.utils.Array;
import lando.systems.ld52.assets.Feature;

import java.util.HashMap;

public class Quota {

    public enum Source {heaven, hell}

    public static class Person {
        public final HashMap<Feature, Boolean> features = new HashMap<>();
        public void addAll(Feature... features) {
            for (Feature feature : features) {
                this.features.put(feature, false);
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
    public final Array<Person> people;

    public Quota(Source source) {
        this.source = source;
        this.people = new Array<>();
    }

    public void addPerson(Feature... features) {
        Person person = new Person();
        person.addAll(features);
        people.add(person);
    }

    // TODO - need to adjust this to set all features for the person who has been reaped
    public void satisfy(Feature feature) {
//        if (features.containsKey(feature)) {
//            features.put(feature, true);
//        }
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
