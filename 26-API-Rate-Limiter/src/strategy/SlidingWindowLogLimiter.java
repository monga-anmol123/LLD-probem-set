package strategy;

import model.RateLimitConfig;
import model.RateLimitInfo;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Sliding Window Log rate limiting algorithm.
 * Maintains timestamps of all requests. Most accurate but memory intensive.
 */
public class SlidingWindowLogLimiter implements RateLimiter {
    private final RateLimitConfig config;
    private final Map<String, Queue<Long>> requestLogs;
    
    public SlidingWindowLogLimiter(RateLimitConfig config) {
        this.config = config;
        this.requestLogs = new ConcurrentHashMap<>();
    }
    
    @Override
    public synchronized RateLimitInfo allowRequest(String clientId) {
        long now = System.currentTimeMillis();
        Queue<Long> log = requestLogs.computeIfAbsent(clientId, k -> new LinkedList<>());
        
        // Remove timestamps outside the window
        while (!log.isEmpty() && now - log.peek() >= config.getWindowMillis()) {
            log.poll();
        }
        
        if (log.size() < config.getMaxRequests()) {
            log.offer(now);
            int remaining = config.getMaxRequests() - log.size();
            long resetTime = log.isEmpty() ? now + config.getWindowMillis() : log.peek() + config.getWindowMillis();
            return RateLimitInfo.allowed(remaining, resetTime);
        } else {
            // Calculate when the oldest request will expire
            long resetTime = log.peek() + config.getWindowMillis();
            return RateLimitInfo.rejected(resetTime);
        }
    }
    
    @Override
    public String getAlgorithmName() {
        return "Sliding Window Log";
    }
    
    @Override
    public void reset(String clientId) {
        requestLogs.remove(clientId);
    }
    
    @Override
    public int getRemainingQuota(String clientId) {
        Queue<Long> log = requestLogs.get(clientId);
        if (log == null) {
            return config.getMaxRequests();
        }
        
        long now = System.currentTimeMillis();
        // Remove expired timestamps
        while (!log.isEmpty() && now - log.peek() >= config.getWindowMillis()) {
            log.poll();
        }
        
        return config.getMaxRequests() - log.size();
    }
}

