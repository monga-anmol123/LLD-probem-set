# Problem 28: Notification Service

## 🎯 Problem Statement

Design and implement a **multi-channel notification service** similar to systems used by Facebook, WhatsApp, Slack, and Discord. The system should support multiple delivery channels (Email, SMS, Push, In-App), priority-based queuing, retry mechanisms, template management, and user preferences.

---

## 📋 Functional Requirements (FR)

### Core Features

1. **Multi-Channel Delivery**
   - Email (SMTP-like)
   - SMS (Twilio-like)
   - Push Notifications (FCM/APNS-like)
   - In-App Notifications
   - Webhook (optional)

2. **Priority-Based Queuing**
   - URGENT (highest priority)
   - HIGH
   - MEDIUM
   - LOW (lowest priority)
   - Process notifications by priority

3. **Retry Mechanism**
   - Automatic retry on failure
   - Exponential backoff (simulated)
   - Max retry limit (3 attempts)
   - Dead letter queue for failed notifications

4. **Template Management**
   - Create reusable templates
   - Variable substitution ({{variable}})
   - Template validation
   - Per-channel templates

5. **User Preferences**
   - Enable/disable specific channels
   - Per-user notification settings
   - Device token management (for push)

6. **Analytics & Tracking**
   - Delivery status tracking
   - Success/failure rates
   - Observer pattern for real-time analytics
   - Notification history per user

7. **Bulk Notifications**
   - Send to multiple users
   - Efficient batch processing

---

## 🔧 Non-Functional Requirements (NFR)

1. **Reliability**
   - Guaranteed delivery with retries
   - Fault tolerance
   - Graceful degradation

2. **Scalability**
   - Handle high throughput
   - Priority queue for efficient processing
   - Async processing support

3. **Extensibility**
   - Easy to add new channels
   - Pluggable notification strategies
   - Template system for customization

4. **Performance**
   - Fast queue processing
   - Minimal latency
   - Efficient bulk operations

---

## 🎨 Design Patterns Used

### 1. **Observer Pattern**
- **Where:** Analytics tracking
- **Why:** Decouple notification events from analytics
- **Benefit:** Real-time monitoring without tight coupling

### 2. **Strategy Pattern**
- **Where:** Notification channels (Email, SMS, Push, In-App)
- **Why:** Different delivery mechanisms
- **Benefit:** Easy to add new channels

### 3. **Factory Pattern**
- **Where:** NotificationSenderFactory
- **Why:** Create appropriate sender based on channel
- **Benefit:** Centralized sender creation

### 4. **Singleton Pattern**
- **Where:** NotificationService
- **Why:** Single instance managing all notifications
- **Benefit:** Global access, centralized state

### 5. **Template Method Pattern**
- **Where:** NotificationTemplate
- **Why:** Reusable notification templates
- **Benefit:** Consistency, reduced duplication

---

## 📊 Architecture Overview

```
┌──────────────────────────────────────────────────────────────┐
│                         Main.java                             │
│                    (Demo/Entry Point)                         │
└───────────────────────┬──────────────────────────────────────┘
                        │
        ┌───────────────┼───────────────┬───────────────┐
        │               │               │               │
        ▼               ▼               ▼               ▼
┌──────────────┐ ┌─────────────┐ ┌────────────┐ ┌────────────┐
│   Factory    │ │   Service   │ │  Strategy  │ │  Observer  │
│              │ │             │ │            │ │            │
│ Notification │ │Notification │ │   Email    │ │ Analytics  │
│   Sender     │ │  Service    │ │   Sender   │ │  Observer  │
│  Factory     │ │ (Singleton) │ │            │ │            │
│              │ │             │ │    SMS     │ │            │
│              │ │ Priority    │ │   Sender   │ │            │
│              │ │   Queue     │ │            │ │            │
│              │ │             │ │   Push     │ │            │
│              │ │  Retry      │ │   Sender   │ │            │
│              │ │  Logic      │ │            │ │            │
│              │ │             │ │   In-App   │ │            │
│              │ │             │ │   Sender   │ │            │
└──────────────┘ └─────────────┘ └────────────┘ └────────────┘
```

---

## 🚀 How to Run

### Compile
```bash
cd 28-Notification-Service/src/
javac enums/*.java observer/*.java model/*.java strategy/*.java factory/*.java service/*.java Main.java
```

### Run
```bash
java Main
```

---

## ✨ Key Features Demonstrated

### 1. **Multi-Channel Delivery (Strategy Pattern)**
- Email: SMTP-like delivery
- SMS: Twilio-like delivery
- Push: FCM/APNS-like delivery
- In-App: Database-stored notifications

### 2. **Priority Queue**
```
URGENT notifications processed first
HIGH notifications next
MEDIUM notifications next
LOW notifications last
```

### 3. **Retry Mechanism**
```
Attempt 1: Send notification
  ↓ (fails)
Attempt 2: Retry with backoff
  ↓ (fails)
Attempt 3: Final retry
  ↓ (fails)
Mark as FAILED, move to dead letter queue
```

### 4. **Template System**
```
Template: "Welcome {{name}} to {{company}}!"
Variables: {name: "Alice", company: "TechCorp"}
Result: "Welcome Alice to TechCorp!"
```

