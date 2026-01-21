package model;

import enums.ReservationStatus;
import java.time.LocalDate;

public class BookReservation {
    private String reservationId;
    private Book book;
    private Member member;
    private LocalDate reservationDate;
    private ReservationStatus status;
    
    public BookReservation(String reservationId, Book book, Member member) {
        this.reservationId = reservationId;
        this.book = book;
        this.member = member;
        this.reservationDate = LocalDate.now();
        this.status = ReservationStatus.ACTIVE;
    }
    
    public void fulfill() {
        this.status = ReservationStatus.FULFILLED;
    }
    
    public void cancel() {
        this.status = ReservationStatus.CANCELLED;
    }
    
    public void expire() {
        this.status = ReservationStatus.EXPIRED;
    }
    
    // Getters
    public String getReservationId() { return reservationId; }
    public Book getBook() { return book; }
    public Member getMember() { return member; }
    public ReservationStatus getStatus() { return status; }
    public LocalDate getReservationDate() { return reservationDate; }
}


