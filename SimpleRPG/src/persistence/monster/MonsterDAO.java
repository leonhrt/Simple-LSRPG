package persistence.monster;

import business.entities.monster.Monster;
import persistence.exceptions.PersistenceException;

import java.util.List;

/**
 * Interface that provides methods for retrieving and persisting adventure data.
 */
public interface MonsterDAO {
    /**
     * Reads all the monsters from the data source
     *
     * @return a list of monsters
     * @throws PersistenceException if there is an error reading the data source
     */
    List<Monster> readAll() throws PersistenceException;

    /**
     * Retrieves a specific monster by its index from the data source.
     *
     * @param monsterIndex The index of the monster.
     * @return The monster with the specified index.
     * @throws PersistenceException if there is an error reading the data source.
     */
    Monster getMonster(int monsterIndex) throws PersistenceException;
}
