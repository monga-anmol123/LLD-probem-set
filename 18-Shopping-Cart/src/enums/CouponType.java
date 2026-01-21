package enums;

public enum CouponType {
    PERCENTAGE_OFF("Percentage Off"),
    FIXED_AMOUNT_OFF("Fixed Amount Off"),
    FREE_SHIPPING("Free Shipping");
    
    private final String description;
    
    CouponType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
