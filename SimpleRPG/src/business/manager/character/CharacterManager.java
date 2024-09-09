package business.manager.character;

import business.entities.Dice;
import business.entities.character.Character;
import business.entities.character.adventurer.Adventurer;
import business.exceptions.*;
import persistence.character.APICharacterDAO;
import persistence.character.CharacterDAO;
import persistence.character.JSONCharacterDAO;
import persistence.exceptions.PersistenceException;

import java.util.*;

/**
 * This class manages characters and provides methods for retrieving and persisting character data.
 */
public class CharacterManager implements CharacterFacade {
    private CharacterDAO characterDAO;
    private static final String SPACE = " ";
    private static final int MINIMUM_LEVEL = 1;
    private static final int MAXIMUM_LEVEL = 10;
    private static final int EXPERIENCE_PER_LEVEL = 100;
    private static final int INITIAL_EXPERIENCE_OFFSET = 100;
    private static final String ADVENTURER = "Adventurer";
    private static final String WARRIOR = "Warrior";
    private static final String CHAMPION = "Champion";
    private static final String CLERIC = "Cleric";
    private static final String PALADIN = "Paladin";
    private static final String MAGE = "Mage";

    /**
     * Initializes a new instance of the CharacterManager class.
     */
    public CharacterManager() {
    }

    /**
     * Sets up the character DAO to use cloud persistence and reads all characters.
     *
     * @throws PersistenceException If an error occurs during persistence operations.
     */
    @Override
    public void useCloudPersistence() throws PersistenceException {
        characterDAO = new APICharacterDAO();
        characterDAO.readAll();
    }

    /**
     * Sets up the character DAO to use local persistence and reads all characters.
     *
     * @throws PersistenceException If an error occurs during persistence operations.
     */
    @Override
    public void useLocalPersistence() throws PersistenceException {
        characterDAO = new JSONCharacterDAO();
        characterDAO.readAll();
    }

    /**
     * Checks if a character name is valid, not already existing, and formats it if necessary.
     *
     * @param characterName The character name to check.
     * @return The formatted character name.
     * @throws InvalidCharacterNameException      If the character name is invalid.
     * @throws CharacterNameAlreadyExistsException If the character name already exists.
     * @throws PersistenceException               If an error occurs during persistence operations.
     */
    @Override
    public String checkValidCharacterName(String characterName) throws InvalidCharacterNameException,
            CharacterNameAlreadyExistsException, PersistenceException {

        if (!checkValidName(characterName)) {
            throw new InvalidCharacterNameException();
        }

        List<String> allCharactersNames = characterDAO.getAllNames();

        if (allCharactersNames.contains(characterName)) {
            throw new CharacterNameAlreadyExistsException();
        }

        String[] names;

        if (characterName.contains(SPACE)) {
            names = characterName.split(SPACE);

            for (int i = 0; i < names.length; i++) {
                names[i] = formatName(names[i]);
            }

            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < names.length; i++) {
                stringBuilder.append(names[i]);

                if (i < names.length - 1) {
                    stringBuilder.append(SPACE);
                }
            }

            characterName = stringBuilder.toString();

        } else {
            characterName = formatName(characterName);
        }

