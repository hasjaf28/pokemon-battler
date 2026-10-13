package se.systementor.battler.repository;

import se.systementor.battler.model.Pokemon;
import se.systementor.battler.model.Attack;
import se.systementor.battler.model.Type;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class PokemonRepository {
    public static void saveToFile(Path path, ArrayList<Pokemon> pokedex) {
        try (BufferedWriter fileWriter = Files.newBufferedWriter(path)) {
            for (Pokemon pokemon : pokedex) {
                String attacksPart = "";
                for (int i = 0; i < pokemon.getAttacks().size(); i++) {
                    if (i > 0) {
                        attacksPart = attacksPart + ";";
                    }
                    Attack attack = pokemon.getAttacks().get(i);
                    attacksPart = attacksPart + attack.getName() + ":" +
                            attack.getType() + ":" + attack.getBaseDamage() + ":" + attack.getAccuracy();
                }
                fileWriter.write(pokemon.getName() + ", " + pokemon.getType() + ", "
                        + pokemon.getMaxHp() + ", " + pokemon.getCurrentHp() + ", " + attacksPart);
                fileWriter.newLine();
            }
            System.out.println("Pokédexen har sparats till " + path.toAbsolutePath());
        } catch (IOException e) {
            System.out.println("Filen kunde inte sparas: " + e.getMessage());
        }
    }
}
