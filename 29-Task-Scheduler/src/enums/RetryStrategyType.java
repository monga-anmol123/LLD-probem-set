package enums;

/**
 * Types of retry strategies for failed tasks.
 */
public enum RetryStrategyType {
    NONE("No retry on failure"),
    FIXED_DELAY("Fixed delay between retries"),
    EXPONENTIAL_BACKOFF("Exponentially increasing delay"),
    LINEAR_BACKOFF("Linearly increasing delay");
    
    private final String description;
    
    RetryStrategyType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
