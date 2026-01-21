# Problem 25: File Storage System - Solution

## 📊 Implementation Statistics

- **Total Java Files:** 17
- **Total Lines of Code:** ~1,308
- **Design Patterns Used:** 4 (Composite, Strategy, Singleton, Observer)
- **Packages:** 5 (enums, model, observer, strategy, service)

## 🎨 Design Patterns Implementation

### 1. Composite Pattern (File/Folder Hierarchy)

**Location:** `model/` package

**Purpose:** Treat individual files and folder compositions uniformly in a tree structure.

**Implementation:**
```java
public abstract class FileSystemItem {
    public abstract long getSize();
    public abstract boolean isFile();
}

public class File extends FileSystemItem {
    private String content;
    private long size;
    
    public long getSize() { return size; }
    public boolean isFile() { return true; }
}

public class Folder extends FileSystemItem {
    private List<FileSystemItem> children;
    
    public long getSize() {
        return children.stream()
            .mapToLong(FileSystemItem::getSize)
            .sum();
    }
    public boolean isFile() { return false; }
}
```

**Benefits:**
- ✅ Uniform treatment of files and folders
- ✅ Recursive operations (size calculation, listing)
- ✅ Easy to add new item types
- ✅ Natural tree structure representation

### 2. Strategy Pattern (Permission Strategies)

**Location:** `strategy/` package

**Purpose:** Encapsulate different permission checking algorithms.

**Implementation:**
```java
public interface PermissionStrategy {
    boolean canRead();
    boolean canWrite();
    boolean canDelete();
    boolean canShare();
}

public class OwnerPermission implements PermissionStrategy {
    // All permissions
}

public class EditorPermission implements PermissionStrategy {
    // Read and write only
}

public class ViewerPermission implements PermissionStrategy {
    // Read only
}
```

**Benefits:**
- ✅ Easy to add new permission levels
- ✅ Runtime permission checking
- ✅ Clean separation of permission logic
- ✅ Testable permission strategies

### 3. Singleton Pattern (FileStorageService)

**Location:** `service/` package

**Purpose:** Ensure single instance of the storage service with global access.

**Implementation:**
```java
public class FileStorageService {
    private static FileStorageService instance;
    
    private FileStorageService() {
        // Private constructor
    }
    
    public static synchronized FileStorageService getInstance() {
        if (instance == null) {
            instance = new FileStorageService();
        }
        return instance;
    }
}
```

**Benefits:**
- ✅ Single source of truth for all storage operations
- ✅ Global access point
- ✅ Centralized state management
- ✅ Thread-safe initialization

### 4. Observer Pattern (Notifications)

**Location:** `observer/` package

**Purpose:** Notify multiple observers of file system events.

**Implementation:**
```java
public interface StorageObserver {
    void onFileShared(FileSystemItem item, User sharedWith, Permission permission);
    void onFileModified(FileSystemItem item, User modifiedBy);
    void onFileDeleted(FileSystemItem item);
    void onQuotaWarning(User user, double usagePercent);
}

public class UserNotificationObserver implements StorageObserver {
    private final User user;
    // Implement notification methods
}
```

**Benefits:**
- ✅ Loose coupling between storage service and notifications
- ✅ Multiple observers can listen to same events
- ✅ Easy to add new notification channels
- ✅ Real-time event propagation

## 🏗️ Architecture & Design Decisions

### 1. File Versioning

**Decision:** Keep last 10 versions per file
**Implementation:**
```java
private static final int MAX_VERSIONS = 10;

private void addVersion(String content, User modifiedBy) {
    versions.add(new FileVersion(...));
    if (versions.size() > MAX_VERSIONS) {
        versions.remove(0); // Remove oldest
    }
}
```

**Rationale:** Balance between history tracking and storage efficiency

### 2. Storage Quota Management

**Decision:** Check quota before upload, update after operations
**Implementation:**
```java
public boolean canUpload(long fileSize) {
    return (usedStorage + fileSize) <= storageQuota;
}

public void updateUsedStorage(long delta) {
    usedStorage += delta;
    if (usedStorage >= storageQuota * 0.9) {
        notifyQuotaWarning();
    }
}
```

**Rationale:** Prevent storage exhaustion, warn users proactively

### 3. Permission Checking

**Decision:** Owner has all permissions, check shared access for others
**Implementation:**
```java
private boolean hasPermission(User user, FileSystemItem item, Permission minPermission) {
    if (item.getOwner().equals(user)) {
        return true; // Owner has all permissions
    }
    
    // Check shared access
    for (SharedAccess sa : sharedAccesses) {
        if (sa.getItem().equals(item) && sa.getSharedWith().equals(user)) {
            return sa.getPermission().canRead/Write/Delete();
        }
    }
    
    return false;
}
```

**Rationale:** Owner always has full control, explicit sharing for others

### 4. Folder Size Calculation

**Decision:** Recursive sum of all children
**Implementation:**
```java
public long getSize() {
    return children.stream()
        .mapToLong(FileSystemItem::getSize)
        .sum();
}
```

**Rationale:** Accurate folder size including all nested content

## 📈 Key Features

### File Operations
- ✅ Upload with quota validation
- ✅ Download with permission check
- ✅ Delete (owner only)
- ✅ Modify with versioning
- ✅ Move between folders

