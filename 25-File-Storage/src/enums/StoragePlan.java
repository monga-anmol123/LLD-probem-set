package enums;

public enum StoragePlan {
    FREE("Free Plan", 1024L * 1024 * 1024),           // 1 GB
    BASIC("Basic Plan", 10L * 1024 * 1024 * 1024),    // 10 GB
    PREMIUM("Premium Plan", 100L * 1024 * 1024 * 1024), // 100 GB
    BUSINESS("Business Plan", 1024L * 1024 * 1024 * 1024); // 1 TB
    
    private final String displayName;
    private final long quotaBytes;
    
    StoragePlan(String displayName, long quotaBytes) {
        this.displayName = displayName;
        this.quotaBytes = quotaBytes;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public long getQuotaBytes() {
        return quotaBytes;
    }
    
    public String getQuotaDisplay() {
        return formatBytes(quotaBytes);
    }
    
    private String formatBytes(long bytes) {
        if (bytes >= 1024L * 1024 * 1024 * 1024) {
            return (bytes / (1024L * 1024 * 1024 * 1024)) + " TB";
        } else if (bytes >= 1024L * 1024 * 1024) {
            return (bytes / (1024L * 1024 * 1024)) + " GB";
        } else if (bytes >= 1024L * 1024) {
            return (bytes / (1024L * 1024)) + " MB";
        } else if (bytes >= 1024) {
            return (bytes / 1024) + " KB";
        } else {
            return bytes + " bytes";
        }
    }
}
