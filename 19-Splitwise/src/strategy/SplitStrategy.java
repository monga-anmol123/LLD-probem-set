package strategy;

import model.Split;
import java.util.List;

public interface SplitStrategy {
    void calculateSplits(double totalAmount, List<Split> splits);
    String getStrategyName();
}
