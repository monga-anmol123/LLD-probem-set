package strategy;

/**
 * Strategy interface for retry policies.
 * Determines the delay before retrying a failed task.
 */
public interface RetryPolicy {
    /**
     * Calculate the delay in milliseconds before the next retry attempt.
     * 
     * @param attemptNumber The retry attempt number (1 for first retry, 2 for second, etc.)
     * @return Delay in milliseconds
     */
    long getRetryDelay(int attemptNumber);
    
    /**
     * Get the name of this retry strategy.
     */
    String getStrategyName();
}
