package com.mycompany.project2.month1;

import java.io.Serializable;

public class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nextID = 1001;

    private int bookID;
    private String title;
    private String author;
    private String ISBN;
    private int quantity;

    public Book(String title, String author, String ISBN, int quantity) {
        this.bookID = nextID++;
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.quantity = quantity;
    }

    public int getBookID() { return bookID; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getISBN() { return ISBN; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public boolean issueBook() {
        if (quantity > 0) {
            quantity--;
            return true;
        }
        return false;
    }

    public void returnBook() {
        quantity++;
    }
}
