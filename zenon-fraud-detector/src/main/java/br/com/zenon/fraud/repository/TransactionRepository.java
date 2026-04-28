package br.com.zenon.fraud.repository;

import br.com.zenon.fraud.model.Transaction;

import java.util.Optional;

public interface TransactionRepository {

    Optional<Transaction> buscarPorNomeOrigem (String nameOrig);
}
