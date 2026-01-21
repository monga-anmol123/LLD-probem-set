package strategy;

import java.util.HashMap;
import java.util.Map;

public class GreedyStrategy implements CashDispensingStrategy {
    
    @Override
    public Map<Integer, Integer> dispenseCash(int amount, Map<Integer, Integer> availableDenominations) {
        Map<Integer, Integer> dispensed = new HashMap<>();
        int[] denominations = {2000, 1000, 500, 100};
        int remainingAmount = amount;
        
        for (int denom : denominations) {
            if (remainingAmount >= denom && availableDenominations.getOrDefault(denom, 0) > 0) {
                int notesNeeded = remainingAmount / denom;
                int notesAvailable = availableDenominations.get(denom);
                int notesToDispense = Math.min(notesNeeded, notesAvailable);
                
                if (notesToDispense > 0) {
                    dispensed.put(denom, notesToDispense);
                    remainingAmount -= notesToDispense * denom;
                }
            }
        }
        
        // If we couldn't dispense the exact amount, return null
        if (remainingAmount > 0) {
            return null;
        }
        
        return dispensed;
    }
}


