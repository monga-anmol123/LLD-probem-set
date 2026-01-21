package service;

import model.*;
import enums.ReservationStatus;
import java.util.*;

public class Library {
    private static Library instance;
    private String name;
    private Catalog catalog;
    private Map<String, Member> members;
    private Map<String, BookLending> activeLendings;
    private Map<String, BookReservation> activeReservations;
    private int lendingIdCounter;
    private int reservationIdCounter;
    private static final int DEFAULT_LENDING_DAYS = 14;
    
    private Library(String name, String address) {
        this.name = name;
        this.catalog = new Catalog();
        this.members = new HashMap<>();
        this.activeLendings = new HashMap<>();
        this.activeReservations = new HashMap<>();
        this.lendingIdCounter = 1;
        this.reservationIdCounter = 1;
    }
    
    public static synchronized Library getInstance(String name, String address) {
        if (instance == null) {
            instance = new Library(name, address);
        }
        return instance;
    }
    
    public void addBookItem(BookItem bookItem) {
        catalog.addBookItem(bookItem);
    }
    
    public void addMember(Member member) {
        members.put(member.getMemberId(), member);
    }
    
    public BookLending borrowBook(String memberId, String barcode) {
        Member member = members.get(memberId);
        if (member == null) {
            throw new IllegalArgumentException("Member not found: " + memberId);
        }
        
        if (!member.canBorrowMoreBooks()) {
            throw new RuntimeException("Member has reached borrowing limit (" + 
                                       member.getMaxBooksAllowed() + " books)");
        }
        
        BookItem bookItem = catalog.searchByBarcode(barcode);
        if (bookItem == null) {
            throw new IllegalArgumentException("Book not found: " + barcode);
        }
        
        if (bookItem.getStatus() != enums.BookStatus.AVAILABLE) {
            throw new RuntimeException("Book is not available (Status: " + 
                                       bookItem.getStatus() + ")");
        }
        
        // Check if member has overdue fines
        double overdueFine = member.getTotalOverdueFine();
        if (overdueFine > 0) {
            throw new RuntimeException("Please pay overdue fine of $" + overdueFine + " first");
        }
        
        String lendingId = "LEND-" + lendingIdCounter++;
        BookLending lending = new BookLending(lendingId, bookItem, member, DEFAULT_LENDING_DAYS);
        
        activeLendings.put(lendingId, lending);
        member.addBorrowing(lending);
        
        System.out.println("✅ Book '" + bookItem.getBook().getTitle() + 
                           "' borrowed by " + member.getName() + 
                           ". Due date: " + lending.getDueDate());
        
        return lending;
    }
    
    public double returnBook(String lendingId) {
        BookLending lending = activeLendings.get(lendingId);
        if (lending == null) {
            throw new IllegalArgumentException("Lending record not found: " + lendingId);
        }
        
        lending.returnBook();
        double fine = lending.getFineAmount();
        
        Member member = lending.getMember();
        member.removeBorrowing(lending);
        activeLendings.remove(lendingId);
        
        System.out.println("✅ Book '" + lending.getBookItem().getBook().getTitle() + 
                           "' returned by " + member.getName() + 
                           ". Fine: $" + fine);
        
        // Check if anyone has reserved this book
        Book returnedBook = lending.getBookItem().getBook();
        checkAndFulfillReservations(returnedBook);
        
        return fine;
    }
    
    public BookReservation reserveBook(String memberId, String ISBN) {
        Member member = members.get(memberId);
        if (member == null) {
            throw new IllegalArgumentException("Member not found: " + memberId);
        }
        
        List<BookItem> availableBooks = catalog.getAvailableBooks(ISBN);
        if (!availableBooks.isEmpty()) {
            throw new RuntimeException("Book is available, no need to reserve. Please borrow directly.");
        }
        
        // Get book details
        List<BookItem> allBooks = catalog.searchByISBN(ISBN);
        if (allBooks.isEmpty()) {
            throw new IllegalArgumentException("Book not found in catalog: " + ISBN);
        }
        
        Book book = allBooks.get(0).getBook();
        String reservationId = "RES-" + reservationIdCounter++;
        BookReservation reservation = new BookReservation(reservationId, book, member);
        
        activeReservations.put(reservationId, reservation);
        
        System.out.println("✅ Book '" + book.getTitle() + "' reserved for " + member.getName());
        
        return reservation;
    }
    
    private void checkAndFulfillReservations(Book book) {
        for (BookReservation reservation : activeReservations.values()) {
            if (reservation.getBook().getISBN().equals(book.getISBN()) && 
                reservation.getStatus() == ReservationStatus.ACTIVE) {
                
                reservation.fulfill();
                Member member = reservation.getMember();
                member.update("Your reserved book '" + book.getTitle() + 
                              "' is now available! Please borrow it soon.");
                activeReservations.remove(reservation.getReservationId());
                return; // Notify only the first person in queue
            }
        }
    }
    
    public List<BookItem> searchBooks(String query, String searchType) {
        switch (searchType.toLowerCase()) {
            case "title":
                return catalog.searchByTitle(query);
            case "author":
                return catalog.searchByAuthor(query);
            case "isbn":
                return catalog.searchByISBN(query);
            default:
                throw new IllegalArgumentException("Invalid search type: " + searchType + 
                                                   ". Use: title, author, or isbn");
        }
    }
    
    public List<BookLending> getOverdueBooks() {
        List<BookLending> overdueList = new ArrayList<>();
        for (BookLending lending : activeLendings.values()) {
            if (lending.isOverdue()) {
                overdueList.add(lending);
            }
        }
        return overdueList;
    }
    
    public void displayLibraryStats() {
        System.out.println("\n========================================");
        System.out.println("  LIBRARY STATISTICS");
        System.out.println("========================================");
        System.out.println("Library: " + name);
        System.out.println("Total Books: " + catalog.getTotalBooks());
        System.out.println("Available Books: " + catalog.getAvailableBookCount());
        System.out.println("Total Members: " + members.size());
        System.out.println("Active Borrowings: " + activeLendings.size());
        System.out.println("Active Reservations: " + activeReservations.size());
        System.out.println("Overdue Books: " + getOverdueBooks().size());
        System.out.println("========================================\n");
    }
    
    // Getters
    public String getName() { return name; }
    public Catalog getCatalog() { return catalog; }
}


