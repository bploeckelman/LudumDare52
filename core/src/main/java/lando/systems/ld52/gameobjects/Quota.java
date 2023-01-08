package lando.systems.ld52.gameobjects;

import lando.systems.ld52.assets.Feature;
import lando.systems.ld52.serialization.QuotaDto;

import java.util.HashMap;

public class Quota {
    public enum Source {heaven, hell}

    public final Source source;

    public final HashMap<Feature, Boolean> features;

    public Quota(QuotaDto quota) {
        this.source = quota.source;
        this.features = new HashMap<>();
        for (Feature feature : quota.features) {
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
