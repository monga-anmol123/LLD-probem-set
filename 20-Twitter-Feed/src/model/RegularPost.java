package model;

import enums.PostType;

/**
 * Represents a regular post/tweet.
 */
public class RegularPost extends Post {
    private final String content;
    
    public RegularPost(User author, String content) {
        super(author);
        this.content = content;
    }
    
    @Override
    public PostType getPostType() {
        return PostType.REGULAR;
    }
    
    @Override
    public String getDisplayContent() {
        return content;
    }
    
    public String getContent() {
        return content;
    }
}

