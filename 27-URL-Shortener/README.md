# Problem 27: URL Shortener

## 🎯 Difficulty: Medium ⭐⭐⭐

## 📝 Problem Statement

Design a URL shortening service like Bitly or TinyURL that converts long URLs into short, shareable links. The system should support custom aliases, track analytics (clicks, referrers), handle expiration, and ensure no collisions.

**Real-World Context:** This is one of the MOST commonly asked system design problems at Google, Twitter, Bitly, and many startups. It tests your understanding of hashing, base conversion, database design, and scalability.

---

## 🔍 Functional Requirements (FR)

### FR1: URL Shortening
- Convert long URL to short code (e.g., `https://example.com/very/long/url` → `short.ly/abc123`)
- Generate unique short codes (6-8 characters)
- Support custom aliases (user-defined short codes)
- Validate URLs before shortening

### FR2: URL Redirection
- Redirect short URL to original long URL
- Track each redirect (analytics)
- Handle expired URLs gracefully
- Return 404 for non-existent short codes

### FR3: Analytics Tracking
- Track total clicks per short URL
- Track clicks by date/time
- Track referrer information
- Track geographic location (optional)
- Track user agent (browser/device)

### FR4: Custom Aliases
- Allow users to specify custom short codes
- Validate alias availability
- Prevent offensive/reserved words
- Enforce length constraints (3-20 characters)

### FR5: Expiration Management
- Support URL expiration (TTL)
- Auto-delete expired URLs
- Allow users to extend expiration
- Default expiration: never (or configurable)

### FR6: URL Management
- List all URLs for a user
- Delete URLs
- Update long URL (redirect changes)
- View analytics for a URL

---

## 🚫 Non-Functional Requirements (NFR)

### NFR1: Uniqueness
- No collisions (two URLs get same short code)
- Handle race conditions
- Retry on collision

### NFR2: Performance
- Fast URL shortening (<100ms)
- Fast redirection (<50ms)
- Handle high traffic (millions of requests/day)

### NFR3: Scalability
- Support billions of URLs
- Horizontal scaling
- Efficient storage

### NFR4: Availability
- 99.9% uptime
- Handle failures gracefully
- No data loss

---

## 🎨 Design Patterns to Use

### 1. **Strategy Pattern** ⭐⭐⭐ (Primary)
- **Where:** URL shortening algorithms
- **Why:** Multiple algorithms (hash-based, counter-based, custom)
- **Example:** Switch between MD5 hashing and Base62 counter

### 2. **Factory Pattern** ⭐⭐
- **Where:** Creating URL objects, Analytics objects
- **Why:** Centralize object creation
- **Example:** `URLFactory.createShortURL(longURL, strategy)`

### 3. **Singleton Pattern** ⭐⭐
- **Where:** URLShortenerService
- **Why:** Single instance managing all URLs
- **Example:** `URLShortenerService.getInstance()`

---

## 📋 Core Entities

### 1. **URL**
- Attributes: shortCode, longURL, createdAt, expiresAt, userId, clicks
- Methods: isExpired(), incrementClicks()

### 2. **Analytics**
- Attributes: shortCode, timestamp, referrer, userAgent, ipAddress
- Methods: getClicksByDate(), getTopReferrers()

### 3. **ShorteningStrategy** (Interface)
- Methods: generateShortCode(longURL)
- Implementations: HashBasedStrategy, CounterBasedStrategy, CustomAliasStrategy

### 4. **URLShortenerService** (Singleton)
- Methods: shortenURL(), redirect(), getAnalytics(), deleteURL()

---

## 🧪 Test Scenarios

### Scenario 1: Basic URL Shortening
```
1. User submits: https://www.example.com/very/long/url/path
2. System generates: abc123
3. Short URL: short.ly/abc123
Expected: Success, unique code generated
```

### Scenario 2: Custom Alias
```
1. User wants alias: "mylink"
2. Check availability
3. Create: short.ly/mylink
Expected: Custom alias created
```

### Scenario 3: Collision Handling
```
1. Hash generates: abc123
2. abc123 already exists
3. Retry with different approach
Expected: New unique code generated
```

### Scenario 4: URL Redirection
```
1. User visits: short.ly/abc123
2. System looks up long URL
3. Redirects to original URL
4. Increments click count
Expected: Successful redirect + analytics
```

