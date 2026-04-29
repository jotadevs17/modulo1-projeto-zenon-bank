# Zenon Fraud Detector

## Descrição do Projeto
O Zenon Fraud Detector é uma aplicação backend desenvolvida para o processamento e análise de grandes volumes de transações financeiras baseadas no dataset PaySim. O foco principal do projeto é a eficiência no tratamento de dados (6 milhões de registros) e a detecção de padrões de fraude.

## Stack Tecnológica
- **Linguagem:** Java 17 (utilizando Records para imutabilidade)
- **Gerenciador de Dependências:** Maven
- **Persistência:** JDBC puro com MySQL 8.0
- **Infraestrutura:** Docker e Docker Compose
- **Concorrência:** ExecutorService (Fixed Thread Pool) e Java Streams

## Arquitetura e Diferenciais Técnicos
- **Eficiência de Memória:** Implementação de processamento via Streams para evitar `OutOfMemoryError` ao ler arquivos CSV massivos.
- **Otimização de I/O:** Uso de `addBatch` e `executeBatch` no JDBC, aliado ao parâmetro `rewriteBatchedStatements=true`, reduzindo o tempo de inserção de horas para minutos.
- **Processamento Paralelo:** Distribuição da carga de trabalho em lotes (chunks) processados concorrentemente, maximizando o uso dos núcleos da CPU.
- **Resiliência:** Gestão manual de transações (Auto-commit desabilitado) para garantir a integridade dos dados durante inserções em lote.

## Como Executar

### Pré-requisitos
- Docker e Docker Compose instalados.
- JDK 17 ou superior.

### Passo a Passo
1. Inicie a infraestrutura de banco de dados:
   ```bash
   docker-compose up -d