        return characterName;
    }

    /**
     * Checks if a character level is valid.
     *
     * @param characterLevel The character level to check.
     * @return True if the character level is valid, false otherwise.
     * @throws InvalidCharacterLevelException If the character level is invalid.
     */
    @Override
    public boolean checkValidCharacterLevel(int characterLevel) throws InvalidCharacterLevelException {
        if (characterLevel < MINIMUM_LEVEL || characterLevel > MAXIMUM_LEVEL) {
            throw new InvalidCharacterLevelException();
        }

        return true;
    }

    /**
     * Sets the initial experience of a character based on their level.
     *
     * @param characterLevel The level of the character.
     * @return The initial experience value for the character.
     */
    @Override
    public int setInitialCharacterExperience(int characterLevel) {
        return characterLevel * EXPERIENCE_PER_LEVEL - INITIAL_EXPERIENCE_OFFSET;
    }

    /**
     * Rolls two six-sided dice and returns the results.
     *
     * @return An array containing the results of the dice roll.
     */
    @Override
    public int[] rollTwoDices() {
        Dice dice = new Dice();

        int[] diceResults = new int[2];

        for (int i = 0; i < diceResults.length; i++) {
            diceResults[i] = dice.roll(dice.getD6());
        }

        return diceResults;
    }

    /**
     * Calculates the stat value based on the dice result.
     *
     * @param diceResult The result of the dice roll.
     * @return The calculated stat value.
     */
    @Override
    public int calculateStat(int diceResult) {
        return switch (diceResult) {
            case 2 -> -1;
            case 3, 4, 5 -> 0;
            case 6, 7, 8, 9 -> 1;
            case 10, 11 -> 2;
            case 12 -> 3;
            default -> -2;
        };
    }

    /**
     * Updates the character class based on the character level.
     *
     * @param characterClass The current character class.
     * @param characterLevel The level of the character.
     * @return The updated character class.
     * @throws InvalidCharacterClassException If the character class is invalid.
     */
    @Override
    public String updateClass(String characterClass, int characterLevel) throws InvalidCharacterClassException {
        return switch (characterClass) {
            case ADVENTURER -> switch (characterLevel) {
                case 1, 2, 3 -> ADVENTURER;
                case 4, 5, 6, 7 -> WARRIOR;
                case 8, 9, 10 -> CHAMPION;
                default -> null;
            };
            case CLERIC -> switch (characterLevel) {
                case 1, 2, 3, 4 -> CLERIC;
                case 5, 6, 7, 8, 9, 10 -> PALADIN;
                default -> null;
            };
            case MAGE -> MAGE;
            default -> throw new InvalidCharacterClassException();
        };
    }

    /**
     * Creates a new character with the specified attributes and saves it.
     *
     * @param characterName   The name of the character.
     * @param playerName      The name of the player.
     * @param characterExp    The experience of the character.
     * @param bodyStat        The body stat of the character.
     * @param mindStat        The mind stat of the character.
     * @param spiritStat      The spirit stat of the character.
     * @param characterClass  The class of the character.
     * @throws PersistenceException If an error occurs during persistence operations.
     */
    @Override
    public void createCharacter(String characterName, String playerName, int characterExp, int bodyStat, int mindStat,
                                int spiritStat, String characterClass) throws PersistenceException {

        Character character = new Adventurer(characterName, playerName, characterExp, bodyStat, mindStat, spiritStat,
                characterClass);

        characterDAO.saveCharacter(character);
    }

    /**
     * Searches for characters based on the player name and evolves eligible characters.
     *
     * @param playerName The name of the player to search for characters.
     * @return The list of characters found after evolution.
     * @throws PersistenceException      If an error occurs during persistence operations.
     * @throws NoCharactersFoundException If no characters are found for the player name.
     */
    @Override
    public List<Character> searchCharacters(String playerName) throws PersistenceException, NoCharactersFoundException {
        List<Character> characters;

        if (playerName.length() == 0) {
            characters = characterDAO.readAll();

            if (characters.isEmpty()) {
                throw new NoCharactersFoundException("\nThere are no characters in the system.");
            }
        } else {
            characters = characterDAO.getPlayerCharacters(playerName);

            if (characters.isEmpty()) {
                throw new NoCharactersFoundException("\nThere are no characters for " + playerName + ".");
            }
        }

        for (int i = 0; i < characters.size(); i++) {
            Character character = characters.get(i);
            if (character.canEvolve()) {
                characters.set(i, character.evolve());
            }
        }

        return characters;
    }

    /**
     * Deletes a character from persistence.
     *
     * @param character The character to delete.
     * @throws PersistenceException If an error occurs during persistence operations.
     */
    @Override
    public void deleteCharacter(Character character) throws PersistenceException {
        characterDAO.deleteCharacter(character);
    }

    /**
     * Gets the number of characters stored in persistence.
     *
     * @return The number of characters.
     * @throws PersistenceException If an error occurs during persistence operations.
     */
    @Override
    public int getCharactersAmount() throws PersistenceException {
        return characterDAO.readAll().size();
    }

    /**
     * Gets the names of available characters from persistence.
     *
     * @return The list of available character names.
     * @throws PersistenceException If an error occurs during persistence operations.
     */
    @Override
    public List<String> getAvailableCharacterNames() throws PersistenceException {
        return characterDAO.getAllNames();
    }

    /**
     * Gets the character name at the specified index.
     *
     * @param index The index of the character.
     * @return The name of the character.
     * @throws PersistenceException If an error occurs during persistence operations.
     */
    @Override
    public String getCharacterNameByIndex(int index) throws PersistenceException {
        return characterDAO.getCharacterNameByIndex(index);
    }

    /**
     * Sets up characters for the party based on the specified indices and evolves eligible characters.
     *
     * @param indexCharacters The indices of the characters to set up.
     * @return The list of party characters after evolution and HP setup.
     * @throws PersistenceException If an error occurs during persistence operations.
     */
    @Override
    public List<Character> setUpCharacters(List<Integer> indexCharacters) throws PersistenceException {
        List<Character> characters = characterDAO.getCharactersByIndex(indexCharacters);

        List<Character> party = new ArrayList<>();

        for (Character character : characters) {
            if (character.canEvolve()) {
                character = character.evolve();
            }
            character.setUpHpForAdventure();

            party.add(character);
        }

        return party;
    }

    /**
     * Checks if a name is valid according to the defined rules.
     *
     * @param name The name to check.
     * @return True if the name is valid, false otherwise.
     */
    private boolean checkValidName(String name) {
        if (name == null) {
            return false;
        }

        if (name.length() == 0) {
            return false;
        }

        if (!java.lang.Character.isLetter(name.charAt(0))) {
            return false;
        }

        for (int i = 1; i < name.length(); i++) {
            if (!java.lang.Character.isLetter(name.charAt(i)) && name.charAt(i) != SPACE.charAt(0)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Formats a name by capitalizing the first letter and converting the rest to lowercase.
     *
     * @param name The name to format.
     * @return The formatted name.
     */
    private String formatName(String name) {
        String firstLetter = name.substring(0, 1);
        String remainingLetters = name.substring(1);

        if (!firstLetter.matches(firstLetter.toUpperCase())) {
            firstLetter = firstLetter.toUpperCase();
        }

        if (!remainingLetters.matches(remainingLetters.toLowerCase())) {
            remainingLetters = remainingLetters.toLowerCase();
        }

        name = firstLetter + remainingLetters;

        return name;
    }

    /**
     * Saves the list of characters to persistence.
     *
     * @param characters The list of characters to save.
     * @throws PersistenceException If an error occurs during persistence operations.
     */
    @Override
    public void saveCharacters(List<Character> characters) throws PersistenceException {
        characterDAO.saveCharacters(characters);
    }
}
