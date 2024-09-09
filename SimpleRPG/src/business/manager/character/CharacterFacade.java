package business.manager.character;

import business.entities.character.Character;
import business.exceptions.*;
import persistence.exceptions.PersistenceException;

import java.util.List;

/**
 * Interface that provides methods for managing characters.
 */
public interface CharacterFacade {
    /**
     * Uses cloud persistence for data storage.
     *
     * @throws PersistenceException if an error occurs during persistence.
     */
    void useCloudPersistence() throws PersistenceException;

    /**
     * Uses local persistence for data storage.
     *
     * @throws PersistenceException if an error occurs during persistence.
     */
    void useLocalPersistence() throws PersistenceException;

    /**
     * Checks if the character name is valid and not already taken.
     *
     * @param characterName The name of the character to check.
     * @return The valid character name.
     * @throws InvalidCharacterNameException       if the character name is invalid.
     * @throws CharacterNameAlreadyExistsException if the character name already exists.
     * @throws PersistenceException                if an error occurs during persistence.
     */
    String checkValidCharacterName(String characterName) throws InvalidCharacterNameException,
            CharacterNameAlreadyExistsException, PersistenceException;

    /**
     * Checks if the character level is valid.
     *
     * @param characterLevel The level of the character to check.
     * @return true if the character level is valid, false otherwise.
     * @throws InvalidCharacterLevelException if the character level is invalid.
     */
    boolean checkValidCharacterLevel(int characterLevel) throws InvalidCharacterLevelException;

    /**
     * Sets the initial experience points for a character based on their level.
     *
     * @param characterLevel The level of the character.
     * @return The initial experience points for the character.
     */
    int setInitialCharacterExperience(int characterLevel);

    /**
     * Rolls two dice and returns the result as an array of two integers.
     *
     * @return An array containing the results of the two dice rolls.
     */
    int[] rollTwoDices();

    /**
     * Calculates a character's stat value based on the dice result.
     *
     * @param dicesResult The result of the dice roll.
     * @return The calculated stat value.
     */
    int calculateStat(int dicesResult);

    /**
     * Updates the character's class based on their level.
     *
     * @param characterClass The current class of the character.
     * @param characterLevel The level of the character.
     * @return The updated character class.
     * @throws InvalidCharacterClassException if the character class is invalid.
     */
    String updateClass(String characterClass, int characterLevel) throws InvalidCharacterClassException;

    /**
     * Creates a new character with the given parameters.
     *
     * @param characterName  The name of the character.
     * @param playerName     The name of the player controlling the character.
     * @param characterExp   The experience points of the character.
     * @param bodyStat       The body stat value of the character.
     * @param mindStat       The mind stat value of the character.
     * @param spiritStat     The spirit stat value of the character.
     * @param characterClass The class of the character.
     * @throws PersistenceException if an error occurs during persistence.
     */
    void createCharacter(String characterName, String playerName, int characterExp, int bodyStat, int mindStat,
                         int spiritStat, String characterClass) throws PersistenceException;

    /**
     * Searches for characters based on the player name.
     *
     * @param playerName The name of the player.
     * @return The list of characters belonging to the player.
     * @throws PersistenceException      if an error occurs during persistence.
     * @throws NoCharactersFoundException if no characters are found for the player.
     */
    List<Character> searchCharacters(String playerName) throws PersistenceException, NoCharactersFoundException;

    /**
     * Deletes the specified character.
     *
     * @param character The character to delete.
     * @throws PersistenceException if an error occurs during persistence.
     */
    void deleteCharacter(Character character) throws PersistenceException;

    /**
     * Retrieves the number of characters.
     *
     * @return The number of characters.
     * @throws PersistenceException if an error occurs during persistence.
     */
    int getCharactersAmount() throws PersistenceException;

    /**
     * Gets the names of available characters.
     *
     * @return The list of available character names.
     * @throws PersistenceException if an error occurs during persistence.
     */
    List<String> getAvailableCharacterNames() throws PersistenceException;

    /**
     * Gets the name of the character at the specified index.
     *
     * @param index The index of the character.
     * @return The name of the character.
     * @throws PersistenceException if an error occurs during persistence.
     */
    String getCharacterNameByIndex(int index) throws PersistenceException;

    /**
     * Sets up the characters for gameplay based on the specified indexes.
     *
     * @param indexCharacters The list of indexes of characters to set up.
     * @return The list of characters for gameplay.
     * @throws PersistenceException if an error occurs during persistence.
     */
    List<Character> setUpCharacters(List<Integer> indexCharacters) throws PersistenceException;

    /**
     * Saves the list of characters.
     *
     * @param characters The list of characters to save.
     * @throws PersistenceException if an error occurs during persistence.
     */
    void saveCharacters(List<Character> characters) throws PersistenceException;

}
