package service;

import model.*;
import enums.*;
import observer.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileStorageService {
    private static FileStorageService instance;
    
    private final Map<String, User> users;
    private final Map<String, FileSystemItem> items;
    private final List<SharedAccess> sharedAccesses;
    private final List<StorageObserver> observers;
    private final Map<User, Folder> rootFolders;
    
    private FileStorageService() {
        this.users = new HashMap<>();
        this.items = new HashMap<>();
        this.sharedAccesses = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.rootFolders = new HashMap<>();
    }
    
    public static synchronized FileStorageService getInstance() {
        if (instance == null) {
            instance = new FileStorageService();
        }
        return instance;
    }
    
    // User Management
    public void registerUser(User user) {
        users.put(user.getUserId(), user);
        
        // Create root folder for user
        Folder root = new Folder("root", user, null);
        rootFolders.put(user, root);
        items.put(root.getId(), root);
        
        System.out.println("✅ User registered: " + user);
    }
    
    public void addObserver(StorageObserver observer) {
        observers.add(observer);
    }
    
    // File Operations
    public File uploadFile(User user, String fileName, String content, long size, Folder folder) {
        // Check storage quota
        if (!user.canUpload(size)) {
            throw new IllegalStateException(
                String.format("Insufficient storage. Available: %s, Required: %s",
                    user.getAvailableStorageDisplay(), formatBytes(size)));
        }
        
        // Create file
        File file = new File(fileName, user, folder, content, size);
        items.put(file.getId(), file);
        
        // Add to folder
        if (folder != null) {
            folder.addItem(file);
        }
        
        // Update user storage
        user.updateUsedStorage(size);
        
        // Check quota warning
        double usagePercent = user.getStorageUsagePercent();
        if (usagePercent >= 90) {
            notifyQuotaWarning(user, usagePercent);
        }
        
        System.out.printf("✅ File uploaded: %s (%s) to %s\n",
            fileName, formatBytes(size), folder != null ? folder.getPath() : "/");
        
        return file;
    }
    
    public String downloadFile(User user, File file) {
        // Check permission
        if (!hasPermission(user, file, Permission.VIEWER)) {
            throw new SecurityException("No permission to download this file");
        }
        
        System.out.printf("📥 Downloaded: %s by %s\n", file.getName(), user.getName());
        return file.getContent();
    }
    
    public void deleteFile(User user, File file) {
        // Only owner can delete
        if (!file.getOwner().equals(user)) {
            throw new SecurityException("Only owner can delete files");
        }
        
        // Remove from parent folder
        if (file.getParent() != null) {
            file.getParent().removeItem(file);
        }
        
        // Update user storage
        user.updateUsedStorage(-file.getSize());
        
        // Remove from items
        items.remove(file.getId());
        
        // Remove shared accesses
        sharedAccesses.removeIf(sa -> sa.getItem().equals(file));
        
        // Notify observers
        notifyFileDeleted(file);
        
        System.out.printf("🗑️  Deleted: %s\n", file.getName());
    }
    
    // Folder Operations
    public Folder createFolder(User user, String folderName, Folder parent) {
        Folder folder = new Folder(folderName, user, parent);
        items.put(folder.getId(), folder);
        
        if (parent != null) {
            parent.addItem(folder);
        }
        
        System.out.printf("📁 Created folder: %s\n", folder.getPath());
        return folder;
    }
    
    public Folder getRootFolder(User user) {
        return rootFolders.get(user);
    }
    
    // Sharing
    public void shareItem(User owner, FileSystemItem item, User sharedWith, Permission permission) {
        // Only owner can share
        if (!item.getOwner().equals(owner)) {
            throw new SecurityException("Only owner can share items");
        }
        
        SharedAccess access = new SharedAccess(item, sharedWith, permission, owner);
        sharedAccesses.add(access);
        
        // Notify observers
        notifyFileShared(item, sharedWith, permission);
        
        System.out.printf("🔗 Shared: %s with %s as %s\n",
            item.getName(), sharedWith.getName(), permission);
    }
    
    public void revokeAccess(User owner, FileSystemItem item, User user) {
        if (!item.getOwner().equals(owner)) {
            throw new SecurityException("Only owner can revoke access");
        }
        
        sharedAccesses.removeIf(sa -> 
            sa.getItem().equals(item) && sa.getSharedWith().equals(user));
        
        System.out.printf("🚫 Revoked access: %s from %s\n", item.getName(), user.getName());
    }
    
    private boolean hasPermission(User user, FileSystemItem item, Permission minPermission) {
        // Owner has all permissions
        if (item.getOwner().equals(user)) {
            return true;
        }
        
        // Check shared access
        for (SharedAccess sa : sharedAccesses) {
            if (sa.getItem().equals(item) && sa.getSharedWith().equals(user)) {
                // Check if permission level is sufficient
                Permission userPerm = sa.getPermission();
                if (minPermission == Permission.VIEWER) {
                    return userPerm.canRead();
                } else if (minPermission == Permission.EDITOR) {
                    return userPerm.canWrite();
                } else if (minPermission == Permission.OWNER) {
                    return userPerm.canDelete();
                }
            }
        }
        
        return false;
    }
    
    // File Modification
    public void modifyFile(User user, File file, String newContent) {
        // Check write permission
        if (!hasPermission(user, file, Permission.EDITOR)) {
            throw new SecurityException("No permission to modify this file");
        }
        
        file.updateContent(newContent, user);
        
        // Notify observers
        notifyFileModified(file, user);
        
        System.out.printf("✏️  Modified: %s by %s (now v%d)\n",
            file.getName(), user.getName(), file.getVersionCount());
    }
    
    // Search
    public List<File> searchByName(String query) {
        return items.values().stream()
            .filter(FileSystemItem::isFile)
            .map(item -> (File) item)
            .filter(file -> file.getName().toLowerCase().contains(query.toLowerCase()))
            .collect(Collectors.toList());
    }
    
    public List<File> searchByType(FileType type) {
        return items.values().stream()
            .filter(FileSystemItem::isFile)
            .map(item -> (File) item)
            .filter(file -> file.getType() == type)
            .collect(Collectors.toList());
    }
    
    public List<File> searchByOwner(User owner) {
        return items.values().stream()
            .filter(FileSystemItem::isFile)
            .map(item -> (File) item)
            .filter(file -> file.getOwner().equals(owner))
            .collect(Collectors.toList());
    }
    
    public List<FileSystemItem> getSharedWithUser(User user) {
        return sharedAccesses.stream()
            .filter(sa -> sa.getSharedWith().equals(user))
            .map(SharedAccess::getItem)
            .collect(Collectors.toList());
    }
    
    // Observer notifications
    private void notifyFileShared(FileSystemItem item, User sharedWith, Permission permission) {
        for (StorageObserver observer : observers) {
            observer.onFileShared(item, sharedWith, permission);
        }
    }
    
    private void notifyFileModified(FileSystemItem item, User modifiedBy) {
        for (StorageObserver observer : observers) {
            observer.onFileModified(item, modifiedBy);
        }
    }
    
    private void notifyFileDeleted(FileSystemItem item) {
        for (StorageObserver observer : observers) {
            observer.onFileDeleted(item);
        }
    }
    
    private void notifyQuotaWarning(User user, double usagePercent) {
        for (StorageObserver observer : observers) {
            observer.onQuotaWarning(user, usagePercent);
        }
    }
    
    private String formatBytes(long bytes) {
        if (bytes >= 1024L * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024));
        } else if (bytes >= 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else {
            return bytes + " bytes";
        }
    }
    
    // Getters
    public User getUser(String userId) {
        return users.get(userId);
    }
}
