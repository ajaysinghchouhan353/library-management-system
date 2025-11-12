package org.lms.model;

import java.time.LocalDate;
import java.util.Objects;

public class Loan {
    private final String isbn;
    private final String patronId;
    private final LocalDate loanDate;
    private LocalDate returnDate;

    public Loan(String isbn, String patronId) {
        this.isbn = Objects.requireNonNull(isbn);
        this.patronId = Objects.requireNonNull(patronId);
        this.loanDate = LocalDate.now();
    }

    public String getIsbn() {
        return isbn;
    }

    public String getPatronId() {
        return patronId;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void markReturned() {
        this.returnDate = LocalDate.now();
    }

    @Override
    public String toString() {
        return String.format("Loan{isbn='%s', patronId='%s', loanDate=%s, returnDate=%s}", isbn, patronId, loanDate, returnDate);
    }
}
