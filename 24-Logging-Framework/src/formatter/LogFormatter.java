package formatter;

import model.LogMessage;

/**
 * Strategy interface for log formatting
 */
public interface LogFormatter {
    String format(LogMessage message);
    String getFormatterName();
}
