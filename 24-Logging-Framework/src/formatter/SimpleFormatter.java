package formatter;

import model.LogMessage;
import java.time.format.DateTimeFormatter;

/**
 * Simple text formatter
 */
public class SimpleFormatter implements LogFormatter {
    private static final DateTimeFormatter DATE_FORMATTER = 
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public String format(LogMessage message) {
        StringBuilder sb = new StringBuilder();
        
        sb.append(message.getTimestamp().format(DATE_FORMATTER));
        sb.append(" [").append(message.getThreadName()).append("]");
        sb.append(" ").append(message.getLevel());
        sb.append(" ").append(message.getLoggerName());
        sb.append(" - ").append(message.getMessage());
        
        if (message.getThrowable() != null) {
            sb.append("\n").append(formatThrowable(message.getThrowable()));
        }
        
        return sb.toString();
    }

    private String formatThrowable(Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        sb.append(throwable.getClass().getName()).append(": ").append(throwable.getMessage());
        
        for (StackTraceElement element : throwable.getStackTrace()) {
            sb.append("\n\tat ").append(element.toString());
        }
        
        return sb.toString();
    }

    @Override
    public String getFormatterName() {
        return "SIMPLE";
    }
}
