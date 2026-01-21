package service;

import enums.ClientTier;
import enums.RateLimitAlgorithm;
import model.Client;
import model.RateLimitConfig;
import model.RateLimitInfo;
import strategy.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton service for managing rate limiters for different clients.
 */
public class RateLimiterService {
    private static RateLimiterService instance;
    
    private final Map<String, Client> clients;
    private final Map<String, RateLimiter> rateLimiters;
    private RateLimitAlgorithm defaultAlgorithm;
    
    private RateLimiterService() {
        this.clients = new ConcurrentHashMap<>();
        this.rateLimiters = new ConcurrentHashMap<>();
        this.defaultAlgorithm = RateLimitAlgorithm.TOKEN_BUCKET;
    }
    
    /**
     * Get the singleton instance.
     */
    public static synchronized RateLimiterService getInstance() {
        if (instance == null) {
            instance = new RateLimiterService();
        }
        return instance;
    }
    
    /**
     * Register a new client.
     */
    public Client registerClient(String clientId, String name, ClientTier tier) {
        Client client = new Client(clientId, name, tier);
        clients.put(clientId, client);
        
        // Create rate limiter for this client
        RateLimitConfig config = RateLimitConfig.fromTier(tier, defaultAlgorithm);
        RateLimiter limiter = createRateLimiter(config);
        rateLimiters.put(clientId, limiter);
        
        return client;
    }
    
    /**
     * Set the rate limiting algorithm for a specific client.
     */
    public void setClientAlgorithm(String clientId, RateLimitAlgorithm algorithm) {
        Client client = clients.get(clientId);
        if (client == null) {
            throw new IllegalArgumentException("Client not found: " + clientId);
        }
        
        RateLimitConfig config = RateLimitConfig.fromTier(client.getTier(), algorithm);
        RateLimiter limiter = createRateLimiter(config);
        rateLimiters.put(clientId, limiter);
    }
    
    /**
     * Set the default algorithm for new clients.
     */
    public void setDefaultAlgorithm(RateLimitAlgorithm algorithm) {
        this.defaultAlgorithm = algorithm;
    }
    
    /**
     * Check if a request should be allowed for a client.
     */
    public RateLimitInfo allowRequest(String clientId) {
        RateLimiter limiter = rateLimiters.get(clientId);
        if (limiter == null) {
            return RateLimitInfo.error("Client not found: " + clientId);
        }
        
        return limiter.allowRequest(clientId);
    }
    
    /**
     * Get remaining quota for a client.
     */
    public int getRemainingQuota(String clientId) {
        RateLimiter limiter = rateLimiters.get(clientId);
        if (limiter == null) {
            return 0;
        }
        return limiter.getRemainingQuota(clientId);
    }
    
    /**
     * Reset rate limiter for a client.
     */
    public void resetClient(String clientId) {
        RateLimiter limiter = rateLimiters.get(clientId);
        if (limiter != null) {
            limiter.reset(clientId);
        }
    }
    
    /**
     * Get client by ID.
     */
    public Client getClient(String clientId) {
        return clients.get(clientId);
    }
    
    /**
     * Get rate limiter for a client.
     */
    public RateLimiter getRateLimiter(String clientId) {
        return rateLimiters.get(clientId);
    }
    
    /**
     * Display statistics for all clients.
     */
    public void displayStatistics() {
        System.out.println("\n" + repeatString("=", 80));
        System.out.println("RATE LIMITER STATISTICS");
        System.out.println(repeatString("=", 80));
        System.out.println("Total Clients: " + clients.size());
        System.out.println("Default Algorithm: " + defaultAlgorithm);
        System.out.println("\nClients:");
        
        for (Client client : clients.values()) {
            RateLimiter limiter = rateLimiters.get(client.getClientId());
            int remaining = limiter != null ? limiter.getRemainingQuota(client.getClientId()) : 0;
            System.out.println("  " + client + " | Remaining: " + remaining + " | Algorithm: " + 
                             (limiter != null ? limiter.getAlgorithmName() : "None"));
        }
        
        System.out.println(repeatString("=", 80));
    }
    
    /**
     * Create a rate limiter based on configuration.
     */
    private RateLimiter createRateLimiter(RateLimitConfig config) {
        switch (config.getAlgorithm()) {
            case TOKEN_BUCKET:
                return new TokenBucketLimiter(config);
            case LEAKY_BUCKET:
                return new LeakyBucketLimiter(config);
            case FIXED_WINDOW:
                return new FixedWindowLimiter(config);
            case SLIDING_WINDOW_LOG:
                return new SlidingWindowLogLimiter(config);
            case SLIDING_WINDOW_COUNTER:
                return new SlidingWindowCounterLimiter(config);
            default:
                return new TokenBucketLimiter(config);
        }
    }
    
    /**
     * Helper method to repeat a string n times (Java 8 compatible).
     */
    private String repeatString(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}

