package service;

import model.URL;
import model.Analytics;
import enums.URLStatus;
import strategy.ShorteningStrategy;
import strategy.Base62CounterStrategy;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class URLShortenerService {
    private static URLShortenerService instance;
    
    private final Map<String, URL> shortToURL;
    private final Map<String, String> longToShort;
    private final Map<String, List<Analytics>> analytics;
    private ShorteningStrategy defaultStrategy;
    private static final int MAX_RETRIES = 5;
    private static final Pattern URL_PATTERN = Pattern.compile(
        "^(https?://)?([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$"
    );
    
    private URLShortenerService() {
        this.shortToURL = new ConcurrentHashMap<>();
        this.longToShort = new ConcurrentHashMap<>();
        this.analytics = new ConcurrentHashMap<>();
        this.defaultStrategy = new Base62CounterStrategy();
    }
    
    public static synchronized URLShortenerService getInstance() {
        if (instance == null) {
            instance = new URLShortenerService();
        }
        return instance;
    }
    
    public void setDefaultStrategy(ShorteningStrategy strategy) {
        this.defaultStrategy = strategy;
    }
    
    public URL shortenURL(String longURL, String userId) {
        return shortenURL(longURL, userId, null, null);
    }
    
    public URL shortenURL(String longURL, String userId, LocalDateTime expiresAt) {
        return shortenURL(longURL, userId, expiresAt, null);
    }
    
    public URL shortenURL(String longURL, String userId, LocalDateTime expiresAt, ShorteningStrategy strategy) {
        // Validate URL
        if (!isValidURL(longURL)) {
            throw new IllegalArgumentException("Invalid URL format: " + longURL);
        }
        
        // Check if already shortened
        if (longToShort.containsKey(longURL)) {
            String existingCode = longToShort.get(longURL);
            URL existingURL = shortToURL.get(existingCode);
            if (existingURL.isActive()) {
                System.out.println("URL already shortened: " + existingCode);
                return existingURL;
            }
        }
        
        // Use provided strategy or default
        ShorteningStrategy strategyToUse = strategy != null ? strategy : defaultStrategy;
        
        // Generate unique short code
        String shortCode = generateUniqueShortCode(longURL, strategyToUse);
        
        // Create URL object
        URL url = new URL(shortCode, longURL, userId, expiresAt);
        
        // Store mappings
        shortToURL.put(shortCode, url);
        longToShort.put(longURL, shortCode);
        analytics.put(shortCode, new ArrayList<>());
        
        System.out.println("✓ Shortened: " + longURL);
        System.out.println("  Short code: " + shortCode);
        System.out.println("  Strategy: " + strategyToUse.getStrategyName());
        
        return url;
    }
    
    private String generateUniqueShortCode(String longURL, ShorteningStrategy strategy) {
        int attempts = 0;
        while (attempts < MAX_RETRIES) {
            String shortCode = strategy.generateShortCode(longURL);
            
            // Validate short code
            if (!isValidShortCode(shortCode)) {
                attempts++;
                continue;
            }
            
            // Check if available
            if (!shortToURL.containsKey(shortCode)) {
                return shortCode;
            }
            
            attempts++;
        }
        
        throw new RuntimeException("Failed to generate unique short code after " + MAX_RETRIES + " attempts");
    }
    
    public String redirect(String shortCode) {
        return redirect(shortCode, null, null, null);
    }
    
    public String redirect(String shortCode, String referrer, String userAgent, String ipAddress) {
        URL url = shortToURL.get(shortCode);
        
        if (url == null) {
            System.out.println("❌ Short code not found: " + shortCode);
            return null;
        }
        
        if (url.isExpired()) {
            System.out.println("❌ URL expired: " + shortCode);
            url.setStatus(URLStatus.EXPIRED);
            return null;
        }
        
        if (url.getStatus() != URLStatus.ACTIVE) {
            System.out.println("❌ URL not active: " + shortCode + " (status: " + url.getStatus() + ")");
            return null;
        }
        
        // Track analytics
        Analytics analytic = new Analytics(shortCode, referrer, userAgent, ipAddress);
        analytics.get(shortCode).add(analytic);
        
        // Increment click count
        url.incrementClicks();
        
        System.out.println("✓ Redirecting " + shortCode + " -> " + url.getLongURL());
        
        return url.getLongURL();
    }
    
    public boolean deleteURL(String shortCode) {
        URL url = shortToURL.get(shortCode);
        if (url == null) {
            return false;
        }
        
        url.setStatus(URLStatus.DELETED);
        longToShort.remove(url.getLongURL());
        System.out.println("✓ Deleted: " + shortCode);
        return true;
    }
    
    public URL getURL(String shortCode) {
        return shortToURL.get(shortCode);
    }
    
    public List<URL> getUserURLs(String userId) {
        List<URL> userURLs = new ArrayList<>();
        for (URL url : shortToURL.values()) {
            if (url.getUserId().equals(userId)) {
                userURLs.add(url);
            }
        }
        return userURLs;
    }
    
    public List<Analytics> getAnalytics(String shortCode) {
        return analytics.getOrDefault(shortCode, new ArrayList<>());
    }
    
    public void printAnalytics(String shortCode) {
        URL url = shortToURL.get(shortCode);
        if (url == null) {
            System.out.println("URL not found");
            return;
        }
        
        List<Analytics> analyticsData = analytics.get(shortCode);
        
        System.out.println("\n========== ANALYTICS ==========");
        System.out.println("Short Code: " + shortCode);
        System.out.println("Long URL: " + url.getLongURL());
        System.out.println("Total Clicks: " + url.getClicks());
        System.out.println("Created: " + url.getCreatedAt());
        System.out.println("Status: " + url.getStatus());
        
        if (url.getExpiresAt() != null) {
            System.out.println("Expires: " + url.getExpiresAt());
        }
        
        if (!analyticsData.isEmpty()) {
            System.out.println("\nRecent Clicks:");
            int limit = Math.min(5, analyticsData.size());
            for (int i = analyticsData.size() - limit; i < analyticsData.size(); i++) {
                System.out.println("  " + analyticsData.get(i));
            }
            
            // Referrer stats
            Map<String, Integer> referrerCounts = new HashMap<>();
            for (Analytics a : analyticsData) {
                String ref = a.getReferrer() != null ? a.getReferrer() : "direct";
                referrerCounts.put(ref, referrerCounts.getOrDefault(ref, 0) + 1);
            }
            
            System.out.println("\nTop Referrers:");
            referrerCounts.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(3)
                .forEach(e -> System.out.println("  " + e.getKey() + ": " + e.getValue() + " clicks"));
        }
        
        System.out.println("===============================\n");
    }
    
    private boolean isValidURL(String url) {
        return url != null && !url.trim().isEmpty() && URL_PATTERN.matcher(url).matches();
    }
    
    private boolean isValidShortCode(String shortCode) {
        if (shortCode == null || shortCode.length() < 3 || shortCode.length() > 20) {
            return false;
        }
        
        // Check for reserved words
        String[] reserved = {"admin", "api", "www", "http", "https", "delete", "create"};
        for (String word : reserved) {
            if (shortCode.equalsIgnoreCase(word)) {
                return false;
            }
        }
        
        return shortCode.matches("[a-zA-Z0-9]+");
    }
    
    public void cleanupExpiredURLs() {
        int cleaned = 0;
        for (URL url : shortToURL.values()) {
            if (url.isExpired() && url.getStatus() == URLStatus.ACTIVE) {
                url.setStatus(URLStatus.EXPIRED);
                cleaned++;
            }
        }
        if (cleaned > 0) {
            System.out.println("✓ Cleaned up " + cleaned + " expired URLs");
        }
    }
    
    public void printStats() {
        System.out.println("\n========== SYSTEM STATS ==========");
        System.out.println("Total URLs: " + shortToURL.size());
        System.out.println("Active URLs: " + shortToURL.values().stream()
            .filter(URL::isActive).count());
        System.out.println("Total Clicks: " + shortToURL.values().stream()
            .mapToInt(URL::getClicks).sum());
        System.out.println("==================================\n");
    }
}
