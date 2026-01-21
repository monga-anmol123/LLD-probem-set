package enums;

/**
 * Enum representing different cache eviction policies.
 */
public enum EvictionPolicy {
    LRU("Least Recently Used"),
    LFU("Least Frequently Used"),
    FIFO("First In First Out"),
    TTL("Time To Live"),
    RANDOM("Random Eviction");

    private final String description;

    EvictionPolicy(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
