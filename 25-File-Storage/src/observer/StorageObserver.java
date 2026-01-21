package observer;

import model.FileSystemItem;
import model.User;
import enums.Permission;

public interface StorageObserver {
    void onFileShared(FileSystemItem item, User sharedWith, Permission permission);
    void onFileModified(FileSystemItem item, User modifiedBy);
    void onFileDeleted(FileSystemItem item);
    void onQuotaWarning(User user, double usagePercent);
}
