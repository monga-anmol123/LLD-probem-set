package strategy;

import model.RateLimitConfig;
import model.RateLimitInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Fixed Window rate limiting algorithm.
 * Counts requests in fixed time windows. Simple but can allow bursts at window boundaries.
 */
public class FixedWindowLimiter implements RateLimiter {
    private final RateLimitConfig config;
    private final Map<String, WindowState> windows;
    
    private static class WindowState {
        int requestCount;
        long windowStart;
        
        WindowState() {
            this.requestCount = 0;
            this.windowStart = System.currentTimeMillis();
        }
    }
    
    public FixedWindowLimiter(RateLimitConfig config) {
        this.config = config;
        this.windows = new ConcurrentHashMap<>();
    }
    
    @Override
    public synchronized RateLimitInfo allowRequest(String clientId) {
        long now = System.currentTimeMillis();
        WindowState window = windows.computeIfAbsent(clientId, k -> new WindowState());
        
        // Check if we need to reset the window
        if (now - window.windowStart >= config.getWindowMillis()) {
            window.requestCount = 0;
            window.windowStart = now;
        }
        
        if (window.requestCount < config.getMaxRequests()) {
            window.requestCount++;
            int remaining = config.getMaxRequests() - window.requestCount;
            long resetTime = window.windowStart + config.getWindowMillis();
            return RateLimitInfo.allowed(remaining, resetTime);
        } else {
            long resetTime = window.windowStart + config.getWindowMillis();
            return RateLimitInfo.rejected(resetTime);
        }
    }
    
    @Override
    public String getAlgorithmName() {
        return "Fixed Window";
    }
    
    @Override
    public void reset(String clientId) {
        windows.remove(clientId);
    }
    
    @Override
    public int getRemainingQuota(String clientId) {
        WindowState window = windows.get(clientId);
        if (window == null) {
            return config.getMaxRequests();
        }
        
        long now = System.currentTimeMillis();
        if (now - window.windowStart >= config.getWindowMillis()) {
            return config.getMaxRequests();
        }
        
        return config.getMaxRequests() - window.requestCount;
    }
}

