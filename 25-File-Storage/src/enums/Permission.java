package enums;

public enum Permission {
    OWNER("Owner - Full control"),
    EDITOR("Editor - Read and write"),
    VIEWER("Viewer - Read only");
    
    private final String description;
    
    Permission(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean canRead() {
        return true; // All permissions can read
    }
    
    public boolean canWrite() {
        return this == OWNER || this == EDITOR;
    }
    
    public boolean canDelete() {
        return this == OWNER;
    }
    
    public boolean canShare() {
        return this == OWNER;
    }
}
