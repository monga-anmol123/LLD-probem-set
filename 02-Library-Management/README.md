# Problem 02: Library Management System

## 🎯 Difficulty: Easy-Medium ⭐⭐⭐

## 📝 Problem Statement

Design a library management system that supports book inventory, member management, borrowing/returning books, fine calculation for late returns, and book reservations.

---

## 🔍 Functional Requirements (FR)

### FR1: Book Management
- Add new books to library catalog
- Each book can have multiple copies (BookItem)
- Track book status: Available, Borrowed, Reserved, Lost
- Search books by: Title, Author, ISBN, Subject

### FR2: Member Management
- Register members (Standard, Premium)
- Standard members: Max 5 books
- Premium members: Max 10 books
- Track active borrows per member

### FR3: Borrowing System
- Members can borrow available books
- Borrowing period: 14 days
- Generate lending record with due date
- Check member eligibility (not exceeded limit, no overdue fines)

### FR4: Return System
- Calculate days overdue
- Apply fine: $1 per day for late returns
- Update book status to available
- Notify users waiting for reserved books

### FR5: Reservation System
- Reserve books that are currently borrowed
- Notify member when book becomes available
- Reservation automatically expires if not borrowed within timeframe

### FR6: Fine Management
- Calculate fines automatically
- Track total overdue fines per member
- Block borrowing if unpaid fines exist

---

## 🚫 Non-Functional Requirements (NFR)

### NFR1: Scalability
- Support 10,000+ books
- Handle 1,000+ members
- Process 100+ borrowings per day

### NFR2: Extensibility
- Easy to add new membership types
- Support for different fine calculation strategies
- Add new book categories

### NFR3: Reliability
- Handle edge cases:
  - Book already borrowed
  - Member exceeded borrow limit
  - Invalid book barcode
  - Reservation conflicts

### NFR4: Notification System
- Notify members when:
  - Book reserved is available
  - Due date approaching (3 days before)
  - Fine accumulated

---

## 🎨 Design Patterns to Use

### 1. **Observer Pattern**
- **Where:** Notify members when reserved book available
- **Why:** Decouple notification logic from book return logic

### 2. **State Pattern**
- **Where:** Book status transitions
- **Why:** Clean state management (Available → Borrowed → Returned)

### 3. **Singleton Pattern**
- **Where:** Library instance
- **Why:** Single library system, global access

### 4. **Strategy Pattern** (Optional)
- **Where:** Fine calculation (could have different strategies)
- **Why:** Flexible fine calculation logic

---

## 📋 Core Entities

### 1. **Book**
- Attributes: `ISBN`, `title`, `author`, `publisher`, `subjects`
- One book can have multiple physical copies (BookItem)

### 2. **BookItem**
- Attributes: `barcode`, `book`, `status`, `borrowedDate`, `dueDate`
- Represents physical copy with unique barcode

### 3. **Member** (Abstract)
- Attributes: `memberId`, `name`, `email`, `phone`, `activeBorrowings`, `maxBooksAllowed`
- Subclasses: `StandardMember`, `PremiumMember`
- Implements `Observer` for notifications

### 4. **BookLending**
- Attributes: `lendingId`, `bookItem`, `member`, `borrowDate`, `dueDate`, `returnDate`, `fineAmount`
- Methods: `calculateFine()`, `isOverdue()`

### 5. **BookReservation**
- Attributes: `reservationId`, `book`, `member`, `reservationDate`, `status`
- Status: Active, Fulfilled, Cancelled, Expired

### 6. **Catalog**
- Search functionality with multiple indexes
- Methods: `searchByTitle()`, `searchByAuthor()`, `searchByISBN()`

### 7. **Library** (Singleton)
- Manages all operations
- Methods: `borrowBook()`, `returnBook()`, `reserveBook()`

---

## 🧪 Test Scenarios

### Scenario 1: Basic Borrow & Return
```
1. Add books to catalog (2 copies of "Effective Java")
2. Register member (Alice)
3. Alice borrows copy 1
4. Verify book status = BORROWED
5. Simulate time passing
6. Alice returns book
7. Calculate fine if late
8. Book status = AVAILABLE again
```

### Scenario 2: Reservation Flow
```
1. Alice borrows the only copy of "Clean Code"
2. Bob tries to borrow → Not available
3. Bob reserves the book
4. Alice returns the book
5. Bob gets notification: "Your reserved book is available!"
6. Reservation fulfilled
```

### Scenario 3: Member Borrow Limit
```
1. Standard member (max 5 books)
2. Borrow 5 books successfully
3. Try to borrow 6th book
4. Should fail: "Exceeded borrowing limit"
```

