package persistence.character;

import business.entities.character.Character;
import business.entities.character.adventurer.Adventurer;
import business.entities.character.adventurer.Champion;
import business.entities.character.adventurer.Warrior;
import business.entities.character.cleric.Cleric;
import business.entities.character.cleric.Paladin;
import business.entities.character.mage.Mage;
import com.google.gson.*;
import persistence.exceptions.FileErrorException;
import persistence.exceptions.UnknownCharacterClassException;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

/**
 * This class provides methods for retrieving character data from and to a JSON file.
 */
public class JSONCharacterDAO implements CharacterDAO {
    private final Gson gson;
    private static final String FILE_PATH = "data/characters.json";
    private static final String FILE_NAME = "characters.json";
    private static final String ADVENTURER = "Adventurer";
    private static final String WARRIOR = "Warrior";
    private static final String CHAMPION = "Champion";
    private static final String CLERIC = "Cleric";
    private static final String PALADIN = "Paladin";
    private static final String MAGE = "Mage";

    /**
     * Constructs a new instance of JSONCharacterDAO.
     */
    public JSONCharacterDAO() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Reads all characters from the file.
     *
     * @return A list of all characters read from the file.
     * @throws FileErrorException If an error occurs while reading the file.
     */
    @Override
    public List<Character> readAll() throws FileErrorException {
        try (FileReader reader = new FileReader(FILE_PATH)) {

            Character[] allCharacters = gson.fromJson(reader, Adventurer[].class);

            List<Character> characters = new ArrayList<>();

            for (Character character : allCharacters) {
                try {
                    character = getCharacterClass(character);
                    characters.add(character);

                } catch (UnknownCharacterClassException ignored) {

                }
            }

            return characters;

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
     * Gets the names of all characters.
     *
     * @return A list of character names.
     * @throws FileErrorException If an error occurs while reading the file.
     */
    @Override
    public List<String> getAllNames() throws FileErrorException {
        List<Character> allCharacters = readAll();

        List<String> allCharactersName = new ArrayList<>();

        for (Character character : allCharacters) {
            allCharactersName.add(character.getName());
        }

        return allCharactersName;
    }

    /**
     * Saves a character to the file.
     *
     * @param character The character to be saved.
     * @throws FileErrorException If an error occurs while saving the character.
     */
    @Override
    public void saveCharacter(Character character) throws FileErrorException {
        List<Character> allCharacters = readAll();

        allCharacters.add(character);

        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(allCharacters, writer);

        } catch (Exception e) {
            throw new FileErrorException(FILE_NAME, e);
        }
    }

    /**
     * Gets the player characters associated with a given player name.
     *
     * @param playerName The name of the player.
     * @return A list of player characters.
     * @throws FileErrorException If an error occurs while reading the file.
     */
    @Override
    public List<Character> getPlayerCharacters(String playerName) throws FileErrorException {
        String playerNameLowerCase = playerName.toLowerCase();

        List<Character> allCharacters = readAll();

        List<Character> characters = new ArrayList<>();

        for (Character character : allCharacters) {
            if (character.getName() != null) {
                String characterPlayer = character.getPlayer().toLowerCase();

                if (characterPlayer.contains(playerNameLowerCase)) {
                    characters.add(character);
                }
            }
        }

        return characters;
    }

    /**
     * Deletes a character from the file.
     *
     * @param characterToDelete The character to delete.
     * @throws FileErrorException If an error occurs while deleting the character.
     */
    @Override
    public void deleteCharacter(Character characterToDelete) throws FileErrorException {
        List<Character> allCharacters = readAll();

        for (Character character : allCharacters) {
            if (character.getName().equals(characterToDelete.getName())) {
                try (FileWriter writer = new FileWriter(FILE_PATH)) {
                    allCharacters.remove(character);
                    gson.toJson(allCharacters, writer);

                } catch (Exception e) {
                    throw new FileErrorException(FILE_NAME, e);
                }

                break;
            }
        }
    }

    /**
     * Gets the name of a character at a specific index.
     *
     * @param index The index of the character.
     * @return The name of the character.
     * @throws FileErrorException If an error occurs while reading the file.
     */
    @Override
    public String getCharacterNameByIndex(int index) throws FileErrorException {
        List<Character> allCharacters = readAll();

        return allCharacters.get(index).getName();
    }

    /**
     * Gets a list of characters based on their indices.
     *
     * @param indexCharacters A list of indices of characters to retrieve.
     * @return A list of characters.
     * @throws FileErrorException If an error occurs while reading the file.
     */
    @Override
    public List<Character> getCharactersByIndex(List<Integer> indexCharacters) throws FileErrorException {
        List<Character> allCharacters = readAll();

        List<Character> characters = new ArrayList<>();

        for (Integer indexCharacter : indexCharacters) {
            characters.add(allCharacters.get(indexCharacter));
        }

        return characters;
    }

    /**
     * Determines the class of a character based on its character class field.
     *
     * @param character The character to determine the class for.
     * @return The character instance with the appropriate class.
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

    /**
     * Saves a list of characters to the file.
     *
     * @param characters The list of characters to be saved.
     * @throws FileErrorException If an error occurs while saving the characters.
     */
    @Override
    public void saveCharacters(List<Character> characters) throws FileErrorException {
        List<Character> allCharacters = readAll();

        for (int i = 0; i < allCharacters.size(); i++) {
            for (Character character : characters) {
                if (allCharacters.get(i).getName().equals(character.getName())) {
                    allCharacters.remove(allCharacters.get(i));
                }
            }
        }

        allCharacters.addAll(characters);

        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(allCharacters, writer);

        } catch (Exception e) {
            throw new FileErrorException(FILE_NAME, e);
        }
    }
}
