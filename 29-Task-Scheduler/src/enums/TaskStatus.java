package enums;

/**
 * Represents the current status of a task in its lifecycle.
 */
public enum TaskStatus {
    SCHEDULED("Task is scheduled but not yet queued"),
    QUEUED("Task is in the execution queue"),
    RUNNING("Task is currently being executed"),
    COMPLETED("Task completed successfully"),
    FAILED("Task failed after all retries"),
    CANCELLED("Task was cancelled before completion"),
    TIMEOUT("Task exceeded maximum execution time");
    
    private final String description;
    
    TaskStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isTerminal() {
        return this == COMPLETED || this == FAILED || this == CANCELLED || this == TIMEOUT;
    }
}
