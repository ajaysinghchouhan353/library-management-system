package org.example.factory;

import org.example.model.Book;
import org.example.model.Patron;

/** Simple factory for creating domain objects. */
public final class LibraryFactory {
    private LibraryFactory() { }

    public static Book createBook(String isbn, String title, String author, int year) {
        return new Book(isbn, title, author, year);
    }

    public static Patron createPatron(String id, String name, String email) {
        return new Patron(id, name, email);
    }
}
