package lando.systems.ld52.gameobjects;

import lando.systems.ld52.assets.Feature;

import java.util.HashMap;

public class Quota {
    public enum Source {heaven, hell}

    public final Source source;

    // TODO - the value here should probably be a little data structure
    //  that holds metadata on the character who filled this feature (or null if not yet filled)
    public final HashMap<Feature, Boolean> features;

    public Quota(Source source, Feature... features) {
        this.source = source;
        this.features = new HashMap<>();
        for (Feature feature : features) {
            this.features.put(feature, false);
        }
    }

    public void satisfy(Feature feature) {
        if (features.containsKey(feature)) {
            features.put(feature, true);
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
