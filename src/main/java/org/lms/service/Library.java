package org.lms.service;

import org.lms.model.Book;
import org.lms.model.Loan;
import org.lms.model.Patron;
import org.lms.observer.ReservationManager;
import org.lms.recommendation.RecommendationStrategy;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Library service managing books, patrons, loans and reservations.
 * This is an in-memory implementation suitable for demonstrating OOP, SOLID and patterns.
 */
public class Library {
    private static final Logger LOG = Logger.getLogger(Library.class.getName());

    private final Map<String, Book> inventory = new HashMap<>(); // isbn -> book
    private final Map<String, Patron> patrons = new HashMap<>(); // id -> patron
    private final Map<String, Loan> activeLoansByIsbn = new HashMap<>(); // isbn -> loan
    private final List<Loan> loanHistory = new ArrayList<>();
    private final ReservationManager reservationManager = new ReservationManager();
    private RecommendationStrategy recommender;

    public Library() { }

    public void setRecommendationStrategy(RecommendationStrategy recommender) {
        this.recommender = recommender;
    }

    // Book management
    public synchronized void addBook(Book book) {
        inventory.put(book.getIsbn(), book);
        LOG.info(() -> "Book added: " + book.getIsbn());
    }

    public synchronized void removeBook(String isbn) {
        inventory.remove(isbn);
        LOG.info(() -> "Book removed: " + isbn);
    }

    public synchronized void updateBook(Book book) {
        inventory.put(book.getIsbn(), book);
        LOG.info(() -> "Book updated: " + book.getIsbn());
    }

    public synchronized Optional<Book> findByIsbn(String isbn) {
        return Optional.ofNullable(inventory.get(isbn));
    }

    public synchronized List<Book> searchByTitle(String titlePart) {
        String lowered = titlePart.toLowerCase(Locale.ROOT);
        return inventory.values().stream()
                .filter(b -> b.getTitle().toLowerCase(Locale.ROOT).contains(lowered))
                .collect(Collectors.toList());
    }

    public synchronized List<Book> searchByAuthor(String authorPart) {
        String lowered = authorPart.toLowerCase(Locale.ROOT);
        return inventory.values().stream()
                .filter(b -> b.getAuthor().toLowerCase(Locale.ROOT).contains(lowered))
                .collect(Collectors.toList());
    }

    // Patron management
    public synchronized void addPatron(Patron patron) {
        patrons.put(patron.getId(), patron);
        LOG.info(() -> "Patron added: " + patron.getId());
    }

    public synchronized Optional<Patron> getPatron(String id) {
        return Optional.ofNullable(patrons.get(id));
    }

    // Lending
    public synchronized boolean checkoutBook(String isbn, String patronId) {
        if (!inventory.containsKey(isbn)) return false;
        if (activeLoansByIsbn.containsKey(isbn)) {
            // already borrowed - allow reservation
            LOG.info(() -> "Attempt to checkout borrowed book, suggest reservation: " + isbn);
            return false;
        }

        Patron patron = patrons.get(patronId);
        if (patron == null) {
            LOG.warning("Unknown patron: " + patronId);
            return false;
        }

        Loan loan = new Loan(isbn, patronId);
        activeLoansByIsbn.put(isbn, loan);
        loanHistory.add(loan);
        patron.recordBorrow(isbn);
        LOG.info(() -> String.format("Book %s checked out to %s", isbn, patronId));
        return true;
    }

    public synchronized boolean returnBook(String isbn) {
        Loan loan = activeLoansByIsbn.remove(isbn);
        if (loan == null) return false;
        loan.markReturned();
        LOG.info(() -> "Book returned: " + isbn);
        reservationManager.notifyNextAvailable(inventory.get(isbn));
        return true;
    }

    public synchronized void reserveBook(String isbn, org.lms.observer.BookObserver observer) {
        if (!inventory.containsKey(isbn)) {
            LOG.warning(() -> "Cannot reserve unknown ISBN=" + isbn);
            return;
        }
        reservationManager.reserve(isbn, observer);
    }

    public synchronized boolean isAvailable(String isbn) {
        return inventory.containsKey(isbn) && !activeLoansByIsbn.containsKey(isbn);
    }

    public synchronized List<Book> getAllBooks() {
        return new ArrayList<>(inventory.values());
    }

    public synchronized List<Loan> getLoanHistory() {
        return Collections.unmodifiableList(loanHistory);
    }

    public synchronized List<Book> recommendFor(String patronId) {
        if (recommender == null) return Collections.emptyList();
        Patron p = patrons.get(patronId);
        if (p == null) return Collections.emptyList();
        return recommender.recommend(patronId, p.getBorrowingHistory(), getAllBooks());
    }

}
