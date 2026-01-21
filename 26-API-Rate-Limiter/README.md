# Problem 26: API Rate Limiter

## 🎯 Difficulty: Hard ⭐⭐⭐⭐

## 📝 Problem Statement

Design an API rate limiter system that controls the rate of traffic sent or received by a network. It limits the number of requests a user/client can make to an API within a specific time window.

**Real-world examples:** Stripe API (100 requests/second), Twitter API (300 requests/15 min), GitHub API (5000 requests/hour)

---

## 🔍 Functional Requirements (FR)

### FR1: Rate Limiting Strategies
- Support multiple rate limiting algorithms:
  - **Token Bucket:** Fixed capacity, tokens refill at constant rate
  - **Leaky Bucket:** Process requests at constant rate
  - **Fixed Window:** Fixed time window (e.g., 100 req/min)
  - **Sliding Window Log:** Precise tracking with timestamps
  - **Sliding Window Counter:** Hybrid approach (memory efficient)

### FR2: Client Management
- Each client/user has unique identifier (API key, user ID)
- Different rate limits per client tier (Free, Basic, Premium, Enterprise)
- Per-endpoint rate limits (different limits for different APIs)

### FR3: Request Handling
- Allow request if within rate limit
- Reject request if limit exceeded
- Return remaining quota information
- Return time until quota resets

### FR4: Multi-level Rate Limiting
- Per-user rate limits
- Per-IP rate limits
- Global rate limits (system-wide)
- Per-endpoint rate limits

### FR5: Distributed Support
- Support for distributed systems (multiple servers)
- Consistent rate limiting across servers
- Shared state management

---

## 🚫 Non-Functional Requirements (NFR)

### NFR1: Performance
- Low latency (<1ms overhead)
- High throughput (handle millions of requests/sec)
- Minimal memory footprint

