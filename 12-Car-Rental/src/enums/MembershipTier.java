package enums;

public enum MembershipTier {
    REGULAR(0.0),
    SILVER(0.10),   // 10% discount
    GOLD(0.15);     // 15% discount
    
    private final double discountRate;
    
    MembershipTier(double discountRate) {
        this.discountRate = discountRate;
    }
    
    public double getDiscountRate() {
        return discountRate;
    }
}


