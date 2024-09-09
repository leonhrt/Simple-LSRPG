package business.manager.adventure;

import business.entities.Adventure;
import business.entities.monster.Monster;
import business.entities.character.Character;
import business.exceptions.*;
import persistence.exceptions.PersistenceException;

import java.util.List;
import java.util.Map;

/**
 * Interface that provides methods for managing adventures.
 */
public interface AdventureFacade {
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
     * Checks if the adventure name is valid and if it already exists.
     *
     * @param adventureName The name of the adventure to check.
     * @throws PersistenceException             if an error occurs during persistence.
     * @throws AdventureNameAlreadyExistsException if the adventure name already exists.
     */
    void checkValidAdventureName(String adventureName) throws PersistenceException, AdventureNameAlreadyExistsException;

    /**
     * Creates a new adventure with the given parameters.
     *
     * @param adventureName   The name of the adventure.
     * @param numberEncounters The number of encounters in the adventure.
     * @param encounters       The list of encounters, each containing a map of monsters and their quantities.
     * @throws PersistenceException if an error occurs during persistence.
     */
    void createAdventure(String adventureName, int numberEncounters, List<Map<Monster, Integer>> encounters) throws PersistenceException;

    /**
     * Checks if the boss amount in the given monsters map exceeds the maximum allowed amount for the given boss monster.
     *
     * @param monstersMap The map of monsters and their quantities.
     * @param monster     The boss monster to check.
     * @throws BossAmountExceededException if the boss amount exceeds the maximum allowed.
     */
    void checkBossAmount(Map<Monster, Integer> monstersMap, Monster monster) throws BossAmountExceededException;

    /**
     * Checks if the number of bosses to add of the given monster exceeds the maximum allowed.
     *
     * @param monster     The monster to add.
     * @param numMonster  The number of monsters to add.
     * @throws BossesToAddExceededException if the number of monsters to add exceeds the maximum allowed.
     */
    void checkBossAmountToAdd(Monster monster, int numMonster) throws BossesToAddExceededException;

    /**
     * Checks if the encounter contains any monsters.
     *
     * @param monsters The map of monsters and their quantities.
     * @throws EmptyEncounterException if the encounter is empty.
     */
    void checkMonstersInEncounter(Map<Monster, Integer> monsters) throws EmptyEncounterException;

    /**
     * Removes the specified monster from the encounter.
     *
     * @param monsterToRemove The monster to remove from the encounter.
     * @param monsters        The map of monsters and their quantities.
     */
    void removeMonstersFromEncounter(Monster monsterToRemove, Map<Monster, Integer> monsters);

    /**
     * Checks if the number of characters is equal to or greater than the minimum required amount.
     *
     * @param characterAmount The number of characters.
     * @return true if the number of characters meets the minimum requirement, false otherwise.
     * @throws PersistenceException                if an error occurs during persistence.
     * @throws InsufficientCharactersAmountException if the number of characters is insufficient.
     */
    boolean minimumCharactersRequired(int characterAmount) throws PersistenceException, InsufficientCharactersAmountException;

    /**
     * Gets the names of available adventures.
     *
     * @return The list of available adventure names.
     * @throws PersistenceException if an error occurs during persistence.
     */
    List<String> getAvailableAdventuresNames() throws PersistenceException;

    /**
     * Checks if the adventure option to play is valid.
     *
     * @param adventureToPlay The adventure option to play.
     * @throws PersistenceException            if an error occurs during persistence.
     * @throws InvalidAdventureOptionException if the adventure option is invalid.
     */
    void checkAdventureToPlay(int adventureToPlay) throws PersistenceException, InvalidAdventureOptionException;

    /**
     * Gets the name of the adventure at the specified index.
     *
     * @param index The index of the adventure.
     * @return The name of the adventure.
     * @throws PersistenceException if an error occurs during persistence.
     */
    String getAdventureName(int index) throws PersistenceException;

    /**
     * Checks if the number of characters is valid for the adventure.
     *
     * @param numCharacters   The number of characters.
     * @param max_characters The maximum number of characters allowed for the adventure.
     * @throws InvalidCharactersAmountForAdventureException if the number of characters is invalid for the adventure.
     */
    void checkNumberOfCharacters(int numCharacters, int max_characters) throws InvalidCharactersAmountForAdventureException;

    /**
     * Checks if the character to add is valid and not already in the party.
     *
     * @param indexCharacters        The list of indexes of characters in the party.
     * @param characterToAdd         The index of the character to add.
     * @param availableCharacters The number of available characters.
     * @throws CharacterAlreadyInThePartyException      if the character is already in the party.
     * @throws InvalidCharacterToAddOptionException if the character to add is invalid.
     */
    void checkCharacterToAdd(List<Integer> indexCharacters, int characterToAdd, int availableCharacters) throws CharacterAlreadyInThePartyException, InvalidCharacterToAddOptionException;

    /**
     * Sets up the adventure to play with the specified characters.
     *
     * @param adventureToPlay The adventure option to play.
     * @param characters      The list of characters for the adventure.
     * @return The created Adventure object.
     * @throws PersistenceException if an error occurs during persistence.
     */
    Adventure setUpAdventure(int adventureToPlay, List<Character> characters) throws PersistenceException;

    /**
     * Performs the preparation action for the party.
     *
     * @param party                  The list of characters in the party.
     * @param partyStatsIncreased The map of characters and their increased stats.
     * @return The list of strings representing the actions performed during the preparation.
     */
    List<String> doPreparationAction(List<Character> party, Map<Character, Map<String, Integer>> partyStatsIncreased);

    /**
     * Rolls initiatives for the characters and monsters in the encounter.
     *
     * @param party              The list of characters in the party.
     * @param encounterMonsters  The map of monsters in the encounter and their quantities.
     * @param characterInitiatives The list of characters' initiative values.
     * @param monsterInitiatives   The list of monsters' initiative values.
     * @return The list of strings representing the rolled initiatives.
     */
    List<String> rollInitiatives(List<Character> party, Map<Monster, Integer> encounterMonsters,
                                 List<Character> characterInitiatives, List<Monster> monsterInitiatives);

    /**
     * Performs the battle actions for the party and monsters.
     *
     * @param party      The list of characters in the party.
     * @param monsters   The list of monsters in the encounter.
     * @param experience The array to store the gained experience points for each character.
     * @return The list of strings representing the battle actions.
     */
    List<String> doBattleActions(List<Character> party, List<Monster> monsters, int[] experience);

    /**
     * Checks if the entire party is unconscious.
     *
     * @param party The list of characters in the party.
     * @return true if the entire party is unconscious, false otherwise.
     */
    boolean isTotalPartyUnconscious(List<Character> party);

    /**
     * Returns the party's stats to their default values after a battle.
     *
     * @param party                  The list of characters in the party.
     * @param partyStatIncreased The map of characters and their increased stats.
     */
    void returnToDefaultStats(List<Character> party, Map<Character, Map<String, Integer>> partyStatIncreased);

    /**
     * Performs the short rest action for the party.
     *
     * @param party The list of characters in the party.
     * @return The list of strings representing the actions performed during the short rest.
     */
    List<String> doShortRestAction(List<Character> party);

    /**
     * Updates the level and evolves the characters based on the gained experience.
     *
     * @param party      The list of characters in the party.
     * @param experience The total experience gained.
     * @return The list of strings representing the level and evolution updates.
     */
    List<String> updateLevelAndEvolve(List<Character> party, int experience);

}
