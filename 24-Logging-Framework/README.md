# Problem 24: Logging Framework

## 🎯 Problem Statement

Design and implement a **production-quality Logging Framework** similar to Log4j, SLF4J, or Logback. The system should support multiple log levels, multiple output destinations (appenders), different formatting strategies, asynchronous logging, file rotation, and hierarchical loggers.

---

## 📋 Functional Requirements (FR)

### Core Features

1. **Log Levels**
   - DEBUG (lowest severity)
   - INFO
   - WARN
   - ERROR
   - FATAL (highest severity)
   - Level-based filtering

2. **Appenders (Output Destinations)**
   - Console Appender
   - File Appender with rotation (size-based)
   - Async Appender (queue-based)
   - Support multiple appenders per logger

3. **Formatters (Output Formats)**
   - Simple text format
   - JSON format
   - XML format
   - Pluggable formatter system

4. **Logger Management**
   - Hierarchical logger structure (e.g., com.example.service)
   - Logger factory (Singleton + Factory pattern)
   - Parent-child logger relationships
   - Additive logging (propagate to parent)

5. **Advanced Features**
   - Async logging with queue
   - File rotation (size-based)
   - Exception logging with stack traces
   - Thread information
   - Timestamp tracking
   - Context data (MDC-like)

---

## 🔧 Non-Functional Requirements (NFR)

1. **Performance**
   - Async logging for high throughput
   - Minimal overhead when level is disabled
   - Efficient queue management

2. **Reliability**
   - Thread-safe operations
   - Graceful handling of I/O errors
   - No message loss in async mode

3. **Extensibility**
   - Easy to add new appenders
   - Easy to add new formatters
   - Pluggable architecture

4. **Usability**
   - Simple API
   - Hierarchical configuration
   - Clear error messages

---

## 🎨 Design Patterns Used

### 1. **Singleton Pattern**
- **Where:** LoggerFactory
- **Why:** Single instance managing all loggers
- **Benefit:** Global access, centralized management

### 2. **Factory Pattern**
- **Where:** LoggerFactory creates Logger instances
- **Why:** Centralized logger creation with hierarchy
- **Benefit:** Consistent logger creation, hierarchy management

