package presentation;

import business.entities.monster.Monster;
import business.entities.character.Character;

import java.util.*;

/**
 * Class that contains the needed methods to show to the user.
 */
public class Menu {
    private final Scanner scanner;

    public Menu() {
        this.scanner = new Scanner(System.in);
    }

    public void showWelcome() {
        String SIMPLELSRPG = """
                   ____ _               __       __    ____ ___   ___   _____
                  / __/(_)__ _   ___   / /___   / /   / __// _ \\ / _ \\ / ___/
                 _\\ \\ / //  ' \\ / _ \\ / // -_) / /__ _\\ \\ / , _// ___// (_ /
                /___//_//_/_/_// .__//_/ \\__/ /____//___//_/|_|/_/    \\___/
                              /_/
                """;
        System.out.println(SIMPLELSRPG);
        System.out.println("Welcome to Simple LSRPG.\n");
    }

    public void showPersistenceSourceMenu() {
        System.out.println("Do you want to use your local or cloud data?");
        System.out.println("\t1) Local data");
        System.out.println("\t2) Cloud data\n");
    }

    public void showMainMenu() {
        System.out.println("\nThe tavern keeper looks at you and says:");
        System.out.println("“Welcome adventurer! How can I help you?”\n");
        System.out.println("\t1) Character creation");
        System.out.println("\t2) List characters");
        System.out.println("\t3) Create an adventure");
        System.out.println("\t4) Start an adventure");
        System.out.println("\t5) Exit\n");
    }

    public void showMainMenuStartAdventureDisabled() {
        System.out.println("\nThe tavern keeper looks at you and says:");
        System.out.println("“Welcome adventurer! How can I help you?”\n");
        System.out.println("\t1) Character creation");
        System.out.println("\t2) List characters");
        System.out.println("\t3) Create an adventure");
        System.out.println("\t4) Start an adventure (disabled: create 3 characters first)");
        System.out.println("\t5) Exit\n");
    }

    public void showDicesResults(int bodyDiceSum, int mindDiceSum, int spiritDiceSum, int[] bodyDice, int[] mindDice,
                                 int[] spiritDice) {
        System.out.printf("Body:\tYou rolled %d (%d and %d).\n", bodyDiceSum, bodyDice[0], bodyDice[1]);
        System.out.printf("Mind:\tYou rolled %d (%d and %d).\n", mindDiceSum, mindDice[0], mindDice[1]);
        System.out.printf("Spirit:\tYou rolled %d (%d and %d).\n\n", spiritDiceSum, spiritDice[0], spiritDice[1]);
    }

    public void showStats(int body, int mind, int spirit) {
        System.out.println("Your stats are:");
        System.out.printf("  - Body: %+d\n", body);
        System.out.printf("  - Mind: %+d\n", mind);
        System.out.printf("  - Spirit: %+d\n", spirit);
    }

    public void listCharacters(List<Character> characters) {
        for (int i = 0; i < characters.size(); i++) {
            System.out.println("\t" + (i + 1) + ". " + characters.get(i).getName());
        }

        System.out.println("\n\t0. Back");
    }

    public void listMonstersToAdd(List<Monster> monsters) {
        for (int i = 0; i < monsters.size(); i++) {
            System.out.println("\t" + (i + 1) + ". " + monsters.get(i).getName() + " (" + monsters.get(i).getChallenge() + ")");
        }
    }

    public void showEncounter(Map<Monster, Integer> monstersMap, List<Monster> monsters, int index, int numEncounters) {
        System.out.println("\n\n* Encounter " + index + " / " + numEncounters);
        System.out.println("* Monsters in encounter");

        for (int i = 0; i < monstersMap.size(); i++) {
            System.out.println("\t" + (i + 1) + ". " + monsters.get(i).getName() + " (x" + monstersMap.get(monsters.get(i)) + ")");
        }
    }

    public void showCharacterDetails(Character character) {
        System.out.println("\nTavern keeper: “Hey " + character.getName() + " get here; the boss wants to see you!”\n");

        System.out.printf("* Name:   %s\n", character.getName());
        System.out.printf("* Player: %s\n", character.getPlayer());
        System.out.printf("* Class:  %s\n", character.getCharacterClass());
        System.out.printf("* Level:  %s\n", character.getLevel());
        System.out.printf("* XP:     %d\n", character.getXp());
        System.out.printf("* Body:   %+d\n", character.getBody());
        System.out.printf("* Mind:   %+d\n", character.getMind());
        System.out.printf("* Spirit: %+d\n", character.getSpirit());
    }

