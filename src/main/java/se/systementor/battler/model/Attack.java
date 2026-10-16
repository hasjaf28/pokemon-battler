package se.systementor.battler.model;

public abstract class Attack {
    private final String name;
    private final Type type;
    private final int baseDamage;
    private final int accuracy;

    public Attack(String name, Type type, int baseDamage, int accuracy) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Tomt namn");
        if (type == null)
            throw new IllegalArgumentException("Type får inte vara tom!");
        if (baseDamage < 1 || baseDamage > 1000)
            throw new IllegalArgumentException("Basskadan ska vara mellan 1-1000!");
        if (accuracy < 1 || accuracy > 100)
            throw new IllegalArgumentException("Träffsäkerheten ska vara mellan 1-100!");
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

    public abstract void execute(Pokemon attacker, Pokemon defender, int amount);

    public abstract String getAttackKind();

    public double getMultiplier(Type defenderType) {
        return 1.0;
    }

    // värdet om träffen blir kritisk
    public double getCritMultiplier() {
        return 1.0;
    }

}