### Scenario 5: Expired URL
```
1. URL created with 7-day expiration
2. 8 days later, user visits short URL
3. System checks expiration
Expected: 404 or "URL expired" message
```

### Scenario 6: Analytics Tracking
```
1. Short URL clicked 100 times
2. View analytics
3. See clicks by date, referrers
Expected: Detailed analytics displayed
```

### Scenario 7: Duplicate Long URL
```
1. User shortens URL A
2. Same user shortens URL A again
3. System checks if already exists
Expected: Return existing short code or create new
```

### Scenario 8: Invalid URL
```
1. User submits: "not-a-valid-url"
2. System validates
Expected: Error message
```

---

## ⏱️ Time Allocation (60 minutes)

- **5 mins:** Clarify requirements
- **10 mins:** Design class structure
- **30 mins:** Implement core classes (Strategy, Service, Models)
- **10 mins:** Write Main.java with scenarios
- **5 mins:** Test and verify

---

## 💡 Hints

<details>
<summary>Hint 1: Base62 Encoding</summary>

Use Base62 (a-z, A-Z, 0-9) for short codes:
```java
private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

public String toBase62(long num) {
    StringBuilder sb = new StringBuilder();
    while (num > 0) {
        sb.append(BASE62.charAt((int)(num % 62)));
        num /= 62;
    }
    return sb.reverse().toString();
}
```

6 characters = 62^6 = 56 billion combinations
</details>

<details>
<summary>Hint 2: Hash-Based Strategy</summary>

Use MD5 hash of URL + timestamp:
```java
String input = longURL + System.currentTimeMillis();
String hash = MD5(input);
String shortCode = hash.substring(0, 6); // Take first 6 chars
```

Handle collisions by rehashing with different salt.
</details>

<details>
<summary>Hint 3: Counter-Based Strategy</summary>

Use auto-incrementing counter:
```java
private static long counter = 1000000; // Start from 1M

public String generateShortCode(String longURL) {
    long id = counter++;
    return toBase62(id); // Convert to Base62
}
```

Pros: No collisions  
Cons: Predictable, need distributed counter for scale
</details>

<details>
<summary>Hint 4: Collision Handling</summary>

```java
public String generateUniqueCode(String longURL) {
    int attempts = 0;
    while (attempts < MAX_RETRIES) {
        String code = strategy.generateShortCode(longURL);
        if (!exists(code)) {
            return code;
        }
        attempts++;
    }
    throw new RuntimeException("Failed to generate unique code");
}
```
</details>

<details>
<summary>Hint 5: Analytics Storage</summary>

Store analytics separately from URLs:
```java
Map<String, URL> urls; // shortCode -> URL
Map<String, List<Analytics>> analytics; // shortCode -> List<Analytics>
```

This allows efficient querying without loading all analytics.
</details>

---

## ✅ Success Criteria

- [ ] Code compiles without errors
- [ ] Uses Strategy pattern (3+ strategies)
- [ ] Uses Factory pattern for object creation
- [ ] Singleton pattern for service
- [ ] Handles all 8 test scenarios
- [ ] Base62 encoding implemented
- [ ] Collision handling works
- [ ] Analytics tracking functional
- [ ] Expiration management works
- [ ] Custom aliases supported

---

## 🎓 Learning Objectives

After completing this problem, you should understand:

1. **Base Conversion:** Base62 encoding for compact URLs
2. **Hashing:** MD5/SHA for generating short codes
3. **Collision Handling:** Strategies to handle hash collisions
4. **Analytics:** Tracking and aggregating click data
5. **Expiration:** TTL-based resource management
6. **Validation:** URL and alias validation

---

## 🚀 Extensions (If Time Permits)

1. **QR Code Generation:** Generate QR codes for short URLs
2. **Rate Limiting:** Prevent abuse (max URLs per user/day)
3. **Bulk Shortening:** Shorten multiple URLs at once
4. **API Keys:** Authenticate users with API keys
5. **Geographic Analytics:** Track clicks by country/city
6. **A/B Testing:** Multiple long URLs for one short code

---

## 📚 Related Problems

- **Problem 26:** API Rate Limiter (rate limiting for URL creation)
- **Problem 30:** Distributed Cache (caching short URLs)
- **Problem 23:** LRU Cache (cache frequently accessed URLs)

---

**Good luck! Focus on Base62 encoding, collision handling, and clean strategy implementation.** 🎯
