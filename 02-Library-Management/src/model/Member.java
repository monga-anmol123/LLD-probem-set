package model;

import observer.Observer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Member implements Observer {
    protected String memberId;
    protected String name;
    protected String email;
    protected String phone;
    protected LocalDate membershipDate;
    protected int maxBooksAllowed;
    protected List<BookLending> activeBorrowings;
    
    public Member(String memberId, String name, String email) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.membershipDate = LocalDate.now();
        this.activeBorrowings = new ArrayList<>();
    }
    
    public boolean canBorrowMoreBooks() {
        return activeBorrowings.size() < maxBooksAllowed;
    }
    
    public void addBorrowing(BookLending lending) {
        activeBorrowings.add(lending);
    }
    
    public void removeBorrowing(BookLending lending) {
        activeBorrowings.remove(lending);
    }
    
    public double getTotalOverdueFine() {
        return activeBorrowings.stream()
            .mapToDouble(BookLending::calculateFine)
            .sum();
    }
    
    @Override
    public void update(String message) {
        System.out.println("📧 Notification to " + name + ": " + message);
    }
    
    // Getters
    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<BookLending> getActiveBorrowings() { return activeBorrowings; }
    public int getMaxBooksAllowed() { return maxBooksAllowed; }
}


