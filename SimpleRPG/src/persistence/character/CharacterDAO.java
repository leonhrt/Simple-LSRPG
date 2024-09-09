package persistence.character;

import business.entities.character.Character;
import persistence.exceptions.APICannotBeAccessedException;
import persistence.exceptions.FileErrorException;
import persistence.exceptions.PersistenceException;
import persistence.exceptions.UnknownCharacterClassException;

import java.util.List;

/**
 * Interface that provides methods for retrieving and persisting character data.
 */
public interface CharacterDAO {
    /**
     * Reads all characters from the persistence storage.
     * @return A list of all characters.
     * @throws PersistenceException If an error occurs during the read operation.
     */
    List<Character> readAll() throws PersistenceException;

    /**
     * Gets the names of all characters.
     * @return A list of character names.
     * @throws PersistenceException If an error occurs during the read operation.
     */
    List<String> getAllNames() throws PersistenceException;

    /**
     * Saves a character to the persistence storage.
     * @param character The character to be saved.
     * @throws FileErrorException If an error occurs while saving the character.
     * @throws UnknownCharacterClassException If the character class is unknown.
     * @throws APICannotBeAccessedException If the API cannot be accessed.
     */
    void saveCharacter(Character character) throws FileErrorException, UnknownCharacterClassException, APICannotBeAccessedException;

    /**
     * Gets the player characters associated with a given player name.
     * @param playerName The name of the player.
     * @return A list of player characters.
     * @throws FileErrorException If an error occurs during the read operation.
     * @throws APICannotBeAccessedException If the API cannot be accessed.
     */
    List<Character> getPlayerCharacters(String playerName) throws FileErrorException, APICannotBeAccessedException;

    /**
     * Deletes a character from the persistence storage.
     * @param characterToDelete The character to delete.
     * @throws FileErrorException If an error occurs during the delete operation.
     * @throws APICannotBeAccessedException If the API cannot be accessed.
     */
    void deleteCharacter(Character characterToDelete) throws FileErrorException, APICannotBeAccessedException;

    /**
     * Gets the name of a character at a specific index.
     * @param index The index of the character.
     * @return The name of the character.
     * @throws FileErrorException If an error occurs during the read operation.
     * @throws APICannotBeAccessedException If the API cannot be accessed.
     */
    String getCharacterNameByIndex(int index) throws FileErrorException, APICannotBeAccessedException;

    /**
     * Gets a list of characters based on their indices.
     * @param indexCharacters A list of indices of characters to retrieve.
     * @return A list of characters.
     * @throws FileErrorException If an error occurs during the read operation.
     * @throws APICannotBeAccessedException If the API cannot be accessed.
     */
    List<Character> getCharactersByIndex(List<Integer> indexCharacters) throws FileErrorException, APICannotBeAccessedException;

    /**
     * Saves a list of characters to the persistence storage.
     * @param characters The list of characters to be saved.
     * @throws FileErrorException If an error occurs during the save operation.
     * @throws APICannotBeAccessedException If the API cannot be accessed.
     */
    void saveCharacters(List<Character> characters) throws FileErrorException, APICannotBeAccessedException;

}
