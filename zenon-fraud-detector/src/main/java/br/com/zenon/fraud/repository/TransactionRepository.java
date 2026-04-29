package br.com.zenon.fraud.repository;

import br.com.zenon.fraud.model.Transaction;
import java.util.Optional;
import java.util.List;

public interface TransactionRepository {
    Optional<Transaction> buscarPorNomeOrigem(String nameOrig);
    void save(Transaction transaction);
    void saveAll(List<Transaction> transactions);
    void deleteAll();
}