package business.entities.monster;

/**
 * Class that represents a StandardMonster (Minion or Lieutenant), extending from Monster.
 */
public class StandardMonster extends Monster {

    /**
     * Constructs a StandardMonster object with the specified parameters.
     *
     * @param name        the name of the monster
     * @param challenge   the challenge rating of the monster
     * @param experience  the experience points awarded for defeating the monster
     * @param hitPoints   the hit points of the monster
     * @param initiative  the initiative value of the monster
     * @param damageDice  the damage dice of the monster's attack
     * @param damageType  the damage type of the monster's attack
     */
    public StandardMonster(String name, String challenge, int experience, int hitPoints, int initiative, String damageDice, String damageType) {
        super(name, challenge, experience, hitPoints, initiative, damageDice, damageType);
    }

    /**
     * Constructs a StandardMonster object based on an existing Monster object.
     *
     * @param that  the Monster object to be copied
     */
    public StandardMonster(Monster that) {
        super(that);
    }

    /**
     * Returns if is an area attack.
     *
     * @return true if is an area attack, false otherwise
     */
    @Override
    public boolean isAreaAttack() {
        return false;
    }

    /**
     * Retrieves the damage resistance of the Monster.
     *
     * @return the damage resistance of the Monster
     */
    @Override
    public float damageResistance() {
        return 1;
    }
}
