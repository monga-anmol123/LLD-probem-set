# Solution: Library Management System

## ✅ Complete Implementation

This folder contains a fully working library management system demonstrating Observer, State, and Singleton design patterns.

---

## 🏗️ Architecture Overview

```
┌──────────────────────────────────────────────────────────────┐
│                         Main.java                             │
│                    (Demo/Entry Point)                         │
└───────────────────────┬──────────────────────────────────────┘
                        │
        ┌───────────────┼───────────────┐
        │               │               │
        ▼               ▼               ▼
┌──────────────┐ ┌─────────────┐ ┌────────────┐
│   Observer   │ │   Service   │ │   Model    │
│              │ │             │ │            │
│ Member       │ │ Library     │ │ Book       │
│ implements   │ │ (Singleton) │ │ BookItem   │
│ Observer     │ │ Catalog     │ │ Lending    │
│              │ │             │ │ Reservation│
└──────────────┘ └─────────────┘ └────────────┘
```

---

## 📦 Package Structure

```
src/
├── enums/                       # Type-safe enumerations
│   ├── BookStatus.java         # AVAILABLE, BORROWED, RESERVED, LOST
│   ├── MembershipType.java     # STANDARD, PREMIUM
│   └── ReservationStatus.java  # ACTIVE, FULFILLED, CANCELLED, EXPIRED
│
├── model/                       # Domain entities
│   ├── Book.java               # Book metadata (ISBN, title, author)
│   ├── BookItem.java           # Physical copy with barcode and status
│   ├── Member.java             # Abstract base class (implements Observer)
│   ├── StandardMember.java     # Max 5 books
│   ├── PremiumMember.java      # Max 10 books
│   ├── BookLending.java        # Lending record with fine calculation
│   └── BookReservation.java    # Reservation record
│
├── observer/                    # Observer Pattern
│   ├── Observer.java           # Interface for notifications
│   └── Subject.java            # Interface for observable subjects
│
├── service/                     # Business logic
│   ├── Catalog.java            # Book search with multiple indexes
│   └── Library.java            # Main service (Singleton)
│
└── Main.java                    # Comprehensive demo
```

---

## 🎨 Design Patterns Explained

### 1. **Observer Pattern** (Reservation Notifications)

**Purpose:** Notify members when reserved books become available

**Implementation:**

```java
// Observer interface
public interface Observer {
    void update(String message);
}

// Member implements Observer
public abstract class Member implements Observer {
    @Override
    public void update(String message) {
        System.out.println("📧 Notification to " + name + ": " + message);
    }
}

// Library notifies observers
private void checkAndFulfillReservations(Book book) {
    for (BookReservation reservation : activeReservations.values()) {
        if (reservation.getBook().getISBN().equals(book.getISBN())) {
            Member member = reservation.getMember();
            member.update("Your reserved book '" + book.getTitle() + 
                          "' is now available!");
            return;
        }
    }
}
```

**Benefits:**
- ✅ Decouples notification logic from book return logic
- ✅ Easy to add new notification types (Email, SMS)
- ✅ Members are automatically notified without Library knowing implementation details

**Usage:**
```java
// When Alice returns a book
library.returnBook(lendingId);

// If Bob reserved it, he automatically receives notification
// Output: "📧 Notification to Bob: Your reserved book 'Effective Java' is now available!"
```

---

### 2. **State Pattern** (Book Status Management)

**Purpose:** Clean state transitions for book items

**Implementation:**

```java
public enum BookStatus {
    AVAILABLE, BORROWED, RESERVED, LOST
}

public class BookItem {
    private BookStatus status;
    
    public void borrow(int borrowingDays) {
        if (status != BookStatus.AVAILABLE) {
            throw new IllegalStateException("Book is not available");
        }
        this.status = BookStatus.BORROWED;
        // Set dates...
    }
    
    public void returnBook() {
        this.status = BookStatus.AVAILABLE;
        // Clear dates...
    }
}
```

**State Transitions:**
```
AVAILABLE → BORROWED (when borrowed)
BORROWED → AVAILABLE (when returned)
AVAILABLE → RESERVED (when reserved)
RESERVED → AVAILABLE (when reservation fulfilled/cancelled)
```

