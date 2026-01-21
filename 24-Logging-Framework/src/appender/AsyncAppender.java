package appender;

import model.LogMessage;
import formatter.LogFormatter;

import java.util.concurrent.*;

/**
 * Async appender - wraps another appender with async processing
 */
public class AsyncAppender implements LogAppender {
    private LogAppender wrappedAppender;
    private BlockingQueue<LogMessage> messageQueue;
    private ExecutorService executor;
    private volatile boolean running;

    public AsyncAppender(LogAppender wrappedAppender) {
        this(wrappedAppender, 1000);
    }

    public AsyncAppender(LogAppender wrappedAppender, int queueSize) {
        this.wrappedAppender = wrappedAppender;
        this.messageQueue = new LinkedBlockingQueue<>(queueSize);
        this.executor = Executors.newSingleThreadExecutor();
        this.running = true;
        
        // Start background thread
        executor.submit(this::processQueue);
    }

    private void processQueue() {
        while (running || !messageQueue.isEmpty()) {
            try {
                LogMessage message = messageQueue.poll(100, TimeUnit.MILLISECONDS);
                if (message != null) {
                    wrappedAppender.append(message);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    @Override
    public void append(LogMessage message) {
        try {
            if (!messageQueue.offer(message, 100, TimeUnit.MILLISECONDS)) {
                System.err.println("Log queue full, message dropped: " + message.getMessage());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void setFormatter(LogFormatter formatter) {
        wrappedAppender.setFormatter(formatter);
    }

    @Override
    public LogFormatter getFormatter() {
        return wrappedAppender.getFormatter();
    }

    @Override
    public String getAppenderName() {
        return "ASYNC(" + wrappedAppender.getAppenderName() + ")";
    }

    @Override
    public void close() {
        running = false;
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
        wrappedAppender.close();
    }

    public int getQueueSize() {
        return messageQueue.size();
    }
}
