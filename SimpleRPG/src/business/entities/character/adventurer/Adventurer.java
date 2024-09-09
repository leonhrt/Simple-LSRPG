package business.entities.character.adventurer;

import business.entities.Dice;
import business.entities.character.Character;

/**
 * Class that represents an Adventurer, extends from Character.
 */
public class Adventurer extends Character {
    private static final String PREPARATION_ACTION = "Self-Motivated";
    private static final String PREPARATION_EFFECT = "Their Spirit increases in";
    private static final int PREPARATION_STAT_VALUE = 1;
    private static final String SHORT_REST_ACTION = "Bandage time";
    private static final String SHORT_REST_EFFECT = "Heals";
    private static final int ADVENTURER_MAX_LEVEL = 3;
    private static final String WARRIOR_CLASS = "Warrior";
    private static final String CHAMPION_CLASS = "Champion";
    private static final String BATTLE_ACTION = "Sword slash";
    private static final String BATTLE_EFFECT = "attacks";
    private static final String DAMAGE_TYPE = "physical";

    /**
     * Constructs a new Adventurer with the specified parameters.
     *
     * @param name           the name of the Adventurer
     * @param player         the player controlling the Adventurer
     * @param xp             the experience points of the Adventurer
     * @param body           the body attribute of the Adventurer
     * @param mind           the mind attribute of the Adventurer
     * @param spirit         the spirit attribute of the Adventurer
     * @param characterClass the character class of the Adventurer
     */
    public Adventurer(String name, String player, int xp, int body, int mind, int spirit, String characterClass) {
        super(name, player, xp, body, mind, spirit, characterClass);
    }

    /**
     * Constructs a new Adventurer based on an existing Character.
     *
     * @param that the Character to create the Adventurer from
     */
    public Adventurer(Character that) {
        super(that);
    }

    /**
     * Checks if the Adventurer can evolve to a higher class (Warrior or Champion).
     *
     * @return true if the Adventurer can evolve, false otherwise
     */
    @Override
    public boolean canEvolve() {
        return getLevel() > ADVENTURER_MAX_LEVEL;
    }

    /**
     * Evolves the Adventurer to a higher class and returns the evolved Character.
     * If the Adventurer is in a level range between 4 and 7, will evolve to a Warrior.
     * If the Adventurer is in a level range between 8 and 10, will evolve to a Champion.
     * Otherwise, will stay as an Adventurer
     *
     * @return the evolved Character
     */
    @Override
    public Character evolve() {
        switch (getLevel()) {
            case 4, 5, 6, 7 -> {
                setCharacterClass(WARRIOR_CLASS);
                return new Warrior(this);
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
     * Rolls the initiative for the Adventurer using the provided dice.
     * The initiative is calculated based on a dice roll and the Adventurer's spirit attribute.
     *
     * @param dice the dice to use for the initiative roll
     */
    @Override
    public void rollInitiative(Dice dice) {
        int initiative = dice.roll(dice.getD12()) + getSpirit();

        setInitiative(initiative);
    }

    /**
     * Performs the preparation action for the Adventurer, increasing the Adventurer's spirit attribute.
     *
     * @param ignored ignored dice parameter
     * @return the increased value of the spirit attribute
     */
    @Override
    public int doPreparationAction(Dice ignored) {
        return increaseStat(getSpiritStat(), PREPARATION_STAT_VALUE);
    }

    /**
     * Checks if the Adventurer's preparation action is for his self.
     *
     * @return true if the preparation action is self-motivated, false otherwise
     */
    @Override
    public boolean isSelfPreparationAction() {
        return true;
    }

    /**
     * Performs the battle action for the Adventurer, calculating the damage dealt.
     *
     * @param dice    the dice to use for the attack roll
     * @param ignored ignored parameter
     * @return the calculated damage dealt
     */
    @Override
    public int doBattleAction(Dice dice, boolean ignored) {
        rollAttackDice(dice);

        if (hasAttackFailed()) {
            return getAttackFailedResult();
        }

        int diceResult = dice.roll(dice.getD6());

        int damage = diceResult + getBody();

        if (isAttackCritical()) {
            return damage * getCriticalMultiplier();
        }

        return damage;
    }

    /**
     * Returns the damage type of the Adventurer.
     *
     * @return the damage type
     */
    @Override
    public String getDamageType() {
        return DAMAGE_TYPE;
    }

    /**
     * Performs the short rest action for the Adventurer, healing the Adventurer's hit points.
     *
     * @param dice the dice to use for the healing roll
     * @return the amount of hit points healed
     */
    @Override
    public int doShortRestAction(Dice dice) {
        int value = dice.roll(dice.getD8()) + getMind();

        int previousHp = getCurrentHp();

        increaseStat(getHpStat(), value);

        return getCurrentHp() - previousHp;
    }

    /**
     * Returns the value of the Adventurer's passive ability.
     * The Adventurer does not have a passive ability.
     *
     * @return the value of the passive ability
     */
    @Override
    public float passiveAbility() {
        return 1;
    }

    /**
     * Returns the name of the Adventurer's preparation action.
     *
     * @return the name of the preparation action
     */
    @Override
    public String getPreparationAction() {
        return PREPARATION_ACTION;
    }

    /**
     * Returns the effect of the Adventurer's preparation action.
     *
     * @return the effect of the preparation action
     */
    @Override
    public String getPreparationEffect() {
        return PREPARATION_EFFECT;
    }

    /**
     * Returns the stat affected by the Adventurer's preparation action.
     *
     * @return the affected stat
     */
    @Override
    public String getPreparationStat() {
        return getSpiritStat();
    }

    /**
     * Returns the value of the Adventurer's preparation action stat increase.
     *
     * @return the value of the stat increase
     */
    @Override
    public int getPreparationStatValue() {
        return 0;
    }

    /**
     * Returns the name of the Adventurer's battle action.
     *
     * @return the name of the battle action
     */
    @Override
    public String getBattleAction() {
        return BATTLE_ACTION;
    }

    /**
     * Returns the effect of the Adventurer's battle action.
     *
     * @return the effect of the battle action
     */
    @Override
    public String getBattleEffect() {
        return BATTLE_EFFECT;
    }

    /**
     * Returns the name of the Adventurer's short rest action.
     *
     * @return the name of the short rest action
     */
    @Override
    public String getShortRestAction() {
        return SHORT_REST_ACTION;
    }

    /**
     * Returns the effect of the Adventurer's short rest action.
     *
     * @return the effect of the short rest action
     */
    @Override
    public String getShortRestEffect() {
        return SHORT_REST_EFFECT;
    }

    /**
     * Returns the stat affected by the Adventurer's short rest action.
     *
     * @return the affected stat
     */
    @Override
    public String getShortRestStat() {
        return getHpStat();
    }

}
