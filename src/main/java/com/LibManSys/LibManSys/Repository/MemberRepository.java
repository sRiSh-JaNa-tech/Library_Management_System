package com.LibManSys.LibManSys.Repository;

import org.springframework.stereotype.Repository;

import java.util.*;
import java.sql.*;

@Repository
public class MemberRepository {

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library_db",
                "root",
                "admin"
        );
    }

    public List<Map<String, Object>> fetchAllMembers() {

        String sql = """
            SELECT 
                m.member_id,
                m.name,
                m.email,
                m.phone,
                COUNT(i.issue_id) AS books_issued,
                SUM(CASE WHEN i.return_date IS NULL AND i.due_date < CURDATE() THEN 1 ELSE 0 END) AS overdue,
                CASE 
                    WHEN COUNT(i.issue_id) > 0 THEN 'active'
                    ELSE 'inactive'
                END AS status
            FROM members m
            LEFT JOIN issued_books i ON m.member_id = i.member_id
            GROUP BY m.member_id, m.name, m.email, m.phone
        """;

        List<Map<String, Object>> list = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("member_id", rs.getString("member_id"));
                row.put("name", rs.getString("name"));
                row.put("email", rs.getString("email"));
                row.put("phone", rs.getString("phone"));
                row.put("books_issued", rs.getInt("books_issued"));
                row.put("overdue", rs.getInt("overdue"));
                row.put("status", rs.getString("status"));
                list.add(row);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public void addMember(Map<String, Object> data) {

        String sql = "INSERT INTO members(member_id, name, email, phone) VALUES (?, ?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, data.get("member_id").toString());
            ps.setString(2, data.get("name").toString());
            ps.setString(3, data.get("email").toString());
            ps.setString(4, data.get("phone").toString());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

