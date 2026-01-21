package enums;

/**
 * Enum representing different client subscription tiers with different rate limits.
 */
public enum ClientTier {
    FREE(10, 60),           // 10 requests per minute
    BASIC(100, 60),         // 100 requests per minute
    PREMIUM(1000, 60),      // 1000 requests per minute
    ENTERPRISE(10000, 60);  // 10000 requests per minute
    
    private final int maxRequests;
    private final int windowSeconds;
    
    ClientTier(int maxRequests, int windowSeconds) {
        this.maxRequests = maxRequests;
        this.windowSeconds = windowSeconds;
    }
    
    public int getMaxRequests() {
        return maxRequests;
    }
    
    public int getWindowSeconds() {
        return windowSeconds;
    }
    
    public long getWindowMillis() {
        return windowSeconds * 1000L;
    }
}

