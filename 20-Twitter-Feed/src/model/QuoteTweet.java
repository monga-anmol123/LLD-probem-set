package model;

import enums.NotificationType;
import enums.PostType;

/**
 * Represents a quote tweet (retweet with additional comment).
 */
public class QuoteTweet extends Post {
    private final String comment;
    private final Post originalPost;
    
    public QuoteTweet(User author, String comment, Post originalPost) {
        super(author);
        this.comment = comment;
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
        return PostType.QUOTE_TWEET;
    }
    
    @Override
    public String getDisplayContent() {
        return comment + " | 💬 Quoting @" + originalPost.getAuthor().getUsername() + ": \"" + 
               originalPost.getDisplayContent() + "\"";
    }
    
    public String getComment() {
        return comment;
    }
    
    public Post getOriginalPost() {
        return originalPost;
    }
}

