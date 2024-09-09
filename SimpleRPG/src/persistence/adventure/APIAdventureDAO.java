package persistence.adventure;

import business.entities.Adventure;
import business.entities.monster.Monster;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import persistence.ApiHelper;
import persistence.exceptions.APICannotBeAccessedException;
import persistence.exceptions.FileErrorException;
import persistence.exceptions.PersistenceException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class provides methods for retrieving and persisting adventure data from and to an API.
 */
public class APIAdventureDAO implements AdventureDAO {
    private static final String URL = "https://balandrau.salle.url.edu/dpoo/S1-Project_66/adventures";
    private final ApiHelper apiHelper;
    private final Gson gson;


    /**
     * Constructs a new APIAdventureDAO instance. Initializes the API helper, Gson, and the type adapter for the Monster map.
     *
     * @throws APICannotBeAccessedException If the API cannot be accessed.
     */
    public APIAdventureDAO() throws APICannotBeAccessedException {
        try {
            apiHelper = new ApiHelper();
            Type mapType = new TypeToken<Map<Monster, Integer>>() {}.getType();
            gson = new GsonBuilder().registerTypeAdapter(mapType, new MonsterMapAdapter()).create();

        } catch (IOException e) {
            throw new APICannotBeAccessedException(e);
        }
    }

    /**
     * Retrieves all adventures from persistence.
     *
     * @return The list of all adventures.
     * @throws PersistenceException If an error occurs during persistence operations.
     */
    @Override
    public List<Adventure> readAll() throws APICannotBeAccessedException {
        try {
            String adventures = apiHelper.getFromUrl(URL);

            Adventure[] adventuresArray = gson.fromJson(adventures, Adventure[].class);

            List<Adventure> allAdventures = new ArrayList<>();

            allAdventures.addAll(List.of(adventuresArray));

            return allAdventures;

        } catch (IOException e) {
            throw new APICannotBeAccessedException(e);
        }
    }

    /**
     * Saves an adventure.
     *
     * @param adventure The adventure to be saved.
     * @throws FileErrorException      If an error occurs while accessing the file.
     * @throws APICannotBeAccessedException If the API cannot be accessed.
     */
    @Override
    public void saveAdventure(Adventure adventure) throws APICannotBeAccessedException {
        String body = gson.toJson(adventure);

        try {
            apiHelper.postToUrl(URL, body);

        } catch (IOException e) {
            throw new APICannotBeAccessedException(e);
        }
    }

    /**
     * Retrieves the names of all available adventures.
     *
     * @return The list of available adventure names.
     * @throws FileErrorException      If an error occurs while accessing the file.
     * @throws APICannotBeAccessedException If the API cannot be accessed.
     */
    @Override
    public List<String> getAvailableAdventuresName() throws APICannotBeAccessedException {
        List<Adventure> adventures = readAll();

        List<String> adventureNames = new ArrayList<>();

        for (Adventure adventure : adventures) {
            adventureNames.add(adventure.getName());
        }

        return adventureNames;
    }

    /**
     * Retrieves a specific adventure by its index.
     *
     * @param index The index of the adventure.
     * @return The adventure with the specified index.
     * @throws FileErrorException      If an error occurs while accessing the file.
     * @throws APICannotBeAccessedException If the API cannot be accessed.
     */
    @Override
    public Adventure getAdventure(int index) throws APICannotBeAccessedException {
        String url = URL + "/" + index;
        try {
            String adventureString = apiHelper.getFromUrl(url);

            return gson.fromJson(adventureString, Adventure.class);

        } catch (IOException e) {
            throw new APICannotBeAccessedException(e);
        }
    }
}
