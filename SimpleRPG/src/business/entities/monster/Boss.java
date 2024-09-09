package business.entities.monster;

/**
 * Class that represents a Boss, extending from Monster.
 */
public class Boss extends Monster {

    /**
     * Constructs a new Boss object with the specified attributes.
     *
     * @param name        the name of the boss
     * @param challenge   the challenge rating of the boss
     * @param experience  the experience points awarded for defeating the boss
     * @param hitPoints   the hit points of the boss
     * @param initiative  the initiative value of the boss
     * @param damageDice  the dice representing the boss's damage
     * @param damageType  the type of damage inflicted by the boss
     */
    public Boss(String name, String challenge, int experience, int hitPoints, int initiative, String damageDice, String damageType) {
        super(name, challenge, experience, hitPoints, initiative, damageDice, damageType);
    }

    /**
     * Constructs a new Boss object based on an existing Monster object.
     *
     * @param that  the Monster object to be converted into a Boss
     */
    public Boss(Monster that) {
        super(that);
    }

    /**
     * Indicates whether the boss has an area attack.
     *
     * @return true if the boss has an area attack, false otherwise
     */
    @Override
    public boolean isAreaAttack() {
        return true;
    }

    /**
     * Retrieves the damage resistance of the boss.
     *
     * @return the damage resistance of the boss as a float value
     */
    @Override
    public float damageResistance() {
        return 0.5F;
    }
}
