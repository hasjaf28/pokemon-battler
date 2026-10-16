package se.systementor.battler.model;

public enum Type {
    WATER,
    FIRE,
    NORMAL,
    ELECTRIC,
    GRASS;

    public double damageMultiplier(Type defenderType) {
        if (this == FIRE && defenderType == GRASS) {
            return 2.0;
        }
        if (this == WATER && defenderType == FIRE) {
            return 2.0;
        }
        if (this == GRASS && defenderType == WATER) {
            return 2.0;
        }
        if (this == ELECTRIC && defenderType == WATER) {
            return 2.0;
        }
        if (this == FIRE && defenderType == WATER) {
            return 0.5;
        }
        if (this == WATER && defenderType == GRASS) {
            return 0.5;
        }
        if (this == GRASS && defenderType == FIRE) {
            return 0.5;
        }
        return 1.0;
    }
}
