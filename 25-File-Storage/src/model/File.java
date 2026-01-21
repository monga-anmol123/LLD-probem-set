package model;

import enums.FileType;
import java.util.ArrayList;
import java.util.List;

public class File extends FileSystemItem {
    private String content;
    private final long size;
    private final FileType type;
    private final List<FileVersion> versions;
    private static final int MAX_VERSIONS = 10;
    
    public File(String name, User owner, Folder parent, String content, long size) {
        super(name, owner, parent);
        this.content = content;
        this.size = size;
        this.type = FileType.fromFileName(name);
        this.versions = new ArrayList<>();
        
        // Create initial version
        addVersion(content, owner);
    }
    
    @Override
    public long getSize() {
        return size;
    }
    
    @Override
    public boolean isFile() {
        return true;
    }
    
    public void updateContent(String newContent, User modifiedBy) {
        this.content = newContent;
        this.updateModifiedDate();
        addVersion(newContent, modifiedBy);
    }
    
    private void addVersion(String content, User modifiedBy) {
        int versionNumber = versions.size() + 1;
        FileVersion version = new FileVersion(versionNumber, content, size, modifiedBy);
        versions.add(version);
        
        // Keep only last MAX_VERSIONS
        if (versions.size() > MAX_VERSIONS) {
            versions.remove(0);
        }
    }
    
    public void restoreVersion(int versionNumber) {
        FileVersion version = getVersion(versionNumber);
        if (version != null) {
            this.content = version.getContent();
            this.updateModifiedDate();
        }
    }
    
    public FileVersion getVersion(int versionNumber) {
        for (FileVersion v : versions) {
            if (v.getVersionNumber() == versionNumber) {
                return v;
            }
        }
        return null;
    }
    
    public FileVersion getLatestVersion() {
        return versions.isEmpty() ? null : versions.get(versions.size() - 1);
    }
    
    // Getters
    public String getContent() {
        return content;
    }
    
    public FileType getType() {
        return type;
    }
    
    public List<FileVersion> getVersions() {
        return new ArrayList<>(versions);
    }
    
    public int getVersionCount() {
        return versions.size();
    }
    
    @Override
    public String toString() {
        return String.format("File[%s, %s, %d bytes, %d versions]",
            name, type, size, versions.size());
    }
}
