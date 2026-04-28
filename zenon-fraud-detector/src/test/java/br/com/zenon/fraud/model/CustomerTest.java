package br.com.zenon.fraud.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void deveInstanciarCustomerCorretamente() {
        //vou criar um cliente com dados validos pra ver se a classe aceita
        Customer customer = new Customer("Jota", new BigDecimal("100.0"), new BigDecimal("50.0"));

        assertNotNull(customer);
        assertEquals("Jota", customer.name());
    }

    @Test
    void naoDeveInstanciarComNomeNuloOuVazio() {
        //testando se a nossa barreira do construtor compacto barra nome vazio
        assertThrows(IllegalArgumentException.class, () -> {
            new Customer("", new BigDecimal("100.0"), new BigDecimal("50.0"));
        }, "Deveria lançar erro ao passar nome vazio");
    }

    @Test
    void naoDeveInstanciarComSaldoNuloOuNegativo() {
        //testando se a barreira barra numero negativo no saldo
        assertThrows(IllegalArgumentException.class, () -> {
            new Customer("Jota", new BigDecimal("-10.0"), new BigDecimal("50.0"));
        }, "Deveria lançar erro ao passar saldo negativo");
    }
}