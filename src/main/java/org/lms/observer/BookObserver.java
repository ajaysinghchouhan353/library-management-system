package org.lms.observer;

import org.lms.model.Book;

/** Observer for book availability notifications. */
public interface BookObserver {
    void onBookAvailable(Book book);
}
