package business.entities.character.adventurer;

import business.entities.Dice;
import business.entities.character.Character;

/**
 * Class that represents a Warrior, extends from Adventurer.
 */
public class Warrior extends Adventurer {
    private static final String ADVENTURER_CLASS = "Adventurer";
    private static final String CHAMPION_CLASS = "Champion";
    private static final int WARRIOR_MIN_LEVEL = 4;
    private static final int WARRIOR_MAX_LEVEL = 7;
    private static final String BATTLE_ACTION = "Improved sword slash";

    /**
     * Constructs a Warrior based on an existing Character.
     *
     * @param that the Character object to be used as the base for the Warrior.
     */
    public Warrior(Character that) {
        super(that);
    }

    /**
     * Checks if the Warrior can evolve based on their current level.
     *
     * @return true if the Warrior can evolve, false otherwise.
     */
    @Override
    public boolean canEvolve() {
        return getLevel() > WARRIOR_MAX_LEVEL || getLevel() < WARRIOR_MIN_LEVEL;
    }

    /**
     * Evolves the Warrior into a higher or lower class based on their current level.
     * If the Warrior is in a level range between 1 and 3, will evolve to an Adventurer.
     * If the Warrior is in a level range between 8 and 10, will evolve to a Chamipn.
     * Otherwise, will stay as a Warrior
     *
     * @return a new Character object representing the evolved form of the Warrior.
     */
    @Override
    public Character evolve() {
        switch (getLevel()) {
            case 1, 2, 3 -> {
                setCharacterClass(ADVENTURER_CLASS);
                return new Adventurer(this);
            }
            case 8, 9, 10 -> {
                setCharacterClass(CHAMPION_CLASS);
                return new Champion(this);
            }
            default -> {
                return this;
            }
        }
    }

    /**
     * Performs the battle action of the Warrior, which is an improved sword slash.
     * Calculates the damage inflicted by the Warrior based on the roll of the attack dice.
     *
     * @param dice        the Dice object used for rolling the attack dice.
     * @param needHealing true if the Warrior needs healing, false otherwise.
     * @return the amount of damage inflicted by the Warrior.
     */
    @Override
    public int doBattleAction(Dice dice, boolean needHealing) {
        rollAttackDice(dice);

        if (hasAttackFailed()) {
            return getAttackFailedResult();
        }

        int diceResult = dice.roll(dice.getD10());

        int damage = diceResult + getBody();

        if (isAttackCritical()) {
            return damage * getCriticalMultiplier();
        }

        return damage;
    }

    /**
     * Retrieves the description of the Warrior's battle action.
     *
     * @return the description of the Warrior's battle action.
     */
    @Override
    public String getBattleAction() {
        return BATTLE_ACTION;
    }

    /**
     * Retrieves the passive ability of the Warrior.
     *
     * @return the passive ability value of the Warrior.
     */
    @Override
    public float passiveAbility() {
        return 0.5F;
    }

}
