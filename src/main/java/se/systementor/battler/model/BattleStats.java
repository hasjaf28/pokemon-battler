package se.systementor.battler.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BattleStats {
    private int wins;
    private int losses;
    private HashMap<String, Integer> winsPerPokemon = new HashMap<>();
    private HashMap<String, Integer> lossesPerPokemon = new HashMap<>();
    private HashMap<String, Integer> attackUsage = new HashMap<>();

    public BattleStats() {
    }

    public BattleStats(int wins, int losses) {
        if (wins < 0 || losses < 0)
            throw new IllegalArgumentException("Vinster och förluster får inte vara negativa!");
        this.wins = wins;
        this.losses = losses;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setWins(int wins) {
        if (wins < 0)
            throw new IllegalArgumentException("Vinster får inte vara negativa!");
        this.wins = wins;
    }

    public void setLosses(int losses) {
        if (losses < 0)
            throw new IllegalArgumentException("Förluster får inte vara negativa!");
        this.losses = losses;
    }

    public HashMap<String, Integer> getWinsPerPokemon() {
        return winsPerPokemon;
    }

    public void setWinsPerPokemon(HashMap<String, Integer> winsPerPokemon) {
        if (winsPerPokemon == null)
            throw new IllegalArgumentException("Vinster per Pokémon saknas!");
        this.winsPerPokemon = winsPerPokemon;
    }

    public HashMap<String, Integer> getLossesPerPokemon() {
        return lossesPerPokemon;
    }

    public void setLossesPerPokemon(HashMap<String, Integer> lossesPerPokemon) {
        if (lossesPerPokemon == null)
            throw new IllegalArgumentException("Förluster per Pokémon saknas!");
        this.lossesPerPokemon = lossesPerPokemon;
    }

    public HashMap<String, Integer> getAttackUsage() {
        return attackUsage;
    }

    public void setAttackUsage(HashMap<String, Integer> attackUsage) {
        if (attackUsage == null)
            throw new IllegalArgumentException("Statistik för attacker saknas!");
        this.attackUsage = attackUsage;
    }

    public void addWin(String pokemonName) {
        wins++;
        winsPerPokemon.merge(pokemonName, 1, Integer::sum);
    }

    public void addLoss(String pokemonName) {
        losses++;
        lossesPerPokemon.merge(pokemonName, 1, Integer::sum);
    }

    public void addAttackUsage(String attackName) {
        attackUsage.merge(attackName, 1, Integer::sum);
    }

    public String mostUsedAttack() {
        return topKey(attackUsage);
    }

    public String mostUsedPokemon() {
        return topKey(battlesPerPokemon());
    }

    private HashMap<String, Integer> battlesPerPokemon() {
        HashMap<String, Integer> battles = new HashMap<>();
        for (Map.Entry<String, Integer> entry : winsPerPokemon.entrySet()) {
            battles.merge(entry.getKey(), entry.getValue(), Integer::sum);
        }
        for (Map.Entry<String, Integer> entry : lossesPerPokemon.entrySet()) {
            battles.merge(entry.getKey(), entry.getValue(), Integer::sum);
        }
        return battles;
    }

    private String topKey(Map<String, Integer> counts) {
        String top = null;
        int highest = 0;
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            if (entry.getValue() > highest) {
                highest = entry.getValue();
                top = entry.getKey();
            }
        }
        return top;
    }

    public double winRatio(String pokemonName) {
        int wins = winsPerPokemon.getOrDefault(pokemonName, 0);
        int losses = lossesPerPokemon.getOrDefault(pokemonName, 0);
        int total = wins + losses;
        if (total == 0) {
            return 0.0;
        }
        return (double) wins / total;
    }

    public Set<String> playedPokemon() {
        return battlesPerPokemon().keySet();
    }
}
