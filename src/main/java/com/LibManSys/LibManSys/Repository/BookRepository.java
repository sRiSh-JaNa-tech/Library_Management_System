package com.LibManSys.LibManSys.Repository;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.sql.*;

@Repository
public class BookRepository {

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library_db",
                "root",
                "admin"
        );
    }

    public void insertBook(String title, String author, String isbn, String category,
                           Integer year, Integer quantity, String coverImage) {

        String getSql = "SELECT last_number FROM book_sequence FOR UPDATE";
        String updateSql = "UPDATE book_sequence SET last_number = ?";
        String insertSql = """
        INSERT INTO books_new (book_id, title, author, isbn, category, publication_year, quantity, cover_image)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    """;

        try (Connection con = getConnection()) {
            con.setAutoCommit(false);

            int next;

            try (PreparedStatement ps = con.prepareStatement(getSql);
                 ResultSet rs = ps.executeQuery()) {
                rs.next();
                next = rs.getInt(1) + 1;
            }

            String bookId = "b" + next;

            try (PreparedStatement ps = con.prepareStatement(insertSql)) {
                ps.setString(1, bookId);
                ps.setString(2, title);
                ps.setString(3, author);
                ps.setString(4, isbn);
                ps.setString(5, category);
                if (year != null) ps.setInt(6, year); else ps.setNull(6, Types.INTEGER);
                ps.setInt(7, quantity);
                ps.setString(8, coverImage);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = con.prepareStatement(updateSql)) {
                ps.setInt(1, next);
                ps.executeUpdate();
            }

            con.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

