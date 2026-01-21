package model;

import enums.NotificationType;
import enums.PostType;
import observer.Observer;
import observer.Subject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Abstract base class for all post types.
 */
public abstract class Post implements Subject {
    protected final String postId;
    protected final User author;
    protected final LocalDateTime timestamp;
    protected final Set<User> likes;
    protected final List<Comment> comments;
    protected int retweetCount;
    protected final List<Observer> observers;
    
    public Post(User author) {
        this.postId = UUID.randomUUID().toString().substring(0, 8);
        this.author = author;
        this.timestamp = LocalDateTime.now();
        this.likes = new HashSet<>();
        this.comments = new ArrayList<>();
        this.retweetCount = 0;
        this.observers = new ArrayList<>();
        
        // Author observes their own post for notifications
        addObserver(author);
    }
    
    /**
     * Get the post type.
     */
    public abstract PostType getPostType();
    
    /**
     * Get the display content of the post.
     */
    public abstract String getDisplayContent();
    
    /**
     * Like the post.
     */
    public void like(User user) {
        if (likes.add(user)) {
            // Notify author if someone else liked their post
            if (!user.equals(author)) {
                Notification notification = new Notification(NotificationType.LIKE, author, user, this);
                notifyObservers(notification);
            }
        }
    }
    
    /**
     * Unlike the post.
     */
    public void unlike(User user) {
        likes.remove(user);
    }
    
    /**
     * Add a comment to the post.
     */
    public void addComment(Comment comment) {
        comments.add(comment);
        
        // Notify author if someone else commented
        if (!comment.getAuthor().equals(author)) {
            Notification notification = new Notification(NotificationType.COMMENT, author, comment.getAuthor(), this);
            notifyObservers(notification);
        }
    }
    
    /**
     * Increment retweet count.
     */
    public void incrementRetweetCount() {
        retweetCount++;
    }
    
    /**
     * Calculate engagement score for ranking.
     */
    public int getEngagementScore() {
        int likeWeight = 1;
        int commentWeight = 3;
        int retweetWeight = 5;
        
        return (likes.size() * likeWeight) + 
               (comments.size() * commentWeight) + 
               (retweetCount * retweetWeight);
    }
    
    /**
     * Calculate engagement velocity (engagement per hour).
     */
    public double getEngagementVelocity() {
        long hoursOld = ChronoUnit.HOURS.between(timestamp, LocalDateTime.now());
        if (hoursOld == 0) hoursOld = 1; // Avoid division by zero
        return (double) getEngagementScore() / hoursOld;
    }
    
    // Observer pattern implementation
    @Override
    public void addObserver(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
    
    @Override
    public void notifyObservers(Notification notification) {
        for (Observer observer : observers) {
            observer.update(notification);
        }
    }
    
    // Getters
    public String getPostId() {
        return postId;
    }
    
    public User getAuthor() {
        return author;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public Set<User> getLikes() {
        return new HashSet<>(likes);
    }
    
    public List<Comment> getComments() {
        return new ArrayList<>(comments);
    }
    
    public int getRetweetCount() {
        return retweetCount;
    }
    
    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, HH:mm");
        return timestamp.format(formatter);
    }
    
    @Override
    public String toString() {
        return "[@" + author.getUsername() + "] " + getDisplayContent() + 
               " | ❤️ " + likes.size() + " 💬 " + comments.size() + " 🔄 " + retweetCount +
               " (" + getFormattedTimestamp() + ")";
    }
}

