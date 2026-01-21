package enums;

/**
 * Enum representing the result of a rate limit check.
 */
public enum RateLimitResult {
    ALLOWED,        // Request is allowed
    REJECTED,       // Request is rejected (rate limit exceeded)
    ERROR           // Error occurred during rate limit check
}

