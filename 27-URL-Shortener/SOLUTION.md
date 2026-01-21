# Solution: URL Shortener

## ✅ Complete Implementation

Production-quality URL shortener with Base62 encoding, collision handling, and analytics tracking.

---

## 🎨 Design Patterns

### Strategy Pattern
Different URL shortening algorithms (Hash-based, Counter-based, Custom).

### Factory Pattern
Creating URL objects with different configurations.

---

## 🔑 Key Algorithms

### Base62 Encoding
```java
private static final String BASE62 = 
    "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

public String encode(long id) {
    StringBuilder sb = new StringBuilder();
    while (id > 0) {
        sb.append(BASE62.charAt((int)(id % 62)));
        id /= 62;
    }
    return sb.reverse().toString();
}
```

**Why Base62?**
- Uses: a-z, A-Z, 0-9 (62 characters)
- URL-safe (no special characters)
- Compact: 7 characters = 62^7 = 3.5 trillion URLs

### Hash Collision Handling
```java
public String shortenURL(String longURL) {
    String hash = generateHash(longURL);
    
    while (shortToLong.containsKey(hash)) {
        hash = generateHash(longURL + System.nanoTime());
    }
    
    shortToLong.put(hash, longURL);
    return hash;
}
```

---

## 📊 Analytics Tracking

```java
public class URLAnalytics {
    private int clickCount;
    private Map<String, Integer> referrers;
    private Map<String, Integer> countries;
    
    public void recordClick(String referrer, String country) {
        clickCount++;
        referrers.merge(referrer, 1, Integer::sum);
        countries.merge(country, 1, Integer::sum);
    }
}
```

---

## ⚖️ Scalability Considerations

### Distributed Counter
```java
// Use Redis for distributed counter
public class DistributedIDGenerator {
    public long getNextId() {
        return redisTemplate.opsForValue().increment("url_counter");
    }
}
```

### Sharding
- Shard by hash prefix (first 2 characters)
- Consistent hashing for load distribution

---

## 🚀 Compilation

```bash
cd src/
javac enums/*.java model/*.java strategy/*.java service/*.java Main.java
java Main
```

---

## 🎯 Interview Tips

**Q: "How to handle billions of URLs?"**
- Distributed database (Cassandra, DynamoDB)
- Sharding by hash prefix
- Caching (Redis) for hot URLs

**Q: "How to prevent abuse?"**
- Rate limiting per user/IP
- CAPTCHA for suspicious activity
- Expiration for old URLs

---

**Common take-home assignment - demonstrates Base62 encoding and system design! 🚀**
