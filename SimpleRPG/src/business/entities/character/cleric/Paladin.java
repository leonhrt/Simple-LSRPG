package business.entities.character.cleric;

import business.entities.Dice;
import business.entities.character.Character;

/**
 * Class that represents a Paladin, extends from Cleric.
 */
public class Paladin extends Cleric {
    private static final String PREPARATION_ACTION = "Blessing of good luck";
    private static final String PREPARATION_EFFECT = "Everyone’s Mind increases in";
    private static final String BATTLE_HEAL_ACTION = "Prayer of mass healing";
    private static final String BATTLE_ATTACK_ACTION = "Never on my watch";
    private static final String BATTLE_HEAL_EFFECT = "Heals";
    private static final String BATTLE_ATTACK_EFFECT = "attacks";
    private static final String SHORT_REST_ACTION = BATTLE_HEAL_ACTION;
    private static final String SHORT_REST_EFFECT = "Heals";
    private static final String CLERIC_CLASS = "Cleric";
    private static final int PALADIN_MIN_LEVEL = 5;


    /**
     * Creates a new Paladin object based on the provided character.
     *
     * @param that the character to be converted to a paladin
     */
    public Paladin(Character that) {
        super(that);
    }

    /**
     * Checks if the paladin can evolve to a lower class.
     *
     * @return true if the paladin can evolve, false otherwise
     */
    @Override
    public boolean canEvolve() {
        return getLevel() < PALADIN_MIN_LEVEL;
    }

    /**
     * Evolves the paladin to another class.
     * If the Paladin is in level range between 1 and 4, will evolve to a Cleric.
     * Otherwise, the Paladin remains the same.
     *
     * @return the evolved character as a Cleric instance
     */
    @Override
    public Character evolve() {
        switch (getLevel()) {
            case 1, 2, 3, 4 -> {
                setCharacterClass(CLERIC_CLASS);
                return new Cleric(this);
            }
            default -> {
                return this;
            }
        }
    }

    private transient int preparationActionValue;

    /**
     * Performs the preparation action for the paladin, rolling a D3 dice.
     *
     * @param dice the dice object used for rolling
     * @return the value obtained from rolling the dice for the preparation action
     */
    @Override
    public int doPreparationAction(Dice dice) {
        preparationActionValue = dice.roll(dice.getD3());
        return preparationActionValue;
    }

    /**
     * Checks if the paladin can heal the party during battles.
     *
     * @return true if the paladin can heal the party, false otherwise
     */
    @Override
    public boolean healsPartyInBattle() {
        return true;
    }

    private transient boolean needHealing;
    /**
     * Performs the battle action for the paladin, rolling a dice and considering the need for healing.
     *
     * @param dice        the dice object used for rolling
     * @param needHealing indicates whether the paladin needs to perform a healing action
     * @return the result of the battle action, either healing or damage
     */
    @Override
    public int doBattleAction(Dice dice, boolean needHealing) {
        this.needHealing = needHealing;

        // Heal
        if (needHealing) {
            return battleHealAction(dice);
        }

        // Attack
        rollAttackDice(dice);

        if (hasAttackFailed()) {
            return getAttackFailedResult();
        }

        int diceResult = dice.roll(dice.getD8());
        int damage = diceResult + getSpirit();

        if (isAttackCritical()) {
            return damage * getCriticalMultiplier();
        }

        return damage;
    }

    /**
     * Performs the short rest action for the paladin, considering the need for healing.
     *
     * @param dice the dice object used for rolling
     * @return the result of the short rest action, either healing or damage
     */
    @Override
    public int doShortRestAction(Dice dice) {
        return doBattleAction(dice, true);
    }

    /**
     * Checks if the paladin's short rest action affects the entire party.
     *
     * @return true if the short rest action affects the entire party, false otherwise
     */
    @Override
    public boolean isPartyShortRestAction() {
        return true;
    }

    /**
     * Retrieves the name of the preparation action for the paladin.
     *
     * @return the name of the preparation action
     */
    @Override
    public String getPreparationAction() {
        return PREPARATION_ACTION;
    }

    /**
     * Retrieves the effect of the preparation action for the paladin.
     *
     * @return the effect of the preparation action
     */
    @Override
    public String getPreparationEffect() {
        return PREPARATION_EFFECT;
    }

    /**
     * Retrieves the value obtained from the last preparation action performed by the paladin.
     *
     * @return the value of the preparation action
     */
    @Override
    public int getPreparationStatValue() {
        return preparationActionValue;
    }

    /**
     * Retrieves the passive ability value for the paladin.
     *
     * @return the value of the passive ability
     */
    @Override
    public float passiveAbility() {
        return 0.5F;
    }

    /**
     * Retrieves the name of the battle action for the paladin, depending on the need for healing.
     *
     * @return the name of the battle action
     */
    @Override
    public String getBattleAction() {
        if (needHealing) {
            return BATTLE_HEAL_ACTION;
        }
        return BATTLE_ATTACK_ACTION;
    }

    /**
     * Retrieves the effect of the battle action for the paladin, depending on the need for healing.
     *
     * @return the effect of the battle action
     */
    @Override
    public String getBattleEffect() {
        if (needHealing) {
            return BATTLE_HEAL_EFFECT;
        }
        return BATTLE_ATTACK_EFFECT;
    }

    /**
     * Retrieves the name of the short rest action for the paladin.
     *
     * @return the name of the short rest action
     */
    @Override
    public String getShortRestAction() {
        return SHORT_REST_ACTION;
    }

    /**
     * Retrieves the effect of the short rest action for the paladin.
     *
     * @return the effect of the short rest action
     */
    @Override
    public String getShortRestEffect() {
        return SHORT_REST_EFFECT;
    }
}
