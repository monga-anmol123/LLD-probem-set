package enums;

public enum FileType {
    DOCUMENT(".txt", ".doc", ".docx", ".pdf"),
    IMAGE(".jpg", ".jpeg", ".png", ".gif"),
    VIDEO(".mp4", ".avi", ".mov"),
    AUDIO(".mp3", ".wav", ".flac"),
    ARCHIVE(".zip", ".rar", ".tar"),
    CODE(".java", ".py", ".js", ".cpp"),
    OTHER();
    
    private final String[] extensions;
    
    FileType(String... extensions) {
        this.extensions = extensions;
    }
    
    public static FileType fromFileName(String fileName) {
        String lowerName = fileName.toLowerCase();
        for (FileType type : values()) {
            for (String ext : type.extensions) {
                if (lowerName.endsWith(ext)) {
                    return type;
                }
            }
        }
        return OTHER;
    }
    
    public String[] getExtensions() {
        return extensions;
    }
}