### 5. **Observer Pattern for Analytics**
```
Notification Sent → Analytics Observer → Log metrics
Notification Delivered → Analytics Observer → Update stats
Notification Failed → Analytics Observer → Alert team
```

---

## 🧪 Test Scenarios (14 Total)

1. ✅ User Registration
2. ✅ Analytics Observer (Observer Pattern)
3. ✅ Email Notification (Strategy Pattern)
4. ✅ SMS Notification (Strategy Pattern)
5. ✅ Push Notification (Strategy Pattern)
6. ✅ Priority Queue Processing
7. ✅ Template-based Notifications
8. ✅ Bulk Notifications
9. ✅ User Preferences
10. ✅ User Notification History
11. ✅ Statistics & Success Rate
12. ✅ Retry Mechanism
13. ✅ Edge Case - Invalid User
14. ✅ Edge Case - Missing Template Variables

---

## 📈 Extensions & Future Enhancements

### 1. **Rate Limiting**
```java
public class RateLimiter {
    private Map<NotificationChannel, TokenBucket> limiters;
    
    public boolean allowSend(NotificationChannel channel) {
        return limiters.get(channel).tryConsume(1);
    }
}
```

### 2. **Scheduled Notifications**
```java
public class ScheduledNotification extends Notification {
    private LocalDateTime scheduledTime;
    
    public boolean isReadyToSend() {
        return LocalDateTime.now().isAfter(scheduledTime);
    }
}
```

### 3. **Notification Batching**
```java
public class BatchProcessor {
    private Map<NotificationChannel, List<Notification>> batches;
    
    public void sendBatch(NotificationChannel channel) {
        // Send multiple notifications in one API call
    }
}
```

### 4. **Real Email/SMS Integration**
```java
public class RealEmailSender implements NotificationSender {
    private JavaMailSender mailSender;
    
    @Override
    public boolean send(Notification notification) {
        MimeMessage message = mailSender.createMimeMessage();
        // Configure and send via SMTP
    }
}
```

### 5. **Webhook Notifications**
```java
public class WebhookSender implements NotificationSender {
    @Override
    public boolean send(Notification notification) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(notification.getMetadata().get("webhook_url")))
            .POST(HttpRequest.BodyPublishers.ofString(notification.getMessage()))
            .build();
        // Send HTTP POST
    }
}
```

---

## 🎓 Learning Objectives

After completing this problem, you should understand:

1. ✅ How to implement **Observer pattern** for event tracking
2. ✅ How to use **Strategy pattern** for multiple delivery channels
3. ✅ How to implement **priority queues** in Java
4. ✅ How to design **retry mechanisms** with exponential backoff
5. ✅ How to create **template systems** with variable substitution
6. ✅ How to handle **user preferences** and channel control
7. ✅ How to track **analytics** and success rates
8. ✅ How to design **fault-tolerant** notification systems

---

## 🏆 Interview Tips

### Common Questions

**Q: How would you handle rate limiting per channel?**
```java
public class ChannelRateLimiter {
    private Map<NotificationChannel, RateLimiter> limiters;
    
    public boolean canSend(NotificationChannel channel) {
        RateLimiter limiter = limiters.get(channel);
        return limiter.tryAcquire();
    }
}
```

**Q: How would you implement exponential backoff?**
```java
public class RetryStrategy {
    public long getBackoffDelay(int retryCount) {
        return (long) Math.pow(2, retryCount) * 1000; // 1s, 2s, 4s, 8s...
    }
}
```

**Q: How would you scale this to millions of notifications?**
```
1. Use message queue (Kafka, RabbitMQ)
2. Horizontal scaling with worker pools
3. Database sharding by user_id
4. Caching with Redis
5. Async processing
6. Batch API calls to external services
```

**Q: How would you ensure exactly-once delivery?**
```java
public class IdempotentSender {
    private Set<String> sentNotificationIds;
    
    public boolean send(Notification notification) {
        if (sentNotificationIds.contains(notification.getNotificationId())) {
            return true; // Already sent
        }
        boolean success = actualSend(notification);
        if (success) {
            sentNotificationIds.add(notification.getNotificationId());
        }
        return success;
    }
}
```

---

## ⚖️ Trade-offs

| Aspect | Current Approach | Alternative | Trade-off |
|--------|-----------------|-------------|-----------|
| **Queue** | In-memory PriorityQueue | Kafka/RabbitMQ | Simple vs Distributed |
| **Retry** | Immediate retry | Exponential backoff | Fast vs Gentle |
| **Storage** | In-memory Map | Database | Speed vs Persistence |
| **Delivery** | Synchronous | Async workers | Simple vs Scalable |

---

## 📚 Related Problems

- **Problem 26:** API Rate Limiter (rate limiting notifications)
- **Problem 29:** Task Scheduler (scheduled notifications)
- **Problem 30:** Distributed Cache (caching notification templates)

---

**Difficulty:** ⭐⭐⭐⭐ Hard  
**Time to Solve:** 75-90 minutes  
**Design Patterns:** Observer, Strategy, Factory, Singleton, Template Method  
**Key Concepts:** Priority queues, Retry logic, Multi-channel delivery, Template systems

---

**Companies:** Facebook, WhatsApp, Slack, Discord, Twilio, SendGrid

*This is a production-grade notification service demonstrating real-world patterns!* 🚀
