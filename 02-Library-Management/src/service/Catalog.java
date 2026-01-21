package service;

import model.Book;
import model.BookItem;
import enums.BookStatus;
import java.util.*;
import java.util.stream.Collectors;

public class Catalog {
    private Map<String, List<BookItem>> booksByTitle;
    private Map<String, List<BookItem>> booksByAuthor;
    private Map<String, BookItem> booksByBarcode;
    private Map<String, List<BookItem>> booksByISBN;
    
    public Catalog() {
        this.booksByTitle = new HashMap<>();
        this.booksByAuthor = new HashMap<>();
        this.booksByBarcode = new HashMap<>();
        this.booksByISBN = new HashMap<>();
    }
    
    public void addBookItem(BookItem bookItem) {
        Book book = bookItem.getBook();
        
        // Index by title (lowercase for case-insensitive search)
        booksByTitle.computeIfAbsent(book.getTitle().toLowerCase(), 
                                      k -> new ArrayList<>()).add(bookItem);
        
        // Index by author (lowercase for case-insensitive search)
        booksByAuthor.computeIfAbsent(book.getAuthor().toLowerCase(), 
                                       k -> new ArrayList<>()).add(bookItem);
        
        // Index by barcode
        booksByBarcode.put(bookItem.getBarcode(), bookItem);
        
        // Index by ISBN
        booksByISBN.computeIfAbsent(book.getISBN(), 
                                     k -> new ArrayList<>()).add(bookItem);
    }
    
    public List<BookItem> searchByTitle(String title) {
        return booksByTitle.getOrDefault(title.toLowerCase(), new ArrayList<>());
    }
    
    public List<BookItem> searchByAuthor(String author) {
        return booksByAuthor.getOrDefault(author.toLowerCase(), new ArrayList<>());
    }
    
    public BookItem searchByBarcode(String barcode) {
        return booksByBarcode.get(barcode);
    }
    
    public List<BookItem> searchByISBN(String ISBN) {
        return booksByISBN.getOrDefault(ISBN, new ArrayList<>());
    }
    
    public List<BookItem> getAvailableBooks(String ISBN) {
        return booksByISBN.getOrDefault(ISBN, new ArrayList<>())
            .stream()
            .filter(item -> item.getStatus() == BookStatus.AVAILABLE)
            .collect(Collectors.toList());
    }
    
    public int getTotalBooks() {
        return booksByBarcode.size();
    }
    
    public int getAvailableBookCount() {
        return (int) booksByBarcode.values().stream()
            .filter(item -> item.getStatus() == BookStatus.AVAILABLE)
            .count();
    }
}


