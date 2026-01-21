package strategy;

import model.RateLimitConfig;
import model.RateLimitInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Sliding Window Counter rate limiting algorithm.
 * Hybrid approach: uses counters from current and previous windows.
 * More memory efficient than log-based, more accurate than fixed window.
 */
public class SlidingWindowCounterLimiter implements RateLimiter {
    private final RateLimitConfig config;
    private final Map<String, WindowCounters> counters;
    
    private static class WindowCounters {
        int currentWindowCount;
        int previousWindowCount;
        long currentWindowStart;
        
        WindowCounters() {
            this.currentWindowCount = 0;
            this.previousWindowCount = 0;
            this.currentWindowStart = System.currentTimeMillis();
        }
    }
    
    public SlidingWindowCounterLimiter(RateLimitConfig config) {
        this.config = config;
        this.counters = new ConcurrentHashMap<>();
    }
    
    @Override
    public synchronized RateLimitInfo allowRequest(String clientId) {
        long now = System.currentTimeMillis();
        WindowCounters counter = counters.computeIfAbsent(clientId, k -> new WindowCounters());
        
        updateWindows(counter, now);
        
        // Calculate weighted count based on position in current window
        double windowProgress = (double)(now - counter.currentWindowStart) / config.getWindowMillis();
        double weightedCount = counter.currentWindowCount + 
                              (counter.previousWindowCount * (1 - windowProgress));
        
        if (weightedCount < config.getMaxRequests()) {
            counter.currentWindowCount++;
            int remaining = (int)(config.getMaxRequests() - weightedCount - 1);
            long resetTime = counter.currentWindowStart + config.getWindowMillis();
            return RateLimitInfo.allowed(Math.max(0, remaining), resetTime);
        } else {
            long resetTime = counter.currentWindowStart + config.getWindowMillis();
            return RateLimitInfo.rejected(resetTime);
        }
    }
    
    private void updateWindows(WindowCounters counter, long now) {
        long timeSinceWindowStart = now - counter.currentWindowStart;
        
        if (timeSinceWindowStart >= config.getWindowMillis()) {
            // Move to next window
            long windowsPassed = timeSinceWindowStart / config.getWindowMillis();
            
            if (windowsPassed == 1) {
                // Just moved to next window
                counter.previousWindowCount = counter.currentWindowCount;
            } else {
                // Skipped windows (no requests for a while)
                counter.previousWindowCount = 0;
            }
            
            counter.currentWindowCount = 0;
            counter.currentWindowStart = now - (timeSinceWindowStart % config.getWindowMillis());
        }
    }
    
    @Override
    public String getAlgorithmName() {
        return "Sliding Window Counter";
    }
    
    @Override
    public void reset(String clientId) {
        counters.remove(clientId);
    }
    
    @Override
    public int getRemainingQuota(String clientId) {
        WindowCounters counter = counters.get(clientId);
        if (counter == null) {
            return config.getMaxRequests();
        }
        
        long now = System.currentTimeMillis();
        updateWindows(counter, now);
        
        double windowProgress = (double)(now - counter.currentWindowStart) / config.getWindowMillis();
        double weightedCount = counter.currentWindowCount + 
                              (counter.previousWindowCount * (1 - windowProgress));
        
        return (int)Math.max(0, config.getMaxRequests() - weightedCount);
    }
}

