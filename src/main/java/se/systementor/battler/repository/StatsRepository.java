package se.systementor.battler.repository;

import se.systementor.battler.model.BattleStats;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StatsRepository {

    public static void saveStats(Path path, BattleStats stats) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), stats);
        } catch (IOException e) {
            System.out.println("Kunde inte spara statistiken: " + e.getMessage());
        }
    }

    public static BattleStats loadStats(Path path) {
        if (!Files.exists(path)) {
            return new BattleStats(0, 0);
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(path.toFile(), BattleStats.class);
        }catch (IOException e) {
            System.out.println("Kunde inte läsa statistiken: " + e.getMessage());
            return new BattleStats(0, 0);
        }
    }
}
