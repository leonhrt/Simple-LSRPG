package business.entities.character;

import business.entities.Dice;
import com.google.gson.annotations.SerializedName;

/**
 * Abstract class that represents a Character.
 */
public abstract class Character {
    private static final int CRITICAL_MULTIPLIER = 2;
    private static final int CRITICAL_ATTACK = 10;
    private static final String BODY = "Body";
    private static final String MIND = "Mind";
    private static final String SPIRIT = "Spirit";
    private static final String SHIELD = "Shield";
    private static final String HP = "hit points";
    private static final int ATTACK_FAILED = 1;
    private static final int ATTACK_FAILED_RESULT = 0;
    private static final int UNCONSCIOUS = 0;
    private static final int BASE_HP = 10;

    private String name;
    private String player;
    private int xp;
    private int body;
    private int mind;
    private int spirit;
    @SerializedName("class")
    private String characterClass;
    private transient int maxHp;
    private transient int currentHp;
    private transient int initiative;
    private transient int shield;

    /**
     * Constructs a new Character object with the specified name,
     * player, experience, stats, and character class.
     *
     * @param name           the name of the character
     * @param player         the name of the player controlling the character
     * @param xp             the experience points of the character
     * @param body           the body stat of the character
     * @param mind           the mind stat of the character
     * @param spirit         the spirit stat of the character
     * @param characterClass the character class of the character
     */
    public Character(String name, String player, int xp, int body, int mind, int spirit, String characterClass) {
        this.name = name;
        this.player = player;
        this.xp = xp;
        this.body = body;
        this.mind = mind;
        this.spirit = spirit;
        this.characterClass = characterClass;
    }

    /**
     * Constructs a new Character object by copying the attributes
     * of the specified character.
     *
     * @param that the character to be copied
     */
    public Character(Character that) {
        this.name = that.name;
        this.player = that.player;
        this.xp = that.xp;
        this.body = that.body;
        this.mind = that.mind;
        this.spirit = that.spirit;
        this.characterClass = that.characterClass;
        this.maxHp = that.maxHp;
        this.currentHp = that.currentHp;
        this.initiative = that.initiative;
        this.shield = that.shield;
    }

    /**
     * Checks if the character is unconscious.
     *
     * @return true if the character is unconscious, false otherwise
     */
    public boolean isUnconscious() {
        return UNCONSCIOUS >= currentHp;
    }

    /**
     * Increases the character's experience points and checks if the character
     * has leveled up as a result.
     *
     * @param experience the experience points to be added
     * @return true if the character leveled up, false otherwise
     */
    public boolean levelUp(int experience) {
        int previousLevel = getLevel();

        this.xp += experience;

        int newLevel = getLevel();

        return newLevel > previousLevel;
    }

    /**
     * Increases the character's current health points by the specified amount.
     * If the resulting health points exceed the maximum health points, it is
     * adjusted to the maximum value.
     *
     * @param amount the amount of health points to be added
     * @return the actual amount of health points added
     */
    public int heal(int amount) {
        this.currentHp += amount;

        if (currentHp > maxHp) {
            currentHp = maxHp;
        }

        return amount;
    }

    /**
     * Decreases the character's current health points by the specified damage.
     * If the resulting health points are less than or equal to zero, the
     * character becomes unconscious.
     *
     * @param damage the amount of damage to be taken
     */
    public void takeDamage(int damage) {
        currentHp -= damage;

        if (currentHp < UNCONSCIOUS) {
            currentHp = UNCONSCIOUS;
        }
    }

    /**
     * Increases the value of the specified stat by the given amount.
     * The valid stat values are "Body", "Mind", "Spirit", "Shield", and "hit points".
     * For "hit points", the heal method is called instead.
     *
     * @param stat  the stat to be increased
     * @param value the amount by which to increase the stat
     * @return the actual amount by which the stat was increased
     */
    public int increaseStat(String stat, int value) {
        switch (stat) {
            case BODY -> body += value;
            case MIND -> mind += value;
            case SPIRIT -> spirit += value;
            case SHIELD -> shield += value;
            case HP -> heal(value);
        }

        return value;
    }

    /**
     * Decreases the value of the specified stat by the given amount.
     * The valid stat values are "Body", "Mind", "Spirit", "Shield", and "hit points".
     * For "hit points", the takeDamage method is called instead.
     *
     * @param stat  the stat to be decreased
     * @param value the amount by which to decrease the stat
     */
    public void decreaseStat(String stat, int value) {
        switch (stat) {
            case BODY -> body -= value;
            case MIND -> mind -= value;
            case SPIRIT -> spirit -= value;
            case SHIELD -> decreaseShield(value);
            case HP -> takeDamage(value);
        }

    }

