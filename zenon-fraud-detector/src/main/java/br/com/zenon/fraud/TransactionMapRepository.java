package br.com.zenon.fraud;

import javax.swing.text.html.Option;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TransactionMapRepository implements TransactionRepository {
    private final Map<String, Transaction> mapTransactions = new HashMap<>();

    public TransactionMapRepository(List<Transaction> transactions) {

        for (Transaction transaction : transactions) {
            mapTransactions.put(transaction.origin().name(), transaction);
        }
    }

    @Override
    public Optional<Transaction> buscarPorNomeOrigem(String nameOrig) {
        return Optional.ofNullable(mapTransactions.get(nameOrig));
    }

}
