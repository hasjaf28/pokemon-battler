package se.systementor.battler.ui;


import se.systementor.battler.model.Attack;
import se.systementor.battler.model.Pokemon;
import se.systementor.battler.model.Type;
import se.systementor.battler.model.DamageAttack;
import se.systementor.battler.model.BattleStats;
import se.systementor.battler.repository.PokemonRepository;
import se.systementor.battler.repository.StatsRepository;


import java.util.Scanner;
import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Files;

public class Main {


    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Path path = Path.of("pokemon.txt");

        ArrayList<Pokemon> pokedex;
        if (Files.exists(path)) {
            pokedex = PokemonRepository.loadFromFile(path, new ArrayList<>());
            System.out.println("Sparad pokémondata hittades och lästes in");
        } else {
            pokedex = PokemonRepository.createSeedData();
            System.out.println("Inget sparad pokémondata hittades därför fyller pokédexen på med startdata!");
        }

        Path statsPath = Path.of("stats.txt");
        BattleStats stats = StatsRepository.loadStats(statsPath);

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
            System.out.println("8. Starta strid");
            System.out.println("9. Stridsresultat");
            System.out.println("10. Avsluta");

            try {
                int choice = InputHelper.readIntInRange(input, "Välj: ", 1, 10);
                switch (choice) {
                    case 1 -> showAllPokemons(pokedex);
                    case 2 -> addPokemon(input, pokedex);
                    case 3 -> editPokemon(input, pokedex);
                    case 4 -> deletePokemon(input, pokedex);
                    case 5 -> PokemonRepository.saveToFile(path, pokedex);
                    case 6 -> pokedex = PokemonRepository.loadFromFile(path, pokedex);
                    case 7 -> pokedex = resetToSeed(input, pokedex);
                    case 8 -> {
                        BattleMenu.startBattle(input, pokedex, stats);
                        StatsRepository.saveStats(statsPath, stats);
                    }
                    case 9 -> {
                        System.out.println("===== Stridsresultat =====");
                        System.out.println("Vinster: " + stats.getWins());
                        System.out.println("Förluster: " + stats.getLosses());
                    }
                    case 10 -> running = false;
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
        PokemonRepository.saveToFile(path, pokedex);
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
        int baseDamage = InputHelper.readIntInRange(input, "Basskada (1-1000): ", 1, 1000);
        int accuracy = InputHelper.readIntInRange(input, "Träffsäkerhet (1-100): ", 1, 100);
        return new DamageAttack(name, type, baseDamage, accuracy);
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

    public static ArrayList<Pokemon> resetToSeed(Scanner input, ArrayList<Pokemon> pokedex) {
        boolean confirm = InputHelper.readYesNo(input,
                "Du är på väg att radera all data, vill du återställa Pokédexen till startdata?");
        if (confirm) {
            System.out.println("Pokédexen har återställts till startdata!");
            return PokemonRepository.createSeedData();
        } else {
            System.out.println("Återställningen avbröts!");
            return pokedex;
        }
    }
}