package business.manager.adventure;

import business.entities.Adventure;
import business.entities.Dice;
import business.entities.character.cleric.Paladin;
import business.entities.monster.Boss;
import business.entities.monster.Monster;
import business.entities.character.Character;
import business.entities.monster.StandardMonster;
import business.exceptions.*;
import persistence.adventure.APIAdventureDAO;
import persistence.adventure.AdventureDAO;
import persistence.adventure.JSONAdventureDAO;
import persistence.exceptions.PersistenceException;

import java.util.*;

/**
 * This class manages adventures and provides methods for retrieving and persisting adventure data.
 */
public class AdventureManager implements AdventureFacade{
    private AdventureDAO adventureDAO;
    private static final String BOSS_MONSTER = "Boss";
    private static final String MAGICAL_DAMAGE = "magical";
    private static final String MAGE = "Mage";
    private static final int MINIMUM_INDEX = 0;
    private static final int MINIMUM_CHARACTERS = 3;

    /**
     * Creates a new instance of AdventureManager.
     */
    public AdventureManager() {
    }

    /**
     * Sets up the AdventureManager to use cloud persistence.
     *
     * @throws PersistenceException if an error occurs during the persistence operation.
     */
    @Override
    public void useCloudPersistence() throws PersistenceException {
        adventureDAO = new APIAdventureDAO();
        adventureDAO.readAll();
    }

    /**
     * Sets up the AdventureManager to use local persistence.
     *
     * @throws PersistenceException if an error occurs during the persistence operation.
     */
    @Override
    public void useLocalPersistence() throws PersistenceException {
        adventureDAO = new JSONAdventureDAO();
        adventureDAO.readAll();
    }

    /**
     * Checks if the adventure name is valid and does not already exist.
     *
     * @param adventureName The name of the adventure to check.
     * @throws PersistenceException              if an error occurs during the persistence operation.
     * @throws AdventureNameAlreadyExistsException if the adventure name already exists.
     */
    @Override
    public void checkValidAdventureName(String adventureName) throws PersistenceException, AdventureNameAlreadyExistsException {
        List<Adventure> allAdventures = adventureDAO.readAll();

        if (allAdventures.isEmpty()) {
            return;
        }

        for (Adventure adventure : allAdventures) {
            if (adventure.getName().equals(adventureName)) {
                throw new AdventureNameAlreadyExistsException();
            }
        }
    }

    /**
     * Creates a new adventure with the specified name, number of encounters, and encounters list.
     *
     * @param adventureName     The name of the adventure.
     * @param numberEncounters  The number of encounters in the adventure.
     * @param encounters        The list of encounters.
     * @throws PersistenceException if an error occurs during the persistence operation.
     */
    @Override
    public void createAdventure(String adventureName, int numberEncounters, List<Map<Monster, Integer>> encounters) throws PersistenceException {

        Adventure adventure = new Adventure(adventureName, numberEncounters, encounters);
        adventureDAO.saveAdventure(adventure);
    }

    /**
     * Checks if adding the given monster to the encounter will exceed the allowed number of boss monsters.
     *
     * @param monstersMap The map of monsters in the encounter.
     * @param monster     The monster to be added.
     * @throws BossAmountExceededException if adding the monster exceeds the allowed number of boss monsters.
     */
    @Override
    public void checkBossAmount(Map<Monster, Integer> monstersMap, Monster monster) throws BossAmountExceededException {
        List<Monster> monsters = new ArrayList<>(monstersMap.keySet());

        for (Monster value : monsters) {
            if (value.getChallenge().equals(BOSS_MONSTER) && monster.getChallenge().equals(BOSS_MONSTER)) {
                throw new BossAmountExceededException("\nError: There is already a Boss in the encounter.");
            }
        }
    }

    /**
     * Checks if adding the given boss monster exceeds the allowed number of bosses.
     *
     * @param monster     The boss monster to be added.
     * @param numMonster  The number of boss monsters to add.
     * @throws BossesToAddExceededException if adding the boss monster exceeds the allowed number of bosses.
     */
    @Override
    public void checkBossAmountToAdd(Monster monster, int numMonster) throws BossesToAddExceededException {
        if (monster.getChallenge().equals(BOSS_MONSTER) && numMonster > 1) {
            throw new BossesToAddExceededException("\nError: You can only add one Boss to the encounter.");
        }
    }

    /**
     * Checks if there are any monsters in the encounter.
     *
     * @param monsters The map of monsters in the encounter.
     * @throws EmptyEncounterException if there are no monsters in the encounter.
     */
    @Override
    public void checkMonstersInEncounter(Map<Monster, Integer> monsters) throws EmptyEncounterException {
        if (monsters.isEmpty()) {
            throw new EmptyEncounterException("\nError: There are no monsters in the encounter.");
        }
    }

    /**
     * Removes a monster from the encounter.
     *
     * @param monsterToRemove The monster to remove from the encounter.
     * @param monsters        The map of monsters in the encounter.
     */
    @Override
    public void removeMonstersFromEncounter(Monster monsterToRemove, Map<Monster, Integer> monsters) {
        monsters.remove(monsterToRemove);
    }

