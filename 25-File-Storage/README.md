# Problem 25: File Storage System (Dropbox/Google Drive)

## 🎯 Difficulty: Hard ⭐⭐⭐⭐

## 📝 Problem Statement

Design a cloud file storage system similar to Dropbox or Google Drive that supports file upload/download, folder hierarchy, sharing with permissions, file versioning, search functionality, and storage quota management. The system should handle multiple users, enforce access control, track file versions, and provide a complete file management experience.

## 🔍 Functional Requirements (FR)

### FR1: User Management
- Register users with storage quota (e.g., 10GB free, 100GB premium)
- Track used storage per user
- Enforce storage limits on uploads
- User authentication and authorization

### FR2: File Operations
- **Upload**: Add files with size validation against quota
- **Download**: Retrieve file contents (with permission check)
- **Delete**: Remove files (owner only)
- **Move**: Move files between folders
- **Copy**: Duplicate files
- **Rename**: Change file names
- Track file metadata (name, size, type, owner, created/modified dates)

### FR3: Folder Operations (Composite Pattern)
- Create nested folder hierarchies
- Delete folders (and all contents)
- List folder contents
- Move folders
- Copy folders
- Navigate folder tree

### FR4: Sharing & Permissions (Strategy Pattern)
- Share files/folders with other users
- Permission levels:
  - **Owner**: Full control (read, write, delete, share)
  - **Editor**: Read and write (cannot delete or share)
  - **Viewer**: Read-only access
- Revoke access
- List shared items

### FR5: File Versioning
- Maintain version history for files
- Track who modified and when
- Restore previous versions
- View version history
- Limit version count (e.g., keep last 10 versions)

### FR6: Search Functionality
- Search by file name (partial match)
- Search by file type/extension
- Search by owner
- Search in specific folders
- Search shared files

### FR7: Storage Quota Management
- Calculate total storage used
- Prevent uploads exceeding quota
- Display available storage
- Upgrade storage plans

### FR8: Notifications (Observer Pattern)
- Notify on file shared with user
- Notify on file modified (for shared files)
- Notify on storage quota warnings (90%, 100%)
- Notify on file deleted

## 🚫 Non-Functional Requirements (NFR)

### NFR1: Performance
- Fast file lookup (O(1) by ID)
- Efficient folder traversal
- Quick search operations

### NFR2: Scalability
- Support millions of files
- Handle large folder hierarchies
- Efficient storage calculation

### NFR3: Security
- Permission-based access control
- Owner-only operations enforced
- Secure file sharing

### NFR4: Data Integrity
- Atomic file operations
- Consistent version history
- Accurate storage tracking

### NFR5: Extensibility
- Easy to add new permission types
- Easy to add new file operations
- Support for future features (encryption, compression)

## 🎨 Design Patterns to Use

### 1. **Composite Pattern**
- **Where:** File and Folder hierarchy
- **Why:** Files and folders form a tree structure. Composite pattern allows treating individual files and folder compositions uniformly.

### 2. **Strategy Pattern**
- **Where:** Permission checking strategies
- **Why:** Different permission levels (Owner, Editor, Viewer) have different access rules. Strategy pattern allows runtime permission checking.

### 3. **Singleton Pattern**
- **Where:** FileStorageService (central storage manager)
- **Why:** Single instance to manage all files, users, and operations. Global access point for storage operations.

### 4. **Observer Pattern**
- **Where:** Notifications for file events
- **Why:** Multiple observers (users, analytics) need to be notified of file operations. Observer pattern provides loose coupling.

## 📋 Core Entities

### 1. **User**
- Attributes: userId, name, email, storageQuota, usedStorage
- Methods: canUpload(size), updateUsedStorage()

### 2. **FileSystemItem** (Abstract - Composite Pattern)
- Attributes: id, name, owner, createdDate, modifiedDate, parent
- Methods: getSize(), isFile(), isFolder()
- Subclasses: File, Folder

### 3. **File** (extends FileSystemItem)
- Attributes: content, size, type, versions
- Methods: addVersion(), getVersion(), getLatestVersion()

### 4. **Folder** (extends FileSystemItem - Composite)
- Attributes: children (List<FileSystemItem>)
- Methods: addItem(), removeItem(), getChildren(), getSize()

### 5. **FileVersion**
- Attributes: versionNumber, content, size, modifiedBy, modifiedDate
- Methods: restore()

### 6. **Permission** (Enum)
- Values: OWNER, EDITOR, VIEWER
- Methods: canRead(), canWrite(), canDelete(), canShare()

### 7. **SharedAccess**
- Attributes: item, user, permission, sharedBy, sharedDate
- Methods: hasPermission(operation)

### 8. **PermissionStrategy** (Interface - Strategy Pattern)
- Methods: canRead(), canWrite(), canDelete(), canShare()
- Implementations: OwnerPermission, EditorPermission, ViewerPermission

### 9. **FileStorageService** (Singleton)
- Attributes: users, files, folders, sharedAccess
- Methods: upload(), download(), delete(), share(), search()

### 10. **StorageObserver** (Interface - Observer Pattern)
- Methods: onFileShared(), onFileModified(), onFileDeleted(), onQuotaWarning()

## 🧪 Test Scenarios

### Scenario 1: User Registration & Storage Quota
```
1. Register User A with 1GB quota
2. Register User B with 500MB quota
3. Display storage info
```

### Scenario 2: File Upload & Download
```
1. User A uploads file1.txt (10MB)
2. User A uploads file2.pdf (20MB)
3. User A downloads file1.txt
4. Display storage used
```

