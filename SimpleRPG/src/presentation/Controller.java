package presentation;

import business.manager.adventure.AdventureFacade;
import business.manager.character.CharacterFacade;
import business.entities.Adventure;
import business.entities.monster.Monster;
import business.entities.character.Character;
import business.exceptions.BusinessException;
import business.manager.monster.MonsterFacade;
import persistence.exceptions.PersistenceException;

import java.util.*;

/**
 * Class that interactions with the user, controls his inputs and request to the respective manager to do a certain logic.
 */
public class Controller {
    private static final int CLOUD_OPTION = 2;
    private static final int LOCAL_OPTION = 1;
    private static final int ERROR = -1;
    private static final int MIN_REMOVE_INDEX = 1;

    private final CharacterFacade characterManager;
    private final AdventureFacade adventureManager;
    private final MonsterFacade monsterManager;
    private final Menu menu;


    /**
     * Constructor for the Controller class.
     *
     * @param characterManager  an instance of CharacterFacade to manage characters
     * @param adventureManager  an instance of AdventureFacade to manage adventures
     * @param monsterManager    an instance of MonsterFacade to manage monsters
     * @param menu              an instance of Menu to display the menu
     */
    public Controller(CharacterFacade characterManager, AdventureFacade adventureManager,
                      MonsterFacade monsterManager, Menu menu) {

        this.characterManager = characterManager;
        this.adventureManager = adventureManager;
        this.monsterManager = monsterManager;
        this.menu = menu;
    }

    /**
     * Asks the user for the persistence source.
     *
     * @return true if the persistence source is valid, false otherwise
     * @throws PersistenceException if there is an error with the persistence source
     */
    public boolean askPersistenceSource() throws PersistenceException {
        menu.showWelcome();

        menu.showPersistenceSourceMenu();

        int persistenceOption = menu.askForInteger("-> Answer: ");

        return executePersistenceSourceOption(persistenceOption);
    }

    /**
     * Runs the main loop of the Controller class.
     *
     * @throws PersistenceException if there is an error with the persistence source
     */
    public void run() throws PersistenceException {
        boolean stop;

        do {
            try {
                if (adventureManager.minimumCharactersRequired(characterManager.getCharactersAmount())) {
                    menu.showMainMenu();
                }

            } catch (BusinessException e) {
                menu.showMainMenuStartAdventureDisabled();
            }

            int menuOption = menu.askForInteger("Your answer: ");
            stop = executeMenuOption(menuOption);

        } while (!stop);
    }

    /**
     * Executes the persistence source option selected by the user.
     *
     * @param persistenceOption the persistence source option selected by the user
     * @return true if the data was successfully loaded, false otherwise
     * @throws PersistenceException if there is an error with the persistence source
     */
    private boolean executePersistenceSourceOption(int persistenceOption) throws PersistenceException {
        // Try to load the data selected by the user.
        try {
            if (CLOUD_OPTION == persistenceOption) {
                menu.showMessage("\nLoading data...");
                monsterManager.useCloudPersistence();
                characterManager.useCloudPersistence();
                adventureManager.useCloudPersistence();

            } else if (LOCAL_OPTION == persistenceOption) {
                menu.showMessage("\nLoading data...");
                monsterManager.useLocalPersistence();
                characterManager.useLocalPersistence();
                adventureManager.useLocalPersistence();

            } else {
                menu.showMessage("\nError: Option not valid.");
                return false;
            }

            // If there is an error with the persistence, will try to load the local data.
        } catch (PersistenceException e) {
            menu.showMessage("Couldn’t connect to the remote server.");
            menu.showMessage("Reverting to local data.\n");

            menu.showMessage("Loading data...");
            monsterManager.useLocalPersistence();
            characterManager.useLocalPersistence();
            adventureManager.useLocalPersistence();
        }

        menu.showMessage("Data was successfully loaded.");

        return true;
    }

    /**
     * Executes the main menu option selected by the user.
     *
     * @param option the main menu option selected by the user
     * @return true if the user wants to exit, false otherwise
     */
    private boolean executeMenuOption(int option) {
        try {
            switch (menu.getSelectedMainMenuOption(option)) {
                case CREATE_CHARACTER -> createCharacter();
                case LIST_CHARACTERS -> listCharacters();
                case CREATE_ADVENTURE -> createAdventure();
                case START_ADVENTURE -> startAdventure();
                case EXIT -> {
                    menu.showGoodbye();
                    return true;
                }
                default -> menu.showMessage("\nError: Select a valid option.");
            }

        } catch (BusinessException | PersistenceException e) {
            menu.showMessage(e.getMessage());
        }

        return false;
    }

