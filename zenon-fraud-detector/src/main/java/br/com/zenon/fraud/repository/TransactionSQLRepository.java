package br.com.zenon.fraud.repository;

import br.com.zenon.fraud.model.Customer;
import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.model.TransactionType;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import java.util.Optional;

public class TransactionSQLRepository implements TransactionRepository {

    private final String url = "jdbc:mysql://localhost:3308/zenon_bank";
    private final String user = "root";
    private final String password = "root";

    @Override
    public Optional<Transaction> buscarPorNomeOrigem(String nameOrig) {
        String sql = "SELECT * FROM TRANSACTIONS WHERE name_orig = ?";
        // restante do código...
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nameOrig);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Customer origin = new Customer(rs.getString("name_orig"), rs.getBigDecimal("old_balance_orig"), rs.getBigDecimal("new_balance_orig"));
                Customer dest = new Customer(rs.getString("name_dest"), rs.getBigDecimal("old_balance_dest"), rs.getBigDecimal("new_balance_dest"));

                return Optional.of(new Transaction(
                        rs.getInt("step"),
                        TransactionType.valueOf(rs.getString("type")),
                        rs.getBigDecimal("amount"),
                        origin,
                        dest,
                        rs.getBoolean("is_fraud"),
                        rs.getBoolean("is_flagged_fraud")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void save(Transaction t) {
        String sql = "INSERT INTO TRANSACTIONS (step, type, amount, name_orig, old_balance_orig, new_balance_orig, name_dest, old_balance_dest, new_balance_dest, is_fraud, is_flagged_fraud) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setStatementParameters(stmt, t);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveAll(List<Transaction> transactions) {
        String sql = "INSERT INTO TRANSACTIONS (step, type, amount, name_orig, old_balance_orig, new_balance_orig, name_dest, old_balance_dest, new_balance_dest, is_fraud, is_flagged_fraud) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false); // Inicia transação manual
            for (Transaction t : transactions) {
                setStatementParameters(stmt, t);
                stmt.addBatch();
            }
            stmt.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setStatementParameters(PreparedStatement stmt, Transaction t) throws SQLException {
        stmt.setInt(1, t.step());
        stmt.setString(2, t.type().name());
        stmt.setBigDecimal(3, t.amount());
        stmt.setString(4, t.origin().name());
        stmt.setBigDecimal(5, t.origin().oldBalance());
        stmt.setBigDecimal(6, t.origin().newBalance());
        stmt.setString(7, t.destination().name());
        stmt.setBigDecimal(8, t.destination().oldBalance());
        stmt.setBigDecimal(9, t.destination().newBalance());
        stmt.setBoolean(10, t.isFraud());
        stmt.setBoolean(11, t.isFlaggedFraud());
    }
}