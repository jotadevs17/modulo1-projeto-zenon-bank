package br.com.zenon.fraud.model;

import java.math.BigDecimal;

public record Customer(String name, BigDecimal oldBalance, BigDecimal newBalance) {

    public Customer {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name should not be empty");
        }

        if (oldBalance == null || oldBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("oldBalance should be positive:");
        }

        if (newBalance == null || newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("newBalance should be positive:");
        }
    }

}
