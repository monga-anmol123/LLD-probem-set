package strategy;

import model.RateLimitConfig;
import model.RateLimitInfo;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Leaky Bucket rate limiting algorithm.
 * Requests are processed at a constant rate. Excess requests are queued or rejected.
 * Smooths out bursts of traffic.
 */
public class LeakyBucketLimiter implements RateLimiter {
    private final RateLimitConfig config;
    private final Map<String, BucketState> buckets;
    private final int bucketCapacity;
    
    private static class BucketState {
        Queue<Long> queue;
        long lastLeakTime;
        
        BucketState() {
            this.queue = new LinkedList<>();
            this.lastLeakTime = System.currentTimeMillis();
        }
    }
    
    public LeakyBucketLimiter(RateLimitConfig config) {
        this.config = config;
        this.buckets = new ConcurrentHashMap<>();
        // Bucket capacity is same as max requests
        this.bucketCapacity = config.getMaxRequests();
    }
    
    @Override
    public synchronized RateLimitInfo allowRequest(String clientId) {
        long now = System.currentTimeMillis();
        BucketState bucket = buckets.computeIfAbsent(clientId, k -> new BucketState());
        
        // Leak (process) requests at constant rate
        leakRequests(bucket, now);
        
        if (bucket.queue.size() < bucketCapacity) {
            bucket.queue.offer(now);
            int remaining = bucketCapacity - bucket.queue.size();
            long resetTime = bucket.lastLeakTime + config.getWindowMillis();
            return RateLimitInfo.allowed(remaining, resetTime);
        } else {
            // Queue is full, reject request
            long resetTime = bucket.lastLeakTime + config.getWindowMillis();
            return RateLimitInfo.rejected(resetTime);
        }
    }
    
    private void leakRequests(BucketState bucket, long now) {
        long timePassed = now - bucket.lastLeakTime;
        
        // Calculate how many requests should have leaked (processed)
        int requestsToLeak = (int) ((timePassed * config.getMaxRequests()) / config.getWindowMillis());
        
        // Remove leaked requests from queue
        for (int i = 0; i < requestsToLeak && !bucket.queue.isEmpty(); i++) {
            bucket.queue.poll();
        }
        
        if (requestsToLeak > 0) {
            bucket.lastLeakTime = now;
        }
    }
    
    @Override
    public String getAlgorithmName() {
        return "Leaky Bucket";
    }
    
    @Override
    public void reset(String clientId) {
        buckets.remove(clientId);
    }
    
    @Override
    public int getRemainingQuota(String clientId) {
        BucketState bucket = buckets.get(clientId);
        if (bucket == null) {
            return bucketCapacity;
        }
        
        long now = System.currentTimeMillis();
        leakRequests(bucket, now);
        
        return bucketCapacity - bucket.queue.size();
    }
}

