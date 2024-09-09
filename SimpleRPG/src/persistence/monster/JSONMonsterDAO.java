package persistence.monster;

import business.entities.monster.Monster;
import business.entities.monster.StandardMonster;
import com.google.gson.Gson;
import persistence.exceptions.FileErrorException;
import persistence.exceptions.PersistenceException;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides methods for retrieving monster data from a JSON file.
 */
public class JSONMonsterDAO implements MonsterDAO{
    private Gson gson;
    private static final String FILE_PATH = "data/monsters.json";
    private static final String FILE_NAME = "monsters.json";
    private static final String BOSS = "Boss";
    private static final String LIEUTENANT = "Lieutenant";
    private static final String MINION = "Minion";

    public JSONMonsterDAO() {
        this.gson = new Gson();
    }

    /**
     * Reads all the monsters from the data source
     *
     * @return a list of monsters
     * @throws FileErrorException if there is an error reading the data source
     */
    @Override
    public List<Monster> readAll() throws FileErrorException {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Monster[] allMonsters = gson.fromJson(reader, StandardMonster[].class);

            List<Monster> monsters = new ArrayList<>();

            for (Monster monster : allMonsters) {
                if (validMonsterChallenge(monster)) {
                    monsters.add(monster);
                }
            }

            return monsters;

        } catch (NullPointerException e) {
            return new ArrayList<>();

        } catch (Exception noFile) {
            throw new FileErrorException(FILE_NAME, noFile);
        }
    }

    /**
     * Retrieves a specific monster by its index from the file.
     *
     * @param monsterIndex The index of the monster.
     * @return The monster with the specified index.
     * @throws FileErrorException If an error occurs while accessing the file.
     */
    @Override
    public Monster getMonster(int monsterIndex) throws FileErrorException {
        List<Monster> monsters = readAll();

        return monsters.get(monsterIndex);
    }

    /**
     * Checks if the monster is valid
     *
     * @param monster the monster to check
     * @return true if the monster is valid, false otherwise
     */
    private boolean validMonsterChallenge(Monster monster) {
        return switch (monster.getChallenge()) {
            case BOSS, LIEUTENANT, MINION -> true;
            default -> false;
        };
    }
}