    public void showAvailableAdventures(List<String> adventures) {
        System.out.println("Available adventures:");

        for (int i = 0; i < adventures.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + adventures.get(i));
        }

        System.out.println();
    }

    public void showParty(List<String> characterNames, int maxPartySize) {
        final String EMPTY = "Empty";

        System.out.println("\n------------------------------");
        System.out.println("Your party (" + characterNames.size() + " / " + maxPartySize + "):");

        for (int i = 0; i < maxPartySize; i++) {
            String character = (i < characterNames.size()) ? characterNames.get(i) : EMPTY;

            System.out.println("  " + (i + 1) + ". " + character);
        }

        System.out.println("------------------------------");
    }

    public void showAvailableCharacters(List<String> availableCharacters) {
        System.out.println("Available characters:");

        for (int i = 0; i < availableCharacters.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + availableCharacters.get(i));
        }

        System.out.println();
    }

    public void showStartEncounter(int numEncounter, Map<Monster, Integer> monstersMap) {
        final String BOSS = "Boss";

        System.out.println("---------------------");
        System.out.println("Starting Encounter " + numEncounter + ":");

        List<Monster> monsters = new ArrayList<>(monstersMap.keySet());
        for (int i = 0; i < monstersMap.size(); i++) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("  - ");
            stringBuilder.append(monstersMap.get(monsters.get(i)));
            stringBuilder.append("x ");
            stringBuilder.append(monsters.get(i).getName());

            if (monsters.get(i).getChallenge().equals(BOSS)) {
                stringBuilder.append(" (");
                stringBuilder.append(BOSS);
                stringBuilder.append(")");
            }

            System.out.println(stringBuilder);
        }

        System.out.println("---------------------\n\n");
    }

    public void showPreparationStageMessage() {
        String message = """
                -------------------------
                *** Preparation stage ***
                -------------------------""";

        System.out.println(message);
    }

    public void showAction(List<String> actions) {
        for (String action : actions) {
            System.out.println(action);
        }

        System.out.println();
    }

    public void showRollInitiatives(List<String> initiatives) {
        System.out.println("\nRolling initiative...");

        for (String initiative : initiatives) {
            System.out.println(initiative);
        }

        System.out.println("\n");
    }

    public void showCombatStageMessage() {
        String message = """
                --------------------
                *** Combat stage ***
                --------------------""";

        System.out.println(message);
    }

    public void showBattleAction(List<String> actions) {
        for (String action : actions) {
            System.out.println(action);
        }
    }

    public void showBattleRoundAndParty(List<Character> party, int round) {
        System.out.println("Round " + round + ":");

        System.out.println("Party:");

        int maxLength = 0;
        for (Character character : party) {
            if (character.getName().length() > maxLength) {
                maxLength = character.getName().length();
            }
        }

        for (Character character : party) {
            String message = String.format("  - %-" + (maxLength + 4) + "s %s",
                    character.getName(), character.getCurrentHp() + " / " + character.getMaxHp() + " hit points");

            System.out.println(message);
        }

        System.out.println();
    }

    public void showShortRestStageMessage() {
        String message = """
                ------------------------
                *** Short rest stage ***
                ------------------------""";

        System.out.println(message);
    }

    public void showGoodbye() {
        System.out.println("\nTavern keeper: “Are you leaving already? See you soon, adventurer.”");
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public int askForInteger(String message) {
        while (true) {
            try {
                System.out.print(message);
                return scanner.nextInt();

            } catch (InputMismatchException e) {
                System.out.println("Error: This isn't an integer!");

            } finally {
                scanner.nextLine();
            }
        }
    }

    public String askForString(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    public void showCreateEncounterMenu() {
        System.out.println("\n1. Add monster");
        System.out.println("2. Remove monster");
        System.out.println("3. Continue");
    }

    public enum mainMenuOptions {
        CREATE_CHARACTER,
        LIST_CHARACTERS,
        CREATE_ADVENTURE,
        START_ADVENTURE,
        EXIT,
        ERROR
    }

    public mainMenuOptions getSelectedMainMenuOption(int selection) {
        switch (selection) {
            case 1 -> {
                return mainMenuOptions.CREATE_CHARACTER;
            }
            case 2 -> {
                return mainMenuOptions.LIST_CHARACTERS;
            }
            case 3 -> {
                return mainMenuOptions.CREATE_ADVENTURE;
            }
            case 4 -> {
                return mainMenuOptions.START_ADVENTURE;
            }
            case 5 -> {
                return mainMenuOptions.EXIT;
            }
            default -> {
                return mainMenuOptions.ERROR;
            }
        }
    }
}
