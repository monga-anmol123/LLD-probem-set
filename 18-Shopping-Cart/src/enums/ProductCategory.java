package enums;

public enum ProductCategory {
    ELECTRONICS("Electronics", false),
    CLOTHING("Clothing", false),
    GROCERIES("Groceries", true),  // Tax-exempt
    BOOKS("Books", true),           // Tax-exempt
    HOME_GARDEN("Home & Garden", false),
    TOYS("Toys", false),
    SPORTS("Sports & Outdoors", false);
    
    private final String displayName;
    private final boolean taxExempt;
    
    ProductCategory(String displayName, boolean taxExempt) {
        this.displayName = displayName;
        this.taxExempt = taxExempt;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public boolean isTaxExempt() {
        return taxExempt;
    }
}
