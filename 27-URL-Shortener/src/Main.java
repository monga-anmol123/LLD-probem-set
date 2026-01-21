import service.URLShortenerService;
import model.URL;
import strategy.*;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  URL SHORTENER SERVICE DEMO");
        System.out.println("========================================\n");
        
        URLShortenerService service = URLShortenerService.getInstance();
        
        // Scenario 1: Basic URL Shortening (Base62 Counter)
        System.out.println("========================================");
        System.out.println("  SCENARIO 1: Basic URL Shortening");
        System.out.println("========================================\n");
        
        URL url1 = service.shortenURL("https://www.example.com/very/long/url/path/to/resource", "user1");
        System.out.println("Result: " + url1);
        
        // Scenario 2: Hash-Based Strategy
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 2: Hash-Based Strategy");
        System.out.println("========================================\n");
        
        service.setDefaultStrategy(new HashBasedStrategy(6));
        URL url2 = service.shortenURL("https://github.com/user/repository/issues/123", "user1");
        System.out.println("Result: " + url2);
        
        // Scenario 3: Custom Alias
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 3: Custom Alias");
        System.out.println("========================================\n");
        
        System.out.println("Creating custom alias 'mylink':");
        URL url3 = service.shortenURL("https://www.google.com", "user2", null, 
            new CustomAliasStrategy("mylink"));
        System.out.println("Result: " + url3);
        
        // Scenario 4: URL Redirection & Analytics
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 4: URL Redirection");
        System.out.println("========================================\n");
        
        String shortCode1 = url1.getShortCode();
        System.out.println("Simulating clicks on: " + shortCode1);
        
        service.redirect(shortCode1, "https://google.com", "Mozilla/5.0", "192.168.1.1");
        service.redirect(shortCode1, "https://facebook.com", "Chrome/90.0", "192.168.1.2");
        service.redirect(shortCode1, null, "Safari/14.0", "192.168.1.3");
        service.redirect(shortCode1, "https://google.com", "Firefox/88.0", "192.168.1.4");
        service.redirect(shortCode1, "https://twitter.com", "Edge/90.0", "192.168.1.5");
        
        System.out.println("\nTotal clicks: " + url1.getClicks());
        
        // Scenario 5: Analytics Display
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 5: Analytics");
        System.out.println("========================================");
        
        service.printAnalytics(shortCode1);
        
        // Scenario 6: URL with Expiration
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 6: URL Expiration");
        System.out.println("========================================\n");
        
        LocalDateTime expiresIn5Seconds = LocalDateTime.now().plusSeconds(5);
        URL url4 = service.shortenURL("https://temporary-link.com/promo", "user3", expiresIn5Seconds);
        System.out.println("Created URL with expiration: " + url4.getShortCode());
        System.out.println("Expires at: " + expiresIn5Seconds);
        
        System.out.println("\nTrying to access immediately:");
        String result1 = service.redirect(url4.getShortCode());
        System.out.println("Access result: " + (result1 != null ? "SUCCESS" : "FAILED"));
        
        System.out.println("\nWaiting 6 seconds for expiration...");
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Trying to access after expiration:");
        String result2 = service.redirect(url4.getShortCode());
        System.out.println("Access result: " + (result2 != null ? "SUCCESS" : "FAILED"));
        
        // Scenario 7: Duplicate URL Handling
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 7: Duplicate URL");
        System.out.println("========================================\n");
        
        System.out.println("Shortening same URL again:");
        service.setDefaultStrategy(new Base62CounterStrategy());
        URL url5 = service.shortenURL("https://www.example.com/very/long/url/path/to/resource", "user1");
        System.out.println("Same short code returned: " + (url1.getShortCode().equals(url5.getShortCode())));
        
        // Scenario 8: Invalid URL
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 8: Invalid URL Handling");
        System.out.println("========================================\n");
        
        try {
            System.out.println("Trying to shorten invalid URL:");
            service.shortenURL("not-a-valid-url", "user1");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Caught exception: " + e.getMessage());
        }
        
        // Scenario 9: Delete URL
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 9: Delete URL");
        System.out.println("========================================\n");
        
        String shortCode2 = url2.getShortCode();
        System.out.println("Deleting URL: " + shortCode2);
        service.deleteURL(shortCode2);
        
        System.out.println("Trying to access deleted URL:");
        String result3 = service.redirect(shortCode2);
        System.out.println("Access result: " + (result3 != null ? "SUCCESS" : "FAILED"));
        
        // Scenario 10: User URLs
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 10: User's URLs");
        System.out.println("========================================\n");
        
        System.out.println("All URLs for user1:");
        for (URL url : service.getUserURLs("user1")) {
            System.out.println("  " + url);
        }
        
        // Scenario 11: Multiple Strategies
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 11: Strategy Comparison");
        System.out.println("========================================\n");
        
        String testURL = "https://www.test.com/page";
        
        System.out.println("Same URL with different strategies:");
        service.setDefaultStrategy(new Base62CounterStrategy());
        URL urlCounter = service.shortenURL(testURL + "?v=1", "user4");
        
        service.setDefaultStrategy(new HashBasedStrategy(7));
        URL urlHash = service.shortenURL(testURL + "?v=2", "user4");
        
        URL urlCustom = service.shortenURL(testURL + "?v=3", "user4", null, 
            new CustomAliasStrategy("custom123"));
        
        System.out.println("\nBase62Counter: " + urlCounter.getShortCode());
        System.out.println("HashBased: " + urlHash.getShortCode());
        System.out.println("CustomAlias: " + urlCustom.getShortCode());
        
        // Scenario 12: Cleanup Expired URLs
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 12: Cleanup Expired URLs");
        System.out.println("========================================\n");
        
        service.cleanupExpiredURLs();
        
        // Final Statistics
        System.out.println("\n========================================");
        System.out.println("  FINAL STATISTICS");
        System.out.println("========================================");
        
        service.printStats();
        
        // Summary
        System.out.println("\n========================================");
        System.out.println("  DEMO COMPLETE!");
        System.out.println("========================================");
        System.out.println("\nDesign Patterns Demonstrated:");
        System.out.println("✓ Strategy Pattern: Multiple shortening strategies");
        System.out.println("  - Base62CounterStrategy");
        System.out.println("  - HashBasedStrategy");
        System.out.println("  - CustomAliasStrategy");
        System.out.println("✓ Singleton Pattern: URLShortenerService");
        System.out.println("\nKey Features Demonstrated:");
        System.out.println("✓ URL shortening with Base62 encoding");
        System.out.println("✓ Multiple shortening strategies");
        System.out.println("✓ Custom alias support");
        System.out.println("✓ URL redirection");
        System.out.println("✓ Analytics tracking (clicks, referrers)");
        System.out.println("✓ URL expiration management");
        System.out.println("✓ Duplicate URL handling");
        System.out.println("✓ Invalid URL validation");
        System.out.println("✓ URL deletion");
        System.out.println("✓ User URL management");
        System.out.println("✓ Expired URL cleanup");
        System.out.println("✓ System statistics");
    }
}
