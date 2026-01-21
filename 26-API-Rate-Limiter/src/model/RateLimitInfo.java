package model;

import enums.RateLimitResult;

/**
 * Information about a rate limit check result.
 */
public class RateLimitInfo {
    private final RateLimitResult result;
    private final int remainingQuota;
    private final long resetTimeMillis;
    private final String message;
    
    public RateLimitInfo(RateLimitResult result, int remainingQuota, long resetTimeMillis, String message) {
        this.result = result;
        this.remainingQuota = remainingQuota;
        this.resetTimeMillis = resetTimeMillis;
        this.message = message;
    }
    
    public static RateLimitInfo allowed(int remainingQuota, long resetTimeMillis) {
        return new RateLimitInfo(RateLimitResult.ALLOWED, remainingQuota, resetTimeMillis, "Request allowed");
    }
    
    public static RateLimitInfo rejected(long resetTimeMillis) {
        return new RateLimitInfo(RateLimitResult.REJECTED, 0, resetTimeMillis, "Rate limit exceeded");
    }
    
    public static RateLimitInfo error(String message) {
        return new RateLimitInfo(RateLimitResult.ERROR, 0, 0, message);
    }
    
    // Getters
    public RateLimitResult getResult() {
        return result;
    }
    
    public int getRemainingQuota() {
        return remainingQuota;
    }
    
    public long getResetTimeMillis() {
        return resetTimeMillis;
    }
    
    public String getMessage() {
        return message;
    }
    
    public boolean isAllowed() {
        return result == RateLimitResult.ALLOWED;
    }
    
    @Override
    public String toString() {
        if (result == RateLimitResult.ALLOWED) {
            return "✅ ALLOWED (Remaining: " + remainingQuota + ", Reset in: " + 
                   (resetTimeMillis - System.currentTimeMillis()) / 1000 + "s)";
        } else if (result == RateLimitResult.REJECTED) {
            return "❌ REJECTED (Retry after: " + (resetTimeMillis - System.currentTimeMillis()) / 1000 + "s)";
        } else {
            return "⚠️  ERROR: " + message;
        }
    }
}

