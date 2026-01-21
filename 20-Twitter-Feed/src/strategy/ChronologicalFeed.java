package strategy;

import model.Post;
import model.User;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Chronological feed algorithm - shows latest posts first.
 */
public class ChronologicalFeed implements FeedAlgorithm {
    
    @Override
    public List<Post> generateFeed(User user, List<Post> allPosts) {
        return allPosts.stream()
            .filter(post -> user.isFollowing(post.getAuthor()) || post.getAuthor().equals(user))
            .sorted(Comparator.comparing(Post::getTimestamp).reversed())
            .collect(Collectors.toList());
    }
    
    @Override
    public String getAlgorithmName() {
        return "Chronological (Latest First)";
    }
}