### Scenario 3: Folder Hierarchy
```
1. User A creates /Documents folder
2. User A creates /Documents/Work folder
3. User A uploads file to /Documents/Work
4. List folder contents recursively
```

### Scenario 4: Sharing with Permissions
```
1. User A shares file1.txt with User B as EDITOR
2. User B modifies file1.txt
3. User A shares folder with User C as VIEWER
4. User C tries to modify (should fail)
```

### Scenario 5: File Versioning
```
1. User A uploads document.txt v1
2. User A modifies document.txt → v2
3. User B (editor) modifies document.txt → v3
4. View version history
5. Restore to v2
```

### Scenario 6: Storage Quota Enforcement
```
1. User B has 500MB quota, used 450MB
2. User B tries to upload 100MB file → Rejected
3. User B deletes old files
4. User B uploads 40MB file → Success
```

### Scenario 7: Search Functionality
```
1. Search files by name "report"
2. Search files by type ".pdf"
3. Search files by owner "User A"
4. Search in specific folder
```

### Scenario 8: File Operations (Move, Copy, Delete)
```
1. User A moves file from /Documents to /Archive
2. User A copies file to /Backup
3. User A deletes file from /Archive
4. Verify operations
```

## ⏱️ Time Allocation (90 minutes)

- **10 mins:** Clarify requirements, identify entities
- **15 mins:** Design class structure (Composite pattern)
- **15 mins:** Implement Strategy pattern for permissions
- **15 mins:** Implement Singleton service with file operations
- **10 mins:** Implement Observer pattern for notifications
- **10 mins:** Implement versioning and search
- **15 mins:** Write Main.java with comprehensive scenarios

## 💡 Hints

<details>
<summary>Hint 1: Composite Pattern for File System</summary>

```java
public abstract class FileSystemItem {
    protected String id;
    protected String name;
    protected User owner;
    protected Folder parent;
    
    public abstract long getSize();
    public abstract boolean isFile();
}

public class File extends FileSystemItem {
    private byte[] content;
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
</details>

<details>
<summary>Hint 2: Strategy Pattern for Permissions</summary>

```java
public interface PermissionStrategy {
    boolean canRead();
    boolean canWrite();
    boolean canDelete();
    boolean canShare();
}

public class OwnerPermission implements PermissionStrategy {
    public boolean canRead() { return true; }
    public boolean canWrite() { return true; }
    public boolean canDelete() { return true; }
    public boolean canShare() { return true; }
}

public class EditorPermission implements PermissionStrategy {
    public boolean canRead() { return true; }
    public boolean canWrite() { return true; }
    public boolean canDelete() { return false; }
    public boolean canShare() { return false; }
}
```
</details>

<details>
<summary>Hint 3: File Versioning</summary>

```java
public class File extends FileSystemItem {
    private List<FileVersion> versions;
    private int maxVersions = 10;
    
    public void addVersion(byte[] content, User modifiedBy) {
        FileVersion version = new FileVersion(
            versions.size() + 1, content, modifiedBy);
        versions.add(version);
        
        // Keep only last N versions
        if (versions.size() > maxVersions) {
            versions.remove(0);
        }
    }
    
    public void restoreVersion(int versionNumber) {
        FileVersion version = getVersion(versionNumber);
        this.content = version.getContent();
    }
}
```
</details>

<details>
<summary>Hint 4: Storage Quota Management</summary>

```java
public class User {
    private long storageQuota;
    private long usedStorage;
    
    public boolean canUpload(long fileSize) {
        return (usedStorage + fileSize) <= storageQuota;
    }
    
    public void updateUsedStorage(long delta) {
        usedStorage += delta;
        
        if (usedStorage >= storageQuota * 0.9) {
            notifyQuotaWarning();
        }
    }
}
```
</details>

<details>
<summary>Hint 5: Search Implementation</summary>

```java
public List<File> searchByName(String query) {
    return allFiles.stream()
        .filter(f -> f.getName().toLowerCase()
            .contains(query.toLowerCase()))
        .collect(Collectors.toList());
}

public List<File> searchByType(String extension) {
    return allFiles.stream()
        .filter(f -> f.getName().endsWith(extension))
        .collect(Collectors.toList());
}
```
</details>

## ✅ Success Criteria

- [ ] Compiles without errors
- [ ] Uses Composite pattern correctly for file/folder hierarchy
- [ ] Uses Strategy pattern correctly for permissions
- [ ] Uses Singleton pattern for FileStorageService
- [ ] Uses Observer pattern for notifications
- [ ] Handles all test scenarios
- [ ] File versioning works correctly
- [ ] Storage quota enforced properly
- [ ] Permission checks work for all operations
- [ ] Search functionality works
- [ ] Clear error messages for failed operations
- [ ] Proper folder hierarchy navigation

## 🎓 Key Learning Points

1. **Composite Pattern:** Best for tree structures (files/folders)
2. **Strategy Pattern:** Best for interchangeable behaviors (permissions)
3. **Singleton Pattern:** Best for centralized resource management
4. **Observer Pattern:** Best for event notifications
5. **Access Control:** Proper permission checking is critical
6. **Versioning:** Track changes for audit and recovery
7. **Quota Management:** Prevent resource exhaustion
8. **Tree Traversal:** Recursive operations on hierarchies

## 📚 Related Problems

- **Problem 02:** Library Management (similar resource management)
- **Problem 16:** Ride Sharing (similar Observer pattern)
- **Problem 18:** Shopping Cart (similar Strategy pattern)
- **Problem 22:** Stock Trading (similar Observer pattern)

---

**Time to implement! This is a complex cloud storage system! ☁️📁**
