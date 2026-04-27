package br.com.zenon.fraud;

import java.math.BigDecimal;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        /*Customer origem = new Customer("C1231006815", new BigDecimal("170136.0"), new BigDecimal("160296.36"));
        Customer destino = new Customer("M1979787155", new BigDecimal("0.0"), new BigDecimal("0.0"));

        Transaction transacao1 = new Transaction(
                1,
                TransactionType.PAYMENT,
                new BigDecimal("9839.64"),
                origem,
                destino,
                false,
                false
        );

        System.out.println("--- Transação 1 ---");
        System.out.println(transacao1);


        Customer origem2 = new Customer("C1280323807", new BigDecimal("850002.52"), new BigDecimal("0.0"));
        Customer destino2 = new Customer("C873221189", new BigDecimal("6510099.11"), new BigDecimal("7360101.63"));

        Transaction transacao2 = new Transaction(
                743,
                TransactionType.CASH_OUT,
                new BigDecimal("850002.52"),
                origem2,
                destino2,
                true,
                false
        );

        System.out.println("--- Transação 2 ---");
        System.out.println(transacao2);
        FIM DOS PRIMEIROS TESTES DA ATIVIDADE 2
        */

        TransactionIngestor ingestor = new TransactionIngestor();

        List<Transaction> transacoes = ingestor.loadTransactions("data/PS_20174392719_1491204439457_log.csv");
        System.out.println("--- Primeiras 10 Transações Ingeridas ---");
        for (int i = 0; i < 10 && i < transacoes.size(); i++) {
            System.out.println(transacoes.get(i));
        }
    }
}