    /**
     * Decreases the character's shield points by the specified value.
     *
     * @param damage the amount of damage to be taken
     */
    private void decreaseShield(int damage) {
        shield -= damage;

        if (shield < 0) {
            shield = 0;
        }
    }

    /**
     * Sets up the character's maximum health points for an adventure based on
     * the character's body stat and level.
     */
    public void setUpHpForAdventure() {
        setMaxHp((BASE_HP + getBody()) * getLevel());

        setCurrentHp(getMaxHp());
    }

    /**
     * Checks if the character's current health points are below half of the
     * maximum health points.
     *
     * @return true if the character's current health points are below
     *         half of the maximum health points, false otherwise
     */
    public boolean isBelowHalfHp() {
        return currentHp < maxHp / 2;
    }

    /**
     * Checks if the character heals during battle.
     *
     * @return false
     */
    public boolean healsInBattle() {
        return false;
    }

    /**
     * Checks if the character heals the party during battle.
     *
     * @return false
     */
    public boolean healsPartyInBattle() {
        return false;
    }

    /**
     * Checks if the character's attack is an area attack.
     *
     * @param ignored a parameter that is ignored
     * @return false
     */
    public boolean isAreaAttack(boolean ignored) {
        return false;
    }

    /**
     * Checks if the character's battle action is a party short rest action.
     *
     * @return false
     */
    public boolean isPartyShortRestAction() {
        return false;
    }

    private transient int attackDiceResult;

    /**
     * Rolls the attack dice using the specified dice object.
     *
     * @param dice the dice object used to roll the attack dice
     */
    protected void rollAttackDice(Dice dice) {
        attackDiceResult = dice.roll(dice.getD10());
    }

    /**
     * Checks if the character's attack has failed.
     *
     * @return true if the attack has failed, false otherwise
     */
    public boolean hasAttackFailed() {
        return ATTACK_FAILED == attackDiceResult;
    }

    /**
     * Checks if the character's attack is critical.
     *
     * @return true if the attack is critical, false otherwise
     */
    public boolean isAttackCritical() {
        return CRITICAL_ATTACK == attackDiceResult;
    }

    /**
     * Checks if the character can evolve.
     *
     * @return true if the character can evolve, false otherwise
     */
    public abstract boolean canEvolve();

    /**
     * Evolves the character to its next form.
     *
     * @return the evolved character
     */
    public abstract Character evolve();

    /**
     * Rolls the character's initiative using the specified dice object.
     *
     * @param dice the dice object used to roll the initiative
     */
    public abstract void rollInitiative(Dice dice);

    /**
     * Performs the character's preparation action using the specified dice object.
     *
     * @param dice the dice object used for the preparation action
     * @return the result of the preparation action
     */
    public abstract int doPreparationAction(Dice dice);

    /**
     * Checks if the character's preparation action is self-targeted.
     *
     * @return true if the preparation action is self-targeted,
     *         false otherwise
     */
    public abstract boolean isSelfPreparationAction();

    /**
     * Performs the character's battle action using the specified dice object and option.
     *
     * @param dice   the dice object used for the battle action
     * @param option the battle action option
     * @return the result of the battle action
     */
    public abstract int doBattleAction(Dice dice, boolean option);

    public abstract String getDamageType();

    /**
     * Performs the character's short rest action using the specified dice object.
     *
     * @param dice the dice object used for the short rest action
     * @return the result of the short rest action
     */
    public abstract int doShortRestAction(Dice dice);

    /**
     * Returns the passive ability of the character.
     *
     * @return The passive ability as a float value.
     */
    public abstract float passiveAbility();

    /**
     * Returns the preparation action of the character.
     *
     * @return The preparation action as a String.
     */
    public abstract String getPreparationAction();

    /**
     * Returns the preparation effect of the character.
     *
     * @return The preparation effect as a String.
     */
    public abstract String getPreparationEffect();

    /**
     * Returns the preparation stat of the character.
     *
     * @return The preparation stat as a String.
     */
    public abstract String getPreparationStat();

    /**
     * Returns the preparation stat value of the character.
     *
     * @return The preparation stat value as an integer.
     */
    public abstract int getPreparationStatValue();

    /**
     * Returns the battle action of the character.
     *
     * @return The battle action as a String.
     */
    public abstract String getBattleAction();

