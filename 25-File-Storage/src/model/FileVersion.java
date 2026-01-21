package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileVersion {
    private final int versionNumber;
    private final String content;
    private final long size;
    private final User modifiedBy;
    private final LocalDateTime modifiedDate;
    
    public FileVersion(int versionNumber, String content, long size, User modifiedBy) {
        this.versionNumber = versionNumber;
        this.content = content;
        this.size = size;
        this.modifiedBy = modifiedBy;
        this.modifiedDate = LocalDateTime.now();
    }
    
    // Getters
    public int getVersionNumber() {
        return versionNumber;
    }
    
    public String getContent() {
        return content;
    }
    
    public long getSize() {
        return size;
    }
    
    public User getModifiedBy() {
        return modifiedBy;
    }
    
    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format("v%d - Modified by %s on %s",
            versionNumber, modifiedBy.getName(), modifiedDate.format(formatter));
    }
}