    /**
     * Checks if the given character amount meets the minimum requirement.
     *
     * @param characterAmount the character amount to check
     * @return true if the character amount meets the minimum requirement
     * @throws InsufficientCharactersAmountException if the character amount is less than the minimum requirement
     */
    @Override
    public boolean minimumCharactersRequired(int characterAmount) throws InsufficientCharactersAmountException {
        if (characterAmount < MINIMUM_CHARACTERS) {
            throw new InsufficientCharactersAmountException();
        }

        return true;
    }

    /**
     * Retrieves the names of the available adventures.
     *
     * @return a list of available adventure names
     * @throws PersistenceException if there is an error retrieving the adventure names from the persistence layer
     */
    @Override
    public List<String> getAvailableAdventuresNames() throws PersistenceException {
        return adventureDAO.getAvailableAdventuresName();
    }

    /**
     * Checks if the provided adventure option is valid.
     *
     * @param adventureToPlay the adventure option to check
     * @throws PersistenceException if there is an error retrieving the available adventures from the persistence layer
     * @throws InvalidAdventureOptionException if the adventure option is invalid
     */
    @Override
    public void checkAdventureToPlay(int adventureToPlay) throws PersistenceException, InvalidAdventureOptionException {
        List<String> allAdventures = getAvailableAdventuresNames();

        if (adventureToPlay < MINIMUM_INDEX || adventureToPlay > allAdventures.size() - 1) {
            throw new InvalidAdventureOptionException();
        }
    }

    /**
     * Retrieves the adventure at the specified index.
     *
     * @param index the index of the adventure to retrieve
     * @return the adventure at the specified index
     * @throws PersistenceException if there is an error retrieving the adventure from the persistence layer
     */

    private Adventure getAdventure(int index) throws PersistenceException {
        return adventureDAO.getAdventure(index);
    }

    /**
     * Retrieves the name of the adventure at the specified index.
     *
     * @param index the index of the adventure
     * @return the name of the adventure at the specified index
     * @throws PersistenceException if there is an error retrieving the adventure from the persistence layer
     */
    @Override
    public String getAdventureName(int index) throws PersistenceException {
        return getAdventure(index).getName();
    }

    /**
     * Checks if the number of characters is within the valid range for an adventure.
     *
     * @param numCharacters the number of characters
     * @param max_characters the maximum number of characters allowed
     * @throws InvalidCharactersAmountForAdventureException if the number of characters is outside the valid range
     */
    @Override
    public void checkNumberOfCharacters(int numCharacters, int max_characters) throws InvalidCharactersAmountForAdventureException {
        if (numCharacters < MINIMUM_CHARACTERS || numCharacters > max_characters) {
            throw new InvalidCharactersAmountForAdventureException();
        }
    }

    /**
     * Checks if the character to add is valid for the party.
     *
     * @param indexCharacters the indices of the characters in the party
     * @param characterToAdd the index of the character to add
     * @param availableCharacters the total number of available characters
     * @throws CharacterAlreadyInThePartyException if the character is already in the party
     * @throws InvalidCharacterToAddOptionException if the character to add is invalid
     */
    @Override
    public void checkCharacterToAdd(List<Integer> indexCharacters, int characterToAdd, int availableCharacters) throws CharacterAlreadyInThePartyException, InvalidCharacterToAddOptionException {
        if (characterToAdd < MINIMUM_INDEX || characterToAdd > availableCharacters - 1) {
            throw new InvalidCharacterToAddOptionException();
        }

        if (indexCharacters.contains(characterToAdd)) {
            throw new CharacterAlreadyInThePartyException();
        }
    }

    /**
     * Sets up the adventure by associating the specified characters with it.
     *
     * @param adventureToPlay the adventure option to play
     * @param characters the list of characters to associate with the adventure
     * @return the adventure with the associated characters
     * @throws PersistenceException if there is an error retrieving the adventure from the persistence layer
     */
    @Override
    public Adventure setUpAdventure(int adventureToPlay, List<Character> characters) throws PersistenceException {
        Adventure adventure = adventureDAO.getAdventure(adventureToPlay);

        adventure.setParty(characters);

        return adventure;
    }

    /**
     * Performs the preparation action for each character in the party and increases the corresponding party stats.
     *
     * @param party the list of characters in the party
     * @param partyStatsIncreased a map containing the party characters and their increased stats
     * @return a list of strings representing the output of each character's preparation action
     */
    @Override
    public List<String> doPreparationAction(List<Character> party, Map<Character, Map<String, Integer>> partyStatsIncreased) {
        List<String> output = new ArrayList<>();

        for (Character character : party) {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(character.getName());

            if (character.isUnconscious()) {
                stringBuilder.append(" is unconscious.");
                output.add(stringBuilder.toString());
                continue;
            }

            Dice dice = new Dice();

            int statValue = character.doPreparationAction(dice);

            for (Character partyCharacter : party) {
                if (!partyCharacter.isUnconscious()) {
                    partyCharacter.increaseStat(character.getPreparationStat(), character.getPreparationStatValue());
                }
            }

            Map<String, Integer> statIncreased = new LinkedHashMap<>();

            statIncreased.put(character.getPreparationStat(), statValue);

            partyStatsIncreased.put(character, statIncreased);

            buildPreparationActionText(stringBuilder, character, statValue);

            output.add(stringBuilder.toString());
        }
        return output;
    }

