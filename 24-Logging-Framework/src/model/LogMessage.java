package model;

import enums.LogLevel;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a log message with metadata
 */
public class LogMessage {
    private String loggerName;
    private LogLevel level;
    private String message;
    private LocalDateTime timestamp;
    private String threadName;
    private Throwable throwable;
    private Map<String, String> context;

    public LogMessage(String loggerName, LogLevel level, String message) {
        this.loggerName = loggerName;
        this.level = level;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.threadName = Thread.currentThread().getName();
        this.throwable = null;
        this.context = new HashMap<>();
    }

    public LogMessage(String loggerName, LogLevel level, String message, Throwable throwable) {
        this(loggerName, level, message);
        this.throwable = throwable;
    }

    public void addContext(String key, String value) {
        context.put(key, value);
    }

    public String getLoggerName() {
        return loggerName;
    }

    public LogLevel getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getThreadName() {
        return threadName;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public Map<String, String> getContext() {
        return new HashMap<>(context);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s - %s", 
                timestamp, level, loggerName, message);
    }
}
