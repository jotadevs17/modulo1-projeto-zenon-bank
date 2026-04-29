package br.com.zenon.fraud;

import br.com.zenon.fraud.io.EfficientTransactionIngestor;
import br.com.zenon.fraud.repository.TransactionRepository;
import br.com.zenon.fraud.repository.TransactionSQLRepository;

class DBMain {
    void main() {
        TransactionRepository sqlRepo = new TransactionSQLRepository();
        EfficientTransactionIngestor ingestor = new EfficientTransactionIngestor();

        sqlRepo.deleteAll();

        System.out.println("Iniciando ingestão concorrente do arquivo no MySQL...");
        long start = System.currentTimeMillis();

        ingestor.readBatchConcurrent("data/PS_20174392719_1491204439457_log.csv", 10000, sqlRepo::saveAll);

        long end = System.currentTimeMillis();
        long durationSeconds = (end - start) / 1000;

        System.out.println("Tempo total de inserção: " + durationSeconds + " segundos");

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