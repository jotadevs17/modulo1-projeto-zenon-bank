package br.com.zenon.fraud.service;

import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.model.TransactionType;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FraudAnalyzer {

    private final List<Transaction> transactions;

    public FraudAnalyzer(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public long getTotalFraudes() {
        return transactions.stream()
                .filter(t -> t.isFraud())
                .count();
    }

    public List<BigDecimal> getTop3FraudesDeMaiorValor() {
        return transactions.stream()
                .filter(t -> t.isFraud())
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .limit(3)
                .map(t -> t.amount())
                .collect(Collectors.toList());
    }

    public List<String> getClientesSuspeitos() {
        return transactions.stream()
                .filter(t -> t.isFraud())
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .map(t -> t.origin().name())
                .distinct()
                .limit(5)
                .collect(Collectors.toList());
    }

    public BigDecimal getPrejuizoTotal() {
        return transactions.stream()
                .filter(t -> t.isFraud())
                .map(t -> t.amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<TransactionType, Long> getFraudesPorTipo() {
        return transactions.stream()
                .filter(t -> t.isFraud())
                .collect(Collectors.groupingBy(Transaction::type, Collectors.counting()));
    }

}
