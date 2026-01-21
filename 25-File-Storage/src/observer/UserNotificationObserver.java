package observer;

import model.FileSystemItem;
import model.User;
import enums.Permission;

public class UserNotificationObserver implements StorageObserver {
    private final User user;
    
    public UserNotificationObserver(User user) {
        this.user = user;
    }
    
    @Override
    public void onFileShared(FileSystemItem item, User sharedWith, Permission permission) {
        if (sharedWith.equals(user)) {
            System.out.printf("🔔 [%s] %s was shared with you as %s\n",
                user.getName(), item.getName(), permission);
        }
    }
    
    @Override
    public void onFileModified(FileSystemItem item, User modifiedBy) {
        System.out.printf("🔔 [%s] %s was modified by %s\n",
            user.getName(), item.getName(), modifiedBy.getName());
    }
    
    @Override
    public void onFileDeleted(FileSystemItem item) {
        System.out.printf("🔔 [%s] %s was deleted\n",
            user.getName(), item.getName());
    }
    
    @Override
    public void onQuotaWarning(User targetUser, double usagePercent) {
        if (targetUser.equals(user)) {
            System.out.printf("⚠️  [%s] Storage quota warning: %.1f%% used\n",
                user.getName(), usagePercent);
        }
    }
    
    public User getUser() {
        return user;
    }
}
