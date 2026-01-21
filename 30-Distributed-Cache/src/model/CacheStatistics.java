package model;

/**
 * CacheStatistics tracks cache performance metrics.
 */
public class CacheStatistics {
    private long hits;
    private long misses;
    private long evictions;
    private long expirations;

    public CacheStatistics() {
        this.hits = 0;
        this.misses = 0;
        this.evictions = 0;
        this.expirations = 0;
    }

    public synchronized void recordHit() {
        hits++;
    }

    public synchronized void recordMiss() {
        misses++;
    }

    public synchronized void recordEviction() {
        evictions++;
    }

    public synchronized void recordExpiration() {
        expirations++;
    }

    public long getHits() {
        return hits;
    }

    public long getMisses() {
        return misses;
    }

    public long getEvictions() {
        return evictions;
    }

    public long getExpirations() {
        return expirations;
    }

    public long getTotalRequests() {
        return hits + misses;
    }

    /**
     * Calculate hit rate as a percentage.
     */
    public double getHitRate() {
        long total = getTotalRequests();
        return total == 0 ? 0.0 : (double) hits / total * 100.0;
    }

    /**
     * Calculate miss rate as a percentage.
     */
    public double getMissRate() {
        long total = getTotalRequests();
        return total == 0 ? 0.0 : (double) misses / total * 100.0;
    }

    public void reset() {
        hits = 0;
        misses = 0;
        evictions = 0;
        expirations = 0;
    }

    @Override
    public String toString() {
        return String.format(
            "CacheStatistics{requests=%d, hits=%d, misses=%d, hitRate=%.2f%%, evictions=%d, expirations=%d}",
            getTotalRequests(), hits, misses, getHitRate(), evictions, expirations
        );
    }
}