### Scenario 4: Overdue Fine
```
1. Borrow book (due date: 14 days)
2. Simulate 17 days passing
3. Return book
4. Fine = (17 - 14) * $1 = $3
5. Try to borrow another book
6. Should fail: "Pay overdue fine first"
```

### Scenario 5: Search Functionality
```
1. Add books by different authors
2. Search by title: "Java"
3. Returns all books with "Java" in title
4. Search by author: "Robert Martin"
5. Returns all books by that author
```

---

## ⏱️ Time Allocation (60 minutes)

- **5 mins:** Clarify requirements
- **5 mins:** Identify entities (Book, BookItem, Member, Lending, Reservation)
- **10 mins:** Design class diagram
- **30 mins:** Code (enums → model → service → observer)
- **10 mins:** Test with demo, handle edge cases

---

## 💡 Hints

<details>
<summary>Hint 1: Observer Pattern for Reservations</summary>

```java
public interface Observer {
    void update(String message);
}

public class Member implements Observer {
    @Override
    public void update(String message) {
        System.out.println("Notification to " + name + ": " + message);
    }
}

public interface Subject {
    void attach(Observer observer);
    void notifyObservers(String message);
}
```

</details>

<details>
<summary>Hint 2: Book vs BookItem Separation</summary>

```java
// Book = metadata (title, author, ISBN)
public class Book {
    private String ISBN;
    private String title;
    private String author;
}

// BookItem = physical copy with barcode and status
public class BookItem {
    private String barcode;
    private Book book;              // Reference to book metadata
    private BookStatus status;
}

// Why? One book can have multiple copies
// Book: "Effective Java" (ISBN: 978-0134685991)
// BookItem 1: Barcode BC-001, Status: BORROWED
// BookItem 2: Barcode BC-002, Status: AVAILABLE
```

</details>

<details>
<summary>Hint 3: Fine Calculation</summary>

```java
public double calculateFine() {
    if (returnDate == null) {
        returnDate = LocalDate.now();
    }
    
    if (returnDate.isAfter(dueDate)) {
        long overdueDays = ChronoUnit.DAYS.between(dueDate, returnDate);
        return overdueDays * FINE_PER_DAY;
    }
    return 0.0;
}
```

</details>

<details>
<summary>Hint 4: Catalog with Multiple Indexes</summary>

```java
public class Catalog {
    private Map<String, List<BookItem>> booksByTitle;     // Title → BookItems
    private Map<String, List<BookItem>> booksByAuthor;    // Author → BookItems
    private Map<String, BookItem> booksByBarcode;         // Barcode → BookItem
    
    public List<BookItem> searchByTitle(String title) {
        return booksByTitle.getOrDefault(title.toLowerCase(), new ArrayList<>());
    }
}
```

</details>

---

## 🚀 Extensions (If Time Permits)

1. **Add Email Notifications**
   ```java
   public class EmailNotifier implements Observer {
       public void sendEmail(String email, String message) { ... }
   }
   ```

2. **Add Book Ratings**
   ```java
   public class BookRating {
       private double averageRating;
       private List<Review> reviews;
   }
   ```

3. **Add Librarian Role**
   ```java
   public class Librarian {
       public void addBook(Book book);
       public void removeBook(String ISBN);
       public void generateReport();
   }
   ```

4. **Add Late Fee Waivers**
   ```java
   public void waiveFine(String memberId, double amount) {
       // Librarian can waive fines
   }
   ```

---

## ✅ Success Criteria

Your solution should:
- [ ] Compile without errors
- [ ] Use Observer, State, Singleton patterns correctly
- [ ] Handle all test scenarios
- [ ] Separate Book and BookItem properly
- [ ] Calculate fines correctly
- [ ] Support reservations with notifications
- [ ] Handle edge cases (borrow limit, overdue fines, etc.)
- [ ] Have efficient search (multiple indexes)

---

## 📁 File Structure

```
02-Library-Management/
├── README.md (this file)
├── src/
│   ├── enums/
│   │   ├── BookStatus.java
│   │   ├── MembershipType.java
│   │   └── ReservationStatus.java
│   ├── model/
│   │   ├── Book.java
│   │   ├── BookItem.java
│   │   ├── Member.java
│   │   ├── StandardMember.java
│   │   ├── PremiumMember.java
│   │   ├── BookLending.java
│   │   └── BookReservation.java
│   ├── observer/
│   │   ├── Observer.java
│   │   ├── Subject.java
│   │   └── BookReservationSubject.java
│   ├── service/
│   │   ├── Catalog.java
│   │   └── Library.java
│   └── Main.java
└── SOLUTION.md
```

---

**Good luck! This is slightly more complex than Parking Lot - focus on Observer pattern and Book/BookItem separation! 🚀**


