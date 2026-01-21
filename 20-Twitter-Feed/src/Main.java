import enums.*;
import factory.PostFactory;
import model.*;
import service.*;

import java.util.List;

/**
 * Main class demonstrating the Twitter/Social Media Feed system with various scenarios.
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println(repeatString("=", 80));
        System.out.println("  TWITTER / SOCIAL MEDIA FEED SYSTEM DEMO");
        System.out.println(repeatString("=", 80) + "\n");
        
        // Scenario 1: User Registration and Following
        scenario1_UserRegistrationAndFollowing();
        
        // Scenario 2: Post Creation and Engagement
        scenario2_PostCreationAndEngagement();
        
        // Scenario 3: Feed Generation - Chronological
        scenario3_ChronologicalFeed();
        
        // Scenario 4: Feed Generation - Popular
        scenario4_PopularFeed();
        
        // Scenario 5: Notifications
        scenario5_Notifications();
        
        // Scenario 6: Retweets and Quote Tweets
        scenario6_RetweetsAndQuoteTweets();
        
        System.out.println("\n" + repeatString("=", 80));
        System.out.println("  ALL SCENARIOS COMPLETED SUCCESSFULLY!");
        System.out.println(repeatString("=", 80) + "\n");
    }
    
    /**
     * Scenario 1: User Registration and Following
     */
    private static void scenario1_UserRegistrationAndFollowing() {
        System.out.println("\n" + repeatString("-", 80));
        System.out.println("SCENARIO 1: User Registration and Following");
        System.out.println(repeatString("-", 80));
        
        TwitterService twitter = TwitterService.getInstance();
        
        // Register users
        System.out.println("\n👥 Registering Users...");
        User alice = twitter.registerUser("U001", "alice", "Alice Johnson");
        User bob = twitter.registerUser("U002", "bob", "Bob Smith");
        User charlie = twitter.registerUser("U003", "charlie", "Charlie Brown");
        User david = twitter.registerUser("U004", "david", "David Lee");
        User emma = twitter.registerUser("U005", "emma", "Emma Wilson");
        
        System.out.println("✅ Registered 5 users");
        
        // Set bios
        alice.setBio("Tech enthusiast | Coffee lover ☕");
        bob.setBio("Software Engineer | Design Patterns Expert");
        charlie.setBio("Content Creator | Sharing daily insights");
        
        // Follow relationships
        System.out.println("\n🔗 Creating Follow Relationships...");
        alice.follow(bob);
        alice.follow(charlie);
        System.out.println("✓ Alice follows Bob and Charlie");
        
        bob.follow(alice);
        bob.follow(charlie);
        bob.follow(david);
        System.out.println("✓ Bob follows Alice, Charlie, and David");
        
        charlie.follow(alice);
        charlie.follow(bob);
        charlie.follow(david);
        charlie.follow(emma);
        System.out.println("✓ Charlie follows everyone");
        
        david.follow(alice);
        david.follow(emma);
        System.out.println("✓ David follows Alice and Emma");
        
        emma.follow(bob);
        emma.follow(charlie);
        System.out.println("✓ Emma follows Bob and Charlie");
        
        // Display user stats
        System.out.println("\n📊 User Statistics:");
        System.out.println(alice);
        System.out.println(bob);
        System.out.println(charlie);
        System.out.println(david);
        System.out.println(emma);
    }
    
    /**
     * Scenario 2: Post Creation and Engagement
     */
    private static void scenario2_PostCreationAndEngagement() {
        System.out.println("\n" + repeatString("-", 80));
        System.out.println("SCENARIO 2: Post Creation and Engagement");
        System.out.println(repeatString("-", 80));
        
        TwitterService twitter = TwitterService.getInstance();
        User alice = twitter.getUserByUsername("alice");
        User bob = twitter.getUserByUsername("bob");
        User charlie = twitter.getUserByUsername("charlie");
        User david = twitter.getUserByUsername("david");
        User emma = twitter.getUserByUsername("emma");
        
        // Create posts
        System.out.println("\n📝 Creating Posts...");
        
        Post alicePost1 = twitter.createPost(PostType.REGULAR, alice, 
            "Hello Twitter! Excited to join this platform! 🎉", null);
        System.out.println("✓ Alice: " + alicePost1.getDisplayContent());
        
        Post bobPost1 = twitter.createPost(PostType.REGULAR, bob, 
            "Learning design patterns today. Observer pattern is fascinating!", null);
        System.out.println("✓ Bob: " + bobPost1.getDisplayContent());
        
        Post charliePost1 = twitter.createPost(PostType.REGULAR, charlie, 
            "Just finished reading a great book on software architecture", null);
        System.out.println("✓ Charlie: " + charliePost1.getDisplayContent());
        
        Post davidPost1 = twitter.createPost(PostType.REGULAR, david, 
            "Working on a new project. Stay tuned for updates!", null);
        System.out.println("✓ David: " + davidPost1.getDisplayContent());
        
        // Engagement: Likes
        System.out.println("\n❤️  Adding Likes...");
        twitter.likePost(alicePost1.getPostId(), bob);
        twitter.likePost(alicePost1.getPostId(), charlie);
        twitter.likePost(alicePost1.getPostId(), david);
        System.out.println("✓ Bob, Charlie, and David liked Alice's post");
        
        twitter.likePost(bobPost1.getPostId(), alice);
        twitter.likePost(bobPost1.getPostId(), charlie);
        System.out.println("✓ Alice and Charlie liked Bob's post");
        
        // Engagement: Comments
        System.out.println("\n💬 Adding Comments...");
        twitter.commentOnPost(bobPost1.getPostId(), david, "Great topic! I love the Observer pattern too!");
        System.out.println("✓ David commented on Bob's post");
        
        twitter.commentOnPost(alicePost1.getPostId(), emma, "Welcome to Twitter, Alice!");
        System.out.println("✓ Emma commented on Alice's post");
        
        twitter.commentOnPost(charliePost1.getPostId(), alice, "Which book? I'd love to read it!");
        System.out.println("✓ Alice commented on Charlie's post");
        
        // Display engagement metrics
        System.out.println("\n📊 Engagement Metrics:");
        System.out.println(alicePost1);
        System.out.println(bobPost1);
        System.out.println(charliePost1);
        System.out.println(davidPost1);
    }
    
    /**
     * Scenario 3: Feed Generation - Chronological
     */
    private static void scenario3_ChronologicalFeed() {
        System.out.println("\n" + repeatString("-", 80));
        System.out.println("SCENARIO 3: Feed Generation - Chronological Algorithm");
        System.out.println(repeatString("-", 80));
        
        TwitterService twitter = TwitterService.getInstance();
        User alice = twitter.getUserByUsername("alice");
        User bob = twitter.getUserByUsername("bob");
        User charlie = twitter.getUserByUsername("charlie");
        
        // Create more posts at different times
        System.out.println("\n📝 Creating Additional Posts...");
        
        Post bobPost2 = twitter.createPost(PostType.REGULAR, bob, 
            "Factory pattern makes object creation so much cleaner!", null);
        
        Post charliePost2 = twitter.createPost(PostType.REGULAR, charlie, 
            "Morning coffee and code. Perfect combination! ☕💻", null);
        
        Post alicePost2 = twitter.createPost(PostType.REGULAR, alice, 
            "Just deployed my first microservice. Feeling accomplished!", null);
        
        System.out.println("✓ Created 3 more posts");
        
        // Generate chronological feed for Alice
        FeedService feedService = new FeedService(FeedAlgorithmType.CHRONOLOGICAL);
        feedService.displayFeed(alice, twitter.getAllPosts(), 10);
    }
    
    /**
     * Scenario 4: Feed Generation - Popular Algorithm
     */
    private static void scenario4_PopularFeed() {
        System.out.println("\n" + repeatString("-", 80));
        System.out.println("SCENARIO 4: Feed Generation - Popular Algorithm");
        System.out.println(repeatString("-", 80));
        
        TwitterService twitter = TwitterService.getInstance();
        User bob = twitter.getUserByUsername("bob");
        User charlie = twitter.getUserByUsername("charlie");
        User david = twitter.getUserByUsername("david");
        User emma = twitter.getUserByUsername("emma");
        
        // Add more engagement to create popularity differences
        System.out.println("\n❤️  Adding More Engagement...");
        
        List<Post> allPosts = twitter.getAllPosts();
        if (allPosts.size() > 0) {
            Post popularPost = allPosts.get(0);  // Alice's first post
            
            // Make it very popular
            twitter.likePost(popularPost.getPostId(), emma);
            twitter.commentOnPost(popularPost.getPostId(), bob, "This is amazing!");
            twitter.commentOnPost(popularPost.getPostId(), charlie, "Congrats!");
            
            System.out.println("✓ Added extra engagement to make posts more popular");
        }
        
        // Generate popular feed for Bob
        FeedService feedService = new FeedService(FeedAlgorithmType.POPULAR);
        feedService.displayFeed(bob, twitter.getAllPosts(), 10);
    }
    
    /**
     * Scenario 5: Notifications
     */
    private static void scenario5_Notifications() {
        System.out.println("\n" + repeatString("-", 80));
        System.out.println("SCENARIO 5: Notifications System");
        System.out.println(repeatString("-", 80));
        
        TwitterService twitter = TwitterService.getInstance();
        User alice = twitter.getUserByUsername("alice");
        User bob = twitter.getUserByUsername("bob");
        User charlie = twitter.getUserByUsername("charlie");
        User emma = twitter.getUserByUsername("emma");
        
        // Display Alice's notifications
        System.out.println("\n📬 Notifications for @" + alice.getUsername() + ":");
        List<Notification> aliceNotifications = alice.getAllNotifications();
        
        if (aliceNotifications.isEmpty()) {
            System.out.println("No notifications");
        } else {
            System.out.println("Total: " + aliceNotifications.size() + " notifications\n");
            
            // Show unread first
            List<Notification> unread = alice.getUnreadNotifications();
            if (!unread.isEmpty()) {
                System.out.println("UNREAD (" + unread.size() + "):");
                for (Notification notification : unread) {
                    System.out.println("  " + notification);
                }
            }
            
            // Show all notifications
            System.out.println("\nALL NOTIFICATIONS:");
            for (Notification notification : aliceNotifications) {
                System.out.println("  " + notification);
            }
        }
        
        // Display Bob's notifications
        System.out.println("\n📬 Notifications for @" + bob.getUsername() + ":");
        List<Notification> bobNotifications = bob.getAllNotifications();
        
        if (bobNotifications.isEmpty()) {
            System.out.println("No notifications");
        } else {
            System.out.println("Total: " + bobNotifications.size() + " notifications\n");
            for (Notification notification : bobNotifications) {
                System.out.println("  " + notification);
            }
        }
        
        // Mark Alice's notifications as read
        alice.markAllNotificationsAsRead();
        System.out.println("\n✓ Marked all of Alice's notifications as read");
        System.out.println("Unread count: " + alice.getUnreadNotifications().size());
    }
    
    /**
     * Scenario 6: Retweets and Quote Tweets
     */
    private static void scenario6_RetweetsAndQuoteTweets() {
        System.out.println("\n" + repeatString("-", 80));
        System.out.println("SCENARIO 6: Retweets and Quote Tweets");
        System.out.println(repeatString("-", 80));
        
        TwitterService twitter = TwitterService.getInstance();
        User alice = twitter.getUserByUsername("alice");
        User bob = twitter.getUserByUsername("bob");
        User charlie = twitter.getUserByUsername("charlie");
        User david = twitter.getUserByUsername("david");
        User emma = twitter.getUserByUsername("emma");
        
        // Create an original post
        System.out.println("\n📝 Creating Original Post...");
        Post originalPost = twitter.createPost(PostType.REGULAR, alice, 
            "Design patterns are the key to writing maintainable code! 🔑", null);
        System.out.println("✓ Alice: " + originalPost.getDisplayContent());
        
        // Bob retweets it
        System.out.println("\n🔄 Creating Retweet...");
        Post bobRetweet = twitter.createPost(PostType.RETWEET, bob, null, originalPost);
        System.out.println("✓ " + bobRetweet);
        
        // Charlie quote tweets with comment
        System.out.println("\n💬 Creating Quote Tweet...");
        Post charlieQuote = twitter.createPost(PostType.QUOTE_TWEET, charlie, 
            "Absolutely agree! Strategy pattern is my favorite", originalPost);
        System.out.println("✓ " + charlieQuote);
        
        // Emma also quote tweets
        Post emmaQuote = twitter.createPost(PostType.QUOTE_TWEET, emma, 
            "Factory pattern has saved me so many times!", originalPost);
        System.out.println("✓ " + emmaQuote);
        
        // David retweets Charlie's quote tweet
        Post davidRetweet = twitter.createPost(PostType.RETWEET, david, null, charlieQuote);
        System.out.println("✓ " + davidRetweet);
        
        // Display engagement on original post
        System.out.println("\n📊 Engagement on Original Post:");
        System.out.println(originalPost);
        System.out.println("Retweet Count: " + originalPost.getRetweetCount());
        
        // Display Alice's new notifications
        System.out.println("\n📬 New Notifications for @" + alice.getUsername() + ":");
        List<Notification> newNotifications = alice.getUnreadNotifications();
        for (Notification notification : newNotifications) {
            System.out.println("  " + notification);
        }
        
        // Display system statistics
        twitter.displayStatistics();
    }
    
    /**
     * Helper method to repeat a string n times (Java 8 compatible).
     */
    private static String repeatString(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}

