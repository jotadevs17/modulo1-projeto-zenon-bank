package br.com.zenon.fraud.io;

import br.com.zenon.fraud.model.Customer;
import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.model.TransactionType;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class EfficientTransactionIngestor {

    public void readBatchConcurrent(String fileName, int batchSize, Consumer<List<Transaction>> consumer) {
        int processors = Runtime.getRuntime().availableProcessors();
        try (ExecutorService executor = Executors.newFixedThreadPool(processors)) {
            try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
                List<Transaction> batch = new ArrayList<>(batchSize);

                lines.skip(1).forEach(line -> {
                    batch.add(parseTransaction(line));
                    if (batch.size() >= batchSize) {
                        List<Transaction> batchToProcess = new ArrayList<>(batch);
                        executor.submit(() -> consumer.accept(batchToProcess));
                        batch.clear();
                    }
                });

                if (!batch.isEmpty()) {
                    executor.submit(() -> consumer.accept(batch));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.HOURS);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    private Transaction parseTransaction(String line) {
        String[] data = line.split(",");
        Customer origin = new Customer(data[3], new BigDecimal(data[4]), new BigDecimal(data[5]));
        Customer dest = new Customer(data[6], new BigDecimal(data[7]), new BigDecimal(data[8]));

        return new Transaction(
                Integer.parseInt(data[0]),
                TransactionType.valueOf(data[1]),
                new BigDecimal(data[2]),
                origin,
                dest,
                Integer.parseInt(data[9]) == 1,
                Integer.parseInt(data[10]) == 1
        );
    }
}