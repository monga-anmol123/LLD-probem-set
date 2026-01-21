package strategy;

import model.RateLimitConfig;
import model.RateLimitInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Token Bucket rate limiting algorithm.
 * Tokens are added to bucket at constant rate. Each request consumes one token.
 * If bucket is empty, request is rejected.
 */
public class TokenBucketLimiter implements RateLimiter {
    private final RateLimitConfig config;
    private final Map<String, BucketState> buckets;
    
    private static class BucketState {
        int tokens;
        long lastRefillTime;
        
        BucketState(int capacity) {
            this.tokens = capacity;
            this.lastRefillTime = System.currentTimeMillis();
        }
    }
    
    public TokenBucketLimiter(RateLimitConfig config) {
        this.config = config;
        this.buckets = new ConcurrentHashMap<>();
    }
    
    @Override
    public synchronized RateLimitInfo allowRequest(String clientId) {
        BucketState bucket = buckets.computeIfAbsent(clientId, 
            k -> new BucketState(config.getMaxRequests()));
        
        refillTokens(bucket);
        
        if (bucket.tokens > 0) {
            bucket.tokens--;
            long resetTime = bucket.lastRefillTime + config.getWindowMillis();
            return RateLimitInfo.allowed(bucket.tokens, resetTime);
        } else {
            long resetTime = bucket.lastRefillTime + config.getWindowMillis();
            return RateLimitInfo.rejected(resetTime);
        }
    }
    
    private void refillTokens(BucketState bucket) {
        long now = System.currentTimeMillis();
        long timePassed = now - bucket.lastRefillTime;
        
        if (timePassed >= config.getWindowMillis()) {
            // Refill to full capacity
            bucket.tokens = config.getMaxRequests();
            bucket.lastRefillTime = now;
        } else {
            // Partial refill based on time passed
            int tokensToAdd = (int) ((timePassed * config.getMaxRequests()) / config.getWindowMillis());
            bucket.tokens = Math.min(config.getMaxRequests(), bucket.tokens + tokensToAdd);
            if (tokensToAdd > 0) {
                bucket.lastRefillTime = now;
            }
        }
    }
    
    @Override
    public String getAlgorithmName() {
        return "Token Bucket";
    }
    
    @Override
    public void reset(String clientId) {
        buckets.remove(clientId);
    }
    
    @Override
    public int getRemainingQuota(String clientId) {
        BucketState bucket = buckets.get(clientId);
        if (bucket == null) {
            return config.getMaxRequests();
        }
        refillTokens(bucket);
        return bucket.tokens;
    }
}

