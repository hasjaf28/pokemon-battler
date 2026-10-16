package se.systementor.battler.ui;

import se.systementor.battler.model.Pokemon;
import se.systementor.battler.model.Attack;
import se.systementor.battler.model.BattleStats;
import se.systementor.battler.service.BattleService;
import se.systementor.battler.repository.PokemonRepository;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;


public class BattleMenu {
    public static void startBattle(Scanner input, ArrayList<Pokemon> pokedex, BattleStats stats) {
        if (pokedex.isEmpty()) {
            System.out.println("Pokédexen är tom! Du behöver ha minst en Pokémon för att strida!");
            return;
        }

        Main.showAllPokemons(pokedex);
        int index = InputHelper.readIntInRange(input, "Vilken Pokémon vill du använda för striden? " +
                "(1-" + pokedex.size() + "): ", 1, pokedex.size()) - 1;
        Pokemon player = pokedex.get(index);

        BattleService service = new BattleService();
        if (!service.canFight(player)) {
            System.out.println(player.getName() + " kan inte strida (saknar attacker eller HP är 0)!");
            return;
        }

        ArrayList<Pokemon> opponents = PokemonRepository.createSeedData();
        Pokemon cpu = opponents.get(new Random().nextInt(opponents.size()));
        System.out.println("Striden börjar! Din " + player.getName() + " möter motståndarens " + cpu.getName() + "!");

        while (!player.isFainted() && !cpu.isFainted()) {
            Main.printAttacks(player);
            int attackIndex = InputHelper.readIntInRange(input, "Välj Attack (1-" +
                    player.getAttacks().size() + "): ", 1, player.getAttacks().size()) -1;

            Attack playerAttack = player.getAttacks().get(attackIndex);
            stats.addAttackUsage(playerAttack.getName());
            service.playTurn(player, cpu, playerAttack);

            for (String logLine : service.getLog()) {
                System.out.println(logLine);
            }
            service.clearLog();
        }
        if (player.isFainted()) {
            System.out.println("Du förlorade striden! Motståndarens " + cpu.getName() +
                    " vann över din " + player.getName() + "!");
            stats.addLoss(player.getName());
        } else {
            System.out.println("Du vann striden! Din " + player.getName() + " vann över motståndarens " +
                    cpu.getName() + "!");
            stats.addWin(player.getName());
        }

        player.heal(player.getMaxHp());
    }
}
