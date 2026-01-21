package service;

import enums.LogLevel;
import model.LogMessage;
import appender.LogAppender;

import java.util.*;

/**
 * Logger class - supports hierarchical logging
 */
public class Logger {
    private String name;
    private LogLevel level;
    private List<LogAppender> appenders;
    private Logger parent;
    private boolean additive; // Whether to propagate to parent

    public Logger(String name) {
        this.name = name;
        this.level = LogLevel.INFO; // Default level
        this.appenders = new ArrayList<>();
        this.parent = null;
        this.additive = true;
    }

    // Logging methods
    public void debug(String message) {
        log(LogLevel.DEBUG, message, null);
    }

    public void info(String message) {
        log(LogLevel.INFO, message, null);
    }

    public void warn(String message) {
        log(LogLevel.WARN, message, null);
    }

    public void error(String message) {
        log(LogLevel.ERROR, message, null);
    }

    public void error(String message, Throwable throwable) {
        log(LogLevel.ERROR, message, throwable);
    }

    public void fatal(String message) {
        log(LogLevel.FATAL, message, null);
    }

    public void fatal(String message, Throwable throwable) {
        log(LogLevel.FATAL, message, throwable);
    }

    public void log(LogLevel level, String message) {
        log(level, message, null);
    }

    public void log(LogLevel level, String message, Throwable throwable) {
        // Check if level is enabled
        if (!isLevelEnabled(level)) {
            return;
        }

        // Create log message
        LogMessage logMessage = throwable == null ? 
                new LogMessage(name, level, message) :
                new LogMessage(name, level, message, throwable);

        // Append to all appenders
        for (LogAppender appender : appenders) {
            try {
                appender.append(logMessage);
            } catch (Exception e) {
                System.err.println("Failed to append log: " + e.getMessage());
            }
        }

        // Propagate to parent if additive
        if (additive && parent != null) {
            parent.log(level, message, throwable);
        }
    }

    public boolean isLevelEnabled(LogLevel level) {
        return level.isGreaterOrEqual(this.level);
    }

    public boolean isDebugEnabled() {
        return isLevelEnabled(LogLevel.DEBUG);
    }

    public boolean isInfoEnabled() {
        return isLevelEnabled(LogLevel.INFO);
    }

    // Configuration methods
    public void setLevel(LogLevel level) {
        this.level = level;
    }

    public LogLevel getLevel() {
        return level;
    }

    public void addAppender(LogAppender appender) {
        if (!appenders.contains(appender)) {
            appenders.add(appender);
        }
    }

    public void removeAppender(LogAppender appender) {
        appenders.remove(appender);
    }

    public List<LogAppender> getAppenders() {
        return new ArrayList<>(appenders);
    }

    public void setParent(Logger parent) {
        this.parent = parent;
    }

    public Logger getParent() {
        return parent;
    }

    public void setAdditive(boolean additive) {
        this.additive = additive;
    }

    public boolean isAdditive() {
        return additive;
    }

    public String getName() {
        return name;
    }

    public void close() {
        for (LogAppender appender : appenders) {
            appender.close();
        }
    }
}
