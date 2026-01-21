package model;

import enums.BookStatus;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BookItem {
    private String barcode;
    private Book book;
    private BookStatus status;
    private LocalDate borrowedDate;
    private LocalDate dueDate;
    
    public BookItem(String barcode, Book book) {
        this.barcode = barcode;
        this.book = book;
        this.status = BookStatus.AVAILABLE;
    }
    
    public void borrow(int borrowingDays) {
        if (status != BookStatus.AVAILABLE) {
            throw new IllegalStateException("Book is not available for borrowing");
        }
        this.status = BookStatus.BORROWED;
        this.borrowedDate = LocalDate.now();
        this.dueDate = borrowedDate.plusDays(borrowingDays);
    }
    
    public void returnBook() {
        this.status = BookStatus.AVAILABLE;
        this.borrowedDate = null;
        this.dueDate = null;
    }
    
    public long calculateOverdueDays() {
        if (dueDate == null) return 0;
        LocalDate today = LocalDate.now();
        return dueDate.isBefore(today) ? ChronoUnit.DAYS.between(dueDate, today) : 0;
    }
    
    // Getters and setters
    public String getBarcode() { return barcode; }
    public Book getBook() { return book; }
    public BookStatus getStatus() { return status; }
    public void setStatus(BookStatus status) { this.status = status; }
    public LocalDate getDueDate() { return dueDate; }
}


