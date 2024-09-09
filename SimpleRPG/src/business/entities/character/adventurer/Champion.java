package business.entities.character.adventurer;

import business.entities.Dice;
import business.entities.character.Character;

/**
 * Class that represents a Champion, extends from Warrior.
 */
public class Champion extends Warrior {
    private static final String PREPARATION_ACTION = "Motivational speech";
    private static final String PREPARATION_EFFECT = "Everyone’s Spirit increases in";
    private static final int PREPARATION_STAT_VALUE = 1;
    private static final String SHORT_REST_ACTION = "Improved bandage time";
    private static final String SHORT_REST_EFFECT = "Heals";
    private static final int CHAMPION_MIN_LEVEL = 8;
    private static final String ADVENTURER_CLASS = "Adventurer";
    private static final String WARRIOR_CLASS = "Warrior";

    /**
     * Constructs a new Champion object based on the provided Character object.
     *
     * @param that the Character object to base the Champion on.
     */
    public Champion(Character that) {
        super(that);
    }

    /**
     * Checks if the Champion character can evolve.
     *
     * @return true if the Champion character's level is less than CHAMPION_MIN_LEVEL, false otherwise.
     */
    @Override
    public boolean canEvolve() {
        return getLevel() < CHAMPION_MIN_LEVEL;
    }

    /**
     * Evolves the Champion if the level does not match the required level.
     *
     * @return a new Character object representing the evolved form of the Champion character.
     */
    @Override
    public Character evolve() {
        switch (getLevel()) {
            case 1, 2, 3 -> {
                setCharacterClass(ADVENTURER_CLASS);
                return new Adventurer(this);
            }
            case 4, 5, 6, 7 -> {
                setCharacterClass(WARRIOR_CLASS);
                return new Warrior(this);
            }
            default -> {
                return this;
            }
        }
    }

    /**
     * Sets up the maximum HP (hit points) for the Champion character before an adventure.
     * The maximum HP is calculated based on the Champion's level, base HP, and body attribute.
     */
    @Override
    public void setUpHpForAdventure() {
        int maxWarriorHp = (getBaseHp() + getBody()) * getLevel();
        int maxChampionHp = maxWarriorHp + getBody() * getLevel();

        setMaxHp(maxChampionHp);
        setCurrentHp(getMaxHp());
    }

    /**
     * Performs the preparation action of the Champion character.
     * The preparation action of the Champion increases a specific stat value.
     *
     * @param ignored a Dice object representing the dice to be ignored (not used in this implementation).
     * @return the value of the preparation stat.
     */
    @Override
    public int doPreparationAction(Dice ignored) {
        return getPreparationStatValue();
    }

    /**
     * Checks if the preparation action of the Champion character is performed on self.
     *
     * @return false, as the preparation action is not performed on self.
     */
    @Override
    public boolean isSelfPreparationAction() {
        return false;
    }

    /**
     * Performs the short rest action of the Champion character.
     * The short rest action of the Champion increases the current HP (hit points) to its maximum HP.
     *
     * @param ignored a Dice object representing the dice to be ignored (not used in this implementation).
     * @return the amount of HP healed during the short rest action.
     */
    @Override
    public int doShortRestAction(Dice ignored) {
        int currentHP = getCurrentHp();
        increaseStat(getHpStat(), getMaxHp());
        return getMaxHp() - currentHP;
    }

    /**
     * Retrieves the name of the preparation action of the Champion character.
     *
     * @return the name of the preparation action.
     */
    @Override
    public String getPreparationAction() {
        return PREPARATION_ACTION;
    }

    /**
     * Retrieves the effect description of the preparation action of the Champion character.
     *
     * @return the effect description of the preparation action.
     */
    @Override
    public String getPreparationEffect() {
        return PREPARATION_EFFECT;
    }

    /**
     * Retrieves the value of the stat affected by the preparation action of the Champion character.
     *
     * @return the value of the preparation stat.
     */
    @Override
    public int getPreparationStatValue() {
        return PREPARATION_STAT_VALUE;
    }

    /**
     * Retrieves the name of the short rest action of the Champion character.
     *
     * @return the name of the short rest action.
     */
    @Override
    public String getShortRestAction() {
        return SHORT_REST_ACTION;
    }

    /**
     * Retrieves the effect description of the short rest action of the Champion character.
     *
     * @return the effect description of the short rest action.
     */
    @Override
    public String getShortRestEffect() {
        return SHORT_REST_EFFECT;
    }

}
