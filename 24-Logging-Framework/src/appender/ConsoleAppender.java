package appender;

import model.LogMessage;
import formatter.LogFormatter;
import formatter.SimpleFormatter;

/**
 * Console appender - writes logs to System.out
 */
public class ConsoleAppender implements LogAppender {
    private LogFormatter formatter;

    public ConsoleAppender() {
        this.formatter = new SimpleFormatter();
    }

    public ConsoleAppender(LogFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public void append(LogMessage message) {
        String formattedMessage = formatter.format(message);
        System.out.println(formattedMessage);
    }

    @Override
    public void setFormatter(LogFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public LogFormatter getFormatter() {
        return formatter;
    }

    @Override
    public String getAppenderName() {
        return "CONSOLE";
    }

    @Override
    public void close() {
        // Nothing to close for console
    }
}
