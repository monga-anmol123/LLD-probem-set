package model;

import enums.Permission;
import java.time.LocalDateTime;

public class SharedAccess {
    private final FileSystemItem item;
    private final User sharedWith;
    private final Permission permission;
    private final User sharedBy;
    private final LocalDateTime sharedDate;
    
    public SharedAccess(FileSystemItem item, User sharedWith, Permission permission, User sharedBy) {
        this.item = item;
        this.sharedWith = sharedWith;
        this.permission = permission;
        this.sharedBy = sharedBy;
        this.sharedDate = LocalDateTime.now();
    }
    
    public boolean hasPermission(String operation) {
        switch (operation.toLowerCase()) {
            case "read":
                return permission.canRead();
            case "write":
                return permission.canWrite();
            case "delete":
                return permission.canDelete();
            case "share":
                return permission.canShare();
            default:
                return false;
        }
    }
    
    // Getters
    public FileSystemItem getItem() {
        return item;
    }
    
    public User getSharedWith() {
        return sharedWith;
    }
    
    public Permission getPermission() {
        return permission;
    }
    
    public User getSharedBy() {
        return sharedBy;
    }
    
    public LocalDateTime getSharedDate() {
        return sharedDate;
    }
    
    @Override
    public String toString() {
        return String.format("%s shared with %s as %s by %s",
            item.getName(), sharedWith.getName(), permission, sharedBy.getName());
    }
}
