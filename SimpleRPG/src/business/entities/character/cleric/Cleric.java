package business.entities.character.cleric;

import business.entities.Dice;
import business.entities.character.Character;

/**
 * Class that represents a Cleric, extends from Character.
 */
public class Cleric extends Character {
    private static final String PREPARATION_ACTION = "Prayer of good luck";
    private static final String PREPARATION_EFFECT = "Everyone’s Mind increases in";
    private static final int PREPARATION_STAT_VALUE = 1;
    private static final String SHORT_REST_ACTION = "Prayer of self-healing";
    private static final String SHORT_REST_EFFECT = "Heals";
    private static final String PALADIN_CLASS = "Paladin";
    private static final int CLERIC_MAX_LEVEL = 4;
    private static final String BATTLE_HEAL_ACTION = "Prayer of healing";
    private static final String BATTLE_ATTACK_ACTION = "Not on my watch";
    private static final String BATTLE_HEAL_EFFECT = "Heals";
    private static final String BATTLE_ATTACK_EFFECT = "attacks";
    private static final String DAMAGE_TYPE = "psychical";

    /**
     * Constructs a new Cleric object based on the given character.
     *
     * @param that the character to create the Cleric from
     */
    public Cleric(Character that) {
        super(that);
    }

    /**
     * Checks if the Cleric character can evolve.
     * The Cleric can evolve if its level is greater than the maximum Cleric level.
     *
     * @return true if the Cleric can evolve, {@code false} otherwise
     */
    @Override
    public boolean canEvolve() {
        return getLevel() > CLERIC_MAX_LEVEL;
    }

    /**
     * Evolves the Cleric character to a higher class.
     * If the current level is within the range of 5 to 10, the Cleric evolves into a Paladin.
     * Otherwise, the Cleric remains the same.
     *
     * @return the evolved character, which is a Paladin if applicable, otherwise the same Cleric
     */
    @Override
    public Character evolve() {
        switch (getLevel()) {
            case 5, 6, 7, 8, 9, 10 -> {
                setCharacterClass(PALADIN_CLASS);
                return new Paladin(this);
            }
            default -> {
                return this;
            }
        }
    }

    /**
     * Rolls the initiative for the Cleric character using the given dice.
     * The initiative is calculated by rolling a d10 dice and adding the Cleric's spirit value.
     * The calculated initiative value is then set as the Cleric's initiative.
     *
     * @param dice the dice object used for rolling
     */
    @Override
    public void rollInitiative(Dice dice) {
        int initiative = dice.roll(dice.getD10()) + getSpirit();
        setInitiative(initiative);
    }

    /**
     * Performs the preparation action for the Cleric character using the given dice.
     * The preparation action returns the value of the preparation stat.
     *
     * @param ignored the dice object (ignored for Cleric)
     * @return the value of the preparation stat
     */
    @Override
    public int doPreparationAction(Dice ignored) {
        return getPreparationStatValue();
    }

    /**
     * Checks if the preparation action of the Cleric character is self-preparation.
     * The Cleric's preparation action is not self-preparation.
     *
     * @return false since the Cleric's preparation action is not self-preparation
     */
    @Override
    public boolean isSelfPreparationAction() {
        return false;
    }

    /**
     * Checks if the Cleric character can heal in battle.
     * The Cleric can heal in battle.
     *
     * @return true since the Cleric can heal in battle
     */
    @Override
    public boolean healsInBattle() {
        return true;
    }

    private transient boolean needHealing;

    /**
     * Performs the battle action for the Cleric character using the given dice and healing requirement.
     * The battle action can be either a heal or an attack based on the healing requirement.
     * If healing is needed, the Cleric performs the battle heal action.
     * Otherwise, the Cleric rolls attack dice and calculates damage based on the dice result and the Cleric's spirit value.
     * If the attack is critical, the damage is multiplied by the critical multiplier.
     *
     * @param dice         the dice object used for rolling
     * @param needHealing  true if healing is needed, false for attacking
     * @return the calculated damage or the result of the battle heal action
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

        int diceResult = dice.roll(dice.getD4());
        int damage = diceResult + getSpirit();

        if (isAttackCritical()) {
            return damage * getCriticalMultiplier();
        }

        return damage;
    }

    /**
     * Retrieves the damage type of the Cleric character.
     *
     * @return the damage type of the Cleric
     */
    @Override
    public String getDamageType() {
        return DAMAGE_TYPE;
    }

    /**
     * Performs the short rest action for the Cleric character using the given dice.
     * The short rest action is equivalent to performing a battle action with healing requirement set to true.
     * The value returned represents the increase in current HP after the short rest action.
     *
     * @param dice the dice object used for rolling
     * @return the increase in current HP after the short rest action
     */
    @Override
    public int doShortRestAction(Dice dice) {
        int value = doBattleAction(dice, true);
        int previousHp = getCurrentHp();
        increaseStat(getHpStat(), value);
        return getCurrentHp() - previousHp;
    }

    /**
     * Retrieves the passive ability value of the Cleric character.
     * The Cleric does not have a passive ability.
     *
     * @return the passive ability value of the Cleric
     */
    @Override
    public float passiveAbility() {
        return 1;
    }

    /**
     * Retrieves the name of the preparation action for the Cleric character.
     *
     * @return the name of the preparation action
     */
    @Override
    public String getPreparationAction() {
        return PREPARATION_ACTION;
    }

    /**
     * Retrieves the effect of the preparation action for the Cleric character.
     *
     * @return the effect of the preparation action
     */
    @Override
    public String getPreparationEffect() {
        return PREPARATION_EFFECT;
    }

    /**
     * Retrieves the preparation stat for the Cleric character.
     *
     * @return the preparation stat for the Cleric
     */
    @Override
    public String getPreparationStat() {
        return getMindStat();
    }

    /**
     * Retrieves the value of the preparation stat for the Cleric character.
     *
     * @return the value of the preparation stat
     */
    @Override
    public int getPreparationStatValue() {
        return PREPARATION_STAT_VALUE;
    }

    /**
     * Performs the battle heal action for the Cleric character using the given dice.
     * The battle heal action rolls a d10 dice and adds the Cleric's mind value to determine the amount healed.
     *
     * @param dice the dice object used for rolling
     * @return the amount healed by the battle heal action
     */
    protected int battleHealAction(Dice dice) {
        int diceResult = dice.roll(dice.getD10());
        return diceResult + getMind();
    }

    /**
     * Retrieves the name of the battle action for the Cleric character.
     * If healing is needed, the battle action is the battle heal action. Otherwise, it is the attack action.
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
     * Retrieves the effect of the battle action for the Cleric character.
     * If healing is needed, the battle effect is the heal effect. Otherwise, it is the attack effect.
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
     * Retrieves the name of the short rest action for the Cleric character.
     *
     * @return the name of the short rest action
     */
    @Override
    public String getShortRestAction() {
        return SHORT_REST_ACTION;
    }

    /**
     * Retrieves the effect of the short rest action for the Cleric character.
     *
     * @return the effect of the short rest action
     */
    @Override
    public String getShortRestEffect() {
        return SHORT_REST_EFFECT;
    }

    /**
     * Retrieves the short rest stat for the Cleric character.
     *
     * @return the short rest stat for the Cleric
     */
    @Override
    public String getShortRestStat() {
        return getHpStat();
    }

}
