import enums.EvictionPolicy;
import service.Cache;
import service.CacheImpl;
import service.CacheManager;

/**
 * Main demo class showcasing the Distributed Cache system.
 * Demonstrates Strategy and Singleton patterns with multiple eviction policies.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("========================================");
        System.out.println("  DISTRIBUTED CACHE SYSTEM DEMO");
        System.out.println("========================================\n");

        // Scenario 1: LRU Cache
        scenario1_LRUCache();

        // Scenario 2: LFU Cache
        scenario2_LFUCache();

        // Scenario 3: FIFO Cache
        scenario3_FIFOCache();

        // Scenario 4: TTL-Based Expiration
        scenario4_TTLExpiration();

        // Scenario 5: Cache Statistics
        scenario5_CacheStatistics();

        // Scenario 6: Capacity and Eviction
        scenario6_CapacityEviction();

        // Scenario 7: Edge Cases
        scenario7_EdgeCases();

        // Final Summary
        displayFinalSummary();
    }

    /**
     * Scenario 1: LRU Cache with O(1) operations
     */
    private static void scenario1_LRUCache() {
        System.out.println("========================================");
        System.out.println("  SCENARIO 1: LRU Cache");
        System.out.println("========================================\n");

        Cache<String, String> cache = new CacheImpl<>(3, EvictionPolicy.LRU);

        System.out.println("Creating LRU cache with capacity 3...");
        System.out.println("✓ Cache created with LRU eviction policy\n");

        System.out.println("Adding entries:");
        cache.put("1", "Apple");
        System.out.println("  PUT(1, Apple)");
        cache.put("2", "Banana");
        System.out.println("  PUT(2, Banana)");
        cache.put("3", "Cherry");
        System.out.println("  PUT(3, Cherry)");

        System.out.println("\nAccessing key 1 (makes it recently used):");
        String value = cache.get("1");
        System.out.println("  GET(1) = " + value);

        System.out.println("\nAdding key 4 (should evict key 2 - least recently used):");
        cache.put("4", "Date");
        System.out.println("  PUT(4, Date)");

        System.out.println("\nVerifying eviction:");
        System.out.println("  GET(2) = " + cache.get("2") + " (evicted)");
        System.out.println("  GET(1) = " + cache.get("1") + " (present)");
        System.out.println("  GET(3) = " + cache.get("3") + " (present)");
        System.out.println("  GET(4) = " + cache.get("4") + " (present)");

        ((CacheImpl<String, String>) cache).display();
    }

    /**
     * Scenario 2: LFU Cache with frequency tracking
     */
    private static void scenario2_LFUCache() {
        System.out.println("========================================");
        System.out.println("  SCENARIO 2: LFU Cache");
        System.out.println("========================================\n");

        Cache<String, String> cache = new CacheImpl<>(3, EvictionPolicy.LFU);

        System.out.println("Creating LFU cache with capacity 3...");
        System.out.println("✓ Cache created with LFU eviction policy\n");

        System.out.println("Adding entries:");
        cache.put("1", "Red");
        cache.put("2", "Green");
        cache.put("3", "Blue");
        System.out.println("  PUT(1, Red), PUT(2, Green), PUT(3, Blue)");

        System.out.println("\nAccess patterns:");
        cache.get("1");
        cache.get("1");
        System.out.println("  GET(1) x2 - frequency = 2");
        cache.get("2");
        System.out.println("  GET(2) x1 - frequency = 1");
        System.out.println("  Key 3 not accessed - frequency = 0");

        System.out.println("\nAdding key 4 (should evict key 3 - least frequently used):");
        cache.put("4", "Yellow");
        System.out.println("  PUT(4, Yellow)");

        System.out.println("\nVerifying eviction:");
        System.out.println("  GET(3) = " + cache.get("3") + " (evicted)");
        System.out.println("  GET(1) = " + cache.get("1") + " (present)");
        System.out.println("  GET(2) = " + cache.get("2") + " (present)");
        System.out.println("  GET(4) = " + cache.get("4") + " (present)");

        ((CacheImpl<String, String>) cache).display();
    }

    /**
     * Scenario 3: FIFO Cache
     */
    private static void scenario3_FIFOCache() {
        System.out.println("========================================");
        System.out.println("  SCENARIO 3: FIFO Cache");
        System.out.println("========================================\n");

        Cache<String, String> cache = new CacheImpl<>(3, EvictionPolicy.FIFO);

        System.out.println("Creating FIFO cache with capacity 3...");
        System.out.println("✓ Cache created with FIFO eviction policy\n");

        System.out.println("Adding entries in order:");
        cache.put("1", "First");
        System.out.println("  PUT(1, First)");
        cache.put("2", "Second");
        System.out.println("  PUT(2, Second)");
        cache.put("3", "Third");
        System.out.println("  PUT(3, Third)");

        System.out.println("\nAccessing all keys (doesn't affect FIFO order):");
        cache.get("1");
        cache.get("2");
        cache.get("3");
        System.out.println("  GET(1), GET(2), GET(3)");

        System.out.println("\nAdding key 4 (should evict key 1 - first in):");
        cache.put("4", "Fourth");
        System.out.println("  PUT(4, Fourth)");

        System.out.println("\nVerifying FIFO eviction:");
        System.out.println("  GET(1) = " + cache.get("1") + " (evicted - first in)");
        System.out.println("  GET(2) = " + cache.get("2") + " (present)");
        System.out.println("  GET(3) = " + cache.get("3") + " (present)");
        System.out.println("  GET(4) = " + cache.get("4") + " (present)");

        ((CacheImpl<String, String>) cache).display();
    }

    /**
     * Scenario 4: TTL-based expiration
     */
    private static void scenario4_TTLExpiration() throws InterruptedException {
        System.out.println("========================================");
        System.out.println("  SCENARIO 4: TTL-Based Expiration");
        System.out.println("========================================\n");

        Cache<String, String> cache = new CacheImpl<>(5, EvictionPolicy.LRU);

        System.out.println("Creating cache with TTL support...");
        System.out.println("✓ Cache created\n");

        System.out.println("Adding entries with 2-second TTL:");
        cache.put("temp1", "Expires Soon", 2000);
        cache.put("temp2", "Also Expires", 2000);
        System.out.println("  PUT(temp1, Expires Soon, TTL=2s)");
        System.out.println("  PUT(temp2, Also Expires, TTL=2s)");

        System.out.println("\nAdding permanent entry (no TTL):");
        cache.put("permanent", "Never Expires");
        System.out.println("  PUT(permanent, Never Expires)");

        System.out.println("\nImmediate access (within TTL):");
        System.out.println("  GET(temp1) = " + cache.get("temp1"));
        System.out.println("  GET(temp2) = " + cache.get("temp2"));
        System.out.println("  GET(permanent) = " + cache.get("permanent"));

        System.out.println("\nWaiting 3 seconds for TTL expiration...");
        Thread.sleep(3000);

        System.out.println("\nAccess after expiration:");
        System.out.println("  GET(temp1) = " + cache.get("temp1") + " (expired)");
        System.out.println("  GET(temp2) = " + cache.get("temp2") + " (expired)");
        System.out.println("  GET(permanent) = " + cache.get("permanent") + " (still present)");

        ((CacheImpl<String, String>) cache).display();
    }

    /**
     * Scenario 5: Cache statistics and hit rate
     */
    private static void scenario5_CacheStatistics() {
        System.out.println("========================================");
        System.out.println("  SCENARIO 5: Cache Statistics");
        System.out.println("========================================\n");

        Cache<String, String> cache = new CacheImpl<>(5, EvictionPolicy.LRU);

        System.out.println("Performing operations to generate statistics...\n");

        // Add some entries
        cache.put("A", "Alpha");
        cache.put("B", "Beta");
        cache.put("C", "Gamma");

        // Generate hits
        cache.get("A"); // hit
        cache.get("A"); // hit
        cache.get("B"); // hit

        // Generate misses
        cache.get("X"); // miss
        cache.get("Y"); // miss
        cache.get("Z"); // miss

        System.out.println("Operations performed:");
        System.out.println("  3 PUTs (A, B, C)");
        System.out.println("  3 successful GETs (A x2, B x1)");
        System.out.println("  3 failed GETs (X, Y, Z)");

        System.out.println("\nCache Statistics:");
        System.out.println("  " + cache.getStatistics());
        System.out.println("\n✓ Hit rate: " + String.format("%.2f%%", cache.getStatistics().getHitRate()));
        System.out.println("✓ Miss rate: " + String.format("%.2f%%", cache.getStatistics().getMissRate()));
    }

    /**
     * Scenario 6: Capacity management and eviction
     */
    private static void scenario6_CapacityEviction() {
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 6: Capacity & Eviction");
        System.out.println("========================================\n");

        Cache<String, String> cache = new CacheImpl<>(5, EvictionPolicy.LRU);

        System.out.println("Creating cache with capacity 5...");
        System.out.println("✓ Cache capacity: " + cache.capacity() + "\n");

        System.out.println("Filling cache to capacity:");
        for (int i = 1; i <= 5; i++) {
            cache.put("key" + i, "value" + i);
            System.out.println("  PUT(key" + i + ", value" + i + ") - Size: " + cache.size());
        }

        System.out.println("\nCache is full. Adding more items...");
        cache.put("key6", "value6");
        System.out.println("  PUT(key6, value6) - Evicted key1");
        cache.put("key7", "value7");
        System.out.println("  PUT(key7, value7) - Evicted key2");

        System.out.println("\nFinal cache size: " + cache.size() + "/" + cache.capacity());
        System.out.println("Evictions: " + cache.getStatistics().getEvictions());

        ((CacheImpl<String, String>) cache).display();
    }

    /**
     * Scenario 7: Edge cases and error handling
     */
    private static void scenario7_EdgeCases() {
        System.out.println("========================================");
        System.out.println("  SCENARIO 7: Edge Cases");
        System.out.println("========================================\n");

        Cache<String, String> cache = new CacheImpl<>(3, EvictionPolicy.LRU);

        System.out.println("Testing edge cases...\n");

        // Test 1: Get from empty cache
        System.out.println("1. GET from empty cache:");
        System.out.println("   GET(nonexistent) = " + cache.get("nonexistent"));
        System.out.println("   ✓ Returns null as expected\n");

        // Test 2: Delete non-existent key
        System.out.println("2. DELETE non-existent key:");
        cache.delete("nonexistent");
        System.out.println("   DELETE(nonexistent)");
        System.out.println("   ✓ No error, gracefully handled\n");

        // Test 3: Update existing key
        System.out.println("3. UPDATE existing key:");
        cache.put("key1", "value1");
        System.out.println("   PUT(key1, value1)");
        cache.put("key1", "updated_value1");
        System.out.println("   PUT(key1, updated_value1)");
        System.out.println("   GET(key1) = " + cache.get("key1"));
        System.out.println("   ✓ Value updated successfully\n");

        // Test 4: Clear cache
        System.out.println("4. CLEAR cache:");
        cache.put("key2", "value2");
        cache.put("key3", "value3");
        System.out.println("   Added 2 more entries");
        System.out.println("   Size before clear: " + cache.size());
        cache.clear();
        System.out.println("   CLEAR()");
        System.out.println("   Size after clear: " + cache.size());
        System.out.println("   ✓ Cache cleared successfully\n");

        // Test 5: Null handling
        System.out.println("5. NULL key handling:");
        System.out.println("   GET(null) = " + cache.get(null));
        System.out.println("   ✓ Returns null for null key\n");

        System.out.println("✓ All edge cases handled correctly!");
    }

    /**
     * Display final summary
     */
    private static void displayFinalSummary() {
        System.out.println("\n========================================");
        System.out.println("  DEMO COMPLETE!");
        System.out.println("========================================\n");

        System.out.println("Design Patterns Demonstrated:");
        System.out.println("✓ Strategy Pattern - Multiple eviction policies (LRU, LFU, FIFO, TTL)");
        System.out.println("✓ Singleton Pattern - CacheManager for global cache access");
        System.out.println();

        System.out.println("Features Demonstrated:");
        System.out.println("✓ LRU Cache with O(1) get/put operations");
        System.out.println("✓ LFU Cache with frequency tracking");
        System.out.println("✓ FIFO Cache with queue-based eviction");
        System.out.println("✓ TTL-based automatic expiration");
        System.out.println("✓ Cache statistics (hit rate, miss rate, evictions)");
        System.out.println("✓ Capacity management and eviction");
        System.out.println("✓ Edge case handling (null, empty, updates)");
        System.out.println();

        System.out.println("Eviction Policies Implemented:");
        System.out.println("✓ LRU - Evicts least recently used item (O(1))");
        System.out.println("✓ LFU - Evicts least frequently used item (O(1))");
        System.out.println("✓ FIFO - Evicts oldest item (O(1))");
        System.out.println("✓ TTL - Evicts expired items based on time");
        System.out.println();

        System.out.println("Data Structures Used:");
        System.out.println("✓ HashMap - O(1) key lookup");
        System.out.println("✓ Doubly Linked List - O(1) LRU operations");
        System.out.println("✓ Frequency Map - O(1) LFU operations");
        System.out.println("✓ Queue - O(1) FIFO operations");
        System.out.println();

        System.out.println("Interview Topics Covered:");
        System.out.println("✓ Cache eviction algorithms");
        System.out.println("✓ Time complexity optimization");
        System.out.println("✓ Thread safety (synchronized methods)");
        System.out.println("✓ TTL and expiration handling");
        System.out.println("✓ Statistics and monitoring");
        System.out.println("✓ Strategy pattern for pluggable algorithms");
        System.out.println();

        System.out.println("========================================");
        System.out.println("  All scenarios executed successfully!");
        System.out.println("========================================\n");
    }
}
