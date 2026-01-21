package strategy;

import java.util.Random;

/**
 * Normal dice strategy - single die rolling 1-6.
 */
public class NormalDice implements DiceStrategy {
    private final Random random;
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 6;

    public NormalDice() {
        this.random = new Random();
    }

    @Override
    public int roll() {
        return random.nextInt(MAX_VALUE) + MIN_VALUE;
    }

    @Override
    public String getName() {
        return "Normal Dice (1-6)";
    }
}