### NFR2: Accuracy
- No false positives (don't block valid requests)
- Acceptable false negatives (may allow slightly over limit)
- Precise tracking for critical APIs

### NFR3: Scalability
- Horizontal scaling support
- Handle millions of users
- Efficient storage for rate limit data

### NFR4: Reliability
- Fail-open vs fail-closed strategies
- Handle clock skew in distributed systems
- Graceful degradation under load

### NFR5: Flexibility
- Easy to change rate limits without restart
- Support different limits for different endpoints
- Dynamic rate limit adjustment

---

## 🎨 Design Patterns to Use

### 1. **Strategy Pattern**
- **Where:** Different rate limiting algorithms
- **Why:** Interchangeable rate limiting strategies

### 2. **Singleton Pattern**
- **Where:** RateLimiterService
- **Why:** Single instance managing all rate limiters

### 3. **Factory Pattern** (Optional)
- **Where:** Creating rate limiters for different tiers
- **Why:** Centralized limiter creation

---

## 📋 Core Entities

### 1. **RateLimiter** (Interface)
- Methods: `allowRequest(clientId)`, `getRemainingQuota()`, `getResetTime()`

### 2. **TokenBucketLimiter**
- Attributes: `capacity`, `refillRate`, `tokens`, `lastRefillTime`
- Refills tokens at constant rate

### 3. **LeakyBucketLimiter**
- Attributes: `capacity`, `leakRate`, `queue`, `lastLeakTime`
- Processes requests at constant rate

### 4. **FixedWindowLimiter**
- Attributes: `maxRequests`, `windowSize`, `requestCount`, `windowStart`
- Resets counter at window boundaries

### 5. **SlidingWindowLogLimiter**
- Attributes: `maxRequests`, `windowSize`, `requestTimestamps`
- Precise tracking with timestamps

### 6. **SlidingWindowCounterLimiter**
- Attributes: `maxRequests`, `windowSize`, `currentWindow`, `previousWindow`
- Hybrid approach (memory efficient)

### 7. **ClientTier**
- Enum: FREE, BASIC, PREMIUM, ENTERPRISE
- Different rate limits per tier

### 8. **RateLimitConfig**
- Attributes: `maxRequests`, `windowSize`, `tier`

---

## 🧪 Test Scenarios

### Scenario 1: Token Bucket - Normal Flow
```
1. Client with 10 tokens/sec limit
2. Send 5 requests → All allowed
3. Send 8 more requests → 3 rejected (exceeded capacity)
4. Wait 1 second (tokens refill)
5. Send 10 requests → All allowed
```

### Scenario 2: Fixed Window - Burst at Boundary
```
1. Limit: 100 req/min
2. Send 100 requests at 00:59 → All allowed
3. Send 100 requests at 01:00 → All allowed (new window)
4. Problem: 200 requests in 1 second!
```

### Scenario 3: Sliding Window Log - Precise Tracking
```
1. Limit: 100 req/min
2. Send 100 requests at 00:00
3. Send 1 request at 00:30 → Rejected (still 100 in last 60 sec)
4. Send 1 request at 01:01 → Allowed (oldest request expired)
```

### Scenario 4: Multi-Tier Clients
```
1. Free tier: 10 req/min
2. Premium tier: 1000 req/min
3. Enterprise tier: Unlimited
4. Test each tier with different request rates
```

### Scenario 5: Per-Endpoint Limits
```
1. /api/search: 100 req/min
2. /api/upload: 10 req/min
3. /api/download: 50 req/min
4. Test different endpoints with same client
```

### Scenario 6: Distributed Rate Limiting
```
1. Multiple servers sharing rate limit state
2. Client sends requests to different servers
3. Verify consistent rate limiting
```

---

## ⏱️ Time Allocation (90 minutes)

- **10 mins:** Clarify requirements, understand algorithms
- **10 mins:** List entities and relationships
- **10 mins:** Identify design patterns
- **50 mins:** Write code (enums → model → strategy → service → main)
- **10 mins:** Test with scenarios, handle edge cases

---

## 💡 Hints

<details>
<summary>Hint 1: Token Bucket Algorithm</summary>

```java
public class TokenBucketLimiter implements RateLimiter {
    private final int capacity;
    private final int refillRate; // tokens per second
    private int tokens;
    private long lastRefillTime;
    
    @Override
    public boolean allowRequest(String clientId) {
        refillTokens();
        
        if (tokens > 0) {
            tokens--;
            return true;
        }
        return false;
    }
    
    private void refillTokens() {
        long now = System.currentTimeMillis();
        long timePassed = now - lastRefillTime;
        int tokensToAdd = (int) (timePassed / 1000 * refillRate);
        
        tokens = Math.min(capacity, tokens + tokensToAdd);
        lastRefillTime = now;
    }
}
```
</details>

<details>
<summary>Hint 2: Fixed Window Algorithm</summary>

```java
public class FixedWindowLimiter implements RateLimiter {
    private final int maxRequests;
    private final long windowSizeMs;
    private int requestCount;
    private long windowStart;
    
    @Override
    public boolean allowRequest(String clientId) {
        long now = System.currentTimeMillis();
        
        // Check if we need to reset window
        if (now - windowStart >= windowSizeMs) {
            requestCount = 0;
            windowStart = now;
        }
        
        if (requestCount < maxRequests) {
            requestCount++;
            return true;
        }
        return false;
    }
}
```
</details>

<details>
<summary>Hint 3: Sliding Window Log Algorithm</summary>

```java
public class SlidingWindowLogLimiter implements RateLimiter {
    private final int maxRequests;
    private final long windowSizeMs;
    private final Queue<Long> requestTimestamps;
    
    @Override
    public boolean allowRequest(String clientId) {
        long now = System.currentTimeMillis();
        
        // Remove old timestamps outside window
        while (!requestTimestamps.isEmpty() && 
               now - requestTimestamps.peek() >= windowSizeMs) {
            requestTimestamps.poll();
        }
        
        if (requestTimestamps.size() < maxRequests) {
            requestTimestamps.offer(now);
            return true;
        }
        return false;
    }
}
```
</details>

---

## 🚀 Extensions (If Time Permits)

1. **Distributed Rate Limiting**
   - Redis-based shared state
   - Consistent hashing for client distribution

2. **Rate Limit Headers**
   - X-RateLimit-Limit
   - X-RateLimit-Remaining
   - X-RateLimit-Reset

3. **Burst Allowance**
   - Allow short bursts above limit
   - Penalty for sustained high traffic

4. **Dynamic Rate Limits**
   - Adjust limits based on system load
   - Machine learning-based prediction

5. **Rate Limit Analytics**
   - Track rejection rates
   - Identify abusive clients
   - Generate reports

---

## ✅ Success Criteria

Your solution should:
- [ ] Compile without errors
- [ ] Use Strategy pattern for different algorithms
- [ ] Implement at least 3 rate limiting algorithms
- [ ] Support multiple client tiers
- [ ] Handle concurrent requests correctly
- [ ] Return remaining quota information
- [ ] Display clear output with metrics
- [ ] Be thread-safe
- [ ] Handle edge cases (clock skew, overflow)

---

## 📁 File Structure

```
26-API-Rate-Limiter/
├── README.md (this file)
├── src/
│   ├── enums/
│   │   ├── ClientTier.java
│   │   ├── RateLimitAlgorithm.java
│   │   └── RateLimitResult.java
│   ├── model/
│   │   ├── RateLimitConfig.java
│   │   ├── RateLimitInfo.java
│   │   └── Client.java
│   ├── strategy/
│   │   ├── RateLimiter.java (interface)
│   │   ├── TokenBucketLimiter.java
│   │   ├── LeakyBucketLimiter.java
│   │   ├── FixedWindowLimiter.java
│   │   ├── SlidingWindowLogLimiter.java
│   │   └── SlidingWindowCounterLimiter.java
│   ├── service/
│   │   └── RateLimiterService.java (Singleton)
│   └── Main.java
├── SOLUTION.md
└── COMPILATION-GUIDE.md
```

---

**Good luck! Start coding! 🚀**

**Companies that ask this:** Google, Amazon, Stripe, Twitter, GitHub, Netflix, Uber

