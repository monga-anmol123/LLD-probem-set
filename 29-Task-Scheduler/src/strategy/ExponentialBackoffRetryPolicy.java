package strategy;

/**
 * Exponential backoff retry policy.
 * Delay increases exponentially: baseDelay * (2 ^ attemptNumber)
 * Commonly used in distributed systems to avoid overwhelming resources.
 */
public class ExponentialBackoffRetryPolicy implements RetryPolicy {
    private final long baseDelayMillis;
    private final long maxDelayMillis;
    
    public ExponentialBackoffRetryPolicy(long baseDelayMillis, long maxDelayMillis) {
        this.baseDelayMillis = baseDelayMillis;
        this.maxDelayMillis = maxDelayMillis;
    }
    
    public ExponentialBackoffRetryPolicy(long baseDelayMillis) {
        this(baseDelayMillis, 60000); // Default max 60 seconds
    }
    
    @Override
    public long getRetryDelay(int attemptNumber) {
        // Calculate: baseDelay * (2 ^ attemptNumber)
        long delay = baseDelayMillis * (1L << attemptNumber);
        
        // Cap at maximum delay
        return Math.min(delay, maxDelayMillis);
    }
    
    @Override
    public String getStrategyName() {
        return String.format("Exponential Backoff (base=%dms, max=%dms)", 
                           baseDelayMillis, maxDelayMillis);
    }
}
