package model;

import enums.NotificationChannel;
import java.util.*;

/**
 * Represents a reusable notification template
 */
public class NotificationTemplate {
    private String templateId;
    private String name;
    private NotificationChannel channel;
    private String subjectTemplate;
    private String messageTemplate;
    private Set<String> requiredVariables;

    public NotificationTemplate(String templateId, String name, NotificationChannel channel,
                               String subjectTemplate, String messageTemplate) {
        this.templateId = templateId;
        this.name = name;
        this.channel = channel;
        this.subjectTemplate = subjectTemplate;
        this.messageTemplate = messageTemplate;
        this.requiredVariables = extractVariables(subjectTemplate, messageTemplate);
    }

    private Set<String> extractVariables(String subject, String message) {
        Set<String> variables = new HashSet<>();
        String combined = subject + " " + message;
        
        // Extract variables in format {{variable}}
        int start = 0;
        while ((start = combined.indexOf("{{", start)) != -1) {
            int end = combined.indexOf("}}", start);
            if (end != -1) {
                String variable = combined.substring(start + 2, end).trim();
                variables.add(variable);
                start = end + 2;
            } else {
                break;
            }
        }
        
        return variables;
    }

    public String renderSubject(Map<String, String> variables) {
        return render(subjectTemplate, variables);
    }

    public String renderMessage(Map<String, String> variables) {
        return render(messageTemplate, variables);
    }

    private String render(String template, Map<String, String> variables) {
        String result = template;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            result = result.replace(placeholder, entry.getValue());
        }
        return result;
    }

    public boolean validateVariables(Map<String, String> variables) {
        return variables.keySet().containsAll(requiredVariables);
    }

    public String getTemplateId() {
        return templateId;
    }

    public String getName() {
        return name;
    }

    public NotificationChannel getChannel() {
        return channel;
    }

    public Set<String> getRequiredVariables() {
        return new HashSet<>(requiredVariables);
    }

    @Override
    public String toString() {
        return String.format("Template %s: %s [%s] - Variables: %s", 
                templateId, name, channel, requiredVariables);
    }
}
