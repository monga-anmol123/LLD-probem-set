package appender;

import model.LogMessage;
import formatter.LogFormatter;
import formatter.SimpleFormatter;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * File appender - writes logs to file with rotation support
 */
public class FileAppender implements LogAppender {
    private LogFormatter formatter;
    private String filePath;
    private long maxFileSize; // in bytes
    private int maxBackupFiles;
    private BufferedWriter writer;
    private long currentFileSize;

    public FileAppender(String filePath) {
        this(filePath, 10 * 1024 * 1024, 5); // 10MB default, 5 backup files
    }

    public FileAppender(String filePath, long maxFileSize, int maxBackupFiles) {
        this.formatter = new SimpleFormatter();
        this.filePath = filePath;
        this.maxFileSize = maxFileSize;
        this.maxBackupFiles = maxBackupFiles;
        this.currentFileSize = 0;
        initializeWriter();
    }

    private void initializeWriter() {
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            
            if (file.exists()) {
                currentFileSize = file.length();
            }
            
            writer = new BufferedWriter(new FileWriter(filePath, true));
        } catch (IOException e) {
            System.err.println("Failed to initialize file appender: " + e.getMessage());
        }
    }

    @Override
    public synchronized void append(LogMessage message) {
        try {
            String formattedMessage = formatter.format(message);
            String line = formattedMessage + System.lineSeparator();
            
            // Check if rotation is needed
            if (currentFileSize + line.length() > maxFileSize) {
                rotateFiles();
            }
            
            writer.write(line);
            writer.flush();
            currentFileSize += line.length();
            
        } catch (IOException e) {
            System.err.println("Failed to write log: " + e.getMessage());
        }
    }

    private void rotateFiles() throws IOException {
        // Close current writer
        if (writer != null) {
            writer.close();
        }

        // Delete oldest backup if exists
        File oldestBackup = new File(filePath + "." + maxBackupFiles);
        if (oldestBackup.exists()) {
            oldestBackup.delete();
        }

        // Rotate existing backups
        for (int i = maxBackupFiles - 1; i > 0; i--) {
            File source = new File(filePath + "." + i);
            if (source.exists()) {
                File dest = new File(filePath + "." + (i + 1));
                source.renameTo(dest);
            }
        }

        // Rename current file to .1
        File currentFile = new File(filePath);
        File backup = new File(filePath + ".1");
        currentFile.renameTo(backup);

        // Create new file
        currentFileSize = 0;
        writer = new BufferedWriter(new FileWriter(filePath, true));
        
        System.out.println("📁 Log file rotated: " + filePath);
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
        return "FILE";
    }

    @Override
    public void close() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            System.err.println("Failed to close file appender: " + e.getMessage());
        }
    }
}
