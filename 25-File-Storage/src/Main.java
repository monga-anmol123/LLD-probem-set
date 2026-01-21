import model.*;
import service.*;
import enums.*;
import observer.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  ☁️  FILE STORAGE SYSTEM DEMO ☁️");
        System.out.println("========================================\n");
        
        // Initialize service (Singleton)
        FileStorageService storage = FileStorageService.getInstance();
        
        // ====================
        // SCENARIO 1: User Registration & Storage Quota
        // ====================
        System.out.println("========================================");
        System.out.println("  SCENARIO 1: USER REGISTRATION");
        System.out.println("========================================\n");
        
        User alice = new User("U001", "Alice", "alice@example.com", StoragePlan.BASIC);
        User bob = new User("U002", "Bob", "bob@example.com", StoragePlan.FREE);
        User charlie = new User("U003", "Charlie", "charlie@example.com", StoragePlan.PREMIUM);
        
        storage.registerUser(alice);
        storage.registerUser(bob);
        storage.registerUser(charlie);
        
        // Add observers
        storage.addObserver(new UserNotificationObserver(alice));
        storage.addObserver(new UserNotificationObserver(bob));
        storage.addObserver(new UserNotificationObserver(charlie));
        
        System.out.println();
        
        // ====================
        // SCENARIO 2: File Upload & Download
        // ====================
        System.out.println("========================================");
        System.out.println("  SCENARIO 2: FILE UPLOAD & DOWNLOAD");
        System.out.println("========================================\n");
        
        Folder aliceRoot = storage.getRootFolder(alice);
        
        File file1 = storage.uploadFile(alice, "report.pdf", "Annual Report Content",
            10 * 1024 * 1024, aliceRoot); // 10 MB
        File file2 = storage.uploadFile(alice, "presentation.pptx", "Q4 Presentation",
            20 * 1024 * 1024, aliceRoot); // 20 MB
        
        System.out.println("\n📊 Alice's Storage:");
        System.out.printf("Used: %s / %s (%.1f%%)\n",
            alice.getUsedStorageDisplay(),
            alice.getStoragePlan().getQuotaDisplay(),
            alice.getStorageUsagePercent());
        
        System.out.println("\n📥 Downloading file...");
        String content = storage.downloadFile(alice, file1);
        System.out.println("Content preview: " + content.substring(0, Math.min(50, content.length())));
        
        // ====================
        // SCENARIO 3: Folder Hierarchy
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 3: FOLDER HIERARCHY");
        System.out.println("========================================\n");
        
        Folder documents = storage.createFolder(alice, "Documents", aliceRoot);
        Folder work = storage.createFolder(alice, "Work", documents);
        Folder personal = storage.createFolder(alice, "Personal", documents);
        
        File workDoc = storage.uploadFile(alice, "project.docx", "Project Plan",
            5 * 1024 * 1024, work);
        File personalDoc = storage.uploadFile(alice, "notes.txt", "Personal Notes",
            1 * 1024, personal);
        
        System.out.println("\n📁 Alice's Folder Structure:");
        aliceRoot.listContents(0);
        
        // ====================
        // SCENARIO 4: Sharing with Permissions
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 4: SHARING WITH PERMISSIONS");
        System.out.println("========================================\n");
        
        storage.shareItem(alice, file1, bob, Permission.EDITOR);
        storage.shareItem(alice, documents, charlie, Permission.VIEWER);
        
        System.out.println("\n✏️  Bob (EDITOR) modifies shared file:");
        storage.modifyFile(bob, file1, "Updated Annual Report - Q4 2026");
        
        System.out.println("\n❌ Charlie (VIEWER) tries to modify:");
        try {
            storage.modifyFile(charlie, workDoc, "Trying to modify");
        } catch (SecurityException e) {
            System.out.println("   Error: " + e.getMessage());
            System.out.println("   ✅ Permission check works!");
        }
        
        // ====================
        // SCENARIO 5: File Versioning
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 5: FILE VERSIONING");
        System.out.println("========================================\n");
        
        File versionedFile = storage.uploadFile(alice, "document.txt",
            "Version 1 content", 2 * 1024, aliceRoot);
        
        // Share with Bob so he can edit
        storage.shareItem(alice, versionedFile, bob, Permission.EDITOR);
        
        System.out.println("\nCreating versions...");
        storage.modifyFile(alice, versionedFile, "Version 2 content");
        storage.modifyFile(bob, versionedFile, "Version 3 content by Bob");
        storage.modifyFile(alice, versionedFile, "Version 4 content");
        
        System.out.println("\n📜 Version History:");
        for (FileVersion version : versionedFile.getVersions()) {
            System.out.println("   " + version);
        }
        
        System.out.println("\n⏮️  Restoring to version 2...");
        versionedFile.restoreVersion(2);
        System.out.println("Current content: " + versionedFile.getContent());
        
        // ====================
        // SCENARIO 6: Storage Quota Enforcement
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 6: STORAGE QUOTA ENFORCEMENT");
        System.out.println("========================================\n");
        
        Folder bobRoot = storage.getRootFolder(bob);
        
        System.out.println("Bob's quota: " + bob.getStoragePlan().getQuotaDisplay());
        
        // Upload files to use most of Bob's quota
        storage.uploadFile(bob, "large1.zip", "Large file 1",
            800 * 1024 * 1024, bobRoot); // 800 MB
        
        System.out.printf("\nBob's storage: %s / %s (%.1f%%)\n",
            bob.getUsedStorageDisplay(),
            bob.getStoragePlan().getQuotaDisplay(),
            bob.getStorageUsagePercent());
        
        System.out.println("\n❌ Trying to upload 300MB file (exceeds quota):");
        try {
            storage.uploadFile(bob, "large2.zip", "Large file 2",
                300 * 1024 * 1024, bobRoot);
        } catch (IllegalStateException e) {
            System.out.println("   Error: " + e.getMessage());
            System.out.println("   ✅ Quota enforcement works!");
        }
        
        // ====================
        // SCENARIO 7: Search Functionality
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 7: SEARCH FUNCTIONALITY");
        System.out.println("========================================\n");
        
        // Upload more files for search
        storage.uploadFile(alice, "report_2025.pdf", "2025 Report",
            3 * 1024 * 1024, documents);
        storage.uploadFile(alice, "budget_report.xlsx", "Budget",
            2 * 1024 * 1024, work);
        storage.uploadFile(charlie, "image.jpg", "Photo",
            5 * 1024 * 1024, storage.getRootFolder(charlie));
        
        System.out.println("🔍 Search by name 'report':");
        for (File file : storage.searchByName("report")) {
            System.out.println("   - " + file.getName() + " (Owner: " + 
                file.getOwner().getName() + ")");
        }
        
        System.out.println("\n🔍 Search by type DOCUMENT:");
        for (File file : storage.searchByType(FileType.DOCUMENT)) {
            System.out.println("   - " + file.getName() + " (" + file.getType() + ")");
        }
        
        System.out.println("\n🔍 Search by owner Alice:");
        for (File file : storage.searchByOwner(alice)) {
            System.out.println("   - " + file.getName());
        }
        
        // ====================
        // SCENARIO 8: File Operations (Move, Delete)
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 8: FILE OPERATIONS");
        System.out.println("========================================\n");
        
        Folder archive = storage.createFolder(alice, "Archive", aliceRoot);
        
        System.out.println("📦 Moving file to Archive...");
        file2.setParent(archive);
        archive.addItem(file2);
        aliceRoot.removeItem(file2);
        System.out.println("   New path: " + file2.getPath());
        
        System.out.println("\n🗑️  Deleting file...");
        long sizeBefore = alice.getUsedStorage();
        storage.deleteFile(alice, file2);
        long sizeAfter = alice.getUsedStorage();
        System.out.printf("   Storage freed: %s\n",
            formatBytes(sizeBefore - sizeAfter));
        
        // ====================
        // SCENARIO 9: Shared Files List
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 9: SHARED FILES");
        System.out.println("========================================\n");
        
        System.out.println("📤 Files shared with Bob:");
        for (FileSystemItem item : storage.getSharedWithUser(bob)) {
            System.out.println("   - " + item.getName() + " by " + 
                item.getOwner().getName());
        }
        
        System.out.println("\n📤 Files shared with Charlie:");
        for (FileSystemItem item : storage.getSharedWithUser(charlie)) {
            System.out.println("   - " + item.getName() + " by " + 
                item.getOwner().getName());
        }
        
        // ====================
        // Final Summary
        // ====================
        System.out.println("\n========================================");
        System.out.println("  📊 FINAL STORAGE SUMMARY");
        System.out.println("========================================\n");
        
        System.out.println("Alice: " + alice);
        System.out.println("Bob: " + bob);
        System.out.println("Charlie: " + charlie);
        
        System.out.println("\n========================================");
        System.out.println("  ✅ DEMO COMPLETE!");
        System.out.println("========================================\n");
        
        System.out.println("Design Patterns Demonstrated:");
        System.out.println("✓ Composite Pattern - File/Folder hierarchy");
        System.out.println("✓ Strategy Pattern - Permission strategies (Owner, Editor, Viewer)");
        System.out.println("✓ Singleton Pattern - FileStorageService");
        System.out.println("✓ Observer Pattern - Notifications for file events");
        
        System.out.println("\nKey Features Demonstrated:");
        System.out.println("✓ User registration with storage quotas");
        System.out.println("✓ File upload/download with quota enforcement");
        System.out.println("✓ Folder hierarchy (nested folders)");
        System.out.println("✓ Sharing with permissions (Owner, Editor, Viewer)");
        System.out.println("✓ File versioning (track changes)");
        System.out.println("✓ Storage quota warnings (90%+)");
        System.out.println("✓ Search by name, type, owner");
        System.out.println("✓ File operations (move, delete)");
        System.out.println("✓ Permission-based access control");
        System.out.println("✓ Observer notifications");
    }
    
    private static String formatBytes(long bytes) {
        if (bytes >= 1024L * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024));
        } else if (bytes >= 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else {
            return bytes + " bytes";
        }
    }
}
