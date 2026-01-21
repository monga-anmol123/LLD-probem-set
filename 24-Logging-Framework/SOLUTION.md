# Solution: Logging Framework

## 📐 Architecture Design

### Component Breakdown

```
24-Logging-Framework/
├── enums/
│   └── LogLevel.java              # DEBUG, INFO, WARN, ERROR, FATAL
├── model/
│   └── LogMessage.java            # Log message with metadata
├── formatter/
│   ├── LogFormatter.java          # Strategy interface
│   ├── SimpleFormatter.java       # Text format
│   ├── JsonFormatter.java         # JSON format
│   └── XmlFormatter.java          # XML format
├── appender/
│   ├── LogAppender.java           # Strategy interface
│   ├── ConsoleAppender.java       # Console output
│   ├── FileAppender.java          # File with rotation
│   └── AsyncAppender.java         # Async wrapper
├── service/
│   ├── Logger.java                # Logger with hierarchy
│   └── LoggerFactory.java         # Singleton + Factory
└── Main.java                      # Demo with 10 scenarios
```

---

## 🎨 Design Patterns Explained

### 1. Singleton Pattern

**Implementation:**
```java
public class LoggerFactory {
    private static LoggerFactory instance;
    
    private LoggerFactory() {
        // Private constructor
    }
    
    public static synchronized LoggerFactory getInstance() {
        if (instance == null) {
            instance = new LoggerFactory();
        }
        return instance;
    }
}
```

**Why Singleton?**
- Single point of logger management
- Centralized configuration
- Consistent logger hierarchy

---

### 2. Factory Pattern

**Implementation:**
```java
public Logger getLogger(String name) {
    Logger logger = loggers.get(name);
    if (logger != null) {
        return logger;
    }
    
    logger = new Logger(name);
    Logger parent = findParent(name);
    logger.setParent(parent);
    loggers.put(name, logger);
    return logger;
}
```

**Why Factory?**
- Centralized logger creation
- Automatic hierarchy setup
- Consistent naming

---

### 3. Strategy Pattern (Formatters)

**Implementation:**
```java
public interface LogFormatter {
    String format(LogMessage message);
}

public class SimpleFormatter implements LogFormatter {
    public String format(LogMessage message) {
        return timestamp + " " + level + " " + message;
    }
}

public class JsonFormatter implements LogFormatter {
    public String format(LogMessage message) {
        return "{\"timestamp\":\"" + timestamp + "\",\"level\":\"" + level + "\"}";
    }
}
```

**Why Strategy?**
- Runtime formatter selection
- Easy to add new formats
- Decoupled formatting logic

---

### 4. Strategy Pattern (Appenders)

**Implementation:**
```java
public interface LogAppender {
    void append(LogMessage message);
    void setFormatter(LogFormatter formatter);
}

public class ConsoleAppender implements LogAppender {
    public void append(LogMessage message) {
        System.out.println(formatter.format(message));
    }
}

public class FileAppender implements LogAppender {
    public void append(LogMessage message) {
        writer.write(formatter.format(message));
        if (needsRotation()) rotateFiles();
    }
}
```

**Why Strategy?**
- Multiple output destinations
- Runtime appender selection
- Easy to add new appenders

---

## 🧠 Key Algorithms

### 1. Hierarchical Logger Resolution

```java
private Logger findParent(String name) {
    // For "com.example.service.UserService"
    // Try: "com.example.service", "com.example", "com", ROOT
    
    int lastDot = name.lastIndexOf('.');
    while (lastDot > 0) {
        String parentName = name.substring(0, lastDot);
        Logger parent = loggers.get(parentName);
        if (parent != null) {
            return parent;
        }
        lastDot = parentName.lastIndexOf('.');
    }
    return rootLogger;
}
```

**Time Complexity:** O(d) where d = depth of hierarchy  
**Space Complexity:** O(1)

---

### 2. File Rotation Algorithm

```java
private void rotateFiles() {
    // Delete oldest: app.log.5
    new File(filePath + "." + maxBackupFiles).delete();
    
    // Rotate: app.log.4 → app.log.5, app.log.3 → app.log.4, ...
    for (int i = maxBackupFiles - 1; i > 0; i--) {
        File source = new File(filePath + "." + i);
        File dest = new File(filePath + "." + (i + 1));
        source.renameTo(dest);
    }
    
    // Current → app.log.1
    new File(filePath).renameTo(new File(filePath + ".1"));
    
    // Create new file
    writer = new BufferedWriter(new FileWriter(filePath));
}
```

**Time Complexity:** O(n) where n = maxBackupFiles  
**Space Complexity:** O(1)

---

### 3. Async Queue Processing

```java
private void processQueue() {
    while (running || !messageQueue.isEmpty()) {
        LogMessage message = messageQueue.poll(100, TimeUnit.MILLISECONDS);
        if (message != null) {
            wrappedAppender.append(message);
        }
    }
}

public void append(LogMessage message) {
    if (!messageQueue.offer(message, 100, TimeUnit.MILLISECONDS)) {
        // Queue full - drop or handle
    }
}
```

**Benefits:**
- Non-blocking logging
- High throughput
- Decoupled I/O from application thread

---

## ⚖️ Trade-offs & Design Decisions

### 1. Synchronous vs Asynchronous Logging