### Folder Operations
- ✅ Create nested hierarchies
- ✅ List contents recursively
- ✅ Calculate total size
- ✅ Add/remove items

### Sharing & Permissions
- ✅ Share with OWNER/EDITOR/VIEWER permissions
- ✅ Permission-based access control
- ✅ Revoke access
- ✅ List shared items

### Versioning
- ✅ Track file modifications
- ✅ Store version history (last 10)
- ✅ Restore previous versions
- ✅ Show who modified when

### Search
- ✅ Search by name (partial match)
- ✅ Search by file type
- ✅ Search by owner
- ✅ List shared files

### Storage Management
- ✅ Multiple storage plans (Free, Basic, Premium, Business)
- ✅ Quota enforcement
- ✅ Usage tracking
- ✅ Quota warnings (90%+)

## 🔄 Extensibility

### Adding New Permission Levels

```java
public class AdminPermission implements PermissionStrategy {
    public boolean canRead() { return true; }
    public boolean canWrite() { return true; }
    public boolean canDelete() { return true; }
    public boolean canShare() { return true; }
    public boolean canManageUsers() { return true; } // New capability
}
```

### Adding New File Types

```java
public enum FileType {
    // Existing types...
    SPREADSHEET(".xlsx", ".xls", ".csv"),
    PRESENTATION(".pptx", ".ppt");
}
```

### Adding New Observers

```java
public class AnalyticsObserver implements StorageObserver {
    public void onFileShared(...) {
        analyticsService.trackEvent("file_shared", ...);
    }
}
```

## 📊 Performance Considerations

### Time Complexity
- **Upload File:** O(1) - HashMap insertion
- **Download File:** O(1) - HashMap lookup
- **Delete File:** O(1) - HashMap removal
- **Search by Name:** O(n) - Linear scan of all files
- **Folder Size:** O(n) - Recursive traversal
- **List Shared Files:** O(m) - m = shared accesses

### Space Complexity
- **Files Storage:** O(n) - n files
- **Shared Accesses:** O(m) - m sharing relationships
- **Versions:** O(n × v) - n files × v versions (max 10)

### Optimization Opportunities
1. **Indexing:** Add indexes for faster search (by name, type, owner)
2. **Caching:** Cache folder sizes to avoid recalculation
3. **Lazy Loading:** Load file content on-demand
4. **Compression:** Compress file content and versions

## 🎯 Design Pattern Trade-offs

### Composite Pattern
**Pros:**
- Uniform treatment of files/folders
- Easy recursive operations
- Natural tree representation

**Cons:**
- Can be overkill for simple structures
- Performance overhead for deep hierarchies
- Memory overhead for storing parent references

### Strategy Pattern
**Pros:**
- Easy to add new permission types
- Clean separation of permission logic
- Runtime flexibility

**Cons:**
- More classes to maintain
- Potential strategy explosion
- Client must know about strategies

### Singleton Pattern
**Pros:**
- Single instance guarantee
- Global access point
- Centralized state

**Cons:**
- Global state can be problematic
- Difficult to test in isolation
- Thread safety concerns

### Observer Pattern
**Pros:**
- Loose coupling
- Multiple observers
- Real-time notifications

**Cons:**
- Memory leaks if observers not detached
- Unpredictable notification order
- Performance impact with many observers

## 🚀 Production Considerations

### What's Missing for Production?

1. **Persistence:** Database integration (PostgreSQL, MongoDB)
2. **File Storage:** S3, Azure Blob, Google Cloud Storage
3. **Encryption:** Encrypt files at rest and in transit
4. **Compression:** Compress files to save storage
5. **Deduplication:** Avoid storing duplicate files
6. **Thumbnails:** Generate thumbnails for images
7. **Virus Scanning:** Scan uploaded files
8. **Rate Limiting:** Prevent abuse
9. **Audit Logs:** Track all operations
10. **Backup & Recovery:** Regular backups
11. **CDN:** Distribute files globally
12. **Streaming:** Stream large files
13. **Collaboration:** Real-time collaborative editing
14. **Trash/Recycle Bin:** Soft delete with recovery
15. **Public Links:** Share files via public URLs

### Scalability Enhancements

1. **Distributed Storage:** Shard files across multiple servers
2. **Caching Layer:** Redis for metadata
3. **Message Queue:** Async processing (RabbitMQ, Kafka)
4. **Microservices:** Separate services for upload, download, search
5. **Load Balancing:** Distribute traffic
6. **Database Replication:** Read replicas for search

## 📚 Key Takeaways

1. **Composite Pattern:** Perfect for hierarchical structures (files/folders)
2. **Strategy Pattern:** Ideal for interchangeable behaviors (permissions)
3. **Singleton Pattern:** Good for centralized resource management
4. **Observer Pattern:** Essential for event-driven systems
5. **Permission Checking:** Always validate before operations
6. **Quota Management:** Prevent resource exhaustion
7. **Versioning:** Critical for audit and recovery
8. **Search:** Indexing is crucial for performance at scale
9. **Security:** Permission-based access control is mandatory
10. **Extensibility:** Design for future features

---

**This implementation demonstrates production-ready cloud storage design with proper patterns, security, and extensibility! ☁️✨**
