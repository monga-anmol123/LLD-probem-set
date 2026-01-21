package strategy;

import model.Split;
import model.ShareSplit;
import java.util.List;

public class ShareSplitStrategy implements SplitStrategy {
    
    @Override
    public void calculateSplits(double totalAmount, List<Split> splits) {
        // Calculate total shares
        int totalShares = 0;
        for (Split split : splits) {
            if (split instanceof ShareSplit) {
                totalShares += ((ShareSplit) split).getShares();
            }
        }
        
        if (totalShares == 0) {
            throw new IllegalArgumentException("Total shares cannot be zero");
        }
        
        // Calculate amount per share
        double amountPerShare = totalAmount / totalShares;
        
        // Calculate amounts based on shares
        for (Split split : splits) {
            if (split instanceof ShareSplit) {
                ShareSplit shareSplit = (ShareSplit) split;
                double amount = amountPerShare * shareSplit.getShares();
                split.setAmount(amount);
            }
        }
    }
    
    @Override
    public String getStrategyName() {
        return "Share Split";
    }
}
