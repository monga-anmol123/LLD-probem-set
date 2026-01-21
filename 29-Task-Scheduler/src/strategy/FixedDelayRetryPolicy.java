package strategy;

/**
 * Fixed delay retry policy.
 * Uses the same delay between all retry attempts.
 */
public class FixedDelayRetryPolicy implements RetryPolicy {
    private final long delayMillis;
    
    public FixedDelayRetryPolicy(long delayMillis) {
        this.delayMillis = delayMillis;
    }
    
    @Override
    public long getRetryDelay(int attemptNumber) {
        return delayMillis;
    }
    
    @Override
    public String getStrategyName() {
        return String.format("Fixed Delay (%dms)", delayMillis);
    }
}
