package strategy;

import java.util.Random;

/**
 * Loaded dice strategy - biased towards higher values.
 * Probabilities: 1(5%), 2(5%), 3(10%), 4(15%), 5(25%), 6(40%)
 */
public class LoadedDice implements DiceStrategy {
    private final Random random;
    private static final int[] CUMULATIVE_WEIGHTS = {5, 10, 20, 35, 60, 100};
    private static final int[] VALUES = {1, 2, 3, 4, 5, 6};

    public LoadedDice() {
        this.random = new Random();
    }

    @Override
    public int roll() {
        int randomValue = random.nextInt(100) + 1; // 1-100
        
        for (int i = 0; i < CUMULATIVE_WEIGHTS.length; i++) {
            if (randomValue <= CUMULATIVE_WEIGHTS[i]) {
                return VALUES[i];
            }
        }
        
        return 6; // Fallback (should never reach here)
    }

    @Override
    public String getName() {
        return "Loaded Dice (Biased towards higher values)";
    }
}


