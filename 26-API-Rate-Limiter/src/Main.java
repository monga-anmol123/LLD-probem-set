import enums.*;
import model.*;
import service.RateLimiterService;

/**
 * Main class demonstrating the API Rate Limiter system with various scenarios.
 */
public class Main {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println(repeatString("=", 80));
        System.out.println("  API RATE LIMITER SYSTEM DEMO");
        System.out.println(repeatString("=", 80) + "\n");
        
        // Scenario 1: Token Bucket - Normal Flow
        scenario1_TokenBucketNormalFlow();
        
        // Scenario 2: Fixed Window - Burst at Boundary
        scenario2_FixedWindowBurst();
        
        // Scenario 3: Sliding Window Log - Precise Tracking
        scenario3_SlidingWindowLogPrecise();
        
        // Scenario 4: Multi-Tier Clients
        scenario4_MultiTierClients();
        
        // Scenario 5: Algorithm Comparison
        scenario5_AlgorithmComparison();
        
        // Scenario 6: Rate Limit Recovery
        scenario6_RateLimitRecovery();
        
        System.out.println("\n" + repeatString("=", 80));
        System.out.println("  ALL SCENARIOS COMPLETED SUCCESSFULLY!");
        System.out.println(repeatString("=", 80) + "\n");
    }
    
    /**
     * Scenario 1: Token Bucket - Normal Flow
     */
    private static void scenario1_TokenBucketNormalFlow() throws InterruptedException {
        System.out.println("\n" + repeatString("-", 80));
        System.out.println("SCENARIO 1: Token Bucket - Normal Flow");
        System.out.println(repeatString("-", 80));
        
        RateLimiterService service = RateLimiterService.getInstance();
        service.setDefaultAlgorithm(RateLimitAlgorithm.TOKEN_BUCKET);
        
        // Register client with FREE tier (10 req/min)
        Client alice = service.registerClient("C001", "Alice", ClientTier.FREE);
        System.out.println("\n✅ Registered: " + alice);
        System.out.println("Rate Limit: " + alice.getTier().getMaxRequests() + " requests per " + 
                          alice.getTier().getWindowSeconds() + " seconds");
        
        // Send 5 requests - all should be allowed
        System.out.println("\n📤 Sending 5 requests...");
        for (int i = 1; i <= 5; i++) {
            RateLimitInfo info = service.allowRequest(alice.getClientId());
            System.out.println("  Request " + i + ": " + info);
        }
        
        // Send 8 more requests - 3 should be rejected (exceeded capacity of 10)
        System.out.println("\n📤 Sending 8 more requests...");
        for (int i = 6; i <= 13; i++) {
            RateLimitInfo info = service.allowRequest(alice.getClientId());
            System.out.println("  Request " + i + ": " + info);
        }
        
        // Wait 3 seconds for token refill
        System.out.println("\n⏳ Waiting 3 seconds for token refill...");
        Thread.sleep(3000);
        
        // Send 5 more requests - should be allowed (tokens refilled)
        System.out.println("\n📤 Sending 5 requests after refill...");
        for (int i = 1; i <= 5; i++) {
            RateLimitInfo info = service.allowRequest(alice.getClientId());
            System.out.println("  Request " + i + ": " + info);
        }
    }
    
    /**
     * Scenario 2: Fixed Window - Burst at Boundary
     */
    private static void scenario2_FixedWindowBurst() {
        System.out.println("\n" + repeatString("-", 80));
        System.out.println("SCENARIO 2: Fixed Window - Burst at Boundary Problem");
        System.out.println(repeatString("-", 80));
        
        RateLimiterService service = RateLimiterService.getInstance();
        
        // Register client with BASIC tier (100 req/min) using Fixed Window
        Client bob = service.registerClient("C002", "Bob", ClientTier.BASIC);
        service.setClientAlgorithm(bob.getClientId(), RateLimitAlgorithm.FIXED_WINDOW);
        
        System.out.println("\n✅ Registered: " + bob);
        System.out.println("Algorithm: Fixed Window");
        System.out.println("Rate Limit: " + bob.getTier().getMaxRequests() + " requests per " + 
                          bob.getTier().getWindowSeconds() + " seconds");
        
        // Send 100 requests - all allowed
        System.out.println("\n📤 Sending 100 requests...");
        int allowed = 0, rejected = 0;
        for (int i = 1; i <= 100; i++) {
            RateLimitInfo info = service.allowRequest(bob.getClientId());
            if (info.isAllowed()) allowed++;
            else rejected++;
        }
        System.out.println("  ✅ Allowed: " + allowed);
        System.out.println("  ❌ Rejected: " + rejected);
        
        // Try 10 more - should be rejected
        System.out.println("\n📤 Sending 10 more requests (should be rejected)...");
        allowed = 0;
        rejected = 0;
        for (int i = 1; i <= 10; i++) {
            RateLimitInfo info = service.allowRequest(bob.getClientId());
            if (info.isAllowed()) allowed++;
            else rejected++;
        }
        System.out.println("  ✅ Allowed: " + allowed);
        System.out.println("  ❌ Rejected: " + rejected);
        
        System.out.println("\n💡 Note: Fixed Window can allow bursts at window boundaries!");
    }
    
    /**
     * Scenario 3: Sliding Window Log - Precise Tracking
     */
    private static void scenario3_SlidingWindowLogPrecise() throws InterruptedException {
        System.out.println("\n" + repeatString("-", 80));
        System.out.println("SCENARIO 3: Sliding Window Log - Precise Tracking");
        System.out.println(repeatString("-", 80));
        
        RateLimiterService service = RateLimiterService.getInstance();
        
        // Register client with FREE tier using Sliding Window Log
        Client charlie = service.registerClient("C003", "Charlie", ClientTier.FREE);
        service.setClientAlgorithm(charlie.getClientId(), RateLimitAlgorithm.SLIDING_WINDOW_LOG);
        
        System.out.println("\n✅ Registered: " + charlie);
        System.out.println("Algorithm: Sliding Window Log (Most Precise)");
        System.out.println("Rate Limit: " + charlie.getTier().getMaxRequests() + " requests per " + 
                          charlie.getTier().getWindowSeconds() + " seconds");
        
        // Send 10 requests - all allowed
        System.out.println("\n📤 Sending 10 requests (filling quota)...");
        for (int i = 1; i <= 10; i++) {
            RateLimitInfo info = service.allowRequest(charlie.getClientId());
            System.out.println("  Request " + i + ": " + info);
        }
        
        // Try 1 more - should be rejected
        System.out.println("\n📤 Trying 1 more request (should be rejected)...");
        RateLimitInfo info = service.allowRequest(charlie.getClientId());
        System.out.println("  " + info);
        
        // Wait 2 seconds
        System.out.println("\n⏳ Waiting 2 seconds...");
        Thread.sleep(2000);
        
        // Try again - oldest requests should have expired
        System.out.println("\n📤 Trying request after 2 seconds...");
        info = service.allowRequest(charlie.getClientId());
        System.out.println("  " + info);
        
        System.out.println("\n💡 Sliding Window Log provides precise rate limiting!");
    }
    
    /**
     * Scenario 4: Multi-Tier Clients
     */
    private static void scenario4_MultiTierClients() {
        System.out.println("\n" + repeatString("-", 80));
        System.out.println("SCENARIO 4: Multi-Tier Clients");
        System.out.println(repeatString("-", 80));
        
        RateLimiterService service = RateLimiterService.getInstance();
        
        // Register clients with different tiers
        Client freeUser = service.registerClient("C004", "FreeUser", ClientTier.FREE);
        Client basicUser = service.registerClient("C005", "BasicUser", ClientTier.BASIC);
        Client premiumUser = service.registerClient("C006", "PremiumUser", ClientTier.PREMIUM);
        Client enterpriseUser = service.registerClient("C007", "EnterpriseUser", ClientTier.ENTERPRISE);
        
        System.out.println("\n✅ Registered Clients:");
        System.out.println("  " + freeUser);
        System.out.println("  " + basicUser);
        System.out.println("  " + premiumUser);
        System.out.println("  " + enterpriseUser);
        
        // Test each tier with 15 requests
        System.out.println("\n📤 Testing each tier with 15 requests:");
        
        testClientTier(service, freeUser, 15);
        testClientTier(service, basicUser, 15);
        testClientTier(service, premiumUser, 15);
        testClientTier(service, enterpriseUser, 15);
    }
    
    private static void testClientTier(RateLimiterService service, Client client, int numRequests) {
        System.out.println("\n  " + client.getName() + " (" + client.getTier() + "):");
        int allowed = 0, rejected = 0;
        
        for (int i = 1; i <= numRequests; i++) {
            RateLimitInfo info = service.allowRequest(client.getClientId());
            if (info.isAllowed()) allowed++;
            else rejected++;
        }
        
        System.out.println("    ✅ Allowed: " + allowed + " | ❌ Rejected: " + rejected);
    }
    
    /**
     * Scenario 5: Algorithm Comparison
     */
    private static void scenario5_AlgorithmComparison() {
        System.out.println("\n" + repeatString("-", 80));
        System.out.println("SCENARIO 5: Algorithm Comparison");
        System.out.println(repeatString("-", 80));
        
        RateLimiterService service = RateLimiterService.getInstance();
        
        // Create clients with same tier but different algorithms
        Client client1 = service.registerClient("C008", "TokenBucket", ClientTier.BASIC);
        service.setClientAlgorithm(client1.getClientId(), RateLimitAlgorithm.TOKEN_BUCKET);
        
        Client client2 = service.registerClient("C009", "LeakyBucket", ClientTier.BASIC);
        service.setClientAlgorithm(client2.getClientId(), RateLimitAlgorithm.LEAKY_BUCKET);
        
        Client client3 = service.registerClient("C010", "FixedWindow", ClientTier.BASIC);
        service.setClientAlgorithm(client3.getClientId(), RateLimitAlgorithm.FIXED_WINDOW);
        
        Client client4 = service.registerClient("C011", "SlidingLog", ClientTier.BASIC);
        service.setClientAlgorithm(client4.getClientId(), RateLimitAlgorithm.SLIDING_WINDOW_LOG);
        
        Client client5 = service.registerClient("C012", "SlidingCounter", ClientTier.BASIC);
        service.setClientAlgorithm(client5.getClientId(), RateLimitAlgorithm.SLIDING_WINDOW_COUNTER);
        
        System.out.println("\n📊 Comparing 5 Rate Limiting Algorithms:");
        System.out.println("  All clients: BASIC tier (100 req/min)");
        System.out.println("  Test: Send 110 requests to each");
        
        System.out.println("\n" + repeatString("-", 80));
        compareAlgorithm(service, client1, 110);
        compareAlgorithm(service, client2, 110);
        compareAlgorithm(service, client3, 110);
        compareAlgorithm(service, client4, 110);
        compareAlgorithm(service, client5, 110);
        System.out.println(repeatString("-", 80));
        
        System.out.println("\n💡 Each algorithm has different characteristics:");
        System.out.println("  • Token Bucket: Allows bursts, tokens refill gradually");
        System.out.println("  • Leaky Bucket: Smooths traffic, processes at constant rate");
        System.out.println("  • Fixed Window: Simple, but can allow bursts at boundaries");
        System.out.println("  • Sliding Window Log: Most accurate, memory intensive");
        System.out.println("  • Sliding Window Counter: Good balance of accuracy and efficiency");
    }
    
    private static void compareAlgorithm(RateLimiterService service, Client client, int numRequests) {
        int allowed = 0, rejected = 0;
        
        for (int i = 1; i <= numRequests; i++) {
            RateLimitInfo info = service.allowRequest(client.getClientId());
            if (info.isAllowed()) allowed++;
            else rejected++;
        }
        
        String algorithm = service.getRateLimiter(client.getClientId()).getAlgorithmName();
        System.out.printf("  %-25s | ✅ %3d allowed | ❌ %3d rejected%n", 
                         algorithm + ":", allowed, rejected);
    }
    
    /**
     * Scenario 6: Rate Limit Recovery
     */
    private static void scenario6_RateLimitRecovery() throws InterruptedException {
        System.out.println("\n" + repeatString("-", 80));
        System.out.println("SCENARIO 6: Rate Limit Recovery");
        System.out.println(repeatString("-", 80));
        
        RateLimiterService service = RateLimiterService.getInstance();
        
        Client david = service.registerClient("C013", "David", ClientTier.FREE);
        service.setClientAlgorithm(david.getClientId(), RateLimitAlgorithm.TOKEN_BUCKET);
        
        System.out.println("\n✅ Registered: " + david);
        System.out.println("Algorithm: Token Bucket");
        System.out.println("Rate Limit: " + david.getTier().getMaxRequests() + " requests per " + 
                          david.getTier().getWindowSeconds() + " seconds");
        
        // Exhaust quota
        System.out.println("\n📤 Exhausting quota (10 requests)...");
        for (int i = 1; i <= 10; i++) {
            service.allowRequest(david.getClientId());
        }
        
        // Try one more - should be rejected
        RateLimitInfo info = service.allowRequest(david.getClientId());
        System.out.println("  Next request: " + info);
        
        // Show recovery over time
        System.out.println("\n⏳ Monitoring quota recovery over 10 seconds:");
        for (int i = 0; i <= 10; i++) {
            int remaining = service.getRemainingQuota(david.getClientId());
            System.out.println("  After " + i + "s: Remaining quota = " + remaining);
            if (i < 10) Thread.sleep(1000);
        }
        
        // Try request after recovery
        System.out.println("\n📤 Trying request after recovery...");
        info = service.allowRequest(david.getClientId());
        System.out.println("  " + info);
        
        // Display final statistics
        service.displayStatistics();
    }
    
    /**
     * Helper method to repeat a string n times (Java 8 compatible).
     */
    private static String repeatString(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}

