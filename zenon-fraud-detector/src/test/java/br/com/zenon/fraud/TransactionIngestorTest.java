package br.com.zenon.fraud;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TransactionIngestorTest {

    @Test
    void deveLerArquivoCsvERetornarListaDeTransacoes() {

        //vou instanciar o ingestor e passar o caminho do meu csv fake que criei pro teste
        TransactionIngestor ingestor = new TransactionIngestor();
        String filePath = "src/test/resources/transacoes_teste.csv";

        //agora eu chamo o metodo pra carregar as transacoes e guardo na lista
        List<Transaction> transacoes = ingestor.loadTransactions(filePath);

        //hora de validar se deu bom. a lista nao pode ser nula e tem que ter exatamente as 2 linhas do csv (descartou o cabecalho)
        assertNotNull(transacoes, "A lista de transações não deveria ser nula");
        assertEquals(2, transacoes.size(), "Deve ler exatamente 2 transações");

        //vou pegar logo a primeira transacao pra testar se todos os campos vieram certinhos igual no texto do arquivo
        Transaction primeiraTransacao = transacoes.get(0);

        assertEquals(1, primeiraTransacao.step());
        assertEquals(TransactionType.PAYMENT, primeiraTransacao.type());
        assertEquals(new BigDecimal("9839.64"), primeiraTransacao.amount());

        //testando se os dados do cliente de origem montaram certo
        assertEquals("C1231006815", primeiraTransacao.origin().name());
        assertEquals(new BigDecimal("170136.0"), primeiraTransacao.origin().oldBalance());

        //por fim as flags de fraude tb tem que bater com o 0 lá do arquivo
        assertFalse(primeiraTransacao.isFraud());
        assertFalse(primeiraTransacao.isFlaggedFraud());
    }

    @Test
    void deveIgnorarLinhasCorrompidasEIngerirApenasTransacoesValidas() {

        //vou instanciar o ingestor e passar o caminho do csv sujo da tarefa 04
        TransactionIngestor ingestor = new TransactionIngestor();
        String filePath = "data/paysim_with_bad_data.csv";

        //agora eu chamo o metodo pra carregar as transacoes e guardo na lista
        List<Transaction> transacoes = ingestor.loadTransactions(filePath);

        //hora de validar se o escudo de excecoes deu bom. a lista tem que ter exatamente 8 transacoes validas
        assertNotNull(transacoes, "A lista de transações não deveria ser nula");
        assertEquals(8, transacoes.size(), "Deve ler exatamente 8 transações válidas e ignorar as linhas com erro");
    }
}