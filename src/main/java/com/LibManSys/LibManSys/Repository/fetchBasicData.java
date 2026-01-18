package com.LibManSys.LibManSys.Repository;
import org.springframework.stereotype.Repository;
import java.util.Map;
import java.sql.*;

@Repository
public class fetchBasicData {

    private static final double TOTAL_BUDGET = 60000.00;

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library_db",
                "root",
                "admin"
        );
    }

    public Map<String, Object> fetchSummary() {

        String sql = """
            SELECT 
                COUNT(*) AS total_books,
                COALESCE(SUM(cost), 0) AS total_spent,
                COALESCE(SUM(CASE WHEN status = true THEN 1 ELSE 0 END), 0) AS books_issued
            FROM library_stats
        """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int totalBooks = rs.getInt("total_books");
                double totalSpent = rs.getDouble("total_spent");
                int booksIssued = rs.getInt("books_issued");

                double budgetLeft = TOTAL_BUDGET - totalSpent;

                return Map.of(
                        "totalBooks", totalBooks,
                        "booksIssued", booksIssued,
                        "totalSpent", totalSpent,
                        "budgetLeft", budgetLeft
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch library statistics", e);
        }

        return Map.of(
                "totalBooks", 0,
                "booksIssued", 0,
                "totalSpent", 0.0,
                "budgetLeft", TOTAL_BUDGET
        );
    }
}
