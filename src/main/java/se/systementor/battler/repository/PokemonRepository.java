package se.systementor.battler.repository;

import se.systementor.battler.model.Pokemon;
import se.systementor.battler.model.Attack;
import se.systementor.battler.model.Type;
import se.systementor.battler.model.DamageAttack;
import se.systementor.battler.model.HealAttack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;


public class PokemonRepository {

    public static void saveToFile(Path path, ArrayList<Pokemon> pokedex) {
        ArrayList<PokemonData> dataList = new ArrayList<>();
        for (Pokemon pokemon : pokedex) {
            dataList.add(toPokemonData(pokemon));
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), dataList);
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
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<PokemonData> dataList = mapper.readValue(path.toFile(), new TypeReference<List<PokemonData>>() {});
            if (dataList == null) {
                System.out.println("Filen innehåller ingen data!");
                return pokedex;
            }
            for (PokemonData data : dataList) {
                if (data == null) {
                    System.out.println("Hoppar över en ogiltig post i filen!");
                    continue;
                }
                try {
                    result.add(toPokemon(data));
                } catch (Exception e) {
                    System.out.println("Hoppar över " + data.name + ": " + e.getMessage());
                }
            }
            System.out.println("Pokédexen har laddats från " + path.toAbsolutePath());
        } catch (IOException e) {
            System.out.println("Filen kunde inte läsas: " + e.getMessage());
            return pokedex;
        }
        return result;
    }

    private static AttackData toAttackData(Attack attack) {
        AttackData data = new AttackData();
        data.name = attack.getName();
        data.type = attack.getType();
        data.baseDamage = attack.getBaseDamage();
        data.accuracy = attack.getAccuracy();
        data.kind = attack.getAttackKind();
        return data;
    }

    private static PokemonData toPokemonData(Pokemon pokemon) {
        PokemonData data = new PokemonData();
        data.name = pokemon.getName();
        data.type = pokemon.getType();
        data.maxHp = pokemon.getMaxHp();
        data.currentHp = pokemon.getCurrentHp();
        data.attacks = new ArrayList<>();
        for (Attack attack : pokemon.getAttacks()) {
            data.attacks.add(toAttackData(attack));
        }
        return data;
    }

    private static Attack toAttack(AttackData data) {
        if ("Damage".equals(data.kind)) {
            return new DamageAttack(data.name, data.type, data.baseDamage, data.accuracy);
        } else if ("Heal".equals(data.kind)) {
            return new HealAttack(data.name, data.type, data.baseDamage, data.accuracy);
        } else {
            throw new IllegalArgumentException("Okänd attacktyp: " + data.kind);
        }
    }

    private static Pokemon toPokemon(PokemonData data) {
        if (data.attacks == null) {
            throw new IllegalArgumentException("Attacklistan saknas!");
        }
        Pokemon pokemon = new Pokemon(data.name, data.type, data.maxHp);
        pokemon.setCurrentHp(data.currentHp);
        for (AttackData attackData : data.attacks) {
            pokemon.getAttacks().add(toAttack(attackData));
        }
        return pokemon;
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
