package se.systementor.battler.model;

public class HealAttack extends Attack {

    public HealAttack(String name, Type type, int baseDamage, int accuracy) {
        super(name, type, baseDamage, accuracy);
    }

    @Override
    public void execute(Pokemon attacker, Pokemon defender, int amount) {
        attacker.heal(amount);
    }
}
