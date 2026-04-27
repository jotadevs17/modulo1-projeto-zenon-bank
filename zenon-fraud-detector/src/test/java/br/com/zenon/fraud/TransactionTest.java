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

    @Test
    void naoDeveInstanciarComStepInvalido() {
        Customer origem = new Customer("Jota", new BigDecimal("100.0"), new BigDecimal("50.0"));
        Customer destino = new Customer("Bruna", new BigDecimal("0.0"), new BigDecimal("50.0"));

        //vou testar se o construtor compacto barra o step zerado ou negativo
        assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(0, TransactionType.TRANSFER, new BigDecimal("50.0"), origem, destino, false, false);
        }, "Deveria lançar erro ao passar step 0");
    }

    @Test
    void naoDeveInstanciarComAmountNegativoOuNulo() {
        Customer origem = new Customer("Jota", new BigDecimal("100.0"), new BigDecimal("50.0"));
        Customer destino = new Customer("Bruna", new BigDecimal("0.0"), new BigDecimal("50.0"));

        //testando a barreira contra numero negativo no valor
        assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(1, TransactionType.TRANSFER, new BigDecimal("-10.0"), origem, destino, false, false);
        }, "Deveria lançar erro ao passar amount negativo");

        //testando a barreira contra valor nulo
        assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(1, TransactionType.TRANSFER, null, origem, destino, false, false);
        }, "Deveria lançar erro ao passar amount nulo");
    }

    @Test
    void naoDeveInstanciarComClienteOrigemOuDestinoNulo() {
        Customer clienteValido = new Customer("Jota", new BigDecimal("100.0"), new BigDecimal("50.0"));

        //verificando se bloqueia a origem vazia
        assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(1, TransactionType.TRANSFER, new BigDecimal("50.0"), null, clienteValido, false, false);
        }, "Deveria lançar erro ao passar origem nula");

        //verificando se bloqueia o destino vazio
        assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(1, TransactionType.TRANSFER, new BigDecimal("50.0"), clienteValido, null, false, false);
        }, "Deveria lançar erro ao passar destino nulo");
    }

    @Test
    void naoDeveInstanciarComTipoNulo() {
        Customer origem = new Customer("Jota", new BigDecimal("100.0"), new BigDecimal("50.0"));
        Customer destino = new Customer("Bruna", new BigDecimal("0.0"), new BigDecimal("50.0"));

        //garantindo que o enum tambem não passa nulo
        assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(1, null, new BigDecimal("50.0"), origem, destino, false, false);
        }, "Deveria lançar erro ao passar tipo de transação nulo");
    }
}