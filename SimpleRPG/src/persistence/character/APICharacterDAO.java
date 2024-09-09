package persistence.character;

import business.entities.character.Character;
import business.entities.character.adventurer.Adventurer;
import business.entities.character.adventurer.Champion;
import business.entities.character.adventurer.Warrior;
import business.entities.character.cleric.Cleric;
import business.entities.character.cleric.Paladin;
import business.entities.character.mage.Mage;
import com.google.gson.Gson;
import persistence.ApiHelper;
import persistence.exceptions.APICannotBeAccessedException;
import persistence.exceptions.UnknownCharacterClassException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides methods for retrieving and persisting character data from and to an API.
 */
public class APICharacterDAO implements CharacterDAO {
    private static final String URL = "https://balandrau.salle.url.edu/dpoo/S1-Project_66/characters";
    private static final String ADVENTURER = "Adventurer";
    private static final String WARRIOR = "Warrior";
    private static final String CHAMPION = "Champion";
    private static final String CLERIC = "Cleric";
    private static final String PALADIN = "Paladin";
    private static final String MAGE = "Mage";
    private final ApiHelper apiHelper;
    private final Gson gson;

    /**
     * Constructs an APICharacterDAO and initializes the necessary dependencies.
     *
     * @throws APICannotBeAccessedException If the API cannot be accessed.
     */
    public APICharacterDAO() throws APICannotBeAccessedException {
        try {
            apiHelper = new ApiHelper();
            gson = new Gson();

        } catch (IOException e) {
            throw new APICannotBeAccessedException(e);
        }
    }

    /**
     * Reads all the characters from the API.
     *
     * @return A list of all the characters.
     * @throws APICannotBeAccessedException If the API cannot be accessed.
     */
    @Override
    public List<Character> readAll() throws APICannotBeAccessedException {
        try {
            String characters = apiHelper.getFromUrl(URL);

            Character[] charactersArray = gson.fromJson(characters, Adventurer[].class);

            List<Character> allCharacters = new ArrayList<>();

            for (Character character : charactersArray) {
                try {
                    character = getCharacterClass(character);
                    allCharacters.add(character);

                } catch (UnknownCharacterClassException ignored) {

                }
            }

            return allCharacters;

        } catch (IOException e) {
            throw new APICannotBeAccessedException(e);
        }
    }

    /**
     * Get all the names of the characters from the API.
     *
     * @return A list of all the names of the characters.
     * @throws APICannotBeAccessedException If the API cannot be accessed.
     */
    @Override
    public List<String> getAllNames() throws APICannotBeAccessedException {
        List<Character> allCharacters = readAll();

        List<String> allCharactersName = new ArrayList<>();

        for (Character character : allCharacters) {
            allCharactersName.add(character.getName());
        }

        return allCharactersName;
    }

    /**
     * Saves a character to the API.
     *
     * @param character The character to be saved.
     * @throws APICannotBeAccessedException If an error occurs while saving the character.
     */
    @Override
    public void saveCharacter(Character character) throws APICannotBeAccessedException {
        String body = gson.toJson(character);

        try {
            apiHelper.postToUrl(URL, body);

        } catch (IOException e) {
            throw new APICannotBeAccessedException(e);
        }
    }

    /**
     * Get player characters from the API.
     *
     * @param playerName The name of the player.
     * @return A list of all the characters of the player.
     * @throws APICannotBeAccessedException
     */
    @Override
    public List<Character> getPlayerCharacters(String playerName) throws APICannotBeAccessedException {
        String url = URL + "?player=" + playerName;

        String charactersString;
        List<Character> characters = new ArrayList<>();

        try {
            charactersString = apiHelper.getFromUrl(url);

            Character[] charactersArray = gson.fromJson(charactersString, Adventurer[].class);

            characters.addAll(List.of(charactersArray));

            return characters;

        } catch (IOException e) {
            throw new APICannotBeAccessedException(e);
        }
    }

    /**
     * Deletes a character from the API.
     *
     * @param characterToDelete The character to delete.
     * @throws APICannotBeAccessedException If an error occurs while deleting the character.
     */
    @Override
    public void deleteCharacter(Character characterToDelete) throws APICannotBeAccessedException {
        String url = URL + "?name=" + characterToDelete.getName();
        try {
            apiHelper.deleteFromUrl(url);

        } catch (IOException e) {
            throw new APICannotBeAccessedException(e);
        }
    }

    /**
     * Get the character name by index.
     *
     * @param index The index of the character.
     * @return The name of the character.
     * @throws APICannotBeAccessedException
     */
    @Override
    public String getCharacterNameByIndex(int index) throws APICannotBeAccessedException {
        String url = URL + "/" + index;

        try {
            String characterString = apiHelper.getFromUrl(url);

            Character character = gson.fromJson(characterString, Adventurer.class);

            return character.getName();

        } catch (IOException e) {
            throw new APICannotBeAccessedException(e);
        }
    }

    /**
     * Get characters by index.
     *
     * @param indexCharacters A list of indices of characters to retrieve.
     * @return A list of characters.
     * @throws APICannotBeAccessedException
     */
    @Override
    public List<Character> getCharactersByIndex(List<Integer> indexCharacters) throws APICannotBeAccessedException {
        List<Character> characters = new ArrayList<>();

        for (int index : indexCharacters) {
            String url = URL + "/" + index;

            try {
                String characterString = apiHelper.getFromUrl(url);

                Character character = gson.fromJson(characterString, Adventurer.class);

                characters.add(character);

            } catch (IOException e) {
                throw new APICannotBeAccessedException(e);
            }
        }

        return characters;
    }

    /**
     * Save a list of characters to the API.
     *
     * @param characters The list of characters to be saved.
     * @throws APICannotBeAccessedException If an error occurs while saving the characters.
     */
    @Override
    public void saveCharacters(List<Character> characters) throws APICannotBeAccessedException {
        try {
            for (Character character : characters) {
                String url = URL + "?name=" + character.getName();
                apiHelper.deleteFromUrl(url);

                String charactersString = gson.toJson(character);
                apiHelper.postToUrl(URL, charactersString);
            }

        } catch (IOException e) {
            throw new APICannotBeAccessedException(e);
        }
    }

    /**
     * Get the character class.
     *
     * @param character The character to get the class from.
     * @return The character class.
     * @throws UnknownCharacterClassException If the character class is unknown.
     */
    private Character getCharacterClass(Character character) throws UnknownCharacterClassException {

        return switch (character.getCharacterClass()) {
            case ADVENTURER -> new Adventurer(character);
            case WARRIOR -> new Warrior(character);
            case CHAMPION -> new Champion(character);
            case CLERIC -> new Cleric(character);
            case PALADIN -> new Paladin(character);
            case MAGE -> new Mage(character);
            default -> throw new UnknownCharacterClassException(character.getCharacterClass());
        };
    }
}
