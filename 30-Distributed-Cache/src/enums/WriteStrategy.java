package enums;

/**
 * Enum representing different cache write strategies.
 */
public enum WriteStrategy {
    WRITE_THROUGH("Write to cache and database synchronously"),
    WRITE_BACK("Write to cache, async to database"),
    CACHE_ASIDE("Application manages cache and database");

    private final String description;

    WriteStrategy(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
