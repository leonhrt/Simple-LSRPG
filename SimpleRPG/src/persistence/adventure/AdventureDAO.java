package persistence.adventure;

import business.entities.Adventure;
import persistence.exceptions.APICannotBeAccessedException;
import persistence.exceptions.FileErrorException;
import persistence.exceptions.PersistenceException;

import java.util.List;

/**
 * Interface that provides methods for retrieving and persisting adventure data.
 */
public interface AdventureDAO {
    /**
     * Retrieves all adventures from persistence.
     *
     * @return The list of all adventures.
     * @throws PersistenceException If an error occurs during persistence operations.
     */
    List<Adventure> readAll() throws PersistenceException;

    /**
     * Saves an adventure.
     *
     * @param adventure The adventure to be saved.
     * @throws FileErrorException      If an error occurs while accessing the file.
     * @throws APICannotBeAccessedException If the API cannot be accessed.
     */
    void saveAdventure(Adventure adventure) throws FileErrorException, APICannotBeAccessedException;

    /**
     * Retrieves the names of all available adventures.
     *
     * @return The list of available adventure names.
     * @throws FileErrorException      If an error occurs while accessing the file.
     * @throws APICannotBeAccessedException If the API cannot be accessed.
     */
    List<String> getAvailableAdventuresName() throws FileErrorException, APICannotBeAccessedException;

    /**
     * Retrieves a specific adventure by its index.
     *
     * @param index The index of the adventure.
     * @return The adventure with the specified index.
     * @throws FileErrorException      If an error occurs while accessing the file.
     * @throws APICannotBeAccessedException If the API cannot be accessed.
     */
    Adventure getAdventure(int index) throws FileErrorException, APICannotBeAccessedException;

}
