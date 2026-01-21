package model;

import enums.TableType;
import enums.TableStatus;

public class Table {
    private String tableId;
    private TableType type;
    private int capacity;
    private TableStatus status;
    private Reservation currentReservation;
    
    public Table(String tableId, TableType type) {
        this.tableId = tableId;
        this.type = type;
        this.capacity = type.getCapacity();
        this.status = TableStatus.AVAILABLE;
        this.currentReservation = null;
    }
    
    public boolean canAccommodate(int partySize) {
        return capacity >= partySize && status == TableStatus.AVAILABLE;
    }
    
    public void occupy(Reservation reservation) {
        this.status = TableStatus.OCCUPIED;
        this.currentReservation = reservation;
    }
    
    public void reserve(Reservation reservation) {
        this.status = TableStatus.RESERVED;
        this.currentReservation = reservation;
    }
    
    public void release() {
        this.status = TableStatus.CLEANING;
        this.currentReservation = null;
    }
    
    public void markAvailable() {
        this.status = TableStatus.AVAILABLE;
    }
    
    public boolean isAvailable() {
        return status == TableStatus.AVAILABLE;
    }
    
    // Getters and Setters
    public String getTableId() {
        return tableId;
    }
    
    public TableType getType() {
        return type;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public TableStatus getStatus() {
        return status;
    }
    
    public void setStatus(TableStatus status) {
        this.status = status;
    }
    
    public Reservation getCurrentReservation() {
        return currentReservation;
    }
    
    @Override
    public String toString() {
        return "Table{" +
                "id='" + tableId + '\'' +
                ", type=" + type +
                ", capacity=" + capacity +
                ", status=" + status +
                '}';
    }
}


