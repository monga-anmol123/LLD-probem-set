# Solution: API Rate Limiter

## ✅ Complete Implementation

Production-quality API rate limiter with multiple algorithms (Token Bucket, Sliding Window, Fixed Window) demonstrating the Strategy pattern.

---

## 🎨 Design Patterns

### Strategy Pattern
Different rate limiting algorithms that can be swapped at runtime.

**Algorithms Implemented:**
1. **Token Bucket** - Smooth rate limiting with burst support
2. **Sliding Window** - Precise rate limiting
3. **Fixed Window** - Simple, memory-efficient

---

## 📊 Algorithm Comparison

| Algorithm | Accuracy | Memory | Complexity | Burst Handling |
|-----------|----------|--------|------------|----------------|
| Token Bucket | High | Low | Medium | Excellent |
| Sliding Window | Very High | Medium | High | Good |
| Fixed Window | Medium | Low | Low | Poor |

### Token Bucket Algorithm
- Tokens added at constant rate
- Each request consumes 1 token
- Allows burst traffic (up to bucket capacity)
- **Best for:** APIs with occasional bursts

### Sliding Window Algorithm
- Tracks requests in rolling time window
- Precise rate limiting
- **Best for:** Strict rate enforcement

### Fixed Window Algorithm
- Count requests per fixed time window
- Simple implementation
- **Issue:** Burst at window boundaries
- **Best for:** Simple requirements

---

## 🔑 Key Implementation

```java
public interface RateLimitStrategy {
    boolean allowRequest(String userId);
    String getStrategyName();
}

public class TokenBucketStrategy implements RateLimitStrategy {
    private int capacity;
    private int tokensPerSecond;
    private Map<String, TokenBucket> buckets;
    
    @Override
    public boolean allowRequest(String userId) {
        TokenBucket bucket = buckets.computeIfAbsent(
            userId, k -> new TokenBucket(capacity, tokensPerSecond)
        );
        return bucket.tryConsume();
    }
}
```

---

## ⚖️ Trade-offs

### In-Memory vs Distributed

**Current:** In-memory (HashMap)
- ✅ Fast
- ❌ Lost on restart
- ❌ Not distributed

**Production:** Redis-based
```java
public class RedisRateLimiter {
    public boolean allowRequest(String userId) {
        String key = "rate_limit:" + userId;
        Long count = redisTemplate.opsForValue().increment(key);
        
        if (count == 1) {
            redisTemplate.expire(key, windowSize, TimeUnit.SECONDS);
        }
        
        return count <= maxRequests;
    }
}
```

---

## 🚀 Compilation

```bash
cd src/
javac enums/*.java model/*.java strategy/*.java service/*.java Main.java
java Main
```

---

## 🎯 Interview Tips

**Q: "Token Bucket vs Sliding Window?"**
- Token Bucket: Better for burst traffic
- Sliding Window: More accurate, no boundary issues

**Q: "How to handle distributed rate limiting?"**
- Use Redis with Lua scripts for atomicity
- Use consistent hashing for sharding

---

**This demonstrates production-quality rate limiting with multiple algorithms! 🚀**
