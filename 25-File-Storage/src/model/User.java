package model;

import enums.StoragePlan;

public class User {
    private final String userId;
    private final String name;
    private final String email;
    private StoragePlan storagePlan;
    private long usedStorage;
    
    public User(String userId, String name, String email, StoragePlan storagePlan) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.storagePlan = storagePlan;
        this.usedStorage = 0;
    }
    
    public boolean canUpload(long fileSize) {
        return (usedStorage + fileSize) <= storagePlan.getQuotaBytes();
    }
    
    public void updateUsedStorage(long delta) {
        this.usedStorage += delta;
    }
    
    public long getAvailableStorage() {
        return storagePlan.getQuotaBytes() - usedStorage;
    }
    
    public double getStorageUsagePercent() {
        return ((double) usedStorage / storagePlan.getQuotaBytes()) * 100;
    }
    
    public void upgradePlan(StoragePlan newPlan) {
        this.storagePlan = newPlan;
    }
    
    // Getters
    public String getUserId() {
        return userId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public StoragePlan getStoragePlan() {
        return storagePlan;
    }
    
    public long getUsedStorage() {
        return usedStorage;
    }
    
    public String getUsedStorageDisplay() {
        return formatBytes(usedStorage);
    }
    
    public String getAvailableStorageDisplay() {
        return formatBytes(getAvailableStorage());
    }
    
    private String formatBytes(long bytes) {
        if (bytes >= 1024L * 1024 * 1024) {
            return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
        } else if (bytes >= 1024L * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024));
        } else if (bytes >= 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else {
            return bytes + " bytes";
        }
    }
    
    @Override
    public String toString() {
        return String.format("User[%s, %s, %s, Used: %s / %s]",
            userId, name, storagePlan.getDisplayName(),
            getUsedStorageDisplay(), storagePlan.getQuotaDisplay());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return userId.equals(user.userId);
    }
    
    @Override
    public int hashCode() {
        return userId.hashCode();
    }
}
