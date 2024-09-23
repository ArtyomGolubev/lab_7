package common.model;

import java.io.Serializable;

public enum AstartesCategory implements Serializable {

    ASSAULT("Capable of advancing and shooting simultaneously"),
    SUPPRESSOR("Capable of providing rapid response to heavily armoured enemy threats"),
    TERMINATOR("Capable of wearing Tactical Dreadnought Armour");

    private final String capabilities;

    AstartesCategory(String capabilities) {
        this.capabilities = capabilities;
    }

    @Override
    public String toString() {
        return "Astartes Category {"
                + capabilities + '}';
    }
}
