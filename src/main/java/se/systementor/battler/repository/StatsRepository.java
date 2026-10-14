package se.systementor.battler.repository;

import se.systementor.battler.model.BattleStats;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

public class StatsRepository {

    public static void saveStats(Path path, BattleStats stats) {
        try {
            Files.writeString(path, stats.getWins() + "," + stats.getLosses());
        } catch (IOException e) {
            System.out.println("Kunde inte spara statistiken: " + e.getMessage());
        }
    }

    public static BattleStats loadStats(Path path) {
        if (!Files.exists(path)) {
            return new BattleStats(0, 0);
        }
        try {
            String data = Files.readString(path);
            String[] fields = data.trim().split(",");
            int wins = Integer.parseInt(fields[0]);
            int losses = Integer.parseInt(fields[1]);
            return new BattleStats(wins, losses);
        }catch (IOException | ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            System.out.println("Kunde inte läsa statistiken: " + e.getMessage());
            return new BattleStats(0, 0);
        }
    }
}
