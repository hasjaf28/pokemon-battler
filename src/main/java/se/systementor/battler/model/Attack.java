package se.systementor.battler.model;

public class Attack {
    private final String name;
    private final Type type;
    private final int baseDamage;
    private final int accuracy;

    public Attack(String name, Type type, int baseDamage, int accuracy) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Tomt namn");
        if (baseDamage < 0 || baseDamage > 1000)
            throw new IllegalArgumentException("Basskadan ska vara mellan 0-1000!");
        if (accuracy < 0 || accuracy > 100)
            throw new IllegalArgumentException("Träffsäkerheten ska vara mellan 0-100!");
        this.name = name;
        this.type = type;
        this.baseDamage = baseDamage;
        this.accuracy = accuracy;
    }
    public String getName() {
        return name;
    }
    public Type getType() {
        return type;
    }
    public int getBaseDamage() {
        return baseDamage;
    }
    public int getAccuracy() {
        return accuracy;
    }

}

