package persistence.adventure;

import business.entities.Adventure;
import business.entities.monster.Monster;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import persistence.exceptions.FileErrorException;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.*;

/**
 * This class provides methods for retrieving adventure data from and to a JSON file.
 */
public class JSONAdventureDAO implements AdventureDAO {
    private final Gson gson;

    private static final String FILE_PATH = "data/adventures.json";
    private static final String FILE_NAME = "adventures.json";

    /**
     * Constructs a new JSONAdventureDAO instance. Initializes Gson and the type adapter for the Monster map.
     */
    public JSONAdventureDAO() {
        Type mapType = new TypeToken<Map<Monster, Integer>>() {}.getType();
        this.gson = new GsonBuilder().registerTypeAdapter(mapType, new MonsterMapAdapter()).setPrettyPrinting().create();
    }

    /**
     * Saves the given adventure by adding it to the list of all adventures and writing the updated list to a JSON file.
     *
     * @param adventure The adventure to be saved.
     * @throws FileErrorException If an error occurs while accessing or writing to the file.
     */
    @Override
    public void saveAdventure(Adventure adventure) throws FileErrorException {
        List<Adventure> allAdventures = readAll();

        allAdventures.add(adventure);

        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(allAdventures, writer);

        } catch (Exception e) {
            throw new FileErrorException(FILE_NAME, e);
        }

    }

    /**
     * Reads all adventures from the JSON file and returns them as a list.
     *
     * @return The list of all adventures.
     * @throws FileErrorException If an error occurs while accessing the file.
     */
    @Override
    public List<Adventure> readAll() throws FileErrorException {
        try (FileReader reader = new FileReader(FILE_PATH)) {

            Adventure[] allAdventures = gson.fromJson(reader, Adventure[].class);

            List<Adventure> adventures = new ArrayList<>();

            adventures.addAll(List.of(allAdventures));

            return adventures;

        } catch (NullPointerException e) {
            return new ArrayList<>();

        } catch (Exception noFile) {
            try (FileWriter ignored = new FileWriter(FILE_PATH)) {
                return new ArrayList<>();

            } catch (Exception e) {
                throw new FileErrorException(FILE_NAME, e);
            }
        }
    }

    /**
     * Retrieves the names of all available adventures.
     * @return The list of adventure names.
     * @throws FileErrorException If an error occurs while accessing the file.
     */
    @Override
    public List<String> getAvailableAdventuresName() throws FileErrorException {
        List<Adventure> allAdventures = readAll();

        List<String> adventureNames = new ArrayList<>();

        for (Adventure adventure : allAdventures) {
            adventureNames.add(adventure.getName());
        }

        return adventureNames;
    }

    /**
     * Retrieves the adventure at the specified index from the list of all adventures.
     * @param index The index of the adventure to retrieve.
     * @return The adventure at the specified index.
     * @throws FileErrorException If an error occurs while accessing the file or the index is out of bounds.
     */
    @Override
    public Adventure getAdventure(int index) throws FileErrorException {
        List<Adventure> allAdventures = readAll();

        return allAdventures.get(index);
    }
}