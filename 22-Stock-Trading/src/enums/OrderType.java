package enums;

public enum OrderType {
    MARKET("Market Order - Execute immediately at current price"),
    LIMIT("Limit Order - Execute at specified price or better"),
    STOP_LOSS("Stop-Loss Order - Sell when price drops to trigger level");
    
    private final String description;
    
    OrderType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}

