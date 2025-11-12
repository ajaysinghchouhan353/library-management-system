package org.example.observer;

import org.example.model.Book;

/** Observer for book availability notifications. */
public interface BookObserver {
    void onBookAvailable(Book book);
}
