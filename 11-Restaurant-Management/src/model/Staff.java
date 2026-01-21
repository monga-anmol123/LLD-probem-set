package model;

import enums.StaffRole;

public abstract class Staff {
    protected String staffId;
    protected String name;
    protected StaffRole role;
    protected String contactInfo;
    
    public Staff(String staffId, String name, StaffRole role, String contactInfo) {
        this.staffId = staffId;
        this.name = name;
        this.role = role;
        this.contactInfo = contactInfo;
    }
    
    public abstract void performDuty();
    
    // Getters
    public String getStaffId() {
        return staffId;
    }
    
    public String getName() {
        return name;
    }
    
    public StaffRole getRole() {
        return role;
    }
    
    public String getContactInfo() {
        return contactInfo;
    }
    
    @Override
    public String toString() {
        return role + " " + name + " (ID: " + staffId + ")";
    }
}


