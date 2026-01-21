import enums.LogLevel;
import model.LogMessage;
import appender.*;
import formatter.*;
import service.*;

/**
 * Demo application for Logging Framework
 * Demonstrates Singleton, Factory, Strategy, and Observer patterns
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("========================================");
        System.out.println("  LOGGING FRAMEWORK DEMO");
        System.out.println("========================================\n");
        
        // Get logger factory (Singleton)
        LoggerFactory factory = LoggerFactory.getInstance();
        System.out.println("✓ Logger Factory initialized (Singleton)\n");
        
        // Scenario 1: Basic Console Logging
        System.out.println("========================================");
        System.out.println("  SCENARIO 1: BASIC CONSOLE LOGGING");
        System.out.println("========================================\n");
        
        Logger logger = factory.getLogger("com.example.MyApp");
        logger.addAppender(new ConsoleAppender());
        logger.setLevel(LogLevel.DEBUG);
        
        logger.debug("This is a DEBUG message");
        logger.info("This is an INFO message");
        logger.warn("This is a WARN message");
        logger.error("This is an ERROR message");
        logger.fatal("This is a FATAL message");
        System.out.println();
        
        // Scenario 2: Log Level Filtering
        System.out.println("========================================");
        System.out.println("  SCENARIO 2: LOG LEVEL FILTERING");
        System.out.println("========================================\n");
        
        Logger filteredLogger = factory.getLogger("com.example.FilteredApp");
        filteredLogger.addAppender(new ConsoleAppender());
        filteredLogger.setLevel(LogLevel.WARN); // Only WARN and above
        
        System.out.println("Logger level set to WARN (only WARN, ERROR, FATAL will show):\n");
        filteredLogger.debug("This DEBUG will NOT appear");
        filteredLogger.info("This INFO will NOT appear");
        filteredLogger.warn("This WARN WILL appear");
        filteredLogger.error("This ERROR WILL appear");
        System.out.println();
        
        // Scenario 3: Multiple Formatters (Strategy Pattern)
        System.out.println("========================================");
        System.out.println("  SCENARIO 3: MULTIPLE FORMATTERS (Strategy Pattern)");
        System.out.println("========================================\n");
        
        Logger formattedLogger = factory.getLogger("com.example.FormattedApp");
        
        System.out.println("--- SIMPLE FORMAT ---");
        ConsoleAppender simpleAppender = new ConsoleAppender(new SimpleFormatter());
        formattedLogger.addAppender(simpleAppender);
        formattedLogger.info("Testing simple formatter");
        formattedLogger.removeAppender(simpleAppender);
        
        System.out.println("\n--- JSON FORMAT ---");
        ConsoleAppender jsonAppender = new ConsoleAppender(new JsonFormatter());
        formattedLogger.addAppender(jsonAppender);
        formattedLogger.info("Testing JSON formatter");
        formattedLogger.removeAppender(jsonAppender);
        
        System.out.println("\n--- XML FORMAT ---");
        ConsoleAppender xmlAppender = new ConsoleAppender(new XmlFormatter());
        formattedLogger.addAppender(xmlAppender);
        formattedLogger.info("Testing XML formatter");
        formattedLogger.removeAppender(xmlAppender);
        System.out.println();
        
        // Scenario 4: File Appender with Rotation
        System.out.println("========================================");
        System.out.println("  SCENARIO 4: FILE APPENDER WITH ROTATION");
        System.out.println("========================================\n");
        
        Logger fileLogger = factory.getLogger("com.example.FileApp");
        FileAppender fileAppender = new FileAppender("logs/app.log", 1024, 3); // 1KB max, 3 backups
        fileAppender.setFormatter(new SimpleFormatter());
        fileLogger.addAppender(fileAppender);
        
        System.out.println("Writing logs to file: logs/app.log");
        System.out.println("Max file size: 1KB, Max backups: 3\n");
        
        // Write enough logs to trigger rotation
        for (int i = 1; i <= 50; i++) {
            fileLogger.info("Log message number " + i + " - This is a test message to fill up the log file");
        }
        
        System.out.println("✓ Written 50 log messages (file rotation may have occurred)\n");
        
        // Scenario 5: Async Logging
        System.out.println("========================================");
        System.out.println("  SCENARIO 5: ASYNC LOGGING");
        System.out.println("========================================\n");
        
        Logger asyncLogger = factory.getLogger("com.example.AsyncApp");
        ConsoleAppender consoleAppender = new ConsoleAppender();
        AsyncAppender asyncAppender = new AsyncAppender(consoleAppender, 100);
        asyncLogger.addAppender(asyncAppender);
        
        System.out.println("Logging asynchronously with queue size 100:\n");
        
        long startTime = System.currentTimeMillis();
        for (int i = 1; i <= 10; i++) {
            asyncLogger.info("Async log message " + i);
        }
        long endTime = System.currentTimeMillis();
        
        System.out.println("\n✓ Logged 10 messages in " + (endTime - startTime) + "ms (async)");
        System.out.println("Queue size: " + asyncAppender.getQueueSize());
        
        // Wait for async processing
        Thread.sleep(500);
        System.out.println("After processing - Queue size: " + asyncAppender.getQueueSize() + "\n");
        
        // Scenario 6: Exception Logging
        System.out.println("========================================");
        System.out.println("  SCENARIO 6: EXCEPTION LOGGING");
        System.out.println("========================================\n");
        
        Logger exceptionLogger = factory.getLogger("com.example.ExceptionApp");
        exceptionLogger.addAppender(new ConsoleAppender());
        
        try {
            int result = 10 / 0; // Intentional division by zero
        } catch (Exception e) {
            exceptionLogger.error("An error occurred during calculation", e);
        }
        System.out.println();
        
        // Scenario 7: Hierarchical Loggers
        System.out.println("========================================");
        System.out.println("  SCENARIO 7: HIERARCHICAL LOGGERS");
        System.out.println("========================================\n");
        
        // Set up root logger
        Logger rootLogger = factory.getRootLogger();
        ConsoleAppender rootAppender = new ConsoleAppender();
        rootAppender.setFormatter(new SimpleFormatter());
        rootLogger.addAppender(rootAppender);
        rootLogger.setLevel(LogLevel.INFO);
        
        System.out.println("Root logger configured with INFO level");
        System.out.println("Child loggers will inherit and propagate to root:\n");
        
        // Create child loggers
        Logger parentLogger = factory.getLogger("com.example");
        Logger childLogger = factory.getLogger("com.example.service");
        Logger grandchildLogger = factory.getLogger("com.example.service.UserService");
        
        System.out.println("--- Logging from grandchild (com.example.service.UserService) ---");
        grandchildLogger.info("Message from UserService");
        
        System.out.println("\n--- Logging from child (com.example.service) ---");
        childLogger.warn("Message from service package");
        
        System.out.println("\n--- Logging from parent (com.example) ---");
        parentLogger.error("Message from example package");
        System.out.println();
        
        // Scenario 8: Multiple Appenders
        System.out.println("========================================");
        System.out.println("  SCENARIO 8: MULTIPLE APPENDERS");
        System.out.println("========================================\n");
        
        Logger multiLogger = factory.getLogger("com.example.MultiApp");
        multiLogger.setAdditive(false); // Don't propagate to parent
        
        // Add console appender with JSON format
        ConsoleAppender jsonConsole = new ConsoleAppender(new JsonFormatter());
        multiLogger.addAppender(jsonConsole);
        
        // Add file appender with simple format
        FileAppender multiFileAppender = new FileAppender("logs/multi.log");
        multiFileAppender.setFormatter(new SimpleFormatter());
        multiLogger.addAppender(multiFileAppender);
        
        System.out.println("Logger with 2 appenders:");
        System.out.println("1. Console (JSON format)");
        System.out.println("2. File (Simple format)\n");
        
        multiLogger.info("This message goes to both console and file");
        System.out.println("\n✓ Message written to both appenders\n");
        
        // Scenario 9: Performance Test
        System.out.println("========================================");
        System.out.println("  SCENARIO 9: PERFORMANCE TEST");
        System.out.println("========================================\n");
        
        Logger perfLogger = factory.getLogger("com.example.PerfTest");
        perfLogger.setAdditive(false);
        
        // Synchronous logging
        ConsoleAppender syncAppender = new ConsoleAppender();
        perfLogger.addAppender(syncAppender);
        
        System.out.println("Testing synchronous logging...");
        startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            perfLogger.info("Sync message " + i);
        }
        endTime = System.currentTimeMillis();
        long syncTime = endTime - startTime;
        
        perfLogger.removeAppender(syncAppender);
        
        // Asynchronous logging
        AsyncAppender perfAsyncAppender = new AsyncAppender(new ConsoleAppender(), 2000);
        perfLogger.addAppender(perfAsyncAppender);
        
        System.out.println("Testing asynchronous logging...");
        startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            perfLogger.info("Async message " + i);
        }
        endTime = System.currentTimeMillis();
        long asyncTime = endTime - startTime;
        
        System.out.println("\nPerformance Results (1000 messages):");
        System.out.println("  Synchronous: " + syncTime + "ms");
        System.out.println("  Asynchronous: " + asyncTime + "ms");
        System.out.println("  Speedup: " + (syncTime / (double) asyncTime) + "x");
        
        // Wait for async processing
        Thread.sleep(1000);
        perfLogger.removeAppender(perfAsyncAppender);
        perfAsyncAppender.close();
        System.out.println();
        
        // Scenario 10: Edge Cases
        System.out.println("========================================");
        System.out.println("  SCENARIO 10: EDGE CASES");
        System.out.println("========================================\n");
        
        Logger edgeLogger = factory.getLogger("com.example.EdgeCase");
        edgeLogger.addAppender(new ConsoleAppender());
        edgeLogger.setAdditive(false);
        
        // Empty message
        System.out.println("--- Empty message ---");
        edgeLogger.info("");
        
        // Null check (would throw NPE in real scenario, so we handle it)
        System.out.println("\n--- Very long message ---");
        StringBuilder longMessage = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            longMessage.append("Long ");
        }
        edgeLogger.info(longMessage.toString());
        
        System.out.println("\n--- Special characters ---");
        edgeLogger.info("Message with special chars: <>&\"'\n\t");
        System.out.println();
        
        // Cleanup
        System.out.println("========================================");
        System.out.println("  CLEANUP");
        System.out.println("========================================\n");
        
        System.out.println("Shutting down logger factory...");
        factory.shutdown();
        System.out.println("✓ All appenders closed\n");
        
        // Final Summary
        System.out.println("========================================");
        System.out.println("  DEMO COMPLETED SUCCESSFULLY!");
        System.out.println("========================================");
        System.out.println("\n✅ Demonstrated Patterns:");
        System.out.println("  1. Singleton Pattern - LoggerFactory");
        System.out.println("  2. Factory Pattern - Logger creation");
        System.out.println("  3. Strategy Pattern - Multiple formatters (Simple, JSON, XML)");
        System.out.println("  4. Strategy Pattern - Multiple appenders (Console, File, Async)");
        System.out.println("\n✅ Features Demonstrated:");
        System.out.println("  • Multiple log levels (DEBUG, INFO, WARN, ERROR, FATAL)");
        System.out.println("  • Log level filtering");
        System.out.println("  • Multiple formatters (Simple, JSON, XML)");
        System.out.println("  • Console appender");
        System.out.println("  • File appender with rotation (size-based)");
        System.out.println("  • Async appender with queue");
        System.out.println("  • Exception logging with stack traces");
        System.out.println("  • Hierarchical loggers");
        System.out.println("  • Multiple appenders per logger");
        System.out.println("  • Performance comparison (sync vs async)");
        System.out.println("  • Edge case handling");
        System.out.println("\n🎉 All 10 scenarios executed successfully!");
    }
}