### 3. **Strategy Pattern**
- **Where:** LogFormatter (Simple, JSON, XML)
- **Where:** LogAppender (Console, File, Async)
- **Why:** Interchangeable formatting and output strategies
- **Benefit:** Runtime selection, easy to extend

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
│   Factory    │ │  Appenders  │ │ Formatters │ │   Model    │
│              │ │  (Strategy) │ │ (Strategy) │ │            │
│  Logger      │ │             │ │            │ │  Logger    │
│  Factory     │ │  Console    │ │   Simple   │ │            │
│ (Singleton)  │ │   File      │ │    JSON    │ │ LogMessage │
│              │ │   Async     │ │    XML     │ │            │
│  - getLogger │ │             │ │            │ │            │
│  - hierarchy │ │             │ │            │ │            │
└──────────────┘ └─────────────┘ └────────────┘ └────────────┘
```

---

## 🚀 How to Run

### Compile
```bash
cd 24-Logging-Framework/src/
javac enums/*.java model/*.java formatter/*.java appender/*.java service/*.java Main.java
```

### Run
```bash
java Main
```

---

## ✨ Key Features Demonstrated

### 1. **Log Levels with Filtering**
```java
logger.setLevel(LogLevel.WARN);
logger.debug("Not shown");  // Below WARN
logger.warn("Shown");       // WARN and above
```

### 2. **Multiple Formatters (Strategy Pattern)**
```java
// Simple format
2026-01-12 10:00:00.123 [main] INFO MyApp - Message

// JSON format
{"timestamp":"2026-01-12T10:00:00.123","level":"INFO","message":"Message"}

// XML format
<log><timestamp>2026-01-12T10:00:00.123</timestamp><level>INFO</level></log>
```

### 3. **File Rotation**
```java
FileAppender appender = new FileAppender("app.log", 10MB, 5);
// Automatically rotates when file exceeds 10MB
// Keeps 5 backup files: app.log.1, app.log.2, ...
```

### 4. **Async Logging**
```java
AsyncAppender async = new AsyncAppender(consoleAppender, 1000);
// Logs are queued and processed in background thread
// Non-blocking, high performance
```

### 5. **Hierarchical Loggers**
```java
Logger root = factory.getRootLogger();
Logger parent = factory.getLogger("com.example");
Logger child = factory.getLogger("com.example.service");
// Child logs propagate to parent (if additive=true)
```

---

## 🧪 Test Scenarios (10 Total)

1. ✅ Basic Console Logging (all levels)
2. ✅ Log Level Filtering
3. ✅ Multiple Formatters (Simple, JSON, XML)
4. ✅ File Appender with Rotation
5. ✅ Async Logging with Queue
6. ✅ Exception Logging with Stack Traces
7. ✅ Hierarchical Loggers
8. ✅ Multiple Appenders per Logger
9. ✅ Performance Test (Sync vs Async)
10. ✅ Edge Cases (empty messages, special chars)

---

## 📈 Extensions & Future Enhancements

### 1. **Time-Based File Rotation**
```java
public class DailyRollingFileAppender extends FileAppender {
    private String datePattern = "yyyy-MM-dd";
    // Rotate at midnight
}
```

### 2. **Remote Appender**
```java
public class RemoteAppender implements LogAppender {
    private String serverUrl;
    public void append(LogMessage message) {
        // Send to remote logging server
    }
}
```

### 3. **Database Appender**
```java
public class DatabaseAppender implements LogAppender {
    private Connection connection;
    public void append(LogMessage message) {
        // Insert into logs table
    }
}
```

### 4. **MDC (Mapped Diagnostic Context)**
```java
public class MDC {
    private static ThreadLocal<Map<String, String>> context = new ThreadLocal<>();
    
    public static void put(String key, String value) {
        context.get().put(key, value);
    }
}
```

### 5. **Configuration File Support**
```xml
<configuration>
  <appender name="CONSOLE" class="ConsoleAppender">
    <formatter class="SimpleFormatter"/>
  </appender>
  
  <logger name="com.example" level="DEBUG">
    <appender-ref ref="CONSOLE"/>
  </logger>
</configuration>
```

---

## 🎓 Learning Objectives

After completing this problem, you should understand:

1. ✅ How to implement **Singleton pattern** for factory management
2. ✅ How to use **Factory pattern** for object creation
3. ✅ How to apply **Strategy pattern** for formatters and appenders
4. ✅ How to design **hierarchical structures** (logger hierarchy)
5. ✅ How to implement **async processing** with queues
6. ✅ How to handle **file rotation** (size-based)
7. ✅ How to design **extensible frameworks**
8. ✅ How to build **production-quality logging systems**

---

## 🏆 Interview Tips

### Common Questions

**Q: How would you make logging thread-safe?**
```java
public synchronized void append(LogMessage message) {
    // Synchronized method
}

// Or use ConcurrentHashMap for logger storage
private Map<String, Logger> loggers = new ConcurrentHashMap<>();
```

**Q: How would you implement time-based rotation?**
```java
public class TimeBasedFileAppender extends FileAppender {
    private LocalDate currentDate;
    
    @Override
    public void append(LogMessage message) {
        LocalDate messageDate = message.getTimestamp().toLocalDate();
        if (!messageDate.equals(currentDate)) {
            rotateByDate();
            currentDate = messageDate;
        }
        super.append(message);
    }
}
```

**Q: How would you scale this for distributed systems?**
```
1. Remote appender sending to centralized log server
2. Message queue (Kafka) for log aggregation
3. Elasticsearch for log storage and search
4. Kibana for visualization
5. Log correlation with trace IDs
```

**Q: How do you prevent log file from filling disk?**
```java
public class FileAppender {
    private int maxBackupFiles = 10;
    private long maxFileSize = 100 * 1024 * 1024; // 100MB
    
    // Delete oldest backups when limit reached
    // Implement disk space monitoring
}
```

---

## ⚖️ Trade-offs

| Aspect | Current Approach | Alternative | Trade-off |
|--------|-----------------|-------------|-----------|
| **Storage** | In-memory factory | Persistent config | Simple vs Configurable |
| **Async** | Queue-based | Lock-free ring buffer | Simple vs Performance |
| **Rotation** | Size-based | Time-based | Predictable vs Organized |
| **Hierarchy** | Parent references | Configuration tree | Dynamic vs Static |

---

## 📚 Related Problems

- **Problem 28:** Notification Service (Observer pattern)
- **Problem 29:** Task Scheduler (Async processing)
- **Problem 30:** Distributed Cache (Strategy pattern)

---

**Difficulty:** ⭐⭐⭐⭐ Hard  
**Time to Solve:** 90-120 minutes  
**Design Patterns:** Singleton, Factory, Strategy  
**Key Concepts:** Logging, Async processing, File rotation, Hierarchical structures

---

**Companies:** Apache (Log4j), SLF4J, Logback, Splunk, Datadog

*This is a production-grade logging framework demonstrating real-world patterns!* 📝
