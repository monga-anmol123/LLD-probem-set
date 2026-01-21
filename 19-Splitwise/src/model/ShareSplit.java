package model;

import enums.SplitType;

public class ShareSplit extends Split {
    private int shares;
    
    public ShareSplit(User user, int shares) {
        super(user, SplitType.SHARE);
        this.shares = shares;
    }
    
    public int getShares() {
        return shares;
    }
    
    @Override
    public String toString() {
        return user.getName() + ": " + shares + " shares ($" + String.format("%.2f", amount) + ")";
    }
}
