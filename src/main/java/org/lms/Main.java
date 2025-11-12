package org.lms;

import org.lms.factory.LibraryFactory;
import org.lms.model.Book;
import org.lms.model.Patron;
import org.lms.observer.BookObserver;
import org.lms.recommendation.SimpleRecommendationStrategy;
import org.lms.service.Library;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        // basic console logger setup
        Logger root = Logger.getLogger("");
        root.setLevel(Level.INFO);
        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(Level.INFO);
        root.addHandler(ch);

        Library library = new Library();
        library.setRecommendationStrategy(new SimpleRecommendationStrategy());

        // create books and patrons via factory
        Book b1 = LibraryFactory.createBook("978-0134685991", "Effective Java", "Joshua Bloch", 2018);
        Book b2 = LibraryFactory.createBook("978-0596009205", "Head First Java", "Kathy Sierra", 2005);
        Book b3 = LibraryFactory.createBook("978-0201633610", "Design Patterns", "Erich Gamma", 1994);

        library.addBook(b1);
        library.addBook(b2);
        library.addBook(b3);

        Patron p1 = LibraryFactory.createPatron("p1", "Alice", "alice@example.com");
        Patron p2 = LibraryFactory.createPatron("p2", "Bob", "bob@example.com");

        library.addPatron(p1);
        library.addPatron(p2);

        // checkout and return flows
        library.checkoutBook(b1.getIsbn(), p1.getId());
        library.checkoutBook(b2.getIsbn(), p2.getId());

        // p2 tries to checkout a book that's already borrowed
        boolean ok = library.checkoutBook(b1.getIsbn(), p2.getId());
        System.out.println("p2 checkout of b1 success? " + ok);

        // Demonstrate reservation/observer: p2 reserves b1 and will be notified when it's available
        library.reserveBook(b1.getIsbn(), new BookObserver() {
            @Override
            public void onBookAvailable(Book book) {
                System.out.println("Notification -> Book available: " + book.getTitle() + " (for p2)");
                // attempt to checkout when notified
                boolean checked = library.checkoutBook(book.getIsbn(), p2.getId());
                System.out.println("p2 auto-checkout on notification success? " + checked);
            }
        });

        // Return book and trigger notification
        library.returnBook(b1.getIsbn());

        // Recommendations for p1 (simple): based on borrow history
        System.out.println("Recommendations for p1: " + library.recommendFor(p1.getId()));

        System.out.println("Demo complete.");
    }
}