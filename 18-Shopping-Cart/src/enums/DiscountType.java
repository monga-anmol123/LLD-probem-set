package enums;

public enum DiscountType {
    PERCENTAGE("Percentage Discount"),
    FIXED_AMOUNT("Fixed Amount Discount"),
    BOGO("Buy One Get One"),
    CATEGORY("Category-Based Discount"),
    BULK("Bulk Purchase Discount");
    
    private final String description;
    
    DiscountType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
