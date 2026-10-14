package se.systementor.battler.model;

public class BattleStats {
    private int wins;
    private int losses;

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

    public void addWin() {
        wins++;
    }

    public void addLoss() {
        losses++;
    }
}
