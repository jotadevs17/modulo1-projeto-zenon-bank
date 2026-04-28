package br.com.zenon.fraud;

import br.com.zenon.fraud.io.TransactionReport;

public class ReportMain {
    public static void main(String[] args) {
        TransactionReport report = new TransactionReport();

        String filePath = "data/PS_20174392719_1491204439457_log.csv";

        System.out.println("Iniciando processamento...");
        report.processarRelatorio(filePath);
        System.out.println("Processamento finalizado!");
    }
}