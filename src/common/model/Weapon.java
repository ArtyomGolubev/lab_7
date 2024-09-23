package common.model;

import java.io.Serializable;

public enum Weapon implements Serializable {

    HEAVY_BOLTGUN("A common anti-infantry weapon"),
    FLAMER("Flamethrower, capable of burning your enemies alive"),
    GRENADE_LAUNCHER("Heavy weapon, perfect for flushing enemies out of cover"),
    MULTI_MELTA("Vicious and effective Imperial anti-tank weapon");

    private final String usage;

    Weapon(String usage) {
        this.usage = usage;
    }

    @Override
    public String toString() {
        return "Weapon {"
                + usage + '}';
    }
}
