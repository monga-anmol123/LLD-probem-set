package strategy;

import model.Split;
import model.ExactSplit;
import java.util.List;

public class ExactSplitStrategy implements SplitStrategy {
    
    @Override
    public void calculateSplits(double totalAmount, List<Split> splits) {
        // Amounts are already set in ExactSplit constructor
        // Just validate that they sum to total
        double sum = splits.stream().mapToDouble(Split::getAmount).sum();
        
        if (Math.abs(sum - totalAmount) > 0.01) {
            throw new IllegalArgumentException(
                String.format("Exact splits ($%.2f) don't match total amount ($%.2f)", sum, totalAmount)
            );
        }
    }
    
    @Override
    public String getStrategyName() {
        return "Exact Split";
    }
}
