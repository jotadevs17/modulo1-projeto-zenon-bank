package br.com.zenon.fraud;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void deveInstanciarTransacaoCorretamente() {
        Customer origem = new Customer("Jota", new BigDecimal("100.0"), new BigDecimal("50.0"));
        Customer destino = new Customer("Bruna", new BigDecimal("0.0"), new BigDecimal("50.0"));

        Transaction transacao = new Transaction(
                1, TransactionType.TRANSFER, new BigDecimal("50.0"), origem, destino, false, false
        );
        
        assertNotNull(transacao, "A transação não deveria ser nula");
        assertEquals(TransactionType.TRANSFER, transacao.type(), "O tipo da transação deveria ser TRANSFER");
    }
}