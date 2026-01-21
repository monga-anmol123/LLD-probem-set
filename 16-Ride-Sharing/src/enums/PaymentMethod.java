package enums;

public enum PaymentMethod {
    CASH("Cash Payment"),
    CREDIT_CARD("Credit Card"),
    DEBIT_CARD("Debit Card"),
    WALLET("Digital Wallet"),
    UPI("UPI Payment");
    
    private final String displayName;
    
    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}

