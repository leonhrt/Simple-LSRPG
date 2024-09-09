package business.entities;

import java.util.Random;

/**
 * Class that represents a Dice. Used to simulate rolls of a dice.
 */
public class Dice {
    private final int d3;
    private final int d4;
    private final int d6;
    private final int d8;
    private final int d10;
    private final int d12;
    private final int d20;

    /**
     * Constructs a new Dice object with default number of sides for each dice.
     * The default number of sides are: d3 = 3, d4 = 4, d6 = 6, d8 = 8, d10 = 10, d12 = 12, d20 = 20.
     */
    public Dice() {
        this.d3 = 3;
        this.d4 = 4;
        this.d6 = 6;
        this.d8 = 8;
        this.d10 = 10;
        this.d12 = 12;
        this.d20 = 20;
    }

    /**
     * Rolls the dice with the specified number of sides and returns the result.
     * The result will be a random integer between 1 and the number of sides (inclusive).
     *
     * @param dice The number of sides on the dice to roll.
     * @return The result of the dice roll.
     */
    public int roll(int dice) {
        final int MIN_RESULT = 1;

        Random random = new Random();

        return random.nextInt(dice) + MIN_RESULT;
    }

    /**
     * Returns the number of sides on the d3 dice.
     *
     * @return The number of sides on the d3 dice.
     */
    public int getD3() {
        return d3;
    }

    /**
     * Returns the number of sides on the d4 dice.
     *
     * @return The number of sides on the d4 dice.
     */
    public int getD4() {
        return d4;
    }

    /**
     * Returns the number of sides on the d6 dice.
     *
     * @return The number of sides on the d6 dice.
     */
    public int getD6() {
        return d6;
    }

    /**
     * Returns the number of sides on the d8 dice.
     *
     * @return The number of sides on the d8 dice.
     */
    public int getD8() {
        return d8;
    }

    /**
     * Returns the number of sides on the d10 dice.
     *
     * @return The number of sides on the d10 dice.
     */
    public int getD10() {
        return d10;
    }

    /**
     * Returns the number of sides on the d12 dice.
     *
     * @return The number of sides on the d12 dice.
     */
    public int getD12() {
        return d12;
    }

    /**
     * Returns the number of sides on the d20 dice.
     *
     * @return The number of sides on the d20 dice.
     */
    public int getD20() {
        return d20;
    }
}
