package strategy;

import java.util.Random;

/**
 * Double dice strategy - roll two dice and sum the values (2-12).
 */
public class DoubleDice implements DiceStrategy {
    private final Random random;
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 6;

    public DoubleDice() {
        this.random = new Random();
    }

    @Override
    public int roll() {
        int die1 = random.nextInt(MAX_VALUE) + MIN_VALUE;
        int die2 = random.nextInt(MAX_VALUE) + MIN_VALUE;
        return die1 + die2;
    }

    @Override
    public String getName() {
        return "Double Dice (2-12)";
    }
}


