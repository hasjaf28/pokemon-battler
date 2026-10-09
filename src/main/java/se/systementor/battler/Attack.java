package se.systementor.battler;

public class Attack {
    public String name;
    public Type type;
    public int baseDamage;
    public int accuracy;

    public Attack(String name, Type type, int baseDamage, int accuracy) {
        this.name = name;
        this.type = type;
        this.baseDamage = baseDamage;
        this.accuracy = accuracy;
    }
}

