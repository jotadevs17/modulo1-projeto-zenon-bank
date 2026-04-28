package br.com.zenon.fraud.io;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class TransactionReport {

    public void processarRelatorio(String filePath, Locale locale) {

        ResourceBundle bundle = ResourceBundle.getBundle("report", locale);

        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

        // Variaveis que guardam o estado (Atomics são necessários para modificar dentro de um lambda)
        AtomicLong totalLinhas = new AtomicLong(0);
        AtomicLong totalFraudes = new AtomicLong(0);
        AtomicReference<BigDecimal> valorTotal = new AtomicReference<>(BigDecimal.ZERO);

        Path path = Paths.get(filePath);

        // ATENÇÃO: Files.lines PRECISA estar dentro de um try-with-resources
        // Se a stream não for fechada, o arquivo fica travado no sistema operacional!
        try (Stream<String> linhas = Files.lines(path)) {

            linhas.skip(1) // Pula o cabeçalho do CSV
                    .forEach(linha -> {
                        // 1. Aumenta o contador de linhas
                        totalLinhas.incrementAndGet();

                        // 2. Faz o split da linha pela vírgula
                        String[] dados = linha.split(",");

                        // Converte o texto da coluna 2 para número
                        BigDecimal valorDaLinha = new BigDecimal(dados[2]);
                        valorTotal.updateAndGet(atual -> atual.add(valorDaLinha));

                        if ("1".equals(dados[9])) {
                            totalFraudes.incrementAndGet();
                        }

                    });

        } catch (IOException e) {
            System.err.println("Erro ao processar arquivo: " + e.getMessage());
        }

        // Utiliza as chaves do ResourceBundle para os cabeçalhos
        System.out.println(bundle.getString("report.total.lines") + totalLinhas.get());
        System.out.println(bundle.getString("report.total.frauds") + totalFraudes.get());

        // Formata o valor total usando o padrão cultural correto (R$ ou $)
        String valorFormatado = currencyFormatter.format(valorTotal.get());
        System.out.println(bundle.getString("report.total.value") + valorFormatado);
    }
}