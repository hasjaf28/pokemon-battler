package se.systementor.battler.ui;

import java.util.Scanner;

public class InputHelper {

    // Läser in ett heltal. Loopar tills användaren skriver giltigt heltal!
    public static int readInt(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);
            String inputLine = input.nextLine();
            try {
                return Integer.parseInt(inputLine.trim());
            } catch (NumberFormatException e) {
                System.out.println("Felaktig inmatning, vänligen ange ett heltal: ");
            }
        }
    }

    // Läser in ett namn och loopar tills användaren anger rätt namn.
    public static String readValidName(Scanner input, String prompt) {
        while (true) {
            String name = readLine(input, prompt);
            if (name.trim().isEmpty()) {
                System.out.println("Namnet får inte vara tomt. Ange rätt namn!");
                continue;
            }
            if (name.split(", ").length > 1 || name.split(":").length > 1 || name.split(";").length > 1) {
                System.out.println("Namnet får inte innehålla följande tecken : eller ; " +
                        "samt kommatecken följt av mellanslag. Vänligen ange rätt namn!");
                continue;
            }
            return name.trim();
        }
    }

    // Kollar så att värdet ligger inom min och max.
    public static int readIntInRange(Scanner input, String prompt, int min, int max) {
        while (true) {
            int value = readInt(input, prompt);
            if (value < min || value > max) {
                System.out.printf("Fel, ange ett tal mellan %d och %d.%n", min, max);
                continue;
            }
            return value;
        }
    }

    // läser in en hel rad text och tom rad ger bara "",
    public static String readLine(Scanner input, String prompt) {
        System.out.print(prompt);
        return input.nextLine();
    }

    //Läser in ja och nej svar. Loopen körs tills programmet får rätt svar.
    public static boolean readYesNo(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt + " (ja/nej): ");
            String answer = input.nextLine().trim().toLowerCase();
            if (answer.equals("ja") || answer.equals("j")) return true;
            if (answer.equals("nej") || answer.equals("n")) return false;
            System.out.println("Svara med ja eller nej");
        }
    }
}
