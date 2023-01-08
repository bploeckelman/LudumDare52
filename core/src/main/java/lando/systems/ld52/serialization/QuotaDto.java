package lando.systems.ld52.serialization;

import lando.systems.ld52.assets.Feature;
import lando.systems.ld52.gameobjects.Quota;

public class QuotaDto {
    public Quota.Source source;
    public Feature[] features;

    public void set(Quota.Source source, Feature... features) {
        this.source = source;
        this.features = features;
    }
}