    /**
     * Asks the user for the name of a character.
     *
     * @return the name of the character
     * @throws BusinessException if there is an error with the business logic
     * @throws PersistenceException if there is an error with the persistence source
     */
    private String askForCharacterName() throws BusinessException, PersistenceException {
        menu.showMessage("\nTavern keeper: “Oh, so you are new to this land.”");
        menu.showMessage("“What’s your name?”\n");

        String characterName = menu.askForString("-> Enter your name: ");

        characterName = characterManager.checkValidCharacterName(characterName);

        menu.showMessage("\nTavern keeper: “Hello, " + characterName + ", be welcome.”");

        return characterName;
    }

    /**
     * Asks the user for the name of the player of the character.
     *
     * @return the name of the player
     */
    private String askForPlayerName() {
        menu.showMessage("“And now, if I may break the fourth wall, who is your Player?”\n");

        String playerName = menu.askForString("-> Enter the player’s name: ");

        menu.showMessage("\nTavern keeper: “I see, I see...”");

        return playerName;
    }

    /**
     * Asks the user for the level of a character.
     * Keeps asking the user for the level of the character till introduces a valid level (between 1 and 10, both included).
     *
     * @return the level of the character
     */
    private int askForCharacterLevel() {
        menu.showMessage("“Now, are you an experienced explorer?”\n");

        boolean validLevel = false;
        int characterLevel;

        do {
            characterLevel = menu.askForInteger("-> Enter the character’s level [1..10]: ");

            try {
                validLevel = characterManager.checkValidCharacterLevel(characterLevel);

            } catch (BusinessException e) {
                menu.showMessage(e.getMessage());
            }

        } while (!validLevel);

        menu.showMessage("\nTavern keeper: “Oh, so you are level " + characterLevel + "!”");

        return characterLevel;
    }

    /**
     * Rolls the dices to generate the stats of a character.
     *
     * @return an array with the sum of the body, mind and spirit dices
     */
    private int[] rollDices() {
        menu.showMessage("“Great, let me get a closer look at you...”\n");

        menu.showMessage("Generating your stats...\n");

        int[] bodyDice = characterManager.rollTwoDices();
        int[] mindDice = characterManager.rollTwoDices();
        int[] spiritDice = characterManager.rollTwoDices();

        int bodyDiceSum = Arrays.stream(bodyDice).sum();
        int mindDiceSum = Arrays.stream(mindDice).sum();
        int spiritDiceSum = Arrays.stream(spiritDice).sum();

        menu.showDicesResults(bodyDiceSum, mindDiceSum, spiritDiceSum, bodyDice, mindDice, spiritDice);

        return new int[] {bodyDiceSum, mindDiceSum, spiritDiceSum};
    }

    /**
     * Asks the user for the class of a character.
     *
     * @param characterLevel the level of the character
     * @return the class of the character
     * @throws BusinessException if there is an error with the business logic
     */
    private String askForCharacterClass(int characterLevel) throws BusinessException {
        menu.showMessage("\nTavern keeper: “Looking good!”");
        menu.showMessage("“And, lastly, ?”\n");

        String characterClass;
        characterClass = menu.askForString("-> Enter the character’s initial class [Adventurer, Cleric, Mage]: ");

        characterClass = characterManager.updateClass(characterClass, characterLevel);

        menu.showMessage("\nTavern keeper: “Any decent party needs one of those.”");
        menu.showMessage("“I guess that means you’re a " + characterClass + " by now, nice!”\n");

        return characterClass;
    }

    /**
     * Creates a new character.
     *
     * @throws BusinessException if there is an error with the business logic
     * @throws PersistenceException if there is an error with the persistence source
     */
    private void createCharacter() throws BusinessException, PersistenceException {
        String characterName = askForCharacterName();

        String playerName = askForPlayerName();

        int characterLevel = askForCharacterLevel();

        int characterXp = characterManager.setInitialCharacterExperience(characterLevel);

        int[] diceResults = rollDices();

        int bodyStat = characterManager.calculateStat(diceResults[0]);
        int mindStat = characterManager.calculateStat(diceResults[1]);
        int spiritStat = characterManager.calculateStat(diceResults[2]);

        menu.showStats(bodyStat, mindStat, spiritStat);

        String characterClass = askForCharacterClass(characterLevel);

        characterManager.createCharacter(characterName, playerName, characterXp, bodyStat, mindStat, spiritStat, characterClass);

        menu.showMessage("The new character " + characterName + " has been created.");
    }

