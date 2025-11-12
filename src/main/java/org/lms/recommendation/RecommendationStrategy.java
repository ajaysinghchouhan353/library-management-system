package org.lms.recommendation;

import org.lms.model.Book;

import java.util.List;

public interface RecommendationStrategy {
    /**
     * Recommend books for a patron given their borrowing history and the library inventory.
     */
    List<Book> recommend(String patronId, List<String> patronHistoryIsbns, List<Book> inventory);
}
