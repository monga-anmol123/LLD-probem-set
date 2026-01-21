package factory;

import enums.NotificationChannel;
import strategy.*;

/**
 * Factory for creating notification senders based on channel
 */
public class NotificationSenderFactory {
    
    public static NotificationSender getSender(NotificationChannel channel) {
        switch (channel) {
            case EMAIL:
                return new EmailSender();
            case SMS:
                return new SmsSender();
            case PUSH:
                return new PushSender();
            case IN_APP:
                return new InAppSender();
            default:
                throw new IllegalArgumentException("Unsupported channel: " + channel);
        }
    }
}
