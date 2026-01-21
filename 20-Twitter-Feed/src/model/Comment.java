package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Represents a comment on a post.
 */
public class Comment {
    private final String commentId;
    private final User author;
    private final String content;
    private final LocalDateTime timestamp;
    private final String postId;
    
    public Comment(User author, String content, String postId) {
        this.commentId = UUID.randomUUID().toString().substring(0, 8);
        this.author = author;
        this.content = content;
        this.postId = postId;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters
    public String getCommentId() {
        return commentId;
    }
    
    public User getAuthor() {
        return author;
    }
    
    public String getContent() {
        return content;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public String getPostId() {
        return postId;
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, HH:mm");
        return "  💬 @" + author.getUsername() + ": " + content + " (" + timestamp.format(formatter) + ")";
    }
}

