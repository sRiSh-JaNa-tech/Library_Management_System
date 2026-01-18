package com.LibManSys.LibManSys.Repository;


import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.*;

@Repository
public class OverdueBooksRepository {

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library_db",
                "root",
                "admin"
        );
    }

    public List<Map<String, Object>> fetchOverdueBooks() {

        String sql = """
            SELECT 
                u.name AS user_name,
                ls.name AS book_name,
                i.issue_date,
                i.due_date,
                DATEDIFF(CURDATE(), i.due_date) AS days_overdue,
                DATEDIFF(CURDATE(), i.due_date) * 25 AS fine
            FROM issued_books i
            JOIN library_stats ls ON i.book_id = ls.book_id
            JOIN members u ON i.member_id = u.member_id
            WHERE i.due_date < CURDATE()
        """;

        List<Map<String, Object>> list = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("userName", rs.getString("user_name"));
                row.put("bookName", rs.getString("book_name"));
                row.put("issueDate", rs.getDate("issue_date").toString());
                row.put("dueDate", rs.getDate("due_date").toString());
                row.put("daysOverdue", rs.getInt("days_overdue"));
                row.put("fine", rs.getInt("fine"));
                list.add(row);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch overdue books", e);
        }

        return list;
    }
}

