package model;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class FileSystemItem {
    protected final String id;
    protected String name;
    protected final User owner;
    protected Folder parent;
    protected final LocalDateTime createdDate;
    protected LocalDateTime modifiedDate;
    
    public FileSystemItem(String name, User owner, Folder parent) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.owner = owner;
        this.parent = parent;
        this.createdDate = LocalDateTime.now();
        this.modifiedDate = LocalDateTime.now();
    }
    
    public abstract long getSize();
    public abstract boolean isFile();
    
    public boolean isFolder() {
        return !isFile();
    }
    
    public void setName(String name) {
        this.name = name;
        this.modifiedDate = LocalDateTime.now();
    }
    
    public void setParent(Folder parent) {
        this.parent = parent;
        this.modifiedDate = LocalDateTime.now();
    }
    
    public String getPath() {
        if (parent == null) {
            return "/" + name;
        }
        return parent.getPath() + "/" + name;
    }
    
    // Getters
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public User getOwner() {
        return owner;
    }
    
    public Folder getParent() {
        return parent;
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }
    
    protected void updateModifiedDate() {
        this.modifiedDate = LocalDateTime.now();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FileSystemItem item = (FileSystemItem) obj;
        return id.equals(item.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
