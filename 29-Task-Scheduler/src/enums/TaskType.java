package enums;

/**
 * Types of tasks supported by the scheduler.
 */
public enum TaskType {
    ONE_TIME("Execute once at scheduled time"),
    RECURRING("Execute repeatedly at intervals"),
    DELAYED("Execute once after a delay"),
    IMMEDIATE("Execute as soon as possible");
    
    private final String description;
    
    TaskType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
