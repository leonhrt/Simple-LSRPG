package business.entities.character.mage;

import business.entities.Dice;
import business.entities.character.Character;

/**
 * Class that represents a Mage, extends from Character.
 */
public class Mage extends Character {
    private static final String PREPARATION_ACTION = "Mage shield";
    private static final String PREPARATION_EFFECT = "Shield recharges to";
    private static final String SHORT_REST_ACTION = "reading a book";
    private static final String BATTLE_AREA_ACTION = "Fireball";
    private static final String BATTLE_SINGLE_ACTION = "Arcane missile";
    private static final String BATTLE_EFFECT = "attacks";
    private static final String DAMAGE_TYPE = "magical";

    /**
     * Constructs a new Mage object based on the provided Character object.
     *
     * @param that The Character object to be used as a base for the Mage.
     */
    public Mage(Character that) {
        super(that);
    }

    /**
     * The Mage class can't evolve.
     *
     * @return Always returns false for a Mage character.
     */
    @Override
    public boolean canEvolve() {
        return false;
    }

    /**
     * The Mage class can't evolve.
     *
     * @return The Mage character itself since it cannot evolve.
     */
    @Override
    public Character evolve() {
        return this;
    }

    /**
     * Takes damage and adjusts the shield accordingly.
     *
     * @param damage The amount of damage taken.
     */
    @Override
    public void takeDamage(int damage) {
        int leftShield = getShield() - damage;

        decreaseStat(getShieldStat(), damage);

        if (leftShield < 0) {
            takeDamage(Math.abs(leftShield));
        }
    }

    /**
     * Rolls the initiative for the Mage character.
     *
     * @param dice The Dice object used for rolling the initiative.
     */
    @Override
    public void rollInitiative(Dice dice) {
        int initiative = dice.roll(dice.getD20()) + getMind();

        setInitiative(initiative);
    }

    /**
     * Performs the preparation action for the Mage character.
     *
     * @param dice The Dice object used for performing the action.
     * @return The value of the shield after the preparation action.
     */
    @Override
    public int doPreparationAction(Dice dice) {
        setShield((dice.roll(dice.getD6()) + getMind()) * getLevel());

        return getShield();
    }

    /**
     * Determines if the preparation action is performed on self.
     *
     * @return Always returns true for the Mage character.
     */
    @Override
    public boolean isSelfPreparationAction() {
        return true;
    }

    /**
     * Determines if the battle action is an area attack.
     *
     * @param fireball true if the battle action is Fireball, false if it is Arcane missile.
     * @return true if the battle action is an area attack, false otherwise.
     */
    @Override
    public boolean isAreaAttack(boolean fireball) {
        return fireball;
    }

    private transient boolean areaAttack;

    /**
     * Performs the battle action for the Mage character.
     *
     * @param dice     The Dice object used for performing the action.
     * @param fireball true if the battle action is Fireball, false if it is Arcane missile.
     * @return The damage dealt by the battle action.
     */
    @Override
    public int doBattleAction(Dice dice, boolean fireball) {
        this.areaAttack = fireball;
        rollAttackDice(dice);

        if (hasAttackFailed()) {
            return getAttackFailedResult();
        }

        int diceResult;
        int damage;

        if (fireball) {
            diceResult = dice.roll(dice.getD4());
        } else {
            diceResult = dice.roll(dice.getD6());
        }

        damage = diceResult + getMind();

        if (isAttackCritical()) {
            return damage * getCriticalMultiplier();
        }

        return damage;
    }

    /**
     * Gets the damage type of the Mage character.
     *
     * @return The damage type ("magical") of the Mage character.
     */
    @Override
    public String getDamageType() {
        return DAMAGE_TYPE;
    }

    /**
     * Performs the short rest action for the Mage character.
     *
     * @param ignored The Dice object (ignored in this case).
     * @return Always returns 0 for the Mage character.
     */
    @Override
    public int doShortRestAction(Dice ignored) {
        return 0;
    }

    /**
     * Gets the passive ability value of the Mage character.
     *
     * @return The passive ability value (equal to the character's level) of the Mage character.
     */
    @Override
    public float passiveAbility() {
        return getLevel();
    }

    /**
     * Gets the name of the preparation action for the Mage character.
     *
     * @return The name of the preparation action ("Mage shield") for the Mage character.
     */
    @Override
    public String getPreparationAction() {
        return PREPARATION_ACTION;
    }

    /**
     * Gets the effect of the preparation action for the Mage character.
     *
     * @return The effect of the preparation action ("Shield recharges to") for the Mage character.
     */
    @Override
    public String getPreparationEffect() {
        return PREPARATION_EFFECT;
    }

    /**
     * Gets the name of the stat associated with the preparation action for the Mage character.
     *
     * @return The name of the stat associated with the preparation action for the Mage character.
     */
    @Override
    public String getPreparationStat() {
        return getShieldStat();
    }

    /**
     * Gets the value of the stat associated with the preparation action for the Mage character.
     *
     * @return Always returns 0 for the Mage character.
     */
    @Override
    public int getPreparationStatValue() {
        return 0;
    }

    /**
     * Gets the name of the short rest action for the Mage character.
     *
     * @return The name of the short rest action ("reading a book") for the Mage character.
     */
    @Override
    public String getShortRestAction() {
        return SHORT_REST_ACTION;
    }

    /**
     * Gets the name of the battle action for the Mage character.
     *
     * @return The name of the battle action ("Fireball" or "Arcane missile") for the Mage character,
     *         depending on whether it is an area attack or not.
     */
    @Override
    public String getBattleAction() {
        if (areaAttack) {
            return BATTLE_AREA_ACTION;
        }

        return BATTLE_SINGLE_ACTION;
    }

    /**
     * Gets the effect of the battle action for the Mage character.
     *
     * @return The effect of the battle action ("attacks") for the Mage character.
     */
    @Override
    public String getBattleEffect() {
        return BATTLE_EFFECT;
    }

    /**
     * Gets the effect of the short rest action for the Mage character.
     *
     * @return Always returns an empty string for the Mage character.
     */
    @Override
    public String getShortRestEffect() {
        return "";
    }

    /**
     * Gets the stat associated with the short rest action for the Mage character.
     *
     * @return Always returns an empty string for the Mage character.
     */
    @Override
    public String getShortRestStat() {
        return "";
    }
}
