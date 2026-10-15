package se.systementor.battler.model;

public class DamageAttack extends Attack {

    public DamageAttack(String name, Type type, int baseDamage, int accuracy) {
        super(name, type, baseDamage, accuracy);
    }

    @Override
    public void execute(Pokemon attacker, Pokemon defender, int amount) {
        defender.takeDamage(amount);
    }

    @Override
    public String getAttackKind() {
        return "Damage";
    }
}
