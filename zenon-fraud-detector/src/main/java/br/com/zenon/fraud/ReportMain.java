package br.com.zenon.fraud;

import br.com.zenon.fraud.io.TransactionReport;
import java.util.Locale;

public class ReportMain {
    public static void main(String[] args) {
        TransactionReport report = new TransactionReport();
        String filePath = "data/PS_20174392719_1491204439457_log.csv";

        // Define o Locale com base no argumento passado na execução
        Locale locale = new Locale("pt", "BR");

        if (args.length > 0 && "en".equalsIgnoreCase(args[0])) {
            locale = Locale.US;
            System.out.println("Language set to English.");
        } else {
            System.out.println("Idioma definido para Português.");
        }

        report.processarRelatorio(filePath, locale);
    }
}