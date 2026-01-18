package com.LibManSys.LibManSys.Repository;

import org.springframework.stereotype.Repository;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Repository
public class CurrentIssuedBooks {

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library_db",
                "root",
                "admin"
        );
    }

    public Map<String, Object> fetchIssuedToday() {

        String sql = """
            SELECT 
                ls.book_id,
                ls.name AS book_name,
                ls.rent,
                i.issue_date AS issued_date,
                i.due_date,
                i.member_id AS user_id,
                u.name AS user_name
            FROM library_stats ls
            JOIN issued_books i ON ls.book_id = i.book_id
            JOIN members u ON i.member_id = u.member_id
        """;



        List<Map<String, Object>> books = new ArrayList<>();

        LocalDate today = LocalDate.now();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            //ps.setDate(1, Date.valueOf(today));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    LocalDate dueDate = rs.getDate("due_date").toLocalDate();
                    long daysLeft = java.time.temporal.ChronoUnit.DAYS.between(today, dueDate);

                    Map<String, Object> book = new HashMap<>();
                    book.put("userName", rs.getString("user_name"));
                    book.put("bookName", rs.getString("book_name"));
                    book.put("issuedDate", rs.getDate("issued_date").toString());
                    book.put("dueDate", rs.getDate("due_date").toString());
                    book.put("daysLeft", daysLeft);

                    books.add(book);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch issued books", e);
        }

        return Map.of(
                "date", today.toString(),
                "count", books.size(),
                "books", books
        );
    }
}
