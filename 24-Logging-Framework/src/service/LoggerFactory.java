package service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Logger Factory - Singleton pattern with Factory pattern
 * Creates and manages loggers with hierarchical structure
 */
public class LoggerFactory {
    private static LoggerFactory instance;
    private Map<String, Logger> loggers;
    private Logger rootLogger;

    private LoggerFactory() {
        this.loggers = new ConcurrentHashMap<>();
        this.rootLogger = new Logger("ROOT");
        loggers.put("ROOT", rootLogger);
    }

    public static synchronized LoggerFactory getInstance() {
        if (instance == null) {
            instance = new LoggerFactory();
        }
        return instance;
    }

    /**
     * Get logger by name (creates if doesn't exist)
     * Supports hierarchical naming: com.example.MyClass
     */
    public Logger getLogger(String name) {
        if (name == null || name.isEmpty()) {
            return rootLogger;
        }

        // Return existing logger if found
        Logger logger = loggers.get(name);
        if (logger != null) {
            return logger;
        }

        // Create new logger
        synchronized (this) {
            // Double-check
            logger = loggers.get(name);
            if (logger != null) {
                return logger;
            }

            logger = new Logger(name);
            
            // Set up hierarchy
            Logger parent = findParent(name);
            if (parent != null) {
                logger.setParent(parent);
            }

            loggers.put(name, logger);
            return logger;
        }
    }

    /**
     * Get logger by class
     */
    public Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }

    /**
     * Find parent logger in hierarchy
     * For "com.example.MyClass", parent would be "com.example"
     */
    private Logger findParent(String name) {
        int lastDot = name.lastIndexOf('.');
        
        while (lastDot > 0) {
            String parentName = name.substring(0, lastDot);
            Logger parent = loggers.get(parentName);
            if (parent != null) {
                return parent;
            }
            lastDot = parentName.lastIndexOf('.');
        }
        
        return rootLogger;
    }

    public Logger getRootLogger() {
        return rootLogger;
    }

    public Collection<Logger> getAllLoggers() {
        return new ArrayList<>(loggers.values());
    }

    public void shutdown() {
        for (Logger logger : loggers.values()) {
            logger.close();
        }
    }

    /**
     * Reset factory (useful for testing)
     */
    public void reset() {
        shutdown();
        loggers.clear();
        rootLogger = new Logger("ROOT");
        loggers.put("ROOT", rootLogger);
    }
}
