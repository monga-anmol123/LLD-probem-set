package appender;

import model.LogMessage;
import formatter.LogFormatter;

/**
 * Strategy interface for log appenders
 */
public interface LogAppender {
    void append(LogMessage message);
    void setFormatter(LogFormatter formatter);
    LogFormatter getFormatter();
    String getAppenderName();
    void close();
}