    /**
     * Rolls initiatives for the party characters and encounter monsters.
     *
     * @param party the list of characters in the party
     * @param encounterMonsters a map containing encounter monsters and their quantities
     * @param characterInitiatives the list to store character initiatives
     * @param monsterInitiatives the list to store monster initiatives
     * @return a list of strings representing the merged initiatives of characters and monsters
     */
    @Override
    public List<String> rollInitiatives(List<Character> party, Map<Monster, Integer> encounterMonsters,
                                        List<Character> characterInitiatives, List<Monster> monsterInitiatives) {

        List<Character> characters = getAllPartyCharacters(party);
        List<Monster> monsters = getAllMonstersInEncounter(encounterMonsters);

        characterInitiatives.addAll(characters);
        monsterInitiatives.addAll(monsters);

        rollCharacterInitiative(characterInitiatives);

        rollMonsterInitiative(monsterInitiatives);

        return mergeInitiatives(characterInitiatives, monsterInitiatives);
    }

    /**
     * Retrieves all active party characters from the given party list.
     *
     * @param party the list of characters in the party
     * @return a list of active party characters
     */
    private List<Character> getAllPartyCharacters(List<Character> party) {
        List<Character> characters = new ArrayList<>();

        for (Character character : party) {
            if (character.isUnconscious()) {
                continue;
            }

            characters.add(character);
        }

        return characters;
    }

    /**
     * Retrieves all monsters in the encounter based on the provided map of monsters and their quantities.
     *
     * @param encounter the map of monsters and their quantities
     * @return a list of all monsters in the encounter
     */
    private List<Monster> getAllMonstersInEncounter(Map<Monster, Integer> encounter) {
        List<Monster> allMonsters = new ArrayList<>();

        List<Monster> monsters = new ArrayList<>(encounter.keySet());

        for (Monster monster : monsters) {
            for (int i = 0; i < encounter.get(monster); i++) {
                allMonsters.add(getMonsterTypeByChallenge(monster));
            }
        }

        return allMonsters;
    }

    /**
     * Retrieves the appropriate monster type based on the provided monster object.
     *
     * @param monster the monster object
     * @return the appropriate monster type based on the challenge
     */
    private Monster getMonsterTypeByChallenge(Monster monster) {
        if (monster instanceof Boss) {
            return new Boss(monster);

        } else {
            return new StandardMonster(monster);
        }
    }

    /**
     * Rolls initiative for each character in the list.
     *
     * @param characterInitiatives the list of character initiatives to update
     */
    private void rollCharacterInitiative(List<Character> characterInitiatives) {
        Dice dice = new Dice();

        for (Character character : characterInitiatives) {
            character.rollInitiative(dice);
        }
    }

    /**
     * Rolls initiative for each monster in the list.
     *
     * @param monsterInitiatives the list of monster initiatives to update
     */
    private void rollMonsterInitiative(List<Monster> monsterInitiatives) {
        Dice dice = new Dice();

        for (Monster monster : monsterInitiatives) {
            monster.rollInitiative(dice);
        }
    }

    /**
     * Merges and sorts the character and monster initiatives in descending order.
     *
     * @param characterInitiatives the list of character initiatives
     * @param monsterInitiatives the list of monster initiatives
     * @return a list of strings representing the merged initiatives
     */
    private List<String> mergeInitiatives(List<Character> characterInitiatives, List<Monster> monsterInitiatives) {
        List<String> initiatives = new ArrayList<>();

        mergeSort(characterInitiatives, Comparator.comparing(Character::getInitiative));
        mergeSort(monsterInitiatives, Comparator.comparing(Monster::getInitiative));

        Collections.reverse(characterInitiatives);
        Collections.reverse(monsterInitiatives);

        Queue<Character> characters = new LinkedList<>(characterInitiatives);
        Queue<Monster> monsters = new LinkedList<>(monsterInitiatives);

        while (!characters.isEmpty() && !monsters.isEmpty()) {
            Character character = characters.peek();
            Monster monster = monsters.peek();

            if (character.getInitiative() >= monster.getInitiative()) {
                initiatives.add(String.format("  - %-5d %s", character.getInitiative(), character.getName()));
                characters.poll();

            } else {
                initiatives.add(String.format("  - %-5d %s", monster.getInitiative(), monster.getName()));
                monsters.poll();
            }
        }

        while (!characters.isEmpty()) {
            Character character = characters.poll();
            initiatives.add(String.format("  - %-5d %s", character.getInitiative(), character.getName()));
        }

        while (!monsters.isEmpty()) {
            Monster monster = monsters.poll();
            initiatives.add(String.format("  - %-5d %s", monster.getInitiative(), monster.getName()));
        }

        return initiatives;
    }

