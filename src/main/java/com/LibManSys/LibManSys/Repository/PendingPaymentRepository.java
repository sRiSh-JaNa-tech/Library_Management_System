package com.LibManSys.LibManSys.Repository;


import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.*;

@Repository
public class PendingPaymentRepository {

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library_db",
                "root",
                "admin"
        );
    }

    public List<Map<String, Object>> fetchPending() {

        String sql = """
            SELECT 
                m.member_id,
                m.name,
                COUNT(p.payment_id) AS overdue_books,
                SUM(p.fine_amount) AS total_fine,
                MAX(p.last_updated) AS last_transaction,
                SUM(CASE WHEN p.is_paid = false THEN 1 ELSE 0 END) AS unpaid_count
            FROM pending_payments p
            JOIN members m ON p.member_id = m.member_id
            GROUP BY m.member_id, m.name
        """;


        List<Map<String, Object>> list = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("member_id", rs.getInt("member_id"));
                row.put("name", rs.getString("name"));
                row.put("overdue_books", rs.getInt("overdue_books"));
                row.put("total_fine", rs.getInt("total_fine"));
                row.put("last_transaction", rs.getTimestamp("last_transaction"));
                row.put("unpaid_count", rs.getInt("unpaid_count"));
                list.add(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public void markPaidForMember(int memberId) {

        String sql = "UPDATE pending_payments SET is_paid = true WHERE member_id = ? AND is_paid = false";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, memberId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

