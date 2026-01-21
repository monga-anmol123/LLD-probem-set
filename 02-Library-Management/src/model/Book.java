package model;

import java.util.ArrayList;
import java.util.List;

public class Book {
    private String ISBN;
    private String title;
    private String author;
    private String publisher;
    private List<String> subjects;
    
    public Book(String ISBN, String title, String author, String publisher) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.subjects = new ArrayList<>();
    }
    
    public void addSubject(String subject) {
        subjects.add(subject);
    }
    
    // Getters
    public String getISBN() { return ISBN; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getPublisher() { return publisher; }
    public List<String> getSubjects() { return subjects; }
}


