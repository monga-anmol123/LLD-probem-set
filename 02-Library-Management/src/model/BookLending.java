package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BookLending {
    private String lendingId;
    private BookItem bookItem;
    private Member member;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double fineAmount;
    private static final double FINE_PER_DAY = 1.0;
    
    public BookLending(String lendingId, BookItem bookItem, Member member, int days) {
        this.lendingId = lendingId;
        this.bookItem = bookItem;
        this.member = member;
        this.borrowDate = LocalDate.now();
        this.dueDate = borrowDate.plusDays(days);
        bookItem.borrow(days);
    }
    
    public void returnBook() {
        this.returnDate = LocalDate.now();
        this.fineAmount = calculateFine();
        bookItem.returnBook();
    }
    
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
    
    public boolean isOverdue() {
        return LocalDate.now().isAfter(dueDate) && returnDate == null;
    }
    
    // Getters
    public String getLendingId() { return lendingId; }
    public BookItem getBookItem() { return bookItem; }
    public Member getMember() { return member; }
    public LocalDate getDueDate() { return dueDate; }
    public double getFineAmount() { return fineAmount; }
    public LocalDate getReturnDate() { return returnDate; }
}