**Synchronous:**
- Pros: Guaranteed order, no message loss
- Cons: Blocks application thread, lower throughput

**Asynchronous:**
- Pros: Non-blocking, high throughput
- Cons: Possible message loss if queue full, complexity

**Solution:** Provide both via AsyncAppender wrapper

---

### 2. File Rotation Strategy

**Size-Based (Current):**
```java
if (currentFileSize + message.length() > maxFileSize) {
    rotateFiles();
}
```
- Pros: Predictable file sizes
- Cons: Rotation at arbitrary times

**Time-Based (Alternative):**
```java
if (!currentDate.equals(message.getTimestamp().toLocalDate())) {
    rotateByDate();
}
```
- Pros: Organized by date
- Cons: Unpredictable file sizes

---

### 3. Logger Hierarchy

**Current:** Parent references
```java
logger.setParent(findParent(name));
```
- Pros: Dynamic, flexible
- Cons: Runtime overhead

**Alternative:** Configuration tree
```xml
<logger name="com.example">
  <logger name="service">
    <logger name="UserService"/>
  </logger>
</logger>
```
- Pros: Static, fast
- Cons: Less flexible

---

## 🚀 Scalability Considerations

### 1. Distributed Logging

```java
public class RemoteAppender implements LogAppender {
    private HttpClient client;
    private String logServerUrl;
    
    @Override
    public void append(LogMessage message) {
        String json = new JsonFormatter().format(message);
        client.post(logServerUrl, json);
    }
}
```

### 2. Log Aggregation

```
Application 1 → Kafka Topic "logs"
Application 2 → Kafka Topic "logs"  → Logstash → Elasticsearch → Kibana
Application 3 → Kafka Topic "logs"
```

### 3. Performance Optimization

```java
// 1. Level check before expensive operations
if (logger.isDebugEnabled()) {
    logger.debug("Expensive: " + computeExpensiveString());
}

// 2. Batch writing
public class BatchingFileAppender extends FileAppender {
    private List<String> buffer = new ArrayList<>();
    
    @Override
    public void append(LogMessage message) {
        buffer.add(formatter.format(message));
        if (buffer.size() >= 100) {
            flush();
        }
    }
}

// 3. Lock-free ring buffer
public class DisruptorAppender implements LogAppender {
    private RingBuffer<LogMessage> ringBuffer;
    // Use LMAX Disruptor for ultra-high performance
}
```

---

## 🎯 Interview Discussion Points

### 1. Thread Safety

**Question:** "How do you ensure thread-safety?"

**Answer:**
```java
// 1. Synchronized methods
public synchronized void append(LogMessage message) {
    writer.write(formatter.format(message));
}

// 2. ConcurrentHashMap for logger storage
private Map<String, Logger> loggers = new ConcurrentHashMap<>();

// 3. ThreadLocal for MDC
private static ThreadLocal<Map<String, String>> context = new ThreadLocal<>();

// 4. Async appender with BlockingQueue (thread-safe)
private BlockingQueue<LogMessage> queue = new LinkedBlockingQueue<>();
```

---

### 2. Memory Management

**Question:** "How do you prevent memory leaks?"

**Answer:**
```java
// 1. Close appenders properly
public void shutdown() {
    for (Logger logger : loggers.values()) {
        logger.close();
    }
}

// 2. Bounded queue for async appender
private BlockingQueue<LogMessage> queue = 
    new LinkedBlockingQueue<>(1000); // Max 1000 messages

// 3. File handle management
@Override
public void close() {
    try {
        if (writer != null) {
            writer.flush();
            writer.close();
        }
    } catch (IOException e) {
        // Handle
    }
}
```

---

### 3. Configuration

**Question:** "How would you add configuration file support?"

**Answer:**
```java
public class LoggerConfiguration {
    public void loadFromFile(String configFile) {
        // Parse XML/JSON/YAML
        // Create appenders
        // Configure loggers
    }
}

// Example XML:
<configuration>
  <appender name="CONSOLE" class="ConsoleAppender">
    <formatter class="JsonFormatter"/>
  </appender>
  
  <appender name="FILE" class="FileAppender">
    <file>app.log</file>
    <maxFileSize>10MB</maxFileSize>
    <maxBackupFiles>5</maxBackupFiles>
  </appender>
  
  <logger name="com.example" level="DEBUG">
    <appender-ref ref="CONSOLE"/>
    <appender-ref ref="FILE"/>
  </logger>
  
  <root level="INFO">
    <appender-ref ref="CONSOLE"/>
  </root>
</configuration>
```

---

## 📊 Complexity Analysis

| Operation | Time Complexity | Space Complexity |
|-----------|----------------|------------------|
| Get Logger | O(d + log n) | O(1) |
| Log Message | O(a × f) | O(1) |
| File Rotation | O(b) | O(1) |
| Async Append | O(1) | O(q) |

Where:
- d = hierarchy depth
- n = number of loggers
- a = number of appenders
- f = formatter complexity
- b = backup files
- q = queue size

---

## 🎓 Key Takeaways

1. **Singleton + Factory** for centralized management
2. **Strategy** for formatters and appenders
3. **Hierarchical structure** for logger organization
4. **Async processing** for performance
5. **File rotation** for disk management
6. **Thread safety** is critical
7. **Extensibility** through interfaces

---

**This solution demonstrates production-ready logging with clean architecture!** 📝
