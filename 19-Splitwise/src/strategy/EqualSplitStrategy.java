package strategy;

import model.Split;
import java.util.List;

public class EqualSplitStrategy implements SplitStrategy {
    
    @Override
    public void calculateSplits(double totalAmount, List<Split> splits) {
        if (splits.isEmpty()) {
            throw new IllegalArgumentException("No participants for split");
        }
        
        double amountPerPerson = totalAmount / splits.size();
        
        for (Split split : splits) {
            split.setAmount(amountPerPerson);
        }
    }
    
    @Override
    public String getStrategyName() {
        return "Equal Split";
    }
}
