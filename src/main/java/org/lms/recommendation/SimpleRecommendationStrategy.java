package org.lms.recommendation;

import org.lms.model.Book;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Very simple recommender: finds the most frequent author in the patron's history
 * and recommends other books by that author that the patron hasn't borrowed yet.
 */
public class SimpleRecommendationStrategy implements RecommendationStrategy {
    @Override
    public List<Book> recommend(String patronId, List<String> patronHistoryIsbns, List<Book> inventory) {
        if (patronHistoryIsbns == null || patronHistoryIsbns.isEmpty()) return Collections.emptyList();

        Map<String, Long> authorCount = inventory.stream()
                .filter(b -> patronHistoryIsbns.contains(b.getIsbn()))
                .collect(Collectors.groupingBy(Book::getAuthor, Collectors.counting()));

        if (authorCount.isEmpty()) return Collections.emptyList();

        String favoriteAuthor = authorCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        if (favoriteAuthor == null) return Collections.emptyList();

        Set<String> already = new HashSet<>(patronHistoryIsbns);
        return inventory.stream()
                .filter(b -> favoriteAuthor.equals(b.getAuthor()) && !already.contains(b.getIsbn()))
                .collect(Collectors.toList());
    }
}
