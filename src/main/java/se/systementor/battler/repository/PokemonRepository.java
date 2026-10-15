package se.systementor.battler.repository;

import se.systementor.battler.model.Pokemon;
import se.systementor.battler.model.Attack;
import se.systementor.battler.model.Type;
import se.systementor.battler.model.DamageAttack;
import se.systementor.battler.model.HealAttack;

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
                            attack.getType() + ":" + attack.getBaseDamage() + ":" + attack.getAccuracy() +
                            ":" + attack.getAttackKind();
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
                        if (attack.length != 5) {
                            throw new IllegalArgumentException("Attacken är korrupt: " + attackPart);
                        }
                        String attackName = attack[0];
                        Type attackType = Type.valueOf(attack[1]);
                        int baseDamage = Integer.parseInt(attack[2]);
                        int accuracy = Integer.parseInt(attack[3]);
                        String attackKind = attack[4];

                        Attack newAttack;
                        if (attackKind.equals("Damage")) {
                            newAttack = new DamageAttack(attackName, attackType, baseDamage, accuracy);
                        } else if (attackKind.equals("Heal")) {
                            newAttack = new HealAttack(attackName, attackType, baseDamage, accuracy);
                        } else {
                            throw new IllegalArgumentException("Attacken är okänd: " + attackKind);
                        }
                        pokemon.getAttacks().add(newAttack);
                    }
                    result.add(pokemon);
                } catch (Exception e) {
                    System.out.println("Hoppar över den korrupta raden: " + line + " (" + e.getMessage() + ")");
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

        Pokemon pikachu = new Pokemon("Pikachu", Type.ELECTRIC, 200);
        pikachu.getAttacks().add(new DamageAttack("Thunderbolt", Type.ELECTRIC, 90, 100));
        pikachu.getAttacks().add(new DamageAttack("Ironfist", Type.NORMAL, 70, 90));
        seed.add(pikachu);

        Pokemon charizard = new Pokemon("Charizard", Type.FIRE, 250);
        charizard.getAttacks().add(new DamageAttack("Fire Blast", Type.FIRE, 95, 85));
        charizard.getAttacks().add(new DamageAttack("Iron Claw", Type.NORMAL, 80, 95));
        seed.add(charizard);

        Pokemon arceus = new Pokemon("Arceus", Type.NORMAL, 310);
        arceus.getAttacks().add(new DamageAttack("Doomsday", Type.NORMAL, 95, 90));
        arceus.getAttacks().add(new DamageAttack("Impactish", Type.NORMAL, 85, 80));
        seed.add(arceus);

        Pokemon gyarados = new Pokemon("Gyarados", Type.WATER, 270);
        gyarados.getAttacks().add(new DamageAttack("Ronin", Type.WATER, 90, 90));
        gyarados.getAttacks().add(new DamageAttack("Water splash", Type.WATER, 70, 75));
        seed.add(gyarados);

        Pokemon snorlax = new Pokemon("Snorlax", Type.GRASS, 390);
        snorlax.getAttacks().add(new DamageAttack("Snuvan", Type.NORMAL, 85, 75));
        snorlax.getAttacks().add(new DamageAttack("Normal Impact", Type.NORMAL, 70, 100));
        seed.add(snorlax);

        Pokemon thanos = new Pokemon("Thanos", Type.FIRE, 450);
        thanos.getAttacks().add(new DamageAttack("Power Stone", Type.FIRE, 100, 98));
        thanos.getAttacks().add(new DamageAttack("Soul Catcher", Type.NORMAL, 90, 90));
        seed.add(thanos);

        return seed;
    }
}
