package br.com.zenon.fraud.repository;

import br.com.zenon.fraud.model.Customer;
import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.model.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TransactionRepositoryTest {

    private List<Transaction> transacoesFake;

    @BeforeEach
    void setup() {
        //vou montar um banco de dados fake na memoria com dois clientes diferentes
        Customer origem1 = new Customer("C111", BigDecimal.ZERO, BigDecimal.ZERO);
        Customer origem2 = new Customer("C222", BigDecimal.ZERO, BigDecimal.ZERO);
        Customer destino = new Customer("M999", BigDecimal.ZERO, BigDecimal.ZERO);

        transacoesFake = List.of(
                new Transaction(1, TransactionType.PAYMENT, new BigDecimal("100.0"), origem1, destino, false, false),
                new Transaction(1, TransactionType.TRANSFER, new BigDecimal("200.0"), origem2, destino, false, false)
        );
    }

    @Test
    void testListRepositoryDeveEncontrarTransacao() {
        //passo a lista pro repo lento
        TransactionRepository listRepo = new TransactionListRepository(transacoesFake);

        //busco o cliente 222
        Optional<Transaction> result = listRepo.buscarPorNomeOrigem("C222");

        //a caixa optional nao pode estar vazia e tem que ter o nome certo
        assertTrue(result.isPresent());
        assertEquals("C222", result.get().origin().name());
    }

    @Test
    void testMapRepositoryDeveEncontrarTransacao() {
        //passo a lista pro repo rapido
        TransactionRepository mapRepo = new TransactionMapRepository(transacoesFake);

        //busco o cliente 111
        Optional<Transaction> result = mapRepo.buscarPorNomeOrigem("C111");

        assertTrue(result.isPresent());
        assertEquals("C111", result.get().origin().name());
    }

    @Test
    void ambosDevemRetornarVazioSeNaoExistir() {
        //agora vou testar o cenario de erro pra ver se a caixa (Optional) vem vazia como manda a regra
        TransactionRepository listRepo = new TransactionListRepository(transacoesFake);
        TransactionRepository mapRepo = new TransactionMapRepository(transacoesFake);

        //buscando um cliente que nao existe na lista fake
        assertTrue(listRepo.buscarPorNomeOrigem("C999").isEmpty(), "O ListRepo deve retornar vazio");
        assertTrue(mapRepo.buscarPorNomeOrigem("C999").isEmpty(), "O MapRepo deve retornar vazio");
    }
}