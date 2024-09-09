package persistence.monster;

import business.entities.monster.Monster;
import business.entities.monster.StandardMonster;
import com.google.gson.Gson;
import persistence.ApiHelper;
import persistence.exceptions.APICannotBeAccessedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides methods for retrieving monster data from an API.
 */
public class APIMonsterDAO implements MonsterDAO {
    private static final String URL = "https://balandrau.salle.url.edu/dpoo/shared/monsters";
    private static final String BOSS = "Boss";
    private static final String LIEUTENANT = "Lieutenant";
    private static final String MINION = "Minion";
    private final ApiHelper apiHelper;
    private final Gson gson;


    /**
     * Creates a new ApiMonsterDAO and initializes the ApiHelper and Gson
     *
     * @throws APICannotBeAccessedException if there is an error initializing the ApiHelper
     */
    public APIMonsterDAO() throws APICannotBeAccessedException {
        try {
            apiHelper = new ApiHelper();
            gson = new Gson();
        } catch (IOException e) {
            throw new APICannotBeAccessedException(e);
        }
    }

    /**
     * Reads all the monsters from the API
     *
     * @return a list of monsters
     * @throws APICannotBeAccessedException if there is an error reading the API
     */
    @Override
    public List<Monster> readAll() throws APICannotBeAccessedException {
        try {
            String monsters = apiHelper.getFromUrl(URL);

            Monster[] monstersArray = gson.fromJson(monsters, StandardMonster[].class);

            List<Monster> allMonsters = new ArrayList<>();

            for (Monster monster : monstersArray) {
                if (validMonsterChallenge(monster)) {
                    allMonsters.add(monster);
                }
            }

            return allMonsters;

        } catch (IOException e) {
            throw new APICannotBeAccessedException(e);
        }
    }

    /**
     * Retrieves a specific monster by its index from the API.
     *
     * @param monsterIndex The index of the monster.
     * @return The monster with the specified index.
     * @throws APICannotBeAccessedException If an error occurs while accessing the API.
     */
    @Override
    public Monster getMonster(int monsterIndex) throws APICannotBeAccessedException {
        String url = URL + "/" + monsterIndex;

        try {
            String monsterString = apiHelper.getFromUrl(url);

            return gson.fromJson(monsterString, StandardMonster.class);

        } catch (IOException e) {
            throw new APICannotBeAccessedException(e);
        }
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
