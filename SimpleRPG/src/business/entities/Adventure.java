package business.entities;

import business.entities.character.Character;
import business.entities.monster.Monster;

import java.util.List;
import java.util.Map;

/**
 * Class that represents an Adventure.
 */
public class Adventure {
    private String name;
    private int numberEncounters;
    private List<Map<Monster, Integer>> encounters;
    private List<Character> party;

    /**
     * Constructs an adventure with the given name, number of encounters, and encounters.
     *
     * @param name             the name of the adventure
     * @param numberEncounters the number of encounters in the adventure
     * @param encounters       the list of encounters, each represented as a map of monsters and their quantities
     */
    public Adventure(String name, int numberEncounters, List<Map<Monster, Integer>> encounters) {
        this.name = name;
        this.numberEncounters = numberEncounters;
        this.encounters = encounters;
        this.party = null;
    }

    /**
     * Returns the name of the adventure.
     *
     * @return the name of the adventure
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the number of encounters in the adventure.
     *
     * @return the number of encounters in the adventure
     */
    public int getNumberEncounters() {
        return numberEncounters;
    }

    /**
     * Returns the list of encounters in the adventure.
     *
     * @return the list of encounters in the adventure
     */
    public List<Map<Monster, Integer>> getEncounters() {
        return encounters;
    }

    /**
     * Returns the party of characters participating in the adventure.
     *
     * @return the party of characters participating in the adventure
     */
    public List<Character> getParty() {
        return party;
    }

    /**
     * Sets the party of characters participating in the adventure.
     *
     * @param party the party of characters participating in the adventure
     */
    public void setParty(List<Character> party) {
        this.party = party;
    }
}
