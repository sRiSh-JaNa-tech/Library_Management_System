package com.LibManSys.LibManSys.Repository;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class IssueReturnRepository {

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library_db",
                "root",
                "admin"
        );
    }

    public List<Map<String, Object>> fetchAll() {

        String sql = """
            SELECT 
                i.issue_id,
                i.member_id,
                b.title,
                i.issue_date,
                i.due_date,
                i.return_date
            FROM issued_books i
            JOIN books_new b ON i.book_id = b.book_id
            ORDER BY i.issue_date DESC
        """;

        List<Map<String, Object>> list = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Date issueDate = rs.getDate("issue_date");
                Date dueDate = rs.getDate("due_date");
                Date returnDate = rs.getDate("return_date");

                String status;
                if (returnDate != null) status = "returned";
                else if (dueDate.before(new java.sql.Date(System.currentTimeMillis()))) status = "overdue";
                else status = "issued";

                Map<String, Object> row = new HashMap<>();
                row.put("issueId", rs.getInt("issue_id"));
                row.put("student", "Member #" + rs.getInt("member_id"));
                row.put("book", rs.getString("title"));
                row.put("issueDate", issueDate.toString());
                row.put("dueDate", dueDate.toString());
                row.put("returnDate", returnDate == null ? null : returnDate.toString());
                row.put("status", status);

                list.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch issuance", e);
        }

        return list;
    }

    public void markReturned(int issueId) {
        String sql = "UPDATE issued_books SET return_date = CURDATE() WHERE issue_id = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, issueId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteIssuance(int issueId) {
        String sql = "DELETE FROM issued_books WHERE issue_id = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, issueId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