    /**
     * Deletes a character.
     * Asks to delete the character (character name, case-sensitive) or not (pressing enter) till the user introduces one of both options.
     *
     * @param character the character to delete
     * @throws PersistenceException if there is an error with the persistence source
     */
    private void deleteCharacter(Character character) throws PersistenceException {
        menu.showCharacterDetails(character);

        menu.showMessage("\n[Enter name to delete, or press enter to cancel]");

        boolean stop = false;
        do {
            String deleteOption = menu.askForString("Do you want to delete " + character.getName() + "? ");

            if (deleteOption.length() == 0) {
                stop = true;

            } else if (deleteOption.equals(character.getName())) {
                characterManager.deleteCharacter(character);

                menu.showMessage("\nTavern keeper: “I’m sorry kiddo, but you have to leave.”\n");
                menu.showMessage("Character " + character.getName() + " left the Guild.");

                stop = true;

            } else {
                menu.showMessage("\nError: Press enter to cancel or type \"" + character.getName()
                        + "\" to delete the character.\n");
            }
        } while (!stop);
    }

    /**
     * Lists the characters.
     * If the character introduced does not exist, shows the pertinent error and cancels the function.
     *
     * @throws PersistenceException if there is an error with the persistence source
     */
    private void listCharacters() throws PersistenceException {
        final int GO_BACK = 0;

        menu.showMessage("\nTavern keeper: “Lads! The Boss wants to see you, come here!”");
        menu.showMessage("“Who piques your interest?”\n");

        String playerName = menu.askForString("-> Enter the name of the Player to filter: ");

        List<Character> characters;

        try {
            characters = characterManager.searchCharacters(playerName);

        } catch (BusinessException e) {
            menu.showMessage(e.getMessage());
            return;
        }

        String message = (playerName.length() == 0)
                ? "\nYou watch as all adventurers get up from their chairs and approach you.\n"
                : "\nYou watch as some adventurers get up from their chairs and approach you.\n";

        menu.showMessage(message);
        menu.listCharacters(characters);

        int characterToDisplay = menu.askForInteger("\nWho would you like to meet [0.." + characters.size() + "]: ");

        if (GO_BACK == characterToDisplay) {
            return;
        }

        Character character = characters.get(characterToDisplay - 1);

        deleteCharacter(character);
    }

    /**
     * Asks the user for the name of an adventure.
     *
     * @return the name of the adventure
     * @throws BusinessException if there is an error with the business logic
     * @throws PersistenceException if there is an error with the persistence source
     */
    private String askForAdventureName() throws BusinessException, PersistenceException {
        menu.showMessage("\nTavern keeper: “Planning an adventure? Good luck with that!”");

        String adventureName = menu.askForString("\n-> Name your adventure: ");

        adventureManager.checkValidAdventureName(adventureName);

        menu.showMessage("\nTavern keeper: “You plan to undertake " + adventureName + ", really?”");

        return adventureName;
    }

    /**
     * Asks the user for the number of encounters in the adventure.
     * The user has 3 opportunities to introduce a valid option of encounters.
     * If the user fails for the third time to introduce a valid option, the functionality will stop.
     *
     * @return the number of encounters in the adventure
     */
    private int askForNumberEncounters() {
        final int MAX_ERRORS = 3;
        final int MIN_ENCOUNTERS = 1;
        final int MAX_ENCOUNTERS = 4;

        int numErrors = 0;

        do {
            menu.showMessage("“How long will that take?”\n");

            int numberEncounters = menu.askForInteger("-> How many encounters do you want [1..4]: ");

            if (numberEncounters < MIN_ENCOUNTERS || numberEncounters > MAX_ENCOUNTERS) {
                numErrors++;
            } else {
                menu.showMessage("\nTavern keeper: “" + numberEncounters + " encounters? That is too much for me...”");
                return numberEncounters;
            }

        } while(numErrors < MAX_ERRORS);

        return ERROR;
    }

