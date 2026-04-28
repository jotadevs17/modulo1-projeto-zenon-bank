package br.com.zenon.fraud.model;

import java.math.BigDecimal;

public record Transaction(int step, TransactionType type, BigDecimal amount, Customer origin, Customer destination, boolean isFraud, boolean isFlaggedFraud) {

    public Transaction {

        if (step <= 0) {
            throw new IllegalArgumentException("step should be positive");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("amount should be positive");
        }

        if (origin == null) {
            throw new IllegalArgumentException("origin should not be null");
        }

        if (destination == null) {
            throw new IllegalArgumentException("destination should not be null");
        }

        if (type == null) {
            throw new IllegalArgumentException("type should not be null");
        }
    }
}
