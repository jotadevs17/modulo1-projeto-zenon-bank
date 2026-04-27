package br.com.zenon.fraud;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class FraudAnalyzerTest {

    private FraudAnalyzer analyzer;

    @BeforeEach
    void setup() {
        //vou criar alguns clientes para usar de origem e destino
        Customer clienteA = new Customer("C100A", BigDecimal.ZERO, BigDecimal.ZERO);
        Customer clienteB = new Customer("C200B", BigDecimal.ZERO, BigDecimal.ZERO);
        Customer clienteC = new Customer("C300C", BigDecimal.ZERO, BigDecimal.ZERO);
        Customer clienteD = new Customer("C400D", BigDecimal.ZERO, BigDecimal.ZERO);
        Customer clienteE = new Customer("C500E", BigDecimal.ZERO, BigDecimal.ZERO);
        Customer clienteDestino = new Customer("M999", BigDecimal.ZERO, BigDecimal.ZERO);

        //agora monto uma lista fake de transacoes misturando fraudes e transacoes legitimas
        List<Transaction> transacoes = List.of(
                // 6 fraudes com valores e tipos variados. o cliente A vai repetir para testar o distinct
                new Transaction(1, TransactionType.CASH_OUT, new BigDecimal("100.00"), clienteA, clienteDestino, true, false),
                new Transaction(1, TransactionType.TRANSFER, new BigDecimal("500.00"), clienteB, clienteDestino, true, false),
                new Transaction(1, TransactionType.CASH_OUT, new BigDecimal("300.00"), clienteC, clienteDestino, true, false),
                new Transaction(1, TransactionType.TRANSFER, new BigDecimal("800.00"), clienteA, clienteDestino, true, false),
                new Transaction(1, TransactionType.CASH_OUT, new BigDecimal("50.00"), clienteD, clienteDestino, true, false),
                new Transaction(1, TransactionType.PAYMENT, new BigDecimal("1000.00"), clienteE, clienteDestino, true, false),

                // 2 transacoes normais que a esteira de fraudes deve ignorar
                new Transaction(1, TransactionType.PAYMENT, new BigDecimal("5000.00"), clienteA, clienteDestino, false, false),
                new Transaction(1, TransactionType.TRANSFER, new BigDecimal("200.00"), clienteB, clienteDestino, false, false)
        );

        //instancio o meu analisador com a lista mockada antes de cada teste rodar
        analyzer = new FraudAnalyzer(transacoes);
    }

    @Test
    void deveContarTotalDeFraudes() {
        //vou chamar o metodo e ver se ele ignorou as 2 transacoes falsas e contou apenas as 6 fraudes
        long total = analyzer.getTotalFraudes();
        assertEquals(6, total, "Deve contar exatamente 6 fraudes");
    }

    @Test
    void deveRetornarTop3FraudesDeMaiorValor() {
        //chamo o metodo que ordena e corta o top 3
        List<BigDecimal> top3 = analyzer.getTop3FraudesDeMaiorValor();

        //valido se a lista tem tamanho 3 e se os valores vieram na ordem decrescente certa (1000, 800 e 500)
        assertEquals(3, top3.size());
        assertEquals(new BigDecimal("1000.00"), top3.get(0));
        assertEquals(new BigDecimal("800.00"), top3.get(1));
        assertEquals(new BigDecimal("500.00"), top3.get(2));
    }

    @Test
    void deveRetornarClientesSuspeitosSemRepeticao() {
        //chamo o metodo que filtra, pega nome, limpa repetidos e limita a 5
        List<String> suspeitos = analyzer.getClientesSuspeitos();

        //como o cliente A repete nas fraudes, ele deve aparecer uma vez só. e deve limitar a 5 nomes no total
        assertEquals(5, suspeitos.size());
        assertTrue(suspeitos.contains("C100A"));
        assertTrue(suspeitos.contains("C200B"));
        assertTrue(suspeitos.contains("C300C"));
        assertTrue(suspeitos.contains("C400D"));
        assertTrue(suspeitos.contains("C500E"));
    }

    @Test
    void deveCalcularPrejuizoTotal() {
        //chamo a maquina de reduce pra somar tudo
        BigDecimal total = analyzer.getPrejuizoTotal();

        //a soma de 100+500+300+800+50+1000 das fraudes é 2750
        assertEquals(new BigDecimal("2750.00"), total);
    }

    @Test
    void deveAgruparFraudesPorTipo() {
        //chamo a maquina de groupingBy
        Map<TransactionType, Long> fraudesPorTipo = analyzer.getFraudesPorTipo();

        //valido se ele separou nos baldes certinhos
        assertEquals(3, fraudesPorTipo.get(TransactionType.CASH_OUT), "Devem ter 3 fraudes de CASH_OUT");
        assertEquals(2, fraudesPorTipo.get(TransactionType.TRANSFER), "Devem ter 2 fraudes de TRANSFER");
        assertEquals(1, fraudesPorTipo.get(TransactionType.PAYMENT), "Deve ter 1 fraude de PAYMENT");

        //garanto que nao criou balde para tipos que nao tiveram fraude
        assertNull(fraudesPorTipo.get(TransactionType.CASH_IN), "Nao deve ter fraudes de CASH_IN");
    }
}