    /**
     * Shows the monsters in the encounter.
     * If there are no monster, will show a text with "# Empty".
     * Otherwise, will show the monsters and their quantity.
     *
     * @param monstersMap a map with the monsters and their amount in the encounter
     * @param index the index of the encounter
     * @param numEncounters the total number of encounters in the adventure
     */
    private void showEncounter(Map<Monster, Integer> monstersMap, int index, int numEncounters) {
        if (monstersMap.isEmpty()) {
            menu.showMessage("\t# Empty");
        } else {
            menu.showEncounter(monstersMap, new ArrayList<>(monstersMap.keySet()), index, numEncounters);
        }
    }

    /**
     * Adds a monster to the encounter.
     * Checks if the monster introduced is valid (index in bounds).
     * Checks if there are more than 1 Boss monster.
     *
     * @param monstersMap a map with the monsters and their amount in the encounter
     * @throws PersistenceException if there is an error with the persistence source
     * @throws BusinessException if there is an error with the business logic
     */
    private void addMonsterToEncounter(Map<Monster, Integer> monstersMap) throws PersistenceException, BusinessException {
        List<Monster> allMonsters = monsterManager.getAllMonsters();

        menu.listMonstersToAdd(allMonsters);

        int monsterToAdd = menu.askForInteger("\n-> Choose a monster to add[1.." + monsterManager.getAllMonsters().size() + "]: ");

        if (monsterToAdd >= MIN_REMOVE_INDEX && monsterToAdd < allMonsters.size() + 1) {
            Monster monster = monsterManager.getMonster(monsterToAdd);

            adventureManager.checkBossAmount(monstersMap, monster);

            int numMonster = menu.askForInteger("-> How many " + monster.getName() + "(s) do you want to add: ");

            adventureManager.checkBossAmountToAdd(monster, numMonster);

            monstersMap.merge(monster, numMonster, Integer::sum);

        } else {
            menu.showMessage("\nError: Invalid option.");
        }
    }

    /**
     * Removes a monster from an encounter.
     * Checks if the monster to remove exists (is in bounds).
     *
     * @param monstersMap a map with the monsters and their amount in the encounter
     * @throws BusinessException if there is an error with the business logic
     */
    private void removeMonsterFromEncounter(Map<Monster, Integer> monstersMap) throws BusinessException {
        adventureManager.checkMonstersInEncounter(monstersMap);

        int monsterToRemoveIndex = menu.askForInteger("-> Which monster do you want to delete: ");

        if (monsterToRemoveIndex > MIN_REMOVE_INDEX && monsterToRemoveIndex < monstersMap.size() + 1) {
            List<Monster> monsters = new ArrayList<>(monstersMap.keySet());
            Monster monsterToRemove = monsters.get(monsterToRemoveIndex - 1);
            adventureManager.removeMonstersFromEncounter(monsterToRemove, monstersMap);
        } else {
            menu.showMessage("\nError: Invalid option.");
        }
    }

    /**
     * Creates a new adventure.
     * Asks for the required data (name and encounters) and controls if the user made a mistake.
     *
     * @throws BusinessException if there is an error with the business logic
     * @throws PersistenceException if there is an error with the persistence source
     */
    private void createAdventure() throws BusinessException, PersistenceException {
        final int ADD_MONSTER = 1;
        final int REMOVE_MONSTER = 2;
        final int CONTINUE = 3;

        String adventureName = askForAdventureName();

        int numberEncounters = askForNumberEncounters();
        if (ERROR == numberEncounters) {
            return;
        }

        List<Map<Monster, Integer>> encounters = new ArrayList<>();

        for (int i = 0; i < numberEncounters; i++) {
            boolean stop = false;
            Map<Monster, Integer> monstersMap = new LinkedHashMap<>();

            int index = i + 1;

            do {
                try {
                    showEncounter(monstersMap, index, numberEncounters);

                    menu.showCreateEncounterMenu();

                    int option = menu.askForInteger("\n-> Enter an option [1..3]: ");

                    switch (option) {
                        case ADD_MONSTER -> addMonsterToEncounter(monstersMap);
                        case REMOVE_MONSTER -> removeMonsterFromEncounter(monstersMap);
                        case CONTINUE -> stop = true;
                        default -> menu.showMessage("\nError: Enter an option [1..3]\n");
                    }

                } catch (BusinessException e) {
                    menu.showMessage(e.getMessage());
                }

            } while (!stop);

            encounters.add(monstersMap);
        }

        adventureManager.createAdventure(adventureName, numberEncounters, encounters);

        menu.showMessage("\nTavern keeper: “Great plan lad! I hope you won’t die!”\n");
        menu.showMessage("The new adventure " + adventureName + " has been created.");
    }

