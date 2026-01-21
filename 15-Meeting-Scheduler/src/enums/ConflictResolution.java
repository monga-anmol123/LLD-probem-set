package enums;

/**
 * Enum representing conflict resolution strategies
 */
public enum ConflictResolution {
    REJECT,           // Reject the new meeting
    NOTIFY,           // Notify and let user decide
    AUTO_RESCHEDULE   // Automatically find next available slot
}
