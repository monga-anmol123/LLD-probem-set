package formatter;

import model.LogMessage;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * JSON formatter
 */
public class JsonFormatter implements LogFormatter {
    private static final DateTimeFormatter DATE_FORMATTER = 
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    @Override
    public String format(LogMessage message) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        
        sb.append("\"timestamp\":\"").append(message.getTimestamp().format(DATE_FORMATTER)).append("\",");
        sb.append("\"level\":\"").append(message.getLevel()).append("\",");
        sb.append("\"logger\":\"").append(message.getLoggerName()).append("\",");
        sb.append("\"thread\":\"").append(message.getThreadName()).append("\",");
        sb.append("\"message\":\"").append(escapeJson(message.getMessage())).append("\"");
        
        if (!message.getContext().isEmpty()) {
            sb.append(",\"context\":{");
            boolean first = true;
            for (Map.Entry<String, String> entry : message.getContext().entrySet()) {
                if (!first) sb.append(",");
                sb.append("\"").append(entry.getKey()).append("\":\"")
                  .append(escapeJson(entry.getValue())).append("\"");
                first = false;
            }
            sb.append("}");
        }
        
        if (message.getThrowable() != null) {
            sb.append(",\"exception\":\"").append(escapeJson(message.getThrowable().toString())).append("\"");
        }
        
        sb.append("}");
        return sb.toString();
    }

    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }

    @Override
    public String getFormatterName() {
        return "JSON";
    }
}
