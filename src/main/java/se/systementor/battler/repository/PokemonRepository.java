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

    public static ArrayList<Pokemon> loadFromFile(Path path, ArrayList<Pokemon> pokedex) {
        if (!Files.exists(path)) {
            System.out.println("Ingen sparad fil kunde hittas!");
            return pokedex;
        }
        ArrayList<Pokemon> result = new ArrayList<>();
        try {
            for (String line : Files.readAllLines(path)) {
                if (line.isBlank()) {
                    continue;
                }
                String[] fields = line.split(", ");
                if (fields.length != 5) {
                    System.out.println("Hoppar över den korrupta raden: " + line);
                    continue;
                }
                try {
                    String name = fields[0];
                    Type type = Type.valueOf(fields[1]);
                    int maxHp = Integer.parseInt(fields[2]);
                    int currentHp = Integer.parseInt(fields[3]);
                    String attacksPart = fields[4];

                    Pokemon pokemon = new Pokemon(name, type, maxHp);
                    pokemon.setCurrentHp(currentHp);

                    String[] attackParts = attacksPart.split(";");
                    for (String attackPart : attackParts) {
                        String[] attack = attackPart.split(":");
                        if (attack.length != 4) {
                            continue;
                        }
                        String attackName = attack[0];
                        Type attackType = Type.valueOf(attack[1]);
                        int baseDamage = Integer.parseInt(attack[2]);
                        int accuracy = Integer.parseInt(attack[3]);
                        pokemon.getAttacks().add(new Attack(attackName, attackType, baseDamage, accuracy));
                    }
                    result.add(pokemon);
                } catch (Exception e) {
                    System.out.println("Hoppar över den korrupta raden: " + line);
                }
            }
            System.out.println("Pokédexen har laddats från " + path.toAbsolutePath());
        } catch (IOException e) {
            System.out.println("Filen kunde inte läsas: " + e.getMessage());
        }
        return result;
    }

    public static ArrayList<Pokemon> createSeedData() {
        ArrayList<Pokemon> seed = new ArrayList<>();

        Pokemon pikachu = new Pokemon("Pikachu", Type.ELECTRIC, 95);
        pikachu.getAttacks().add(new Attack("Thunderbolt", Type.ELECTRIC, 120, 100));
        pikachu.getAttacks().add(new Attack("Ironfist", Type.NORMAL, 140, 88));
        seed.add(pikachu);

        Pokemon charizard = new Pokemon("Charizard", Type.FIRE, 180);
        charizard.getAttacks().add(new Attack("Fire Blast", Type.FIRE, 180, 90));
        charizard.getAttacks().add(new Attack("Iron Claw", Type.NORMAL, 150, 95));
        seed.add(charizard);

        Pokemon arceus = new Pokemon("Arceus", Type.NORMAL, 360);
        arceus.getAttacks().add(new Attack("Doomsday", Type.NORMAL, 240, 98));
        arceus.getAttacks().add(new Attack("Impactish", Type.NORMAL, 170, 80));
        seed.add(arceus);

        Pokemon gyarados = new Pokemon("Gyarados", Type.WATER, 230);
        gyarados.getAttacks().add(new Attack("Ronin", Type.WATER, 220, 95));
        gyarados.getAttacks().add(new Attack("Water splash", Type.WATER, 170, 75));
        seed.add(gyarados);

        Pokemon snorlax = new Pokemon("Snorlax", Type.GRASS, 380);
        snorlax.getAttacks().add(new Attack("Snuvan", Type.NORMAL, 190, 80));
        snorlax.getAttacks().add(new Attack("Normal Impact", Type.NORMAL, 70, 100));
        seed.add(snorlax);

        Pokemon thanos = new Pokemon("Thanos", Type.FIRE, 450);
        thanos.getAttacks().add(new Attack("Power Stone", Type.FIRE, 480, 98));
        thanos.getAttacks().add(new Attack("Soul Catcher", Type.NORMAL, 290, 90));
        seed.add(thanos);

        return seed;
    }
}
