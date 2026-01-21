package strategy;

/**
 * Linear backoff retry policy.
 * Delay increases linearly: baseDelay * attemptNumber
 */
public class LinearBackoffRetryPolicy implements RetryPolicy {
    private final long baseDelayMillis;
    private final long maxDelayMillis;
    
    public LinearBackoffRetryPolicy(long baseDelayMillis, long maxDelayMillis) {
        this.baseDelayMillis = baseDelayMillis;
        this.maxDelayMillis = maxDelayMillis;
    }
    
    public LinearBackoffRetryPolicy(long baseDelayMillis) {
        this(baseDelayMillis, 30000); // Default max 30 seconds
    }
    
    @Override
    public long getRetryDelay(int attemptNumber) {
        // Calculate: baseDelay * attemptNumber
        long delay = baseDelayMillis * attemptNumber;
        
        // Cap at maximum delay
        return Math.min(delay, maxDelayMillis);
    }
    
    @Override
    public String getStrategyName() {
        return String.format("Linear Backoff (base=%dms, max=%dms)", 
                           baseDelayMillis, maxDelayMillis);
    }
}
