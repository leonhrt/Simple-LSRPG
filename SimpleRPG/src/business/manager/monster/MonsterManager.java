package business.manager.monster;

import business.entities.monster.Monster;
import persistence.exceptions.PersistenceException;
import persistence.monster.APIMonsterDAO;
import persistence.monster.JSONMonsterDAO;
import persistence.monster.MonsterDAO;

import java.util.List;

/**
 * This class manages monsters and provides methods for retrieving monster data.
 */
public class MonsterManager implements MonsterFacade {
    private MonsterDAO monsterDAO;

    /**
     * Initializes a new instance of the MonsterManager class.
     */
    public MonsterManager() {
    }

    /**
     * Sets up the monster DAO to use cloud persistence and reads all monsters.
     *
     * @throws PersistenceException If an error occurs during persistence operations.
     */
    @Override
    public void useCloudPersistence() throws PersistenceException {
        monsterDAO = new APIMonsterDAO();
        monsterDAO.readAll();
    }

    /**
     * Sets up the monster DAO to use local persistence and reads all monsters.
     *
     * @throws PersistenceException If an error occurs during persistence operations.
     */
    @Override
    public void useLocalPersistence() throws PersistenceException {
        monsterDAO = new JSONMonsterDAO();
        monsterDAO.readAll();
    }

    /**
     * Retrieves all monsters from persistence.
     *
     * @return The list of all monsters.
     * @throws PersistenceException If an error occurs during persistence operations.
     */
    @Override
    public List<Monster> getAllMonsters() throws PersistenceException {
        return monsterDAO.readAll();
    }

    /**
     * Retrieves a specific monster by its index.
     *
     * @param monsterIndex The index of the monster.
     * @return The monster with the specified index.
     * @throws PersistenceException If an error occurs during persistence operations.
     */
    @Override
    public Monster getMonster(int monsterIndex) throws PersistenceException {
        return monsterDAO.getMonster(monsterIndex - 1);
    }
}
