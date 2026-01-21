package enums;

/**
 * Enum representing log levels in order of severity
 */
public enum LogLevel {
    DEBUG(0),
    INFO(1),
    WARN(2),
    ERROR(3),
    FATAL(4);
    
    private final int severity;
    
    LogLevel(int severity) {
        this.severity = severity;
    }
    
    public int getSeverity() {
        return severity;
    }
    
    public boolean isGreaterOrEqual(LogLevel other) {
        return this.severity >= other.severity;
    }
}
