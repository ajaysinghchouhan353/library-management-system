package org.lms.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a library patron.
 */
public class Patron {
    private final String id;
    private String name;
    private String email;
    private final List<String> borrowingHistoryIsbns = new ArrayList<>();

    public Patron(String id, String name, String email) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.email = Objects.requireNonNull(email);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = Objects.requireNonNull(email);
    }

    public void recordBorrow(String isbn) {
        borrowingHistoryIsbns.add(isbn);
    }

    public List<String> getBorrowingHistory() {
        return Collections.unmodifiableList(borrowingHistoryIsbns);
    }

    @Override
    public String toString() {
        return String.format("Patron{id='%s', name='%s', email='%s'}", id, name, email);
    }
}
