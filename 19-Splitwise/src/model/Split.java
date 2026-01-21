package model;

import enums.SplitType;

public abstract class Split {
    protected User user;
    protected double amount;
    protected SplitType type;
    
    public Split(User user, SplitType type) {
        this.user = user;
        this.type = type;
    }
    
    public User getUser() {
        return user;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public SplitType getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return user.getName() + ": $" + String.format("%.2f", amount);
    }
}