    /**
     * Returns the battle effect of the character.
     *
     * @return The battle effect as a String.
     */
    public abstract String getBattleEffect();

    /**
     * Returns the short rest action of the character.
     *
     * @return The short rest action as a String.
     */
    public abstract String getShortRestAction();

    /**
     * Returns the short rest effect of the character.
     *
     * @return The short rest effect as a String.
     */
    public abstract String getShortRestEffect();

    /**
     * Returns the short rest stat of the character.
     *
     * @return The short rest stat as a String.
     */
    public abstract String getShortRestStat();

    /**
     * Returns the name of the character.
     *
     * @return The name as a String.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the player of the character.
     *
     * @return The player as a String.
     */
    public String getPlayer() {
        return player;
    }

    /**
     * Returns the experience points (XP) of the character.
     *
     * @return The experience points as an integer.
     */
    public int getXp() {
        return xp;
    }

    /**
     * Returns the level of the character.
     *
     * @return The level as an integer.
     */
    public int getLevel() {
        int level = xp / 100 + 1;
        return Math.min(level, 10);
    }

    /**
     * Returns the body stat of the character.
     *
     * @return The body stat as an integer.
     */
    public int getBody() {
        return body;
    }

    /**
     * Returns the mind stat of the character.
     *
     * @return The mind stat as an integer.
     */
    public int getMind() {
        return mind;
    }

    /**
     * Returns the spirit stat of the character.
     *
     * @return The spirit stat as an integer.
     */
    public int getSpirit() {
        return spirit;
    }

    /**
     * Returns the character class of the character.
     *
     * @return The character class as a String.
     */
    public String getCharacterClass() {
        return characterClass;
    }

    /**
     * Sets the character class of the character.
     *
     * @param characterClass The character class to set for the character.
     */
    protected void setCharacterClass(String characterClass) {
        this.characterClass = characterClass;
    }

    /**
     * Returns the maximum HP (Hit Points) of the character.
     *
     * @return The maximum HP as an integer.
     */
    public int getMaxHp() {
        return maxHp;
    }

    /**
     * Sets the maximum HP (Hit Points) of the character.
     *
     * @param maxHp The maximum HP to set for the character.
     */
    protected void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    /**
     * Returns the current HP (Hit Points) of the character.
     *
     * @return The current HP as an integer.
     */
    public int getCurrentHp() {
        return currentHp;
    }

    /**
     * Sets the current HP (Hit Points) of the character.
     *
     * @param currentHp The current HP to set for the character.
     */
    protected void setCurrentHp(int currentHp) {
        this.currentHp = currentHp;
    }

    /**
     * Returns the initiative of the character.
     *
     * @return The initiative as an integer.
     */
    public int getInitiative() {
        return initiative;
    }

    /**
     * Sets the initiative of the character.
     *
     * @param initiative The initiative to set for the character.
     */
    protected void setInitiative(int initiative) {
        this.initiative = initiative;
    }

    /**
     * Returns the shield stat of the character.
     *
     * @return The shield stat as an integer.
     */
    protected int getShield() {
        return shield;
    }

    /**
     * Sets the shield stat of the character.
     *
     * @param shield The shield stat to set for the character.
     */
    protected void setShield(int shield) {
        this.shield = shield;
    }

    /**
     * Returns the critical multiplier for the character.
     *
     * @return The critical multiplier as an integer.
     */
    protected int getCriticalMultiplier() {
        return CRITICAL_MULTIPLIER;
    }

    /**
     * Returns the mind stat as a string.
     *
     * @return The mind stat as a String.
     */
    protected String getMindStat() {
        return MIND;
    }

    /**
     * Returns the spirit stat as a string.
     *
     * @return The spirit stat as a String.
     */
    protected String getSpiritStat() {
        return SPIRIT;
    }

    /**
     * Returns the shield stat as a string.
     *
     * @return The shield stat as a String.
     */
    protected String getShieldStat() {
        return SHIELD;
    }

    /**
     * Returns the HP (Hit Points) stat as a string.
     *
     * @return The HP stat as a String.
     */
    protected String getHpStat() {
        return HP;
    }

    /**
     * Returns the result code for a failed attack.
     *
     * @return The result code for a failed attack as an integer.
     */
    protected int getAttackFailedResult() {
        return ATTACK_FAILED_RESULT;
    }

    /**
     * Returns the base HP (Hit Points) value.
     *
     * @return The base HP value as an integer.
     */
    protected int getBaseHp() {
        return BASE_HP;
    }
}