**Benefits:**
- ✅ Clear state management
- ✅ Prevents invalid transitions (can't borrow if already borrowed)
- ✅ Easy to add new states

---

### 3. **Singleton Pattern** (Library)

**Purpose:** Ensure only one library instance exists

**Implementation:**

```java
public class Library {
    private static Library instance;
    
    private Library(String name, String address) {
        // Private constructor
        this.name = name;
        this.catalog = new Catalog();
        // ...
    }
    
    public static synchronized Library getInstance(String name, String address) {
        if (instance == null) {
            instance = new Library(name, address);
        }
        return instance;
    }
}
```

**Benefits:**
- ✅ Global access point
- ✅ Controlled instantiation
- ✅ Thread-safe (synchronized)

---

## 🔑 Key Design Decisions

### 1. **Book vs BookItem Separation**

**Why separate?**

```java
// Book = Metadata (one entry for all copies)
public class Book {
    private String ISBN;       // Same for all copies
    private String title;      // Same for all copies
    private String author;     // Same for all copies
}

// BookItem = Physical copy (multiple per book)
public class BookItem {
    private String barcode;    // Unique per copy
    private Book book;         // Reference to metadata
    private BookStatus status; // Different per copy
}
```

**Example:**
```
Book: "Effective Java" (ISBN: 978-0134685991)
  ├─ BookItem 1: Barcode BC-001, Status: BORROWED (by Alice)
  └─ BookItem 2: Barcode BC-002, Status: AVAILABLE
```

**Benefits:**
- ✅ Avoids duplicating book metadata
- ✅ Easy to track individual copies
- ✅ Can have different statuses for same book

---

### 2. **Catalog with Multiple Indexes**

**Purpose:** Fast search by different criteria

**Implementation:**

```java
public class Catalog {
    private Map<String, List<BookItem>> booksByTitle;   // Title → BookItems
    private Map<String, List<BookItem>> booksByAuthor;  // Author → BookItems
    private Map<String, BookItem> booksByBarcode;       // Barcode → BookItem
    private Map<String, List<BookItem>> booksByISBN;    // ISBN → BookItems
    
    public void addBookItem(BookItem bookItem) {
        // Index in all maps for fast lookup
    }
}
```

**Time Complexity:**
- Search by title: O(1) average
- Search by author: O(1) average
- Search by barcode: O(1)
- Search by ISBN: O(1)

**Trade-off:**
- ✅ Fast search
- ❌ More memory (multiple indexes)
- ❌ Slower insertion (update all indexes)

**Acceptable because:** Searches >> Insertions in library system

---

### 3. **Fine Calculation**

**Automatic fine calculation on return:**

```java
public double calculateFine() {
    if (returnDate == null) {
        returnDate = LocalDate.now();
    }
    
    if (returnDate.isAfter(dueDate)) {
        long overdueDays = ChronoUnit.DAYS.between(dueDate, returnDate);
        return overdueDays * FINE_PER_DAY; // $1 per day
    }
    return 0.0;
}
```

**Example:**
- Borrow date: Jan 1
- Due date: Jan 15 (14 days)
- Return date: Jan 18
- Overdue: 3 days
- Fine: 3 × $1 = $3

---

### 4. **Membership Types (Inheritance)**

**Standard vs Premium:**

```java
public abstract class Member {
    protected int maxBooksAllowed;  // Different per type
}

public class StandardMember extends Member {
    public StandardMember(...) {
        super(...);
        this.maxBooksAllowed = 5;  // Standard: 5 books
    }
}

public class PremiumMember extends Member {
    public PremiumMember(...) {
        super(...);
        this.maxBooksAllowed = 10; // Premium: 10 books
    }
}
```

**Benefits:**
- ✅ Easy to add new membership types
- ✅ Shared behavior in base class
- ✅ Type-specific limits

---

## ⚖️ Trade-offs

### **1. Observer Pattern**

**✅ Pros:**
- Decoupled notification logic
- Easy to add notification types
- Automatic updates

**❌ Cons:**
- Memory overhead (observer list)
- Can be complex with many observers

**Alternative:** Direct notification (tightly coupled)

---

### **2. In-Memory Storage**

**Current:** All data in `HashMap`

**✅ Pros:**
- Fast (O(1) operations)
- Simple implementation

**❌ Cons:**
- Lost on restart
- Not scalable (memory limit)

**Production Solution:**
```java
public interface LibraryRepository {
    void saveBook(Book book);
    BookItem getBookItem(String barcode);
    void saveMember(Member member);
    void saveLending(BookLending lending);
}

// Implement with SQL or NoSQL
```

---

### **3. Multiple Indexes in Catalog**

**✅ Pros:**
- O(1) search by any field
- Fast lookups

**❌ Cons:**
- 4x memory (4 indexes)
- Slower insertion

**Alternative:** Single index, linear search
- Memory efficient
- Slow search (O(n))

**Decision:** Trade memory for speed (searches are frequent)

---

## 🧪 Test Coverage

### **Scenarios Tested in Main.java:**

1. ✅ **Book Search**
   - By title (case-insensitive)
   - By author (case-insensitive)
   - By ISBN

2. ✅ **Borrowing System**
   - Successful borrow
   - Error: Book already borrowed
   - Error: Member exceeded limit
   - Error: Unpaid overdue fines

3. ✅ **Reservation System (Observer Pattern)**
   - Reserve unavailable book
   - Automatic notification when available
   - First-come-first-served

4. ✅ **Return System**
   - Calculate fine if overdue
   - Update book status
   - Notify waitlist

5. ✅ **Membership Limits**
   - Standard: Max 5 books
   - Premium: Max 10 books
   - Enforce limit

6. ✅ **Fine Calculation**
   - $1 per day overdue
   - Block borrowing if unpaid fines

### **Additional Test Cases:**

```java
// Test Case 1: Invalid Member
try {
    library.borrowBook("INVALID-ID", "BC-001");
} catch (IllegalArgumentException e) {
    // Expected: "Member not found"
}

// Test Case 2: Invalid Barcode
try {
    library.borrowBook("M001", "INVALID-BARCODE");
} catch (IllegalArgumentException e) {
    // Expected: "Book not found"
}

// Test Case 3: Reserve Available Book
try {
    library.reserveBook("M001", "978-0134685991"); // Book is available
} catch (RuntimeException e) {
    // Expected: "Book is available, no need to reserve"
}

// Test Case 4: Multiple Reservations (Queue)
// First person to reserve gets notified first
```

---

## 🚀 How to Compile and Run

### **Option 1: Command Line**

```bash
cd Problem-Questions/02-Library-Management/src/
javac enums/*.java model/*.java observer/*.java service/*.java Main.java
java Main
```

### **Option 2: With Package Structure (Recommended)**

```bash
# From Problem-Questions/02-Library-Management/
javac -d bin src/enums/*.java src/model/*.java src/observer/*.java src/service/*.java src/Main.java
java -cp bin Main
```

### **Expected Output:**

```
========================================
  LIBRARY MANAGEMENT SYSTEM DEMO
========================================

📚 Adding Books to Catalog...
✅ Added 4 book items (2 copies of Effective Java, 1 Clean Code, 1 Design Patterns)

👥 Registering Members...
✅ Registered Alice (Standard - max 5 books)
✅ Registered Bob (Premium - max 10 books)

========================================
  LIBRARY STATISTICS
========================================
Library: City Public Library
Total Books: 4
Available Books: 4
Total Members: 2
Active Borrowings: 0
Active Reservations: 0
Overdue Books: 0
========================================

========================================
  SCENARIO 1: SEARCH FUNCTIONALITY
========================================

🔍 Searching for 'effective java' by title:
   Found: BC-001 - Effective Java (Status: AVAILABLE)
   Found: BC-002 - Effective Java (Status: AVAILABLE)

...
```

---

## 📈 Extensions & Improvements

### **1. Add Database Persistence**

```java
public interface LibraryRepository {
    void saveBookItem(BookItem bookItem);
    BookItem findByBarcode(String barcode);
    List<BookItem> findByISBN(String ISBN);
    void saveLending(BookLending lending);
    List<BookLending> findOverdueLendings();
}

public class JdbcLibraryRepository implements LibraryRepository {
    // JDBC implementation
}
```

### **2. Add Email Notifications**

```java
public class EmailNotifier implements Observer {
    private EmailService emailService;
    
    @Override
    public void update(String message) {
        // Send actual email
        emailService.sendEmail(member.getEmail(), "Library Notification", message);
    }
}

// In Member class
public void attachNotifier(Observer notifier) {
    observers.add(notifier);
}
```

### **3. Add Book Ratings**

```java
public class BookRating {
    private Book book;
    private List<Review> reviews;
    private double averageRating;
    
    public void addReview(Member member, int rating, String comment) {
        reviews.add(new Review(member, rating, comment));
        recalculateAverage();
    }
}
```

### **4. Add Librarian Role**

```java
public class Librarian {
    private String librarianId;
    private String name;
    
    public void addBook(Library library, Book book) {
        // Only librarian can add books
    }
    
    public void removeBook(Library library, String barcode) {
        // Only librarian can remove books
    }
    
    public void waiveFine(Member member, double amount) {
        // Librarian can waive fines
    }
    
    public Report generateReport(Library library) {
        // Generate monthly report
    }
}
```

### **5. Add Concurrency Control**

```java
public class Library {
    private final ReentrantLock lock = new ReentrantLock();
    
    public BookLending borrowBook(String memberId, String barcode) {
        lock.lock();
        try {
            // Borrowing logic
        } finally {
            lock.unlock();
        }
    }
}

// Or use synchronized methods
public synchronized BookLending borrowBook(...) {
    // Thread-safe borrowing
}
```

---

## 🎯 Interview Tips

### **What Interviewers Look For:**

1. ✅ **Book vs BookItem separation** (shows understanding of domain)
2. ✅ **Observer pattern for notifications** (decoupled design)
3. ✅ **Multiple search indexes** (performance awareness)
4. ✅ **Fine calculation logic** (business rules)
5. ✅ **Membership hierarchy** (proper use of inheritance)
6. ✅ **Edge case handling** (already borrowed, limit exceeded, etc.)

### **Common Follow-up Questions:**

**Q: "How would you handle concurrent borrowing of the same book?"**

```java
// Option 1: Synchronize borrowBook method
public synchronized BookLending borrowBook(...) {
    // Thread-safe
}

// Option 2: Optimistic locking
public class BookItem {
    private int version;
    
    public void borrow(...) {
        // Check version before update
        // Throw ConcurrentModificationException if changed
    }
}

// Option 3: Database-level lock
SELECT * FROM book_items WHERE barcode = ? FOR UPDATE;
```

**Q: "How would you implement a waiting queue for popular books?"**

```java
public class BookReservationQueue {
    private Map<String, Queue<BookReservation>> reservationQueues;
    
    public void addToQueue(String ISBN, BookReservation reservation) {
        reservationQueues.computeIfAbsent(ISBN, k -> new LinkedList<>())
                         .offer(reservation);
    }
    
    public Member getNextInQueue(String ISBN) {
        Queue<BookReservation> queue = reservationQueues.get(ISBN);
        BookReservation next = queue.poll();
        return (next != null) ? next.getMember() : null;
    }
}
```

**Q: "How would you implement book recommendations?"**

```java
public class BookRecommendationEngine {
    public List<Book> recommendBasedOnHistory(Member member) {
        // Get member's borrowing history
        List<String> borrowedISBNs = member.getBorrowingHistory()
            .stream()
            .map(lending -> lending.getBookItem().getBook().getISBN())
            .collect(Collectors.toList());
        
        // Find similar books (by author, subject)
        return catalog.findSimilarBooks(borrowedISBNs);
    }
}
```

---

## ✅ Checklist Before Interview

- [ ] Can explain Observer pattern clearly
- [ ] Understand Book vs BookItem separation
- [ ] Can code solution in <60 minutes
- [ ] Handle all edge cases
- [ ] Understand trade-offs (multiple indexes, in-memory storage)
- [ ] Can discuss extensions (database, concurrency, notifications)
- [ ] Code compiles and runs without errors

---

**This solution demonstrates production-quality code with Observer pattern, proper domain modeling, and extensibility! 🚀**

---

*Completed: January 2026*  
*Problem Difficulty: Easy-Medium*  
*Design Patterns: Observer, State, Singleton*  
*Lines of Code: ~1,200*


