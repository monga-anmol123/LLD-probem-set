package strategy;

import model.Split;
import model.PercentSplit;
import java.util.List;

public class PercentSplitStrategy implements SplitStrategy {
    
    @Override
    public void calculateSplits(double totalAmount, List<Split> splits) {
        // Validate percentages sum to 100
        double totalPercent = 0.0;
        for (Split split : splits) {
            if (split instanceof PercentSplit) {
                totalPercent += ((PercentSplit) split).getPercent();
            }
        }
        
        if (Math.abs(totalPercent - 100.0) > 0.01) {
            throw new IllegalArgumentException(
                String.format("Percentages must sum to 100%%, got %.2f%%", totalPercent)
            );
        }
        
        // Calculate amounts based on percentages
        for (Split split : splits) {
            if (split instanceof PercentSplit) {
                PercentSplit percentSplit = (PercentSplit) split;
                double amount = totalAmount * percentSplit.getPercent() / 100.0;
                split.setAmount(amount);
            }
        }
    }
    
    @Override
    public String getStrategyName() {
        return "Percentage Split";
    }
}
