package factory;

import enums.PostType;
import model.Post;
import model.QuoteTweet;
import model.RegularPost;
import model.Retweet;
import model.User;

/**
 * Factory class for creating different types of posts.
 */
public class PostFactory {
    
    /**
     * Create a post based on the specified type.
     * 
     * @param type Type of post to create
     * @param author Author of the post
     * @param content Content of the post (for REGULAR and QUOTE_TWEET)
     * @param originalPost Original post (for RETWEET and QUOTE_TWEET)
     * @return Created post
     */
    public static Post createPost(PostType type, User author, String content, Post originalPost) {
        switch (type) {
            case REGULAR:
                if (content == null || content.trim().isEmpty()) {
                    throw new IllegalArgumentException("Content cannot be empty for regular post");
                }
                return new RegularPost(author, content);
                
            case RETWEET:
                if (originalPost == null) {
                    throw new IllegalArgumentException("Original post required for retweet");
                }
                return new Retweet(author, originalPost);
                
            case QUOTE_TWEET:
                if (originalPost == null) {
                    throw new IllegalArgumentException("Original post required for quote tweet");
                }
                if (content == null || content.trim().isEmpty()) {
                    throw new IllegalArgumentException("Comment required for quote tweet");
                }
                return new QuoteTweet(author, content, originalPost);
                
            default:
                throw new IllegalArgumentException("Unknown post type: " + type);
        }
    }
    
    /**
     * Create a regular post (convenience method).
     */
    public static Post createRegularPost(User author, String content) {
        return createPost(PostType.REGULAR, author, content, null);
    }
    
    /**
     * Create a retweet (convenience method).
     */
    public static Post createRetweet(User author, Post originalPost) {
        return createPost(PostType.RETWEET, author, null, originalPost);
    }
    
    /**
     * Create a quote tweet (convenience method).
     */
    public static Post createQuoteTweet(User author, String comment, Post originalPost) {
        return createPost(PostType.QUOTE_TWEET, author, comment, originalPost);
    }
}