    /**
     * Builds the message for healing a character.
     *
     * @param character the character performing the heal action
     * @param characterToHeal the character to be healed
     * @param party the list of characters in the party
     * @param partyHealed indicates if the entire party is healed
     * @param value the amount of healing
     * @return the formatted message for the healing action
     */
    private String buildCharacterHealMessage(Character character, Character characterToHeal, List<Character> party, boolean partyHealed, int value) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(character.getName());
        stringBuilder.append(" uses ");
        stringBuilder.append(character.getBattleAction());
        stringBuilder.append(". ");
        stringBuilder.append(character.getBattleEffect());
        stringBuilder.append(" ");
        stringBuilder.append(value);
        stringBuilder.append(" hit points to ");

        if (partyHealed) {
            for (int i = 0; i < party.size(); i++) {
                stringBuilder.append(party.get(i).getName());

                if (i < party.size() - 2) {
                    stringBuilder.append(", ");

                } else if (i == party.size() - 2) {
                    stringBuilder.append(" and ");
                }
            }
        } else {
            stringBuilder.append(characterToHeal.getName());
        }

        stringBuilder.append("\n");

        return stringBuilder.toString();
    }

    /**
     * Heals a character or the entire party based on the character's heal action.
     *
     * @param character the character performing the heal action
     * @param party the list of characters in the party
     * @param dice the dice object for rolling values
     * @return the formatted message for the healing action
     */
    private String characterHeal(Character character, List<Character> party, Dice dice) {
        int value;

        if (character.healsPartyInBattle()) {
            value = character.doBattleAction(dice, true);

            for (Character partyCharacter : party) {
                partyCharacter.heal(value);
            }

            return buildCharacterHealMessage(character, null, party, true, value);
        }

        int lowestHpCharacter = Integer.MAX_VALUE;
        int lowestHpCharacterIndex = -1;

        for (int i = 0; i < party.size(); i++) {
            Character partyCharacter = party.get(i);

            if (partyCharacter.getCurrentHp() < lowestHpCharacter) {
                lowestHpCharacter = partyCharacter.getCurrentHp();
                lowestHpCharacterIndex = i;
            }
        }

        value = party.get(lowestHpCharacterIndex).heal(character.doBattleAction(dice, true));

        return buildCharacterHealMessage(character, party.get(lowestHpCharacterIndex), null, false, value);
    }

    /**
     * Attacks the monster with the lowest hit points.
     *
     * @param character the character performing the attack action
     * @param monsters the list of monsters in the encounter
     * @param dice the dice object for rolling values
     * @param areaAttack indicates if it's an area attack or single target
     * @return the formatted message for the attack action
     */
    private String attackLowestHpMonster(Character character, List<Monster> monsters, Dice dice, boolean areaAttack) {
        int lowestHpMonster = Integer.MAX_VALUE;
        int lowestHpMonsterIndex = -1;

        for (int i = 0; i < monsters.size(); i++) {
            if (monsters.get(i).getHitPoints() < lowestHpMonster) {
                lowestHpMonster = monsters.get(i).getHitPoints();
                lowestHpMonsterIndex = i;
            }
        }

        int value = character.doBattleAction(dice, false);

        if (monsters.get(lowestHpMonsterIndex).getDamageType().equals(character.getDamageType())) {
            value = (int) (value * monsters.get(lowestHpMonsterIndex).damageResistance());
        }

        monsters.get(lowestHpMonsterIndex).takeDamage(value);

        return buildCharacterAttackMessage(character, monsters.get(lowestHpMonsterIndex), monsters, value, areaAttack);
    }

    /**
     * Attacks the monster with the highest hit points.
     *
     * @param character    The attacking character.
     * @param monsters     The list of monsters.
     * @param dice         The dice to roll for attack value.
     * @param areaAttack   Indicates whether it's an area attack or not.
     * @return The attack message.
     */
    private String attackHighestHpMonster(Character character, List<Monster> monsters, Dice dice, boolean areaAttack) {
        int highestHpMonster = Integer.MIN_VALUE;
        int highestHpMonsterIndex = -1;

        for (int i = 0; i < monsters.size(); i++) {
            if (monsters.get(i).getHitPoints() > highestHpMonster) {
                highestHpMonster = monsters.get(i).getHitPoints();
                highestHpMonsterIndex = i;
            }
        }

        int value = character.doBattleAction(dice, false);

        if (monsters.get(highestHpMonsterIndex).getDamageType().equals(character.getDamageType())) {
            value = (int) (value * monsters.get(highestHpMonsterIndex).damageResistance());
        }

        monsters.get(highestHpMonsterIndex).takeDamage(value);

        return buildCharacterAttackMessage(character, monsters.get(highestHpMonsterIndex), monsters, value, areaAttack);
    }

    /**
     * Attacks a random monster.
     *
     * @param character    The attacking character.
     * @param monsters     The list of monsters.
     * @param dice         The dice to roll for attack value.
     * @param areaAttack   Indicates whether it's an area attack or not.
     * @return The attack message.
     */
    private String attackRandomMonster(Character character, List<Monster> monsters, Dice dice, boolean areaAttack) {
        Random random = new Random();

        int index = random.nextInt(monsters.size());

        int value = character.doBattleAction(dice, false);

        if (monsters.get(index).getDamageType().equals(character.getDamageType())) {
            value = (int) (value * monsters.get(index).damageResistance());
        }

        monsters.get(index).takeDamage(value);

        return buildCharacterAttackMessage(character, monsters.get(index), monsters, value, areaAttack);
    }

    /**
     * Updates the state of the monsters after an attack.
     *
     * @param monsters        The list of monsters.
     * @param indexMonsters   The index of monsters.
     * @param experience      The experience points.
     * @return The updated state of the monsters.
     */
    private String updateMonster(List<Monster> monsters, int[] indexMonsters, int[] experience) {
        Iterator<Monster> iterator = monsters.iterator();

        StringBuilder stringBuilder = new StringBuilder();

        while (iterator.hasNext()) {
            Monster monster = iterator.next();

            if (monster.isDead()) {
                experience[0] += monster.getExperience();

                stringBuilder.append(monster.getName());

                if (BOSS_MONSTER.equals(monster.getChallenge())) {
                    stringBuilder.append(" (");
                    stringBuilder.append(monster.getChallenge());
                    stringBuilder.append(")");
                }

                stringBuilder.append(" dies.\n");

                if (monsters.indexOf(monster) <= indexMonsters[0] && indexMonsters[0] > 0) {
                    indexMonsters[0]--;
                }

                iterator.remove();
            }
        }

        return stringBuilder.toString();
    }

    /**
     * Performs an area attack for the character.
     *
     * @param character    The attacking character.
     * @param monsters     The list of monsters.
     * @param dice         The dice to roll for attack value.
     * @param areaAttack   Indicates whether it's an area attack or not.
     * @return The attack message.
     */
    private String characterAreaAttack(Character character, List<Monster> monsters, Dice dice, boolean areaAttack) {

        int value = character.doBattleAction(dice, true);

        for (Monster monster : monsters) {
            if (monster.getDamageType().equals(character.getDamageType())) {
                value = (int) (value * monster.damageResistance());
            }
            monster.takeDamage(value);
        }

        return buildCharacterAttackMessage(character, null, monsters, value, areaAttack);
    }

    /**
     * Builds the attack message for the character.
     *
     * @param character    The attacking character.
     * @param monster      The targeted monster.
     * @param monsters     The list of monsters.
     * @param value        The attack value.
     * @param areaAttack   Indicates whether it's an area attack or not.
     * @return The attack message.
     */
    private String buildCharacterAttackMessage(Character character, Monster monster, List<Monster> monsters, int value, boolean areaAttack) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(character.getName());
        stringBuilder.append(" ");
        stringBuilder.append(character.getBattleEffect());
        stringBuilder.append(" ");

        if (character.isAreaAttack(areaAttack)) {
            for (int i = 0; i < monsters.size(); i++) {
                stringBuilder.append(monsters.get(i).getName());

                if (monsters.get(i).getChallenge().equals(BOSS_MONSTER)) {
                    stringBuilder.append(" (");
                    stringBuilder.append(monsters.get(i).getChallenge());
                    stringBuilder.append(")");
                }

                if (i < monsters.size() - 2) {
                    stringBuilder.append(", ");

                } else if (i == monsters.size() - 2) {
                    stringBuilder.append(" and ");
                }
            }
        } else {
            stringBuilder.append(monster.getName());
        }

        stringBuilder.append(" with ");
        stringBuilder.append(character.getBattleAction());
        stringBuilder.append(".\n");

        return getAttackResult(value, stringBuilder, character.hasAttackFailed(), character.isAttackCritical(), character.getDamageType());
    }

    /**
     * Performs the battle action for the character.
     *
     * @param character         The attacking character.
     * @param monsters          The list of monsters.
     * @param experience        The experience points.
     * @param dice              The dice to roll for attack value.
     * @param indexMonsters     The index of monsters.
     * @return The battle action message.
     */
    private String characterAttack(Character character, List<Monster> monsters, int[] experience, Dice dice, int[] indexMonsters) {
        final int MIN_MONSTERS_AREA_ATTACK = 3;
        final String SWORD_SLASH = "Sword slash";
        final String ARCANE_MISSILE = "Arcane missile";

        StringBuilder stringBuilder = new StringBuilder();

        boolean areaAttack = MIN_MONSTERS_AREA_ATTACK <= monsters.size();

        if (character.isAreaAttack(areaAttack)) {
            stringBuilder.append(characterAreaAttack(character, monsters, dice, areaAttack));

        } else if (character.getBattleAction().contains(SWORD_SLASH)) {
            stringBuilder.append(attackLowestHpMonster(character, monsters, dice, areaAttack));

        } else if (character.getBattleAction().equals(ARCANE_MISSILE)) {
            stringBuilder.append(attackHighestHpMonster(character, monsters, dice, areaAttack));

        } else {
            stringBuilder.append(attackRandomMonster(character, monsters, dice, areaAttack));
        }

        stringBuilder.append(updateMonster(monsters, indexMonsters, experience));

        return stringBuilder.toString();
    }

    /**
     * Performs the battle action for the character.
     *
     * @param character         The attacking character.
     * @param party             The list of characters in the party.
     * @param monsters          The list of monsters.
     * @param experience        The experience points.
     * @param indexMonsters     The index of monsters.
     * @return The battle action message.
     */
    private String characterDoBattleAction(Character character, List<Character> party, List<Monster> monsters, int[] experience, int[] indexMonsters) {
        Dice dice = new Dice();
        StringBuilder stringBuilder = new StringBuilder();

        boolean needHealing = false;

        for (Character characterHealing : party) {
            if (characterHealing.isBelowHalfHp()) {
                needHealing = true;
                break;
            }
        }

        if (character.healsInBattle()) {
            if (needHealing) {
                stringBuilder.append(characterHeal(character, party, dice));
                return stringBuilder.toString();
            }
        }

        stringBuilder.append(characterAttack(character, monsters, experience, dice, indexMonsters));

        return stringBuilder.toString();
    }

    /**
     * Builds the attack message for a monster's attack.
     *
     * @param monster   The monster initiating the attack.
     * @param character The character being attacked. Pass null if the attack targets multiple characters.
     * @param party     The list of characters in the party. Pass null if the attack targets a single character.
     * @param value     The value of the attack.
     * @return The formatted attack message.
     */
    private String buildMonsterAttackMessage(Monster monster, Character character, List<Character> party, int value) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(monster.getName());

        if (monster.getChallenge().equals(BOSS_MONSTER)) {
            stringBuilder.append(" (");
            stringBuilder.append(BOSS_MONSTER);
            stringBuilder.append(")");
        }

        stringBuilder.append(" ");
        stringBuilder.append(monster.getBattleEffect());
        stringBuilder.append(" ");

        if (monster.isAreaAttack()) {
            for (int i = 0; i < party.size(); i++) {
                  stringBuilder.append(party.get(i).getName());

                  if (i < party.size() - 2) {
                      stringBuilder.append(", ");

                  } else if (i == party.size() - 2) {
                      stringBuilder.append(" and ");
                  }
            }
        } else {
            stringBuilder.append(character.getName());
        }

        stringBuilder.append("\n");

        return getAttackResult(value, stringBuilder, monster.hasAttackFailed(), monster.isAttackCritical(), monster.getDamageType());
    }

    /**
     * Generates the attack result message based on the attack value and other parameters.
     *
     * @param value          The value of the attack.
     * @param stringBuilder The StringBuilder to append the attack result message to.
     * @param failedAttack   Indicates if the attack failed.
     * @param attackCritical Indicates if the attack was a critical hit.
     * @param damageType     The type of damage inflicted by the attack.
     * @return The formatted attack result message.
     */
    private String getAttackResult(int value, StringBuilder stringBuilder, boolean failedAttack, boolean attackCritical, String damageType) {
        if (failedAttack) {
            stringBuilder.append("Fails and deals 0 physical damage.\n");

        } else if (attackCritical) {
            stringBuilder.append("Critical hit and deals ");
            stringBuilder.append(value);
            stringBuilder.append(" ");
            stringBuilder.append(damageType);
            stringBuilder.append(" damage.\n");

        } else {
            stringBuilder.append("Hits and deals ");
            stringBuilder.append(value);
            stringBuilder.append(" ");
            stringBuilder.append(damageType);
            stringBuilder.append(" damage.\n");
        }

        return stringBuilder.toString();
    }

    /**
     * Performs an area attack by a monster on the party.
     *
     * @param monster The monster performing the area attack.
     * @param party   The list of characters in the party.
     * @param value   The value of the attack.
     * @return The formatted attack message.
     */
    private String monsterAreaAttack(Monster monster, List<Character> party, int value) {
        for (Character character : party) {
            if (monster.getDamageType().equals(character.getDamageType())) {
                if (monster.getDamageType().equalsIgnoreCase(MAGICAL_DAMAGE) && character.getCharacterClass().equals(MAGE)) {
                    value = (int) (value - character.passiveAbility());

                } else {
                    value = (int) (value * character.passiveAbility());
                }
            }

            character.takeDamage(value);
        }

        return buildMonsterAttackMessage(monster, null, party, value);
    }

    /**
     * Performs an attack by a monster on a random character in the party.
     *
     * @param monster The monster performing the attack.
     * @param party   The list of characters in the party.
     * @param value   The value of the attack.
     * @return The formatted attack message.
     */
    private String attackRandomCharacter(Monster monster, List<Character> party, int value) {
        Random random = new Random();

        int index = random.nextInt(party.size());

        Character character = party.get(index);

        if (monster.getDamageType().equals(character.getDamageType())) {
            if (monster.getDamageType().equalsIgnoreCase(MAGICAL_DAMAGE) && character.getCharacterClass().equals(MAGE)) {
                value = (int) (value - character.passiveAbility());

            } else {
                value = (int) (value * character.passiveAbility());
            }
        }

        character.takeDamage(value);

        return buildMonsterAttackMessage(monster, character, null, value);
    }

    /**
     * Updates the character list by removing any unconscious characters and adjusts the index value.
     *
     * @param party           The list of characters in the party.
     * @param indexCharacters The array containing the index value.
     * @return The formatted message describing the character updates.
     */
    private String updateCharacter(List<Character> party, int[] indexCharacters) {
        Iterator<Character> iterator = party.iterator();

        StringBuilder stringBuilder = new StringBuilder();

        while (iterator.hasNext()) {
            Character character = iterator.next();

            if (character.isUnconscious()) {
                stringBuilder.append(character.getName());
                stringBuilder.append(" falls unconscious.\n");

                if (party.indexOf(character) <= indexCharacters[0] && indexCharacters[0] > 0) {
                    indexCharacters[0]--;
                }

                iterator.remove();
            }
        }

        return stringBuilder.toString();
    }

    /**
     * Performs the battle action of a monster, either an area attack or an attack on a random character.
     *
     * @param monster         The monster performing the battle action.
     * @param party           The list of characters in the party.
     * @param indexCharacters The array containing the index value of the characters.
     * @return The formatted battle action message.
     */
    private String monsterDoBattleAction(Monster monster, List<Character> party, int[] indexCharacters) {
        Dice dice = new Dice();
        StringBuilder stringBuilder = new StringBuilder();

        int value = monster.attack(dice);

        if (monster.isAreaAttack()) {
            stringBuilder.append(monsterAreaAttack(monster, party, value));
        } else {
            stringBuilder.append(attackRandomCharacter(monster, party, value));
        }

        stringBuilder.append(updateCharacter(party, indexCharacters));

        return stringBuilder.toString();
    }

    /**
     * Performs the battle actions for the party and monsters.
     *
     * @param party      The list of characters in the party.
     * @param monsters   The list of monsters.
     * @param experience The array containing the experience values.
     * @return The list of formatted battle action messages.
     */
    @Override
    public List<String> doBattleActions(List<Character> party, List<Monster> monsters, int[] experience) {
        List<String> output = new ArrayList<>();

        int[] indexCharacters = {0};
        int[] indexMonsters = {0};

        while (indexCharacters[0] < party.size() && indexMonsters[0] < monsters.size()) {

            if (party.get(indexCharacters[0]).getInitiative() >= monsters.get(indexMonsters[0]).getInitiative()) {
                output.add(characterDoBattleAction(party.get(indexCharacters[0]), party, monsters, experience, indexMonsters));
                indexCharacters[0]++;

            } else {
                output.add(monsterDoBattleAction(monsters.get(indexMonsters[0]), party, indexCharacters));
                indexMonsters[0]++;
            }
        }

        while (indexCharacters[0] < party.size() && !monsters.isEmpty()) {
            output.add(characterDoBattleAction(party.get(indexCharacters[0]), party, monsters, experience, indexMonsters));
            indexCharacters[0]++;
        }

        while (indexMonsters[0] < monsters.size() && !party.isEmpty()) {
            output.add(monsterDoBattleAction(monsters.get(indexMonsters[0]), party, indexCharacters));
            indexMonsters[0]++;
        }

        return output;

    }

    /**
     * Checks if all characters in the party are unconscious.
     *
     * @param party The list of characters in the party.
     * @return True if all characters are unconscious, false otherwise.
     */
    @Override
    public boolean isTotalPartyUnconscious(List<Character> party) {
        return party.isEmpty();
    }

    /**
     * Resets the party's stats to their default values.
     *
     * @param party              The list of characters in the party.
     * @param partyStatIncreased The map containing the increased stats of the party.
     */
    @Override
    public void returnToDefaultStats(List<Character> party, Map<Character, Map<String, Integer>> partyStatIncreased) {
        for (Character character : party) {
            if (null == partyStatIncreased.get(character)) {
                continue;
            }

            if (character.isSelfPreparationAction()) {
                character.decreaseStat(character.getPreparationStat(), partyStatIncreased.get(character).get(character.getPreparationStat()));

            } else {
                for (Character partyCharacter : party) {
                    if (null == partyStatIncreased.get(partyCharacter)) {
                        continue;
                    }

                    partyCharacter.decreaseStat(character.getPreparationStat(), partyStatIncreased.get(character).get(character.getPreparationStat()));
                }
            }
        }
    }

    /**
     * Performs the short rest action for each character in the party.
     *
     * @param party The list of characters in the party.
     * @return The list of formatted short rest action messages.
     */
    @Override
    public List<String> doShortRestAction(List<Character> party) {
        List<String> output = new ArrayList<>();

        for (Character character : party) {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(character.getName());

            if (character.isUnconscious()) {
                stringBuilder.append(" is unconscious.");

                output.add(stringBuilder.toString());

                continue;
            }

            Dice dice = new Dice();

            int statValue = character.doShortRestAction(dice);

            if (character.isPartyShortRestAction()) {
                for (Character partyCharacter : party) {
                    partyCharacter.increaseStat(character.getShortRestStat(), statValue);
                }
            }

            buildShortRestActionText(stringBuilder, character, party, statValue);

            output.add(stringBuilder.toString());
        }
        return output;
    }

    /**
     * Builds the text for a preparation action performed by a character.
     *
     * @param stringBuilder The StringBuilder to append the text to.
     * @param character     The character performing the preparation action.
     * @param statValue     The value of the stat affected by the preparation action.
     */
    private void buildPreparationActionText(StringBuilder stringBuilder, Character character, int statValue) {
        String sign = (statValue >= 0) ? "+" : "";

        stringBuilder.append(" uses ");
        stringBuilder.append(character.getPreparationAction());
        stringBuilder.append(". ");
        stringBuilder.append(character.getPreparationEffect());
        stringBuilder.append(" ");
        stringBuilder.append(sign);
        stringBuilder.append(statValue);
        stringBuilder.append(".");
    }

    /**
     * Builds the text for a short rest action performed by a character.
     *
     * @param stringBuilder The StringBuilder to append the text to.
     * @param character     The character performing the short rest action.
     * @param party         The list of characters in the party.
     * @param statValue     The value of the stat affected by the short rest action.
     */
    private void buildShortRestActionText(StringBuilder stringBuilder, Character character, List<Character> party,
                                          int statValue) {

        // No stat to manipulate.
        if (character.getShortRestStat().length() == 0) {
            stringBuilder.append(" is ");
            stringBuilder.append(character.getShortRestAction());
            stringBuilder.append(".");

        // Stat to manipulate.
        } else {
            stringBuilder.append(" uses ");
            stringBuilder.append(character.getShortRestAction());
            stringBuilder.append(". ");
            stringBuilder.append(character.getShortRestEffect());
            stringBuilder.append(" ");
            stringBuilder.append(statValue);
            stringBuilder.append(" ");
            stringBuilder.append(character.getShortRestStat());

            if (character instanceof Paladin) {
                stringBuilder.append(" to ");

                for (int i = 0; i < party.size(); i++) {
                    stringBuilder.append(party.get(i).getName());

                    if (i < party.size() - 2) {
                        stringBuilder.append(", ");

                    } else if (i == party.size() - 2) {
                        stringBuilder.append(" and ");
                    }
                }
            }
            stringBuilder.append(".");
        }
    }

    /**
     * Updates the level and evolution of characters based on gained experience.
     *
     * @param party           The list of characters in the party.
     * @param experience      The experience gained.
     * @return The list of formatted level and evolution update messages.
     */
    @Override
    public List<String> updateLevelAndEvolve(List<Character> party, int experience) {
        List<String> output = new ArrayList<>();

        for (int i = 0; i < party.size(); i++) {
            Character character = party.get(i);
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(character.getName());
            stringBuilder.append(" gains ");
            stringBuilder.append(experience);
            stringBuilder.append(" xp.");

            if (character.levelUp(experience)) {
                stringBuilder.append(" ");
                stringBuilder.append(character.getName());
                stringBuilder.append(" levels up. They are now lvl ");
                stringBuilder.append(character.getLevel());
                stringBuilder.append("!");

                if (character.canEvolve()) {
                    character = character.evolve();

                    party.set(i, character);

                    stringBuilder.append("\n");
                    stringBuilder.append(character.getName());
                    stringBuilder.append(" evolves to ");
                    stringBuilder.append(character.getCharacterClass());
                    stringBuilder.append("!");
                }

                character.setUpHpForAdventure();
            }

            output.add(stringBuilder.toString());
        }

        return output;
    }

    /**
     * Performs the merge sort algorithm on a list using the provided comparator.
     *
     * @param list       The list to be sorted.
     * @param comparator The comparator used for sorting.
     * @param <T>        The type of elements in the list.
     */
    private <T> void mergeSort(List<T> list, Comparator<? super T> comparator) {
        if (list.size() > 1) {
            // Divides the list in two halfs.
            List<T> left = new ArrayList<>(list.subList(0, list.size() / 2));
            List<T> right = new ArrayList<>(list.subList(list.size() / 2, list.size()));

            // Orders recursively both lists.
            mergeSort(left, comparator);
            mergeSort(right, comparator);

            // Merge both lists.
            int i = 0;
            int j = 0;
            for (int k = 0; k < list.size(); k++) {
                if (j >= right.size() || (i < left.size() && comparator.compare(left.get(i), right.get(j)) <= 0)) {
                    list.set(k, left.get(i));
                    i++;
                } else {
                    list.set(k, right.get(j));
                    j++;
                }
            }
        }
    }
}
