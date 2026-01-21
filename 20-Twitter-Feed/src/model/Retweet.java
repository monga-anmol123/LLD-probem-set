package model;

import enums.NotificationType;
import enums.PostType;

/**
 * Represents a retweet (sharing another user's post).
 */
public class Retweet extends Post {
    private final Post originalPost;
    
    public Retweet(User author, Post originalPost) {
        super(author);
        this.originalPost = originalPost;
        
        // Increment retweet count on original post
        originalPost.incrementRetweetCount();
        
        // Notify original author
        if (!author.equals(originalPost.getAuthor())) {
            Notification notification = new Notification(
                NotificationType.RETWEET, 
                originalPost.getAuthor(), 
                author, 
                originalPost
            );
            originalPost.notifyObservers(notification);
        }
    }
    
    @Override
    public PostType getPostType() {
        return PostType.RETWEET;
    }
    
    @Override
    public String getDisplayContent() {
        return "🔄 Retweeted @" + originalPost.getAuthor().getUsername() + "'s post: " + 
               originalPost.getDisplayContent();
    }
    
    public Post getOriginalPost() {
        return originalPost;
    }
}

