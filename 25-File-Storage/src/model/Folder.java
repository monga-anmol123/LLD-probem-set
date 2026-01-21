package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Folder extends FileSystemItem {
    private final List<FileSystemItem> children;
    
    public Folder(String name, User owner, Folder parent) {
        super(name, owner, parent);
        this.children = new ArrayList<>();
    }
    
    @Override
    public long getSize() {
        return children.stream()
            .mapToLong(FileSystemItem::getSize)
            .sum();
    }
    
    @Override
    public boolean isFile() {
        return false;
    }
    
    public void addItem(FileSystemItem item) {
        if (!children.contains(item)) {
            children.add(item);
            item.setParent(this);
            updateModifiedDate();
        }
    }
    
    public void removeItem(FileSystemItem item) {
        children.remove(item);
        updateModifiedDate();
    }
    
    public List<FileSystemItem> getChildren() {
        return new ArrayList<>(children);
    }
    
    public List<File> getFiles() {
        return children.stream()
            .filter(FileSystemItem::isFile)
            .map(item -> (File) item)
            .collect(Collectors.toList());
    }
    
    public List<Folder> getFolders() {
        return children.stream()
            .filter(FileSystemItem::isFolder)
            .map(item -> (Folder) item)
            .collect(Collectors.toList());
    }
    
    public FileSystemItem findByName(String name) {
        for (FileSystemItem item : children) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }
    
    public boolean isEmpty() {
        return children.isEmpty();
    }
    
    public int getItemCount() {
        return children.size();
    }
    
    public void listContents(int depth) {
        StringBuilder indentBuilder = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            indentBuilder.append("  ");
        }
        String indent = indentBuilder.toString();
        System.out.println(indent + "📁 " + name + "/");
        
        for (FileSystemItem item : children) {
            if (item.isFolder()) {
                ((Folder) item).listContents(depth + 1);
            } else {
                System.out.println(indent + "  📄 " + item.getName() + 
                    " (" + formatBytes(item.getSize()) + ")");
            }
        }
    }
    
    private String formatBytes(long bytes) {
        if (bytes >= 1024L * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024));
        } else if (bytes >= 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else {
            return bytes + " bytes";
        }
    }
    
    @Override
    public String toString() {
        return String.format("Folder[%s, %d items, %s]",
            name, children.size(), formatBytes(getSize()));
    }
}
