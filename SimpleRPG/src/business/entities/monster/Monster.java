package business.entities.monster;

import business.entities.Dice;

import java.util.Objects;

/**
 * Abstract class that represents a Monster.
 */
public abstract class Monster {
    private static final int CRITICAL_MULTIPLIER = 2;
    private static final int CRITICAL_ATTACK = 10;
    private static final int ATTACK_FAILED = 1;
    private static final int ATTACK_FAILED_RESULT = 0;
    private static final String DICE_D3 = "d3";
    private static final String DICE_D4 = "d4";
    private static final String DICE_D6 = "d6";
    private static final String DICE_D8 = "d8";
    private static final String DICE_D10 = "d10";
    private static final String DICE_D12 = "d12";
    private static final String DICE_D20 = "d20";
    private static final int DEAD = 0;
    private static final String BATTLE_EFFECT = "attacks";

    private String name;
    private String challenge;
    private int experience;
    private int hitPoints;
    private int initiative;
    private String damageDice;
    private String damageType;

    /**
     * Constructs a Monster with the specified attributes.
     *
     * @param name        the name of the monster
     * @param challenge   the challenge of the monster
     * @param experience  the experience gained from defeating the monster
     * @param hitPoints   the hit points of the monster
     * @param initiative  the initiative value of the monster
     * @param damageDice  the dice used for damage calculation
     * @param damageType  the type of damage inflicted by the monster
     */
    public Monster(String name, String challenge, int experience, int hitPoints, int initiative,
                   String damageDice, String damageType) {
        this.name = name;
        this.challenge = challenge;
        this.experience = experience;
        this.hitPoints = hitPoints;
        this.initiative = initiative;
        this.damageDice = damageDice;
        this.damageType = damageType;
    }

    /**
     * Constructs a Monster by copying another Monster.
     *
     * @param that the Monster to copy
     */
    public Monster(Monster that) {
        this.name = that.name;
        this.challenge = that.challenge;
        this.experience = that.experience;
        this.hitPoints = that.hitPoints;
        this.initiative = that.initiative;
        this.damageDice = that.damageDice;
        this.damageType = that.damageType;
    }

    /**
     * Checks if the Monster is dead.
     *
     * @return true if the Monster is dead, false otherwise
     */
    public boolean isDead() {
        return DEAD == hitPoints;
    }

    /**
     * Reduces the hit points of the Monster by the specified amount of damage.
     *
     * @param damage the amount of damage to take
     */
    public void takeDamage(int damage) {
        hitPoints -= damage;

        if (DEAD > hitPoints) {
            hitPoints = DEAD;
        }
    }

    /**
     * Rolls the initiative of the Monster using the provided dice.
     *
     * @param dice the dice to roll for initiative
     */
    public void rollInitiative(Dice dice) {
        initiative += dice.roll(dice.getD12());
    }

    /**
     * Retrieves the appropriate dice type based on the damageDice value.
     *
     * @param dice the dice object to retrieve the dice type from
     * @return the dice type for damage calculation
     */
    protected int getDiceType(Dice dice) {
        return switch (damageDice) {
            case DICE_D3 -> dice.getD3();
            case DICE_D4 -> dice.getD4();
            case DICE_D6 -> dice.getD6();
            case DICE_D8 -> dice.getD8();
            case DICE_D10 -> dice.getD10();
            case DICE_D12 -> dice.getD12();
            case DICE_D20 -> dice.getD20();
            default -> 0;
        };
    }

    /**
     * Performs an attack by rolling the attack dice and calculating the damage.
     *
     * @param dice the dice object to use for rolling the attack dice
     * @return the damage inflicted by the attack, or ATTACK_FAILED_RESULT if the attack failed
     */
    public int attack(Dice dice) {
        rollAttackDice(dice);

        if (hasAttackFailed()) {
            return ATTACK_FAILED_RESULT;
        }

        int damage = dice.roll(getDiceType(dice));

        if (isAttackCritical()) {
            return damage * CRITICAL_MULTIPLIER;
        }

        return damage;
    }

    /**
     * Retrieves the battle effect of the Monster.
     *
     * @return the battle effect
     */
    public String getBattleEffect() {
        return BATTLE_EFFECT;
    }

    public abstract boolean isAreaAttack();
    public abstract float damageResistance();

    private transient int attackDiceResult;

    /**
     * Rolls the attack dice using the provided dice.
     *
     * @param dice the dice object to use for rolling the attack dice
     */
    protected void rollAttackDice(Dice dice) {
        attackDiceResult = dice.roll(dice.getD10());
    }

    /**
     * Checks if the attack has failed.
     *
     * @return true if the attack has failed, false otherwise
     */
    public boolean hasAttackFailed() {
        return ATTACK_FAILED == attackDiceResult;
    }

    /**
     * Checks if the attack is a critical hit.
     *
     * @return true if the attack is a critical hit, false otherwise
     */
    public boolean isAttackCritical() {
        return CRITICAL_ATTACK == attackDiceResult;
    }

    /**
     * Retrieves the name of the Monster.
     *
     * @return the name of the Monster
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the challenge of the Monster.
     *
     * @return the challenge of the Monster
     */
    public String getChallenge() {
        return challenge;
    }

    /**
     * Retrieves the experience gained from defeating the Monster.
     *
     * @return the experience gained from defeating the Monster
     */
    public int getExperience() {
        return experience;
    }

    /**
     * Retrieves the hit points of the Monster.
     *
     * @return the hit points of the Monster
     */
    public int getHitPoints() {
        return hitPoints;
    }

    /**
     * Retrieves the initiative value of the Monster.
     *
     * @return the initiative value of the Monster
     */
    public int getInitiative() {
        return initiative;
    }

    /**
     * Retrieves the damage dice used by the Monster.
     *
     * @return the damage dice used by the Monster
     */
    public String getDamageDice() {
        return damageDice;
    }

    /**
     * Retrieves the damage type inflicted by the Monster.
     *
     * @return the damage type inflicted by the Monster
     */
    public String getDamageType() {
        return damageType;
    }

    /**
     * Checks if the Monster is equal to the specified object.
     *
     * @param key the object to compare for equality
     * @return true if the Monster is equal to the specified object, false otherwise
     */
    @Override
    public boolean equals(Object key) {
        if (this == key) {
            return true;
        }
        if (key == null || getClass() != key.getClass()) {
            return false;
        }
        Monster monster = (Monster) key;
        return Objects.equals(name, monster.name);
    }

    /**
     * Generates the hash code for the Monster.
     *
     * @return the hash code for the Monster
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
