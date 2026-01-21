package formatter;

import model.LogMessage;
import java.time.format.DateTimeFormatter;

/**
 * XML formatter
 */
public class XmlFormatter implements LogFormatter {
    private static final DateTimeFormatter DATE_FORMATTER = 
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    @Override
    public String format(LogMessage message) {
        StringBuilder sb = new StringBuilder();
        sb.append("<log>");
        
        sb.append("<timestamp>").append(message.getTimestamp().format(DATE_FORMATTER)).append("</timestamp>");
        sb.append("<level>").append(message.getLevel()).append("</level>");
        sb.append("<logger>").append(escapeXml(message.getLoggerName())).append("</logger>");
        sb.append("<thread>").append(escapeXml(message.getThreadName())).append("</thread>");
        sb.append("<message>").append(escapeXml(message.getMessage())).append("</message>");
        
        if (message.getThrowable() != null) {
            sb.append("<exception>").append(escapeXml(message.getThrowable().toString())).append("</exception>");
        }
        
        sb.append("</log>");
        return sb.toString();
    }

    private String escapeXml(String str) {
        if (str == null) return "";
        return str.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&apos;");
    }

    @Override
    public String getFormatterName() {
        return "XML";
    }
}
