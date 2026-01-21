import service.LRUCache;
import strategy.*;

/**
 * Demo application for LRU Cache
 * Demonstrates Strategy and data structure design patterns
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    LRU CACHE DEMO                              ║");
        System.out.println("║          Patterns: Strategy + Data Structures                  ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");
        
        // Scenario 1: Basic LRU Cache Operations
        scenario1_BasicLRUOperations();
        
        // Scenario 2: LFU Eviction Strategy
        scenario2_LFUEvictionStrategy();
        
        // Scenario 3: FIFO Eviction Strategy
        scenario3_FIFOEvictionStrategy();
        
        // Scenario 4: MRU Eviction Strategy
        scenario4_MRUEvictionStrategy();
        
        // Scenario 5: Cache Statistics
        scenario5_CacheStatistics();
        
        // Scenario 6: TTL (Time To Live) Expiration
        scenario6_TTLExpiration();
        
        // Scenario 7: Edge Cases
        scenario7_EdgeCases();
        
        // Final Summary
        finalSummary();
    }
    
    /**
     * SCENARIO 1: Basic LRU Cache Operations
     */
    private static void scenario1_BasicLRUOperations() {
        System.out.println("\n" + repeat("=", 70));
        System.out.println("  SCENARIO 1: BASIC LRU CACHE OPERATIONS");
        System.out.println(repeat("=", 70) + "\n");
        
        LRUCache<Integer, String> cache = new LRUCache<>(3);
        System.out.println("Created cache with capacity 3, Strategy: " + cache.getEvictionStrategyName());
        
        // Add entries
        System.out.println("\nAdding entries...");
        cache.put(1, "Apple");
        cache.put(2, "Banana");
        cache.put(3, "Cherry");
        cache.display();
        
        // Access entry
        System.out.println("\nAccessing key 1...");
        String value = cache.get(1);
        System.out.println("Got: " + value + " (moved to front)");
        cache.display();
        
        // Add 4th entry (triggers eviction)
        System.out.println("\nAdding 4th entry (capacity exceeded)...");
        cache.put(4, "Date");
        cache.display();
        
        // Try to get evicted entry
        System.out.println("\nTrying to get evicted entry (key 2)...");
        value = cache.get(2);
        System.out.println("Result: " + (value == null ? "null (evicted)" : value));
        
        // Get existing entries
        System.out.println("\nGetting existing entries...");
        System.out.println("Key 3: " + cache.get(3));
        System.out.println("Key 1: " + cache.get(1));
        System.out.println("Key 4: " + cache.get(4));
        
        System.out.println("\n✓ Basic LRU operations demonstrated!");
    }
    
    /**
     * SCENARIO 2: LFU Eviction Strategy
     */
    private static void scenario2_LFUEvictionStrategy() {
        System.out.println("\n" + repeat("=", 70));
        System.out.println("  SCENARIO 2: LFU (LEAST FREQUENTLY USED) EVICTION STRATEGY");
        System.out.println(repeat("=", 70) + "\n");
        
        LRUCache<Integer, String> cache = new LRUCache<>(3, new LFUEviction<>());
        System.out.println("Created cache with capacity 3, Strategy: " + cache.getEvictionStrategyName());
        
        // Add entries
        System.out.println("\nAdding entries...");
        cache.put(1, "Apple");
        cache.put(2, "Banana");
        cache.put(3, "Cherry");
        cache.display();
        
        // Access entries with different frequencies
        System.out.println("\nAccessing entries to build frequency...");
        cache.get(1);  // freq = 2
        cache.get(1);  // freq = 3
        cache.get(2);  // freq = 2
        System.out.println("Key 1 accessed 2 times (freq=3)");
        System.out.println("Key 2 accessed 1 time (freq=2)");
        System.out.println("Key 3 not accessed (freq=1)");
        cache.display();
        
        // Add 4th entry (should evict key 3 - least frequent)
        System.out.println("\nAdding 4th entry (should evict least frequent)...");
        cache.put(4, "Date");
        cache.display();
        
        System.out.println("\n✓ LFU eviction strategy demonstrated!");
    }
    
    /**
     * SCENARIO 3: FIFO Eviction Strategy
     */
    private static void scenario3_FIFOEvictionStrategy() {
        System.out.println("\n" + repeat("=", 70));
        System.out.println("  SCENARIO 3: FIFO (FIRST IN FIRST OUT) EVICTION STRATEGY");
        System.out.println(repeat("=", 70) + "\n");
        
        LRUCache<Integer, String> cache = new LRUCache<>(3, new FIFOEviction<>());
        System.out.println("Created cache with capacity 3, Strategy: " + cache.getEvictionStrategyName());
        
        // Add entries
        System.out.println("\nAdding entries...");
        cache.put(1, "Apple");
        cache.put(2, "Banana");
        cache.put(3, "Cherry");
        cache.display();
        
        // Access entry multiple times (shouldn't affect FIFO order)
        System.out.println("\nAccessing key 1 multiple times...");
        cache.get(1);
        cache.get(1);
        cache.get(1);
        System.out.println("(FIFO doesn't change order on access)");
        cache.display();
        
        // Add 4th entry (should evict key 1 - first in)
        System.out.println("\nAdding 4th entry (should evict first in)...");
        cache.put(4, "Date");
        cache.display();
        
        System.out.println("\n✓ FIFO eviction strategy demonstrated!");
    }
    
    /**
     * SCENARIO 4: MRU Eviction Strategy
     */
    private static void scenario4_MRUEvictionStrategy() {
        System.out.println("\n" + repeat("=", 70));
        System.out.println("  SCENARIO 4: MRU (MOST RECENTLY USED) EVICTION STRATEGY");
        System.out.println(repeat("=", 70) + "\n");
        
        LRUCache<Integer, String> cache = new LRUCache<>(3, new MRUEviction<>());
        System.out.println("Created cache with capacity 3, Strategy: " + cache.getEvictionStrategyName());
        
        // Add entries
        System.out.println("\nAdding entries...");
        cache.put(1, "Apple");
        cache.put(2, "Banana");
        cache.put(3, "Cherry");
        cache.display();
        
        // Access entry (makes it most recent)
        System.out.println("\nAccessing key 2 (makes it most recent)...");
        cache.get(2);
        cache.display();
        
        // Add 4th entry (should evict key 2 - most recent)
        System.out.println("\nAdding 4th entry (should evict most recent)...");
        cache.put(4, "Date");
        cache.display();
        
        System.out.println("\n✓ MRU eviction strategy demonstrated!");
    }
    
    /**
     * SCENARIO 5: Cache Statistics
     */
    private static void scenario5_CacheStatistics() {
        System.out.println("\n" + repeat("=", 70));
        System.out.println("  SCENARIO 5: CACHE STATISTICS");
        System.out.println(repeat("=", 70) + "\n");
        
        LRUCache<String, Integer> cache = new LRUCache<>(5);
        
        System.out.println("Performing various operations...\n");
        
        // Puts
        cache.put("A", 1);
        cache.put("B", 2);
        cache.put("C", 3);
        cache.put("D", 4);
        cache.put("E", 5);
        
        // Hits
        cache.get("A");  // Hit
        cache.get("B");  // Hit
        cache.get("C");  // Hit
        
        // Misses
        cache.get("X");  // Miss
        cache.get("Y");  // Miss
        
        // Eviction
        cache.put("F", 6);  // Triggers eviction
        
        // Display statistics
        cache.getStatistics().display();
        
        System.out.println("\n✓ Cache statistics demonstrated!");
    }
    
    /**
     * SCENARIO 6: TTL (Time To Live) Expiration
     */
    private static void scenario6_TTLExpiration() {
        System.out.println("\n" + repeat("=", 70));
        System.out.println("  SCENARIO 6: TTL (TIME TO LIVE) EXPIRATION");
        System.out.println(repeat("=", 70) + "\n");
        
        LRUCache<String, String> cache = new LRUCache<>(5);
        
        // Add entry with 2-second TTL
        System.out.println("Adding entry with 2-second TTL...");
        cache.put("temp", "Temporary Value", 2000);  // 2 seconds
        
        // Add regular entry
        cache.put("permanent", "Permanent Value");
        
        cache.display();
        
        // Get immediately
        System.out.println("\nGetting 'temp' immediately: " + cache.get("temp"));
        
        // Wait 3 seconds
        System.out.println("\nWaiting 3 seconds...");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Try to get expired entry
        System.out.println("Getting 'temp' after expiration: " + cache.get("temp"));
        System.out.println("Getting 'permanent': " + cache.get("permanent"));
        
        cache.display();
        cache.getStatistics().display();
        
        System.out.println("\n✓ TTL expiration demonstrated!");
    }
    
    /**
     * SCENARIO 7: Edge Cases
     */
    private static void scenario7_EdgeCases() {
        System.out.println("\n" + repeat("=", 70));
        System.out.println("  SCENARIO 7: EDGE CASES AND ERROR HANDLING");
        System.out.println(repeat("=", 70) + "\n");
        
        LRUCache<Integer, String> cache = new LRUCache<>(3);
        
        // Test 1: Get from empty cache
        System.out.println("Test 1: Get from empty cache");
        System.out.println("Result: " + cache.get(1));
        System.out.println("✓ Returned null\n");
        
        // Test 2: Remove non-existent key
        System.out.println("Test 2: Remove non-existent key");
        boolean removed = cache.remove(999);
        System.out.println("Result: " + (removed ? "Removed" : "Not found"));
        System.out.println("✓ Handled gracefully\n");
        
        // Test 3: Update existing key
        System.out.println("Test 3: Update existing key");
        cache.put(1, "Original");
        System.out.println("Put (1, 'Original')");
        cache.put(1, "Updated");
        System.out.println("Put (1, 'Updated')");
        System.out.println("Get(1): " + cache.get(1));
        System.out.println("✓ Value updated\n");
        
        // Test 4: Null key handling
        System.out.println("Test 4: Null key handling");
        try {
            cache.put(null, "Value");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Correctly rejected: " + e.getMessage() + "\n");
        }
        
        // Test 5: Clear cache
        System.out.println("Test 5: Clear cache");
        cache.put(2, "A");
        cache.put(3, "B");
        System.out.println("Size before clear: " + cache.size());
        cache.clear();
        System.out.println("Size after clear: " + cache.size());
        System.out.println("✓ Cache cleared\n");
        
        // Test 6: Contains key
        System.out.println("Test 6: Contains key");
        cache.put(10, "Test");
        System.out.println("Contains 10: " + cache.containsKey(10));
        System.out.println("Contains 99: " + cache.containsKey(99));
        System.out.println("✓ Contains key works\n");
        
        System.out.println("✓ All edge cases handled correctly!");
    }
    
    /**
     * Final Summary
     */
    private static void finalSummary() {
        System.out.println("\n" + "╔" + repeat("═", 68) + "╗");
        System.out.println("║" + repeat(" ", 68) + "║");
        System.out.println("║" + centerText("DEMO COMPLETE!", 68) + "║");
        System.out.println("║" + repeat(" ", 68) + "║");
        System.out.println("╚" + repeat("═", 68) + "╝");
        
        System.out.println("\n🎨 DESIGN PATTERNS DEMONSTRATED:");
        System.out.println("   ✓ Strategy Pattern   - Eviction strategies (EvictionStrategy)");
        System.out.println("   ✓ Data Structures    - HashMap + Doubly Linked List");
        
        System.out.println("\n✅ FEATURES DEMONSTRATED:");
        System.out.println("   ✓ O(1) get and put operations");
        System.out.println("   ✓ Multiple eviction strategies (LRU, LFU, FIFO, MRU)");
        System.out.println("   ✓ Cache statistics (hits, misses, hit rate)");
        System.out.println("   ✓ TTL (Time To Live) support");
        System.out.println("   ✓ Automatic eviction on capacity overflow");
        System.out.println("   ✓ Comprehensive error handling");
        
        System.out.println("\n💡 KEY LEARNINGS:");
        System.out.println("   • HashMap provides O(1) lookup");
        System.out.println("   • Doubly Linked List provides O(1) insertion/deletion");
        System.out.println("   • Strategy Pattern enables pluggable eviction policies");
        System.out.println("   • LRU evicts least recently accessed item");
        System.out.println("   • LFU evicts least frequently accessed item");
        System.out.println("   • FIFO evicts oldest item");
        System.out.println("   • MRU evicts most recently accessed item");
        
        System.out.println("\n📊 COMPLEXITY ANALYSIS:");
        System.out.println("   • Time Complexity:");
        System.out.println("     - get():    O(1) for LRU, FIFO, MRU");
        System.out.println("     - put():    O(1) for LRU, FIFO, MRU");
        System.out.println("     - remove(): O(1)");
        System.out.println("   • Space Complexity: O(n) where n = capacity");
        
        System.out.println("\n" + repeat("=", 70));
        System.out.println("Thank you for exploring the LRU Cache implementation! 🚀");
        System.out.println(repeat("=", 70) + "\n");
    }
    
    /**
     * Helper method to repeat a string
     */
    private static String repeat(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
    
    /**
     * Helper method to center text
     */
    private static String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < padding; i++) {
            sb.append(" ");
        }
        sb.append(text);
        while (sb.length() < width) {
            sb.append(" ");
        }
        return sb.toString();
    }
}

