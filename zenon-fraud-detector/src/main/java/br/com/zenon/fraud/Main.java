package br.com.zenon.fraud;

import br.com.zenon.fraud.io.TransactionIngestor;
import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.repository.TransactionMapRepository;
import br.com.zenon.fraud.repository.TransactionRepository;
import br.com.zenon.fraud.service.FraudAnalyzer;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        /*
        Customer origem = new Customer("C1231006815", new BigDecimal("170136.0"), new BigDecimal("160296.36"));
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



        -------------------
                FIM DOS PRIMEIROS TESTES DA ATIVIDADE 2
*/

        TransactionIngestor ingestor = new TransactionIngestor();

        // Apontando de volta para o arquivo original gigante para ter volume de dados
        String filePath = "data/PS_20174392719_1491204439457_log.csv";
        List<Transaction> transacoes = ingestor.loadTransactions(filePath);

        // Instanciando o analisador de fraudes com a lista carregada
        FraudAnalyzer analyzer = new FraudAnalyzer(transacoes);

        // 1. Total de Fraudes
        System.out.println("1. Total de Fraudes: " + analyzer.getTotalFraudes());

        // 2. Top 3 Fraudes de Maior Valor
        System.out.println("2. Top 3 Fraudes de Maior Valor:");
        analyzer.getTop3FraudesDeMaiorValor().forEach(valor -> System.out.println(valor.toPlainString()));

        // 3. Clientes Suspeitos
        System.out.println("3. Clientes Suspeitos:");
        analyzer.getClientesSuspeitos().forEach(nome -> System.out.println(nome));

        // 4. Prejuízo Total
        System.out.println("4. Prejuízo Total: " + analyzer.getPrejuizoTotal());

        // 5. Fraudes por Tipo
        System.out.println("5. Fraudes por Tipo:");
        analyzer.getFraudesPorTipo().forEach((tipo, quantidade) ->
                System.out.println(" - " + tipo + ": " + quantidade)
        );

        /*--------------------
         FIM DOS PRIMEIROS TESTES DA ATIVIDADE 3, 4 e 5 */

        /* TESTES COM LIST

        TransactionRepository listRepo = new TransactionListRepository(transacoes);

        listRepo.buscarPorNomeOrigem("CSHARP123").ifPresentOrElse(
                t -> System.out.println(t),
                () -> System.out.println("Transação não encontrada para o cliente CSHARP123")
        );

        long inicioLista = System.nanoTime();
        listRepo.buscarPorNomeOrigem("C1868032458");
        long fimLista = System.nanoTime();

        System.out.println("Tempo de consulta: " + (fimLista - inicioLista) + " nanos"); */


        /* TESTES COM MAP*/

        TransactionRepository mapRepo = new TransactionMapRepository(transacoes);

        mapRepo.buscarPorNomeOrigem("CSHARP123").ifPresentOrElse(
                t -> System.out.println(t),
                () -> System.out.println("Transação não encontrada para o cliente CSHARP123")
        );

        long inicioMap = System.nanoTime();
        mapRepo.buscarPorNomeOrigem("C1868032458");
        long fimMap = System.nanoTime();

        System.out.println("Tempo de consulta: " + (fimMap - inicioMap) + " nanos");
    }
}

