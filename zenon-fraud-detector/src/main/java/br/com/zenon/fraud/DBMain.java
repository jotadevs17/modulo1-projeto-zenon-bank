package br.com.zenon.fraud;
import br.com.zenon.fraud.io.TransactionIngestor;
import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.repository.TransactionRepository;
import br.com.zenon.fraud.repository.TransactionSQLRepository;

import java.util.List;

public class DBMain {
    public static void main(String[] args) {
        TransactionIngestor ingestor = new TransactionIngestor();
        // Carrega 10 mil transações conforme o requisito
        List<Transaction> transacoes = ingestor.loadTransactions("data/PS_20174392719_1491204439457_log.csv");

        TransactionRepository sqlRepo = new TransactionSQLRepository();

        System.out.println("Iniciando ingestão de 10.000 transações no MySQL...");
        long start = System.currentTimeMillis();
        sqlRepo.saveAll(transacoes.subList(0, 10000));
        long end = System.currentTimeMillis();

        System.out.println("Tempo total de inserção (Batch): " + (end - start) + "ms");

        System.out.println("\nRealizando buscas:");
        sqlRepo.buscarPorNomeOrigem("C1231006815").ifPresentOrElse(
                System.out::println,
                () -> System.out.println("Transação não encontrada para o cliente C1231006815")
        );

        sqlRepo.buscarPorNomeOrigem("C12345").ifPresentOrElse(
                System.out::println,
                () -> System.out.println("Transação não encontrada para o cliente C12345")
        );
    }
}