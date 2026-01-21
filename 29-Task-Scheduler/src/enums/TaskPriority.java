package enums;

/**
 * Task priority levels for execution ordering.
 * Higher priority tasks execute before lower priority tasks.
 */
public enum TaskPriority {
    LOW(1, "Low priority - background tasks"),
    MEDIUM(2, "Medium priority - normal tasks"),
    HIGH(3, "High priority - important tasks"),
    CRITICAL(4, "Critical priority - urgent tasks");
    
    private final int level;
    private final String description;
    
    TaskPriority(int level, String description) {
        this.level = level;
        this.description = description;
    }
    
    public int getLevel() {
        return level;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Compare priorities for ordering.
     * Returns positive if this priority is higher.
     */
    public int compareLevel(TaskPriority other) {
        return Integer.compare(this.level, other.level);
    }
}
