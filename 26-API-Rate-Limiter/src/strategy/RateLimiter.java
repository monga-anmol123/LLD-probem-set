package strategy;

import model.RateLimitInfo;

/**
 * Strategy interface for different rate limiting algorithms.
 */
public interface RateLimiter {
    /**
     * Check if a request should be allowed.
     * 
     * @param clientId Client identifier
     * @return RateLimitInfo with result and quota information
     */
    RateLimitInfo allowRequest(String clientId);
    
    /**
     * Get the name of this rate limiting algorithm.
     * 
     * @return Algorithm name
     */
    String getAlgorithmName();
    
    /**
     * Reset the rate limiter for a specific client.
     * 
     * @param clientId Client identifier
     */
    void reset(String clientId);
    
    /**
     * Get remaining quota for a client.
     * 
     * @param clientId Client identifier
     * @return Remaining requests allowed
     */
    int getRemainingQuota(String clientId);
}

