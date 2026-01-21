package strategy;

/**
 * No retry policy - tasks fail immediately without retries.
 */
public class NoRetryPolicy implements RetryPolicy {
    
    @Override
    public long getRetryDelay(int attemptNumber) {
        return 0;
    }
    
    @Override
    public String getStrategyName() {
        return "No Retry";
    }
}
