package com.mycompany.project2.month1;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Library {
    private List<Book> books;
    private final String FILE_NAME = "library_data.txt";

    public Library() {
        books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public List<Book> getBooks() {
        return books;
    }

    public Book searchByISBN(String isbn) {
        for (Book b : books) {
            if (b.getISBN().equalsIgnoreCase(isbn.trim())) {
                return b;
            }
        }
        return null;
    }

    public Book searchByTitle(String title) {
        for (Book b : books) {
            if (b.getTitle().equalsIgnoreCase(title.trim())) {
                return b;
            }
        }
        return null;
    }

    public boolean issueBook(String query) {
        Book b = searchByISBN(query);
        if (b == null) b = searchByTitle(query);
        return (b != null) && b.issueBook();
    }

    public boolean returnBook(String query) {
        Book b = searchByISBN(query);
        if (b == null) b = searchByTitle(query);
        if (b != null) {
            b.returnBook();
            return true;
        }
        return false;
    }

    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(books);
        } catch (IOException e) {
            System.err.println("Error saving: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            books = (List<Book>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading: " + e.getMessage());
        }
    }
}
