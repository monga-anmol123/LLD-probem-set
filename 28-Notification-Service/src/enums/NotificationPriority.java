package enums;

/**
 * Enum representing notification priority levels
 */
public enum NotificationPriority {
    LOW(3),
    MEDIUM(2),
    HIGH(1),
    URGENT(0);
    
    private final int value;
    
    NotificationPriority(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
}
