package enums;

/**
 * Enum representing different rate limiting algorithms.
 */
public enum RateLimitAlgorithm {
    TOKEN_BUCKET,           // Tokens refill at constant rate
    LEAKY_BUCKET,           // Process requests at constant rate
    FIXED_WINDOW,           // Fixed time window counter
    SLIDING_WINDOW_LOG,     // Precise tracking with timestamps
    SLIDING_WINDOW_COUNTER  // Hybrid approach (memory efficient)
}

