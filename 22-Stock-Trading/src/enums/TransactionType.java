package enums;

public enum TransactionType {
    BUY("Purchase of stocks"),
    SELL("Sale of stocks");
    
    private final String description;
    
    TransactionType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}

