package enums;

public enum OrderStatus {
    PENDING("Order placed, waiting for execution"),
    EXECUTED("Order successfully executed"),
    CANCELLED("Order cancelled by trader"),
    PARTIALLY_FILLED("Order partially executed"),
    REJECTED("Order rejected due to insufficient funds/stocks");
    
    private final String description;
    
    OrderStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}

