package business.manager.monster;

import business.entities.monster.Monster;
import persistence.exceptions.PersistenceException;

import java.util.List;

/**
 * Interface that provides methods for managing monsters.
 */
public interface MonsterFacade {
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
     * Retrieves a list of all monsters.
     *
     * @return The list of all monsters.
     * @throws PersistenceException if an error occurs during persistence.
     */
    List<Monster> getAllMonsters() throws PersistenceException;

    /**
     * Retrieves the monster at the specified index.
     *
     * @param monsterIndex The index of the monster.
     * @return The monster at the specified index.
     * @throws PersistenceException if an error occurs during persistence.
     */
    Monster getMonster(int monsterIndex) throws PersistenceException;

}
