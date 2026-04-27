package br.com.zenon.fraud;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TransactionIngestor {

    public List<Transaction> loadTransactions(String filePath) {
        List<Transaction> transactions = new ArrayList<>();

        //irá servir para garantir que o arquivo seja fechado automaticamente no final
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            //já vai descartar logo de cara a primeira linha, que s3eria o cabeçalho do CSV
            String line = br.readLine();

            if (line != null) {
                int count = 0;

                //agora para ler enquanto houver linhas eu vou usar WHile. o contador deve ser menor que 1000 tb
                while ((line = br.readLine()) != null && count < 1000) {

                    //serve para cortar a linha inteira usando a virgula como separador
                    String[] data = line.split(",");

                    //agora eu devo converter os textos em seus devidos tipos
                    int step = Integer.parseInt(data[0]);
                    TransactionType type = TransactionType.valueOf(data[1]);
                    BigDecimal amount = new BigDecimal(data[2]);

                    //Montando cliente de origem com nome, balanço antigo e novo
                    Customer origin = new Customer(data[3], new BigDecimal(data[4]), new BigDecimal(data[5]));

                    //Montando cliente de destino com nome, balanço antigo e novo
                    Customer destination = new Customer(data[6], new BigDecimal(data[7]), new BigDecimal(data[8]));

                    //AGora pra finalizar converter as flags.
                    boolean isFraud = "1".equals(data[9]);
                    boolean isFlaggedFraud = "1".equals(data[10]);

                    //Juntando td e add na lista
                    Transaction transaction = new Transaction(step, type, amount, origin, destination, isFraud, isFlaggedFraud);
                    transactions.add(transaction);

                    count++;
                }
            }

        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo CSV: " + e.getMessage());
        }
        return transactions;
    }
}