    /**
     * Asks the user for the adventure to play till the user introduces a valid adventure.
     *
     * @return the index of the adventure to play
     * @throws PersistenceException if there is an error with the persistence source
     */
    private int askForAdventure() throws PersistenceException {
        int adventureToPlay;

        boolean stop = false;
        do {
            adventureToPlay = menu.askForInteger("-> Choose an adventure: ") - 1;

            try {
                adventureManager.checkAdventureToPlay(adventureToPlay);

                stop = true;

            } catch (BusinessException e) {
                menu.showMessage(e.getMessage());
            }

        } while (!stop);

        return adventureToPlay;
    }

    /**
     * Asks the user for the number of characters to play the adventure.
     *
     * @return the number of characters to play the adventure
     * @throws PersistenceException if there is an error with the persistence source
     */
    private int askForNumberOfCharacters() throws PersistenceException {
        int numCharacters;
        final int MAX_CHARACTERS = 5;

        boolean stop = false;
        do {
            int charactersAmount = characterManager.getCharactersAmount();
            int maxCharacters = Math.min(MAX_CHARACTERS, charactersAmount);
            numCharacters = menu.askForInteger("-> Choose a number of characters [3.." + maxCharacters + "]: ");

            try {
                adventureManager.checkNumberOfCharacters(numCharacters, maxCharacters);

                stop = true;

            } catch (BusinessException e) {
                menu.showMessage(e.getMessage());
            }

        } while (!stop);

        return numCharacters;
    }

    /**
     * Asks the user for the characters to play an adventure.
     *
     * @param indexCharacters a list with the indexes of the characters to play the adventure
     * @param characterNames a list with the names of the characters to play the adventure
     * @param numCharacters the number of characters to play the adventure
     * @throws PersistenceException if there is an error with the persistence source
     */
    private void askForCharacters(List<Integer> indexCharacters, List<String> characterNames, int numCharacters)
            throws PersistenceException {
        boolean stop = false;

        do {
            menu.showParty(characterNames, numCharacters);
            menu.showAvailableCharacters(characterManager.getAvailableCharacterNames());

            boolean valid = false;
            do {
                try {
                    int characterToAdd = menu.askForInteger("-> Choose character "
                            + (indexCharacters.size() + 1) + " in your party: ") - 1;

                    adventureManager.checkCharacterToAdd(indexCharacters, characterToAdd,
                            characterManager.getCharactersAmount());

                    valid = true;

                    indexCharacters.add(characterToAdd);
                    characterNames.add(characterManager.getCharacterNameByIndex(characterToAdd));

                    if (indexCharacters.size() == numCharacters) {
                        stop = true;
                    }

                } catch (BusinessException e) {
                    menu.showMessage(e.getMessage());
                }

            } while (!valid);

        } while (!stop);
    }

    /**
     * Sets up an adventure.
     * If there are no adventures in the system, shows the pertinent message and ends the functionality.
     *
     * @return the adventure to play
     * @throws PersistenceException if there is an error with the persistence source
     * @throws BusinessException if there is an error with the business logic
     */
    private Adventure setUpAdventure() throws PersistenceException, BusinessException {
        adventureManager.minimumCharactersRequired(characterManager.getCharactersAmount());

        menu.showMessage("Tavern keeper: “So, you are looking to go on an adventure?”");
        menu.showMessage("“Where do you fancy going?”\n");

        List<String> adventuresNames = adventureManager.getAvailableAdventuresNames();

        if (!adventuresNames.isEmpty()) {
            menu.showAvailableAdventures(adventuresNames);
        } else {
            menu.showMessage("There are no available adventures yet.");
            return null;
        }

        int adventureToPlay = askForAdventure();

        menu.showMessage("\nTavern keeper: “" + adventureManager.getAdventureName(adventureToPlay) + " it is!”");
        menu.showMessage("“And how many people shall join you?”\n");

        int numCharacters = askForNumberOfCharacters();

        menu.showMessage("Tavern keeper: “Great, " + numCharacters + " it is.”");
        menu.showMessage("“Who among these lads shall join you?”\n\n");

        List<Integer> indexCharacters = new ArrayList<>();
        List<String> characterNames = new ArrayList<>();

        askForCharacters(indexCharacters, characterNames, numCharacters);

        menu.showParty(characterNames, numCharacters);
        menu.showMessage("\nTavern keeper: “Great, good luck on your adventure lads!”\n");

        return adventureManager.setUpAdventure(adventureToPlay, characterManager.setUpCharacters(indexCharacters));
    }

