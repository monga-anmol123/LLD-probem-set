package model;

import enums.SplitType;

public class PercentSplit extends Split {
    private double percent;
    
    public PercentSplit(User user, double percent) {
        super(user, SplitType.PERCENT);
        this.percent = percent;
    }
    
    public double getPercent() {
        return percent;
    }
    
    @Override
    public String toString() {
        return user.getName() + ": " + percent + "% ($" + String.format("%.2f", amount) + ")";
    }
}
