package se.systementor.battler.service;

import se.systementor.battler.model.Pokemon;
import se.systementor.battler.model.Attack;

import java.util.Random;
import java.util.ArrayList;

public class BattleService {
    private final Random random = new Random();

    private final ArrayList<String> log = new ArrayList<>();

    private boolean isHit(Attack attack) {
        return random.nextInt(100) < attack.getAccuracy();
    }

    private int calculateDamage(Attack attack, double multiplier, double critMultiplier) {
        int factor = random.nextInt(16) + 85;
        int damage = attack.getBaseDamage() * factor / 100;
        damage = (int) (damage * multiplier * critMultiplier);
        return Math.max(1, damage);
    }

    private void performAttack(Pokemon attacker, Pokemon defender, Attack attack) {
        log.add(attacker.getName() + " använder " + attack.getName() + "!");

        if (!isHit(attack)) {
            log.add("Attacken missade!");
            return;
        }

        double multiplier = attack.getMultiplier(defender.getType());
        double critMultiplier = 1.0;
        if (random.nextInt(16) == 0) {
            critMultiplier = attack.getCritMultiplier();
        }
        int amount = calculateDamage(attack, multiplier, critMultiplier);
        attack.execute(attacker, defender, amount);
        if (critMultiplier > 1.0) {
            log.add("Attacken var kritisk!");
        }
        if (multiplier > 1.0) {
            log.add("Attacken var supereffektiv!");
        } else if (multiplier < 1.0) {
            log.add("Attacken var inte särskilt effektiv!");
        }
        log.add(attacker.getName() + " har nu " + attacker.getCurrentHp() + "/" + attacker.getMaxHp() + " HP!");
        log.add(defender.getName() + " har nu " + defender.getCurrentHp() + "/" + defender.getMaxHp() + " HP!");
    }

    private Attack selectCpuAttack(Pokemon cpu) {
        return cpu.getAttacks().get(random.nextInt(cpu.getAttacks().size()));
    }

    public boolean canFight(Pokemon pokemon) {
        return !pokemon.getAttacks().isEmpty() && !pokemon.isFainted();
    }

    public void playTurn(Pokemon player, Pokemon cpu, Attack playerAttack) {
        performAttack(player, cpu, playerAttack);

        if (cpu.isFainted()) {
            log.add(cpu.getName() + " har svimmat!");
            return;
        }

        Attack cpuAttack = selectCpuAttack(cpu);
        performAttack(cpu, player, cpuAttack);

        if (player.isFainted()) {
            log.add(player.getName() + " har svimmat!");
            return;
        }
    }

    public ArrayList<String> getLog() {
        return log;
    }

    public void clearLog() {
        log.clear();
    }
}
