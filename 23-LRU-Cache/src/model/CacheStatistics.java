package model;

/**
 * Tracks cache performance statistics
 */
public class CacheStatistics {
    private long hits;
    private long misses;
    private long evictions;
    private long expirations;
    private long puts;
    private long removes;
    
    public CacheStatistics() {
        this.hits = 0;
        this.misses = 0;
        this.evictions = 0;
        this.expirations = 0;
        this.puts = 0;
        this.removes = 0;
    }
    
    public void recordHit() {
        hits++;
    }
    
    public void recordMiss() {
        misses++;
    }
    
    public void recordEviction() {
        evictions++;
    }
    
    public void recordExpiration() {
        expirations++;
    }
    
    public void recordPut() {
        puts++;
    }
    
    public void recordRemove() {
        removes++;
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
    
    public long getPuts() {
        return puts;
    }
    
    public long getRemoves() {
        return removes;
    }
    
    public long getTotalOperations() {
        return hits + misses;
    }
    
    public double getHitRate() {
        long total = getTotalOperations();
        return total == 0 ? 0.0 : (double) hits / total * 100.0;
    }
    
    public void reset() {
        hits = 0;
        misses = 0;
        evictions = 0;
        expirations = 0;
        puts = 0;
        removes = 0;
    }
    
    public void display() {
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║              CACHE STATISTICS                          ║");
        System.out.println("╠════════════════════════════════════════════════════════╣");
        System.out.printf("║ Hits              : %-35d ║%n", hits);
        System.out.printf("║ Misses            : %-35d ║%n", misses);
        System.out.printf("║ Hit Rate          : %-34.2f%% ║%n", getHitRate());
        System.out.printf("║ Evictions         : %-35d ║%n", evictions);
        System.out.printf("║ Expirations       : %-35d ║%n", expirations);
        System.out.printf("║ Puts              : %-35d ║%n", puts);
        System.out.printf("║ Removes           : %-35d ║%n", removes);
        System.out.printf("║ Total Operations  : %-35d ║%n", getTotalOperations());
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }
}

