package org.lms.observer;

import org.lms.model.Book;

import java.util.*;
import java.util.logging.Logger;

/**
 * Manages reservations for books and notifies observers when a book becomes available.
 */
public class ReservationManager {
    private static final Logger LOG = Logger.getLogger(ReservationManager.class.getName());

    // isbn -> queue of observers (patron ids could be represented by observers)
    private final Map<String, Deque<BookObserver>> reservations = new HashMap<>();

    public synchronized void reserve(String isbn, BookObserver observer) {
        reservations.computeIfAbsent(isbn, k -> new ArrayDeque<>()).addLast(observer);
        LOG.info(() -> "Reservation added for ISBN=" + isbn);
    }

    public synchronized boolean hasReservation(String isbn) {
        Deque<BookObserver> q = reservations.get(isbn);
        return q != null && !q.isEmpty();
    }

    public synchronized void notifyNextAvailable(Book book) {
        Deque<BookObserver> q = reservations.get(book.getIsbn());
        if (q == null || q.isEmpty()) return;
        BookObserver next = q.pollFirst();
        if (next != null) {
            LOG.info(() -> "Notifying next observer about availability of " + book.getIsbn());
            next.onBookAvailable(book);
        }
    }
}
