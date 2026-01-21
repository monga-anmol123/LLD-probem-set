import model.*;
import service.*;
// import enums.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  LIBRARY MANAGEMENT SYSTEM DEMO");
        System.out.println("========================================\n");
        
        // Initialize library (Singleton)
        Library library = Library.getInstance("City Public Library", "123 Main St");
        
        // Create books
        System.out.println("📚 Adding Books to Catalog...");
        Book book1 = new Book("978-0134685991", "Effective Java", "Joshua Bloch", "Addison-Wesley");
        book1.addSubject("Programming");
        book1.addSubject("Java");
        
        Book book2 = new Book("978-0132350884", "Clean Code", "Robert Martin", "Prentice Hall");
        book2.addSubject("Programming");
        book2.addSubject("Software Engineering");
        
        Book book3 = new Book("978-0201633610", "Design Patterns", "Gang of Four", "Addison-Wesley");
        book3.addSubject("Programming");
        book3.addSubject("Design Patterns");
        
        // Create book items (physical copies)
        BookItem item1 = new BookItem("BC-001", book1);
        BookItem item2 = new BookItem("BC-002", book1); // Another copy of Effective Java
        BookItem item3 = new BookItem("BC-003", book2);
        BookItem item4 = new BookItem("BC-004", book3);
        
        library.addBookItem(item1);
        library.addBookItem(item2);
        library.addBookItem(item3);
        library.addBookItem(item4);
        System.out.println("✅ Added 4 book items (2 copies of Effective Java, 1 Clean Code, 1 Design Patterns)\n");
        
        // Create members
        System.out.println("👥 Registering Members...");
        Member alice = new StandardMember("M001", "Alice Johnson", "alice@email.com");
        Member bob = new PremiumMember("M002", "Bob Smith", "bob@email.com");
        
        library.addMember(alice);
        library.addMember(bob);
        System.out.println("✅ Registered Alice (Standard - max 5 books)");
        System.out.println("✅ Registered Bob (Premium - max 10 books)\n");
        
        // Display initial stats
        library.displayLibraryStats();
        
        // ====================
        // SCENARIO 1: Search Books
        // ====================
        System.out.println("========================================");
        System.out.println("  SCENARIO 1: SEARCH FUNCTIONALITY");
        System.out.println("========================================\n");
        
        System.out.println("🔍 Searching for 'effective java' by title:");
        List<BookItem> searchResults = library.searchBooks("effective java", "title");
        searchResults.forEach(item -> 
            System.out.println("   Found: " + item.getBarcode() + " - " + 
                               item.getBook().getTitle() + " (Status: " + item.getStatus() + ")")
        );
        
        System.out.println("\n🔍 Searching by author 'Robert Martin':");
        searchResults = library.searchBooks("robert martin", "author");
        searchResults.forEach(item -> 
            System.out.println("   Found: " + item.getBarcode() + " - " + 
                               item.getBook().getTitle() + " by " + item.getBook().getAuthor())
        );
        
        // ====================
        // SCENARIO 2: Borrow Books
        // ====================
        System.out.println("\n========================================");
        System.out.println("  SCENARIO 2: BORROWING BOOKS");
        System.out.println("========================================\n");
        
        System.out.println("📖 Alice borrows 'Effective Java' (BC-001):");
        BookLending lending1 = library.borrowBook("M001", "BC-001");
        
        System.out.println("\n📖 Bob borrows 'Clean Code' (BC-003):");
        BookLending lending2 = library.borrowBook("M002", "BC-003");
        
        library.displayLibraryStats();
        
        // ====================
        // SCENARIO 3: Try to borrow already borrowed book
        // ====================
        System.out.println("========================================");
        System.out.println("  SCENARIO 3: BORROWING ERROR");
        System.out.println("========================================\n");
        
        System.out.println("❌ Alice tries to borrow already borrowed book (BC-001):");
        try {
            library.borrowBook("M001", "BC-001");
        } catch (RuntimeException e) {
            System.out.println("   Error: " + e.getMessage());
            System.out.println("   ✓ Error handled correctly!\n");
        }
        
        // ====================
        // SCENARIO 4: Reservation System (Observer Pattern)
        // ====================
        System.out.println("========================================");
        System.out.println("  SCENARIO 4: RESERVATION (Observer Pattern)");
        System.out.println("========================================\n");
        
        System.out.println("📖 Bob tries to borrow 'Effective Java' but it's all borrowed:");
        System.out.println("   Copy 1 (BC-001): Borrowed by Alice");
        System.out.println("   Copy 2 (BC-002): Available");
        System.out.println("\n   Wait, BC-002 is available! Let Bob borrow it:");
        library.borrowBook("M002", "BC-002");
        
        System.out.println("\n📖 Now all copies are borrowed. Charlie wants to reserve:");
        Member charlie = new StandardMember("M003", "Charlie Brown", "charlie@email.com");
        library.addMember(charlie);
        
        System.out.println("📝 Charlie reserves 'Effective Java':");
       library.reserveBook("M003", "978-0134685991");
        
        System.out.println("\n📤 Alice returns her book:");
        library.returnBook(lending1.getLendingId());
        
        System.out.println("\n   Notice: Charlie should receive notification (Observer Pattern)!\n");
        
        // ====================
        // SCENARIO 5: Member Borrow Limit
        // ====================
        System.out.println("========================================");
        System.out.println("  SCENARIO 5: BORROW LIMIT");
        System.out.println("========================================\n");
        
        System.out.println("📖 Alice (Standard member, max 5 books) current borrowings: " + 
                           alice.getActiveBorrowings().size());
        
        // Add more books for testing
        Book book4 = new Book("978-1234567890", "Test Book 1", "Author A", "Publisher A");
        Book book5 = new Book("978-1234567891", "Test Book 2", "Author B", "Publisher B");
        Book book6 = new Book("978-1234567892", "Test Book 3", "Author C", "Publisher C");
        Book book7 = new Book("978-1234567893", "Test Book 4", "Author D", "Publisher D");
        Book book8 = new Book("978-1234567894", "Test Book 5", "Author E", "Publisher E");
        
        library.addBookItem(new BookItem("BC-005", book4));
        library.addBookItem(new BookItem("BC-006", book5));
        library.addBookItem(new BookItem("BC-007", book6));
        library.addBookItem(new BookItem("BC-008", book7));
        library.addBookItem(new BookItem("BC-009", book8));
        
        System.out.println("\n📖 Alice borrows 4 more books:");
        library.borrowBook("M001", "BC-005");
        library.borrowBook("M001", "BC-006");
        library.borrowBook("M001", "BC-007");
        library.borrowBook("M001", "BC-008");
        
        System.out.println("\n📖 Alice now has " + alice.getActiveBorrowings().size() + 
                           " books (max allowed: " + alice.getMaxBooksAllowed() + ")");
        
        System.out.println("\n❌ Alice tries to borrow 6th book:");
        try {
            library.borrowBook("M001", "BC-009");
        } catch (RuntimeException e) {
            System.out.println("   Error: " + e.getMessage());
            System.out.println("   ✓ Limit enforced correctly!\n");
        }
        
        // ====================
        // SCENARIO 6: Overdue Fine
        // ====================
        System.out.println("========================================");
        System.out.println("  SCENARIO 6: OVERDUE FINE CALCULATION");
        System.out.println("========================================\n");
        
        System.out.println("📖 Current lending for Bob (Clean Code):");
        System.out.println("   Due Date: " + lending2.getDueDate());
        System.out.println("   Overdue Days: " + lending2.getBookItem().calculateOverdueDays());
        
        // Simulate manual overdue (in real scenario, days would pass)
        System.out.println("\n   Note: In real scenario, if return is late, fine = overdue days × $1/day");
        System.out.println("   Example: 3 days late = $3 fine\n");
        
        // Return Bob's book
        System.out.println("📤 Bob returns 'Clean Code':");
        library.returnBook(lending2.getLendingId());
        
        // ====================
        // SCENARIO 7: Premium vs Standard
        // ====================
        System.out.println("========================================");
        System.out.println("  SCENARIO 7: MEMBERSHIP COMPARISON");
        System.out.println("========================================\n");
        
        System.out.println("👥 Membership Comparison:");
        System.out.println("   Alice (Standard): Max " + alice.getMaxBooksAllowed() + " books");
        System.out.println("   Bob (Premium): Max " + bob.getMaxBooksAllowed() + " books");
        System.out.println("\n   Premium members can borrow 2x more books!\n");
        
        // ====================
        // Final Stats
        // ====================
        System.out.println("========================================");
        System.out.println("  FINAL STATISTICS");
        System.out.println("========================================\n");
        
        library.displayLibraryStats();
        
        System.out.println("========================================");
        System.out.println("  DEMO COMPLETE!");
        System.out.println("========================================\n");
        
        System.out.println("Design Patterns Demonstrated:");
        System.out.println("✓ Singleton Pattern - Library class");
        System.out.println("✓ Observer Pattern - Book reservation notifications");
        System.out.println("✓ State Pattern - Book status (Available → Borrowed → Returned)");
        System.out.println("\nKey Features Demonstrated:");
        System.out.println("✓ Book search (by title, author, ISBN)");
        System.out.println("✓ Borrow and return books");
        System.out.println("✓ Fine calculation");
        System.out.println("✓ Reservation system with notifications");
        System.out.println("✓ Member borrow limits (Standard vs Premium)");
        System.out.println("✓ Edge case handling (already borrowed, limit exceeded)");
    }
}


