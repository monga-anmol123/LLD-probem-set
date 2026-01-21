package model;

import enums.ReservationStatus;
import java.time.LocalDateTime;

public class Reservation {
    private String reservationId;
    private String customerName;
    private String phone;
    private int partySize;
    private LocalDateTime dateTime;
    private ReservationStatus status;
    private Table assignedTable;
    
    public Reservation(String reservationId, String customerName, String phone, 
                      int partySize, LocalDateTime dateTime) {
        this.reservationId = reservationId;
        this.customerName = customerName;
        this.phone = phone;
        this.partySize = partySize;
        this.dateTime = dateTime;
        this.status = ReservationStatus.PENDING;
        this.assignedTable = null;
    }
    
    public void confirm(Table table) {
        this.status = ReservationStatus.CONFIRMED;
        this.assignedTable = table;
    }
    
    public void seat() {
        this.status = ReservationStatus.SEATED;
    }
    
    public void complete() {
        this.status = ReservationStatus.COMPLETED;
    }
    
    public void cancel() {
        this.status = ReservationStatus.CANCELLED;
        if (assignedTable != null) {
            assignedTable.markAvailable();
        }
    }
    
    public void markNoShow() {
        this.status = ReservationStatus.NO_SHOW;
        if (assignedTable != null) {
            assignedTable.markAvailable();
        }
    }
    
    // Getters and Setters
    public String getReservationId() {
        return reservationId;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public int getPartySize() {
        return partySize;
    }
    
    public LocalDateTime getDateTime() {
        return dateTime;
    }
    
    public ReservationStatus getStatus() {
        return status;
    }
    
    public Table getAssignedTable() {
        return assignedTable;
    }
    
    public void setAssignedTable(Table table) {
        this.assignedTable = table;
    }
    
    @Override
    public String toString() {
        return "Reservation{" +
                "id='" + reservationId + '\'' +
                ", customer='" + customerName + '\'' +
                ", partySize=" + partySize +
                ", status=" + status +
                ", table=" + (assignedTable != null ? assignedTable.getTableId() : "None") +
                '}';
    }
}


