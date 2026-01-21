package strategy;

import java.util.HashMap;
import java.util.Map;

public class MinimumNotesStrategy implements CashDispensingStrategy {
    
    @Override
    public Map<Integer, Integer> dispenseCash(int amount, Map<Integer, Integer> availableDenominations) {
        // This is a more complex strategy that tries to minimize the number of notes
        // Using dynamic programming approach
        
        int[] denominations = {2000, 1000, 500, 100};
        Map<Integer, Integer> result = new HashMap<>();
        
        // For simplicity, we'll use a greedy approach with backtracking
        // In a real implementation, you might use DP for true minimum notes
        
        return greedyWithBacktrack(amount, availableDenominations, denominations, 0, new HashMap<>());
    }
    
    private Map<Integer, Integer> greedyWithBacktrack(int amount, Map<Integer, Integer> available, 
                                                       int[] denoms, int index, Map<Integer, Integer> current) {
        if (amount == 0) {
            return new HashMap<>(current);
        }
        
        if (index >= denoms.length) {
            return null;
        }
        
        int denom = denoms[index];
        int maxNotes = Math.min(amount / denom, available.getOrDefault(denom, 0));
        
        // Try using notes of this denomination
        for (int count = maxNotes; count >= 0; count--) {
            if (count > 0) {
                current.put(denom, count);
            }
            
            Map<Integer, Integer> result = greedyWithBacktrack(
                amount - (count * denom), 
                available, 
                denoms, 
                index + 1, 
                current
            );
            
            if (result != null) {
                return result;
            }
            
            current.remove(denom);
        }
        
        return null;
    }
}


