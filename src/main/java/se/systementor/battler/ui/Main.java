package se.systementor.battler.ui;


import se.systementor.battler.model.Attack;
import se.systementor.battler.model.Pokemon;
import se.systementor.battler.model.Type;

import java.util.Scanner;
import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.io.BufferedWriter;

public class Main {


    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Path path = Path.of("pokemon.txt");

        ArrayList<Pokemon> pokedex;
        if (Files.exists(path)) {
            pokedex = loadFromFile(path, new ArrayList<>());
            System.out.println("Sparad pokémondata hittades och lästes in");
        } else {
            pokedex = createSeedData();
            System.out.println("Inget sparad pokémondata hittades därför fyller pokédexen på med startdata!");
        }

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("=== Välkommen till POKÉDEX ===");
            System.out.println("1. Visa alla Pokémoner");
            System.out.println("2. Lägg till ny Pokémon");
            System.out.println("3. Redigera Pokémon");
            System.out.println("4. Ta bort Pokémon");
            System.out.println("5. Spara till fil");
            System.out.println("6. Ladda från fil");
            System.out.println("7. Återställ till seedad data");
            System.out.println("8. Avsluta");

            try {
                int choice = InputHelper.readIntInRange(input, "Välj: ", 1, 8);
                switch (choice) {
                    case 1 -> showAllPokemons(pokedex);
                    case 2 -> addPokemon(input, pokedex);
                    case 3 -> editPokemon(input, pokedex);
                    case 4 -> deletePokemon(input, pokedex);
                    case 5 -> saveToFile(path, pokedex);
                    case 6 -> pokedex = loadFromFile(path, pokedex);
                    case 7 -> pokedex = resetToSeed(input, pokedex);
                    case 8 -> running = false;
                }
            } catch (Exception e) {
                String message = e.getMessage();
                if (message == null) {
                    message = "Oväntat fel inträffades! Avslutar Pokédexen!";
                }
                System.out.println(message);
                running = false;
            }
        }
        saveToFile(path, pokedex);
        System.out.println("Pokédex avslutat! Ha det bra!");
    }

    public static void showAllPokemons(ArrayList<Pokemon> pokedex) {
        if (pokedex.isEmpty()) {
            System.out.println("Pokédexen är tom!");
            return;
        }
        for (int i = 0; i < pokedex.size(); i++) {
            Pokemon p = pokedex.get(i);
            System.out.printf("%d. %-12s | Type: %-8s | HP: %d/%d | Attacker: %d%n",
                    i + 1, p.getName(), p.getType(), p.getCurrentHp(), p.getMaxHp(), p.getAttacks().size());
        }
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
    public static void addPokemon(Scanner input, ArrayList<Pokemon> pokedex) {
        String name = InputHelper.readValidName(input, "Namn: ");
        Type type = askForType(input);
        int maxHp = InputHelper.readIntInRange(input, "Max HP (1-1000): ", 1, 1000);
        Pokemon pokemon = new Pokemon(name, type, maxHp);

        int numberOfAttacks = InputHelper.readIntInRange(input, "Hur många attacker vill du lägga till (1-4)? ",
                1, 4);
        for (int i = 0; i < numberOfAttacks; i++) {
            pokemon.getAttacks().add(askForNewAttack(input));
        }

        pokedex.add(pokemon);
        System.out.println(pokemon.getName() + " har lagts till!");
    }

    public static Type askForType(Scanner input) {
        Type[] types = Type.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i]);
        }
        int choice = InputHelper.readIntInRange(input, "Type: ", 1, types.length);
        return types[choice -1];
    }

    public static Attack askForNewAttack(Scanner input) {
        String name = InputHelper.readValidName(input, "Attackens namn: ");
        Type type = askForType(input);
        int baseDamage = InputHelper.readIntInRange(input, "Basskada (0-1000): ", 0, 1000);
        int accuracy = InputHelper.readIntInRange(input, "Träffsäkerhet (0-100): ", 0, 100);
        return new Attack(name, type, baseDamage, accuracy);
    }

    public static void printAttacks(Pokemon pokemon) {
        for (int i = 0; i < pokemon.getAttacks().size(); i++) {
            System.out.println((i + 1) + ". " + pokemon.getAttacks().get(i).getName());
        }
    }

    public static void editPokemon(Scanner input, ArrayList<Pokemon> pokedex) {
        if (pokedex.isEmpty()) {
            System.out.println("Pokédexen är tom, det finns inga Pokemon att redigera!");
            return;
        }
        showAllPokemons(pokedex);

        int index = InputHelper.readIntInRange(input,
                "Vilken Pokémon vill du redigera? (1-" + pokedex.size() + "): ", 1, pokedex.size()) - 1;
        Pokemon pokemon = pokedex.get(index);

        boolean editing = true;
        while (editing) {
            System.out.println();
            System.out.println("Redigerar: " + pokemon.getName());
            System.out.println("1. Ändra namn");
            System.out.println("2. Ändra type");
            System.out.println("3. Ändra max HP");
            System.out.println("4. Lägg till attack");
            System.out.println("5. Ta bort attack");
            System.out.println("6. Klar med redigeringen!");

            int choice = InputHelper.readIntInRange(input, "Välj: ", 1, 6);
            switch (choice) {
                case 1 -> {
                    String oldName = pokemon.getName();
                    pokemon.setName(InputHelper.readValidName(input, "Nytt namn: "));
                    System.out.println("Namnet ändrades från " + oldName + " till " + pokemon.getName() + "!");
                }
                case 2 -> {
                    Type oldType = pokemon.getType();
                    pokemon.setType(askForType(input));
                    System.out.println("Typen ändrades från " + oldType + " till " + pokemon.getType() + "!");
                }
                case 3 -> {
                    int oldHp = pokemon.getMaxHp();
                    int newMaxHp = InputHelper.readIntInRange(input, "Nytt max HP (1-1000): ", 1, 1000);
                    pokemon.setMaxHp(newMaxHp);
                    System.out.println("Max HP har ändrats från " + oldHp + " till " + pokemon.getMaxHp() + "!");
                }
                case 4 -> {
                    if (pokemon.getAttacks().size() >= 4) {
                        System.out.println("Den här Pokémonen har redan 4 attacker. Du kan inte lägga till fler!");
                    } else {
                        Attack newAttack = askForNewAttack(input);
                        pokemon.getAttacks().add(newAttack);
                        System.out.println("Attacken \"" + newAttack.getName() + "\" har lagts till!");
                    }
                }
                case 5 -> {
                    if (pokemon.getAttacks().size() <= 1) {
                        System.out.println("En Pokémon måste ha minst en attack. Den enda attacken får inte tas bort!");
                    } else {
                        printAttacks(pokemon);
                        int attackIndex = InputHelper.readIntInRange(input,
                                "Vilken attack vill du ta bort? (1-" + pokemon.getAttacks().size() + "): ",
                                1, pokemon.getAttacks().size()) - 1;

                        String removedName = pokemon.getAttacks().get(attackIndex).getName();
                        pokemon.getAttacks().remove(attackIndex);
                        System.out.println("Attacken " + removedName + " har tagits bort!");
                    }
                }
                case 6 -> {
                    editing = false;
                    System.out.println("Redigeringen är klar för " + pokemon.getName() + "!");
                }
            }
        }
    }

    public static void deletePokemon(Scanner input, ArrayList<Pokemon> pokedex) {
        if (pokedex.isEmpty()) {
            System.out.println("Pokédexen är tom, det finns inte några Pokémon att ta bort!");
            return;
        }
        showAllPokemons(pokedex);

        int index = InputHelper.readIntInRange(input,
                "Vilken Pokémon vill du ta bort? (1-" + pokedex.size() + "): ", 1, pokedex.size()) - 1;
        Pokemon pokemon = pokedex.get(index);

        boolean deleting = InputHelper.readYesNo(input, "Är du säker på borttagning av " + pokemon.getName() + "?");
        if (deleting) {
            pokedex.remove(index);
            System.out.println("Borttagning av " + pokemon.getName() + " genomfördes!");
        } else {
            System.out.println("Borttagningen avbröts!");
        }
    }

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
    public static ArrayList<Pokemon> resetToSeed(Scanner input, ArrayList<Pokemon> pokedex) {
        boolean confirm = InputHelper.readYesNo(input,
                "Du är på väg att radera all data, vill du återställa Pokédexen till startdata?");
        if (confirm) {
            System.out.println("Pokédexen har återställts till startdata!");
            return createSeedData();
        } else {
            System.out.println("Återställningen avbröts!");
            return pokedex;
        }
    }
}