    /**
     * Executes the preparation stage of the encounter.
     *
     * @param adventure the adventure to play
     * @param partyStatIncreased a map with the stats increased of the characters in the party
     * @param party a list with the characters in the party
     * @param monsters a list with the monsters in the encounter
     * @param i the index of the encounter
     */
    private void preparationStage(Adventure adventure, Map<Character, Map<String, Integer>> partyStatIncreased, List<Character> party, List<Monster> monsters, int i) {
        menu.showPreparationStageMessage();
        menu.showAction(adventureManager.doPreparationAction(adventure.getParty(), partyStatIncreased));
        menu.showRollInitiatives(adventureManager.rollInitiatives(adventure.getParty(), adventure.getEncounters().get(i), party, monsters));
    }

    /**
     * Executes the battle stage of the encounter.
     *
     * @param adventure the adventure to play
     * @param party a list with the characters in the party
     * @param monsters a list with the monsters in the encounter
     * @param experience an array with the experience gained by the characters in the party
     */
    private void battleStage(Adventure adventure, List<Character> party, List<Monster> monsters, int[] experience) {
        menu.showCombatStageMessage();
        int round = 1;
        while (!party.isEmpty() && !monsters.isEmpty()) {
            menu.showBattleRoundAndParty(adventure.getParty(), round);
            menu.showBattleAction(adventureManager.doBattleActions(party, monsters, experience));
            menu.showMessage("End of round " + round + ".");
            if (!party.isEmpty() && !monsters.isEmpty()) {
                menu.showMessage("");
            }
            round++;
        }

        if (!party.isEmpty()) {
            menu.showMessage("All enemies area defeated.\n");
        }
    }

    /**
     * Executes the short rest stage of an encounter.
     *
     * @param party a list with the characters in the party
     * @param experience an array with the experience gained by the characters in the party
     */
    private void shortRestStage(List<Character> party, int[] experience) {
        menu.showShortRestStageMessage();
        menu.showAction(adventureManager.doShortRestAction(party));
        menu.showAction(adventureManager.updateLevelAndEvolve(party, experience[0]));
    }

    /**
     * Starts the adventure.
     *
     * @throws BusinessException if there is an error with the business logic
     * @throws PersistenceException if there is an error with the persistence source
     */
    private void startAdventure() throws BusinessException, PersistenceException {
        Adventure adventure = setUpAdventure();

        if (null == adventure) {
            return;
        }

        menu.showMessage("The “" + adventure.getName() + "” will start soon...\n");

        for (int i = 0; i < adventure.getNumberEncounters(); i++) {
            menu.showStartEncounter(i + 1, adventure.getEncounters().get(i));

            List<Character> party = new ArrayList<>();
            List<Monster> monsters = new ArrayList<>();
            Map<Character, Map<String, Integer>> partyStatIncreased = new LinkedHashMap<>();

            // Preparation Stage
            preparationStage(adventure, partyStatIncreased, party, monsters, i);

            // Battle Stage
            int[] experience = new int[1];  // Used to simulate pass by reference. Would use AtomicInteger if there was concurrent programming.
            battleStage(adventure, party, monsters, experience);

            adventureManager.returnToDefaultStats(adventure.getParty(), partyStatIncreased);

            if (adventureManager.isTotalPartyUnconscious(party)) {
                menu.showMessage("\n\nTavern keeper: “Lad, wake up. Yes, your party fell unconscious.”");
                menu.showMessage("“Don’t worry, you are safe back at the Tavern.”");
                return;
            }

            // Short Rest Stage
            shortRestStage(adventure.getParty(), experience);
        }
        characterManager.saveCharacters(adventure.getParty());

        menu.showMessage("\nCongratulations, your party completed “" + adventure.getName() + "”");
    }
}
