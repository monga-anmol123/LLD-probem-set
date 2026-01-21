package model;

import enums.ClientTier;
import enums.RateLimitAlgorithm;

/**
 * Configuration for rate limiting.
 */
public class RateLimitConfig {
    private final int maxRequests;
    private final long windowMillis;
    private final RateLimitAlgorithm algorithm;
    private final ClientTier tier;
    
    public RateLimitConfig(int maxRequests, long windowMillis, RateLimitAlgorithm algorithm, ClientTier tier) {
        this.maxRequests = maxRequests;
        this.windowMillis = windowMillis;
        this.algorithm = algorithm;
        this.tier = tier;
    }
    
    public static RateLimitConfig fromTier(ClientTier tier, RateLimitAlgorithm algorithm) {
        return new RateLimitConfig(
            tier.getMaxRequests(),
            tier.getWindowMillis(),
            algorithm,
            tier
        );
    }
    
    // Getters
    public int getMaxRequests() {
        return maxRequests;
    }
    
    public long getWindowMillis() {
        return windowMillis;
    }
    
    public RateLimitAlgorithm getAlgorithm() {
        return algorithm;
    }
    
    public ClientTier getTier() {
        return tier;
    }
    
    @Override
    public String toString() {
        return "RateLimitConfig[" + tier + ", " + maxRequests + " req/" + (windowMillis/1000) + "s, " + algorithm + "]";
    }
}

