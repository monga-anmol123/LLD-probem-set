package strategy;

import java.util.Map;

public interface CashDispensingStrategy {
    /**
     * Dispense cash using the strategy
     * @param amount Amount to dispense
     * @param availableDenominations Available denominations (denomination -> count)
     * @return Map of denominations to dispense (denomination -> count), or null if cannot dispense
     */
    Map<Integer, Integer> dispenseCash(int amount, Map<Integer, Integer> availableDenominations);
